package edu.northeastern.cs5500.starterbot.command;

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

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /checkout");
        User user = event.getUser();
        ShoppingCart shoppingCart = shoppingCartController.getShoppingCart(user.getId());

        if (shoppingCart == null) {
            event.reply(
                            ":slight_frown: You don't have an order! Please user /neworder restaurant_name")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        MessageEmbed eb = buildEB(shoppingCart.getRestaurantName(), shoppingCart.getOrderItems());
        userController.addOrder(shoppingCart);
        shoppingCartController.deleteCart(user.getId());
        event.replyEmbeds(eb).queue();
    }

    protected MessageEmbed buildEB(String restaurantName, ArrayList<DishObject> orderedDishes) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":grin: Thanks for ordering!");
        eb.setDescription("Your order at **" + restaurantName + "** includes:");

        Double totalPrice = 0.0;

        for (int i = 0; i < orderedDishes.size(); i++) {
            eb.addField(
                    (i + 1) + ". " + orderedDishes.get(i).getDish(),
                    ":heavy_dollar_sign:" + orderedDishes.get(i).getPrice().toString(),
                    false);
            totalPrice += orderedDishes.get(i).getPrice();
        }
        eb.addField(
                ":receipt: Total:",
                ":heavy_dollar_sign:" + Math.round(totalPrice * 100.0) / 100.0,
                false);
        eb.setColor(Color.PINK);
        return eb.build();
    }
}
