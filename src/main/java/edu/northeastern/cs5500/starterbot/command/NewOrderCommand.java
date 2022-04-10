package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import java.awt.Color;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class NewOrderCommand implements Command {

    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public NewOrderCommand() {}

    @Override
    public String getName() {
        return "neworder";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Input the restaurant name")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "The bot will start a new order for you at the given restaurant")
                                .setRequired(true));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /neworder");
        User user = event.getUser();
        String userInput = event.getOption("content").getAsString();
        String restaurantName = restaurantController.getRestaurantName(userInput);
        Boolean isUserInShoppingCart = shoppingCartController.isUserInShoppingCart(user.getId());

        if (restaurantName == null) {
            event.reply("The restaurant does not exist! Please input the right restaurant name.")
                    .queue();
        } else if (isUserInShoppingCart) {
            event.reply(
                            "You still have an unfinished order, please check out that one first to start a new order.")
                    .queue();
        } else {
            shoppingCartController.createNewShoppingCart(
                    user.getId(), user.getName(), restaurantName);

            event.replyEmbeds(buildReplyEmbed(restaurantName));
        }
    }

    /**
     * Return MessageEmbed object with restaurant name
     *
     * @param restaurantName String, restaurant name which is the input where user will order
     * @return MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(String restaurantName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("You start a new order at " + restaurantName + "!");
        eb.addField("/order dish_name: ", "to order a particular dish", false);
        eb.addField("/order dish_num: ", "to order a dish with the given number", false);
        eb.addField("/menu: ", "to show the menu of the current restaurant", false);
        eb.addField("/showcart: ", "to show the shopping cart", false);
        eb.setColor(Color.BLUE);
        return eb.build();
    }
}
