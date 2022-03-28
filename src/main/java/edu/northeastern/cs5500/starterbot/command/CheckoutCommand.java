package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.controller.UserController;
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
public class CheckoutCommand implements Command {

    @Inject ShoppingCartController shoppingCartController;
    @Inject UserController userController;

    @Inject
    public CheckoutCommand() {}

    @Override
    public String getName() {
        return "checkout";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "press enter to checkout");
    }

    protected EmbedBuilder buildEB(String restaurantName, ArrayList<DishObject> orderedDishes) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Thanks for ordering!");
        eb.setDescription("Your order at **" + restaurantName + "** includes:");

        Double totalPrice = 0.0;

        for (int i = 0; i < orderedDishes.size(); i++) {
            eb.addField(
                    (i + 1) + ". " + orderedDishes.get(i).getDish(),
                    orderedDishes.get(i).getPrice().toString(),
                    false);
            totalPrice += orderedDishes.get(i).getPrice();
        }
        eb.addField("Total:", "$" + Math.round(totalPrice * 100.0) / 100.0, false);
        eb.setColor(Color.PINK);
        return eb;
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /checkout");
        User user = event.getUser();
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());

        if (restaurantName == null) {
            event.reply("You don't have an order! Please user /neworder restaurant_name").queue();
        } else {
            ArrayList<DishObject> orderedDishes =
                    shoppingCartController.getOrderedDishes(user.getId());
            userController.addOrder(user.getId(), user.getName(), restaurantName, orderedDishes);
            shoppingCartController.deleteCart(user.getId());
            EmbedBuilder eb = buildEB(restaurantName, orderedDishes);
            event.replyEmbeds(eb.build()).queue();
        }
    }
}
