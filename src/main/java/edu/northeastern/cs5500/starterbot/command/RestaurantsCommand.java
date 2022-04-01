package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

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
        return new CommandData(getName(), "Show the list of all restaurants you can order at");
    }

    /**
     * Helper function which returns the message for replying user
     *
     * @param restaurantsName the arraylist of all the names of restaurants in the database
     * @return the string for replying user
     */
    protected String buildReplyMessage(ArrayList<String> restaurantsName) {
        String title = "Below is the list of all restaurants you can order at:\n\n";
        String content = "";
        for (String name : restaurantsName) {
            content = content + name + "\n";
        }
        return "```" + title + content + "```";
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /restaurants");
        ArrayList<String> resturantsName = restaurantController.getAllRestaurantsName();
        event.reply(buildReplyMessage(resturantsName)).queue();
    }
}
