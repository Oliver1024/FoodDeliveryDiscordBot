package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import java.awt.Color;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class MenuCommand implements Command {

    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public MenuCommand() {}

    @Override
    public String getName() {
        return "menu";
    }

    // need change
    @Override
    public CommandData getCommandData() {
        // return new CommandData(getName(), "Tell the bot what name to address you with")
        //         .addOptions(
        //                 new OptionData(
        //                                 OptionType.STRING,
        //                                 "menu",
        //                                 "The bot will state the menu you are choosing")
        //                         .setRequired(true));
        return new CommandData(getName(), "command");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /menu");

        User user = event.getUser();
        String userInput = event.getOption("content").getAsString();
        Boolean isUserInShoppingCart = shoppingCartController.isUserInShoppingCart(user.getId());
        String restaurantName = null;

        if (userInput != null) {
            restaurantName = restaurantController.getRestaurantName(userInput);

        } else if (isUserInShoppingCart) {
            restaurantName = shoppingCartController.getRestaurantName(user.getId());
        }
        if (restaurantName == null) {
            event.reply(
                            "cannot find restaurant you want to check. Please type in "
                                    + "'/menu' to check the restaurant you are ordering at or '/menu restaurant_name' to check the given restaurant")
                    .queue();
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(restaurantName + "'s menu: ");
        ArrayList<DishObject> resultMenu = restaurantController.getMenu(restaurantName);
        for (int index = 0; index < resultMenu.size(); index++) {
            resultMenu.get(index).getDish();
            eb.addField(
                    index + 1 + ". " + resultMenu.get(index).getDish() + ": ",
                    resultMenu.get(index).getPrice().toString(),
                    true);
        }
        eb.setColor(Color.BLUE);
        event.replyEmbeds(eb.build()).queue();
    }
}
