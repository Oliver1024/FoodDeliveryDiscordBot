package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import java.time.LocalDateTime;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

public class OrderStatusCommandTest {
    OrderStatusCommand orderStatusCommand = new OrderStatusCommand();

    @Test
    void testGetName() {
        String expected = "orderstatus";
        assertEquals(expected, orderStatusCommand.getName());
    }

    @Test
    void testGetCommandData() {
        String expectedDescrption = "Check your order status";
        assertEquals(expectedDescrption, orderStatusCommand.getCommandData().getDescription());
    }

    @Test
    void testBuildReplyEmbedWithNullInput() {
        MessageEmbed actual = orderStatusCommand.buildReplyEmbed(null);
        String expectedTitle = "You don't have any undelivered orders";
        assertEquals(expectedTitle, actual.getTitle());
    }

    @Test
    void testBuildReplyEmbedWithEmptyArrayList() {
        ArrayList<Order> orders = new ArrayList<>();
        MessageEmbed actual = orderStatusCommand.buildReplyEmbed(orders);
        String expectedTitle = "You don't have any undelivered orders";
        assertEquals(expectedTitle, actual.getTitle());
    }

    @Test
    void testBuildReplyEmbedWithOrders() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(7.9);

        DishObject dish4 = new DishObject();
        dish4.setDish("dish4");
        dish4.setPrice(22.9);

        ArrayList<DishObject> orderItems1 = new ArrayList<>();
        orderItems1.add(dish1);
        orderItems1.add(dish2);

        ArrayList<DishObject> orderItems2 = new ArrayList<>();
        orderItems2.add(dish3);

        ArrayList<DishObject> orderItems3 = new ArrayList<>();
        orderItems3.add(dish4);

        Order order1 = new Order();
        order1.setIsDelivered(false);
        order1.setOrderTime(LocalDateTime.now().minusMinutes(2));
        order1.setOrderItems(orderItems1);
        order1.setRestaurantName("restaurant1");

        Order order2 = new Order();
        order2.setIsDelivered(false);
        order2.setOrderTime(LocalDateTime.now().minusMinutes(10));
        order2.setOrderItems(orderItems2);
        order2.setRestaurantName("restaurant2");

        Order order3 = new Order();
        order3.setIsDelivered(false);
        order3.setOrderTime(LocalDateTime.now().minusMinutes(30));
        order3.setOrderItems(orderItems3);
        order3.setRestaurantName("restaurant3");

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        MessageEmbed actual = orderStatusCommand.buildReplyEmbed(orders);
        String expectedFieldName1 = "Your order at restaurant1 is preparing🧑‍🍳";
        String expectedFiledValue1 = "Ordered dishes: dish1, dish2";
        String expectedFieldName2 = "Your order at restaurant2 is being delivered by a driver🚗";
        String expectedFiledValue2 = "Ordered dishes: dish3";
        String expectedFieldName3 = "Your order at restaurant3 is delivered👍";
        String expectedFiledValue3 = "Ordered dishes: dish4";

        assertTrue(actual.getFields().size() == 3);
        assertEquals(expectedFieldName1, actual.getFields().get(0).getName());
        assertEquals(expectedFiledValue1, actual.getFields().get(0).getValue());

        assertEquals(expectedFieldName2, actual.getFields().get(1).getName());
        assertEquals(expectedFiledValue2, actual.getFields().get(1).getValue());

        assertEquals(expectedFieldName3, actual.getFields().get(2).getName());
        assertEquals(expectedFiledValue3, actual.getFields().get(2).getValue());
    }
}
