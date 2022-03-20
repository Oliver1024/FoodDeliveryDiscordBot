package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
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

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /restaurants");
        String returnMes = "Below is the list of all restaurants you can order at:";
        String resturantsName = restaurantController.getAllRestaurantsName();
        event.reply("```" + returnMes + "\n\n" + resturantsName + "```").queue();
    }
}
