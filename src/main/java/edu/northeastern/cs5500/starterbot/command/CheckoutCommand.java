package edu.northeastern.cs5500.starterbot.command;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.controller.UserController;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
import java.awt.Color;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class CheckoutCommand implements SlashCommandHandler {

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

    /**
     * Respond to user's command input
     *
     * @param event, SlashCommandEvent
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /checkout");
        User user = event.getUser();
        ShoppingCart shoppingCart = shoppingCartController.getShoppingCart(user.getId());

        String message = messageForNoOrderOrNoDishes(shoppingCart);
        if (message != null) {
            event.reply(message).setEphemeral(true).queue();
            return;
        }
        MessageEmbed eb = buildEB(shoppingCart.getRestaurantName(), shoppingCart.getOrderItems());
        userController.addOrder(shoppingCart);
        shoppingCartController.deleteCart(user.getId());
        event.replyEmbeds(eb).queue();
    }

    /**
     * Helper function which builds the reply message when the user doesn't have a shopping cart or
     * the shopping cart is empty.
     *
     * @param shoppingCart the user's shopping cart
     * @return String
     */
    @Nullable
    protected String messageForNoOrderOrNoDishes(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            return ":slight_frown: You don't have an ongoing order! Please use '/neworder' to start an order";
        } else if (shoppingCart.getOrderItems().size() == 0) {
            return ":slight_frown: You don't have any dishes in your shopping cart! Please use '/order' to order dishes";
        }
        return null;
    }

    /**
     * Build a reply embed to show the order after the user check out the order.
     *
     * @param restaurantName the restaurant the guild is ordering at
     * @param orderedDishes the dishes the users have ordered
     * @return a MessageEmbed object
     */
    protected MessageEmbed buildEB(String restaurantName, ArrayList<DishObject> orderedDishes) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":grin: Thanks for ordering!");
        eb.setDescription(String.format("Your order at **%s** includes:", restaurantName));

        Double totalPrice = 0.0;

        for (int i = 0; i < orderedDishes.size(); i++) {
            String strForDish = String.format("%d. %s", i + 1, orderedDishes.get(i).getDish());
            String strForPrice = String.format("$%s", orderedDishes.get(i).getPrice().toString());
            eb.addField(strForDish, strForPrice, false);
            totalPrice += orderedDishes.get(i).getPrice();
        }
        eb.addField(":receipt: Total:", "$" + Math.round(totalPrice * 100.0) / 100.0, false);
        eb.setColor(Color.PINK);
        return eb.build();
    }
}
