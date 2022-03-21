package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import java.awt.Color;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.utils.tuple.Pair;

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
        return new CommandData(
                        getName(),
                        "please type in a number to order the corresponding dish, or 'random' to ask bot to recommend one for you.")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "please type in a number to order the corresponding dish, or 'random' to ask bot to recommend one for you.")
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
            Pair<String, Integer> randomDishPair = restaurantController.randomDish(restaurantName);
            event.reply(
                            "how about order "
                                    + randomDishPair.getLeft()
                                    + "? Type `/order "
                                    + randomDishPair.getRight()
                                    + "` to order this dish")
                    .queue();
        } else if (userInput.matches("[+-]?\\d*(\\.\\d+)?")) {
            Pair<String, Double> orderDish =
                    restaurantController.getDish(Integer.parseInt(userInput), restaurantName);
            if (orderDish == null) {
                event.reply("enter right number of dish").queue();
                return;
            }
            ArrayList<Pair<String, Double>> totalDishes =
                    shoppingCartController.addDish(user.getId(), orderDish);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Shopping cart: ");
            eb.setDescription(
                    "**"
                            + orderDish.getLeft()
                            + "** has been added! Your cart at **"
                            + restaurantName
                            + "** include: ");
            Double totalPrice = 0.0;

            for (int i = 0; i < totalDishes.size(); i++) {
                Pair<String, Double> curDish = totalDishes.get(i);
                String dish = curDish.getLeft();
                Double price = curDish.getRight();
                totalPrice += price;
                eb.addField((i + 1) + ". " + dish + ": ", "$" + price.toString(), false);
            }
            eb.addBlankField(false);
            eb.addField("Total:", "$" + Math.round(totalPrice * 100.0) / 100.0, false);

            eb.setColor(Color.GREEN);
            event.replyEmbeds(eb.build()).queue();
        } else {
            event.reply(
                            "please type in a number to order the corresponding dish, or type in 'random' to ask bot to recommend one for you.")
                    .queue();
        }
    }
}
