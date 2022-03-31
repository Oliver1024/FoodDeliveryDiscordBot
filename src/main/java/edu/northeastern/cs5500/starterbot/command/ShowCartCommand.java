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
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class ShowCartCommand implements Command {

    @Inject ShoppingCartController shoppingCartController;
    @Inject UserController userController;

    @Inject
    public ShowCartCommand() {}

    @Override
    public String getName() {
        return "showcart";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "press enter to show your shopping cart");
    }

    protected MessageEmbed buildEB(String restaurantName, ArrayList<DishObject> orderedDishes) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Shopping cart:");
        eb.setDescription("Your order at **" + restaurantName + "** includes:");

        Double totalPrice = 0.0;
        // check orderedDishes is empty or not
        // if orderedDishes is emprty return 0.0 price
        if (orderedDishes.isEmpty()) {
            eb.addField("Your shopping cart is empty. Total:", "$0.0", false);
            eb.setColor(Color.GREEN);
        }
        // if orderedDishes is not empty return dish and price
        else {
            for (int i = 0; i < orderedDishes.size(); i++) {
                eb.addField(
                        (i + 1) + ". " + orderedDishes.get(i).getDish(),
                        orderedDishes.get(i).getPrice().toString(),
                        false);
                totalPrice += orderedDishes.get(i).getPrice();
            }
            eb.addField("Total:", "$" + Math.round(totalPrice * 100.0) / 100.0, false);
            eb.setColor(Color.GREEN);
        }
        return eb.build();
    }

    @Override
    public void onEvent(CommandInteraction event) {
        // check user is start a new order or not
        log.info("event: /showcart");
        User user = event.getUser();
        Boolean isUserInShoppingCart = shoppingCartController.isUserInShoppingCart(user.getId());
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());

        // user is not in shopping cart
        if (isUserInShoppingCart == null) {
            event.reply("You haven’t started an order. There is no shopping cart for you!").queue();
        }
        // user in shipping cart
        else {
            ArrayList<DishObject> orderedDishes =
                    shoppingCartController.getOrderedDishes(user.getId());
            MessageEmbed eb = buildEB(restaurantName, orderedDishes);
            event.replyEmbeds(eb).queue();
        }
    }
}
