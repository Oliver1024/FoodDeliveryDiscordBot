package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class OrderCommand implements Command {

    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public OrderCommand() {}

    @Override
    public String getName() {
        return "order";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "please order")
                .addOptions(
                        new OptionData(OptionType.STRING, "content", "please order")
                                .setRequired(true));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /order");
        String userInput = event.getOption("content").getAsString();
        User user = event.getUser();
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());
        if (restaurantName == null) {
            event.reply("need to start new order").queue();
            return;
        }

        if (userInput.equalsIgnoreCase("random")) {
            event.reply("random order food").queue();
        } else if (userInput.matches("[+-]?\\d*(\\.\\d+)?")) {
            HashMap<String, Double> orderDish =
                    restaurantController.getDish(Integer.parseInt(userInput), restaurantName);
            if (orderDish == null) {
                event.reply("enter right number of dish").queue();
                return;
            }
            HashMap<String, Double> totalDishes =
                    shoppingCartController.addDish(user.getId(), orderDish);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Shopping cart: ");
            eb.setDescription("Your order dishes at " + restaurantName + ": ");

            for (Map.Entry<String, Double> entry : totalDishes.entrySet()) {
                String dish = entry.getKey();
                Double price = entry.getValue();
                eb.addField(dish + ": ", price.toString(), true);
            }
            eb.setColor(Color.BLUE);
            event.replyEmbeds(eb.build()).queue();
        }
    }
}
