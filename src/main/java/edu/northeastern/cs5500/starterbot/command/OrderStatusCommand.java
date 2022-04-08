package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.UserController;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import java.awt.Color;
import java.time.Duration;
import java.time.LocalDateTime;
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
public class OrderStatusCommand implements Command {
    @Inject UserController userController;

    @Inject
    public OrderStatusCommand() {}

    @Override
    public String getName() {
        return "orderstatus";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Check your order status");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /orderstatus");
        User user = event.getUser();
        ArrayList<Order> undeliveredOrders = userController.getUndelivedOrders(user.getId());
        MessageEmbed replyEmbed = buildReplyEmbed(undeliveredOrders);
        userController.deliverOrders(user.getId());
        event.replyEmbeds(replyEmbed).queue();
    }

    /**
     * build the embed with all the information that needed to be replied to the discord user
     *
     * @param undeliveredOrders the user's all undelivered orders (order.isDelivered is false)
     * @return the MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(ArrayList<Order> undeliveredOrders) {
        EmbedBuilder eb = new EmbedBuilder();
        if (undeliveredOrders == null || undeliveredOrders.size() == 0) {
            eb.setTitle("You don't have any undelivered orders");
        } else {
            for (Order order : undeliveredOrders) {
                String name =
                        "Your order at "
                                + order.getRestaurantName()
                                + " "
                                + buildOrderStatusString(order);
                String value = "Ordered dishes: " + buildDishesString(order.getOrderItems());
                eb.addField(name, value, false);
            }
        }
        eb.setColor(Color.MAGENTA);
        return eb.build();
    }

    /**
     * Helper function which builds a string with all the name of dishes
     *
     * @param orderItems an arraylist of ordered items.
     * @return a string
     */
    private String buildDishesString(ArrayList<DishObject> orderItems) {
        String dishesString = "";
        for (int i = 0; i < orderItems.size(); i++) {
            dishesString += orderItems.get(i).getDish();
            if (i != orderItems.size() - 1) {
                dishesString += ", ";
            }
        }
        return dishesString;
    }

    /**
     * Helper function which checks the order status and build the string for the order status
     *
     * @param order Order object needed to be checked
     * @return a string
     */
    private String buildOrderStatusString(Order order) {
        int TIME_PER_DISH = 5;
        int TIME_FOR_DELIVER = 20;
        long passedMins = Duration.between(order.getOrderTime(), LocalDateTime.now()).toMinutes();
        long prepareTime = order.getOrderItems().size() * TIME_PER_DISH;
        if (passedMins <= prepareTime) {
            return "is preparing";
        } else if (passedMins <= prepareTime + TIME_FOR_DELIVER) {
            return "is being delivered by a driver";
        } else {
            return "is delivered";
        }
    }
}
