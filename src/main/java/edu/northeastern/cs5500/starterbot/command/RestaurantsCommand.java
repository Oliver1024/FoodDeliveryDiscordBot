package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class RestaurantsCommand implements Command {

    @Inject RestaurantController restaurantController;

    @Inject
    public RestaurantsCommand() {}

    @Override
    public String getName() {
        return "restaurants";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        getName(),
                        "'/restaurants' or '/restaurants cuisine_type' to show restaurants you can order at")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "'/restaurants cuisine_type' to show restaurants with the particular cuisine type")
                                .setRequired(false));
    }

    /**
     * Helper function which returns the message for replying user
     *
     * @param restaurantsName the arraylist of all the names of restaurants in the database
     * @return the string for replying user
     */
    protected String buildReplyMessage(ArrayList<String> restaurantsName, String cuisineType) {
        String title;
        if (cuisineType != null) {
            if (cuisineType.length() > 1) {
                cuisineType =
                        cuisineType.substring(0, 1).toUpperCase()
                                + cuisineType.substring(1).toLowerCase();
            } else {
                cuisineType = cuisineType.toUpperCase();
            }
            title =
                    "Below is the list of all "
                            + cuisineType
                            + " restaurants you can order at:\n\n";
        } else {
            title = "Below is the list of all restaurants you can order at:\n\n";
        }

        String content = "";
        for (String name : restaurantsName) {
            content = content + name + "\n";
        }
        return "```" + title + content + "```";
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /restaurants");
        String userInput = null;
        if (event.getOption("content") != null) {
            userInput = event.getOption("content").getAsString();
        }
        ArrayList<String> resturantsName =
                restaurantController.getRestaurantsNameWithCertainCusineType(userInput);
        event.reply(buildReplyMessage(resturantsName, userInput)).queue();
    }
}
