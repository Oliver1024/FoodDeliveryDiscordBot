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
            DishObject randomDish = restaurantController.randomDish(restaurantName);
            int randomDishIndex = restaurantController.findDishNumber(restaurantName, randomDish);
            event.reply(
                            "how about order "
                                    + randomDish.getDish()
                                    + "? Type `/order "
                                    + randomDishIndex
                                    + "` to order this dish")
                    .queue();
        } else if (userInput.matches("[+-]?\\d*(\\.\\d+)?")) {
            DishObject orderDish =
                    restaurantController.getDish(Integer.parseInt(userInput), restaurantName);
            if (orderDish == null) {
                event.reply("enter right number of dish").queue();
                return;
            }
            ArrayList<DishObject> totalDishes =
                    shoppingCartController.addDish(user.getId(), orderDish);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Shopping cart: ");
            eb.setDescription(
                    "**"
                            + orderDish.getDish()
                            + "** has been added! Your cart at **"
                            + restaurantName
                            + "** include: ");
            Double totalPrice = 0.0;

            for (int i = 0; i < totalDishes.size(); i++) {
                DishObject curDish = totalDishes.get(i);
                String dish = curDish.getDish();
                Double price = curDish.getPrice();
                totalPrice += price;
                eb.addField((i + 1) + ". " + dish + ": ", "$" + price.toString(), false);
            }
            eb.addField("Total:", "$" + Math.round(totalPrice * 100.0) / 100.0, false);

            eb.setColor(Color.GREEN);
            event.replyEmbeds(eb.build()).queue();
        } else {
            event.reply(
                            "please type in a number to order the corresponding dish, or 'random' to ask bot recommend one for you.")
                    .queue();
        }
    }
}
