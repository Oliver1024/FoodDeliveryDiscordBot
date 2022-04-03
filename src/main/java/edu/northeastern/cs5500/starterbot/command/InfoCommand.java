package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.model.Restaurant;
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
public class InfoCommand implements Command {
    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public InfoCommand() {}

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        getName(), "/info to check the info of the restaurant you are ordering at")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "'/info restaurant_name' to check the info of the given restaurant")
                                .setRequired(false));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /info");
        User user = event.getUser();

        String userInput =
                (event.getOption("content") == null)
                        ? null
                        : event.getOption("content").getAsString();
        String restaurantName =
                (userInput != null)
                        ? restaurantController.getRestaurantName(userInput)
                        : shoppingCartController.getRestaurantName(user.getId());

        if (restaurantName == null) {
            event.reply(
                            "No any restaurant in shopping cart! "
                                    + "'/info' to check the restaurant you are ordering at or '/info restaurant_name' to check the given restaurant")
                    .queue();
            return;
        }

        Restaurant restaurant = restaurantController.getRestaurant(restaurantName);
        event.replyEmbeds(buildReplyEmbed(restaurant)).queue();
    }

    /**
     * Return EmbeBuild object and show restaurant info
     *
     * @param restaurant Restaurant
     * @return return Embuild object
     */
    protected MessageEmbed buildReplyEmbed(Restaurant restaurant) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(restaurant.getName() + "'s info");
        eb.addField("Contact:", restaurant.getContact().toString(), false);
        eb.addField("Address:", restaurant.getAddress(), false);
        String cuisine = "";

        for (int i = 0; i < restaurant.getCuisineType().size(); i++) {
            cuisine += restaurant.getCuisineType().get(i);
            if (i != restaurant.getCuisineType().size() - 1) {
                cuisine += ", ";
            }
        }
        eb.addField("Cuisine Type:", cuisine, false);
        eb.setColor(Color.ORANGE);
        return eb.build();
    }
}
