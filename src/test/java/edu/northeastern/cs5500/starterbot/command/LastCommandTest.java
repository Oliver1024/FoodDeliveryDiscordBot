package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class LastCommandTest {
    LastCommand lastCommand = new LastCommand();

    @Test
    void testGetname() {
        String expected = "last";
        assertEquals(expected, lastCommand.getName());
    }

    @Test
    void testGetCommandData() {
        String name = lastCommand.getName();
        CommandData commandData = lastCommand.getCommandData();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testProcessUserInput() {
        String testOne = "4 sichuan food";
        String testTwo = "5";
        ArrayList<String> list1 = lastCommand.processUserInput(testOne);
        ArrayList<String> list2 = lastCommand.processUserInput(testTwo);

        assertEquals("4", list1.get(0));
        assertEquals("sichuan food", list1.get(1));
        assertEquals(1, list2.size());
        assertEquals("5", list2.get(0));
    }

    @Test
    void testCheckIfStringIsNumber() {
        String testOne = "5sichuanfood";
        String testTwo = "12345";

        assertFalse(lastCommand.checkIfStringIsNumber(testOne));
        assertTrue(lastCommand.checkIfStringIsNumber(testTwo));
    }

    @Test
    void testProcessTimeString() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.MARCH, 8, 20, 44, 55, 3);
        String expectedTimeString = "2022-03-08 20:44";
        assertEquals(expectedTimeString, lastCommand.processTimeString(localDateTime));
        LocalDateTime localDateTimeOne = LocalDateTime.of(2022, Month.MARCH, 8, 20, 44);
        assertEquals(expectedTimeString, lastCommand.processTimeString(localDateTimeOne));
    }

    @Test
    void testBuildOrderedDishesString() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(7.9);

        ArrayList<DishObject> orderItems1 = new ArrayList<>();
        orderItems1.add(dish1);
        orderItems1.add(dish2);
        orderItems1.add(dish3);

        String expectedString = "dish1, dish2, dish3";
        assertEquals(expectedString, lastCommand.buildOrderedDishesString(orderItems1));
    }

    @Test
    void testCheckInput() {
        ArrayList<String> processedInputTestOne = new ArrayList<>();
        processedInputTestOne.add("12a");
        String exceptErrorMessageOne =
                "Invalid number input, please type '/last num_k' or '/last num_k restaurant_name' to check your history orders";
        assertEquals(exceptErrorMessageOne, lastCommand.checkInput(processedInputTestOne, null));

        ArrayList<String> restaurants = new ArrayList<>();
        restaurants.add("restaurantOne");
        restaurants.add("restaurantTwo");

        processedInputTestOne.clear();
        processedInputTestOne.add("12");
        processedInputTestOne.add("restaurant");

        String exceptErrorMessageTwo =
                "The restaurant name you provide doesn't match any restaurants we have. Pease type"
                        + "'/last num_k' or '/last num_k restaurant_name' to check your history orders.";

        assertEquals(
                exceptErrorMessageTwo, lastCommand.checkInput(processedInputTestOne, restaurants));

        processedInputTestOne.clear();
        processedInputTestOne.add("12");
        processedInputTestOne.add("restaurantOne");
        assertNull(lastCommand.checkInput(processedInputTestOne, restaurants));
    }

    @Test
    void testMessageEmbed() {
        ArrayList<Order> testOrders = new ArrayList<>();
        String errorMessage =
                "the name of restaurant you typed do not have any order before, you can type /restaurants to check other restaurants";
        MessageEmbed eb = lastCommand.buildReplyEmbed(testOrders);
        assertEquals(errorMessage, eb.getTitle());

        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(7.9);

        ArrayList<DishObject> orderItems1 = new ArrayList<>();
        orderItems1.add(dish1);
        orderItems1.add(dish2);
        orderItems1.add(dish3);
        ArrayList<DishObject> orderItems2 = new ArrayList<>();
        orderItems2.add(dish2);
        orderItems2.add(dish3);

        Order orderOne = new Order();
        Order orderTwo = new Order();
        orderOne.setOrderItems(orderItems1);
        orderTwo.setOrderItems(orderItems2);
        orderOne.setRestaurantName("testOne");
        orderTwo.setRestaurantName("testTwo");

        orderOne.setOrderTime(LocalDateTime.of(2022, Month.MARCH, 8, 20, 44, 55, 3));
        orderTwo.setOrderTime(LocalDateTime.of(2022, Month.APRIL, 9, 05, 33, 55, 3));

        testOrders.add(orderOne);
        testOrders.add(orderTwo);
        testOrders.add(orderTwo);

        eb = lastCommand.buildReplyEmbed(testOrders);
        assertEquals("3. testOne, 2022-03-08 20:44,", eb.getFields().get(2).getName());
        assertEquals("2. testTwo, 2022-04-09 05:33,", eb.getFields().get(1).getName());
        assertEquals("dish1, dish2, dish3", eb.getFields().get(2).getValue());
        assertFalse(eb.getFields().get(1).isInline());
        assertEquals(3, eb.getFields().size());
    }
}
