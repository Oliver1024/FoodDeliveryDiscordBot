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
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class ShowCartCommand implements SlashCommandHandler {

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
        return new CommandData(getName(), "Press enter to show your shopping cart");
    }

    /**
     * Provide general command feature
     *
     * @param event, SlashCommandEvent
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // check user is start a new order or not
        log.info("event: /showcart");
        User user = event.getUser();
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());

        // user is not in shopping cart
        if (restaurantName == null) {
            event.reply(
                            ":slight_frown: You haven't started an order. There is no shopping cart for you!")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        ArrayList<DishObject> orderedDishes = shoppingCartController.getOrderedDishes(user.getId());
        event.replyEmbeds(buildEB(restaurantName, orderedDishes)).queue();
    }

    /**
     * Helper function that check orderedDishes is empty or not, if orderedDishes is empty return
     * price $0.0 otherwise, orderedDishes is not empty return listing of dishes and price and total
     * price
     *
     * @param restaurantName String the name of restaurants
     * @param orderedDishes ArrayList the dishes that users ordered
     * @return return Your shopping cart is empty. Total:$0.0, when orderedDishes is empty.
     *     Otherwise, return listing of dishes and price of dishes and total price of orders.
     */
    protected MessageEmbed buildEB(String restaurantName, ArrayList<DishObject> orderedDishes) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":shopping_cart: Shopping cart:");
        eb.setDescription("Your order at **" + restaurantName + "** includes:");

        Double totalPrice = 0.0;
        // check orderedDishes is empty or not
        // if orderedDishes is empty return 0.0 price
        if (orderedDishes.isEmpty()) {
            eb.addField(
                    ":receipt: Your shopping cart :shopping_cart: is empty. Total:",
                    ":heavy_dollar_sign: 0.0",
                    false);
        }
        // if orderedDishes is not empty return dish and price
        else {
            for (int i = 0; i < orderedDishes.size(); i++) {
                eb.addField(
                        (i + 1) + ". " + orderedDishes.get(i).getDish() + ":",
                        ":heavy_dollar_sign:" + orderedDishes.get(i).getPrice().toString(),
                        false);
                totalPrice += orderedDishes.get(i).getPrice();
            }
            eb.addField(
                    ":receipt: Total:",
                    ":heavy_dollar_sign:" + Math.round(totalPrice * 100.0) / 100.0,
                    false);
        }
        eb.setColor(Color.GREEN);
        return eb.build();
    }
}
