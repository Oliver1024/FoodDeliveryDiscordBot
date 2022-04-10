package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import java.util.ArrayList;
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
    void testTreatInput() {
        String testOne = "4 sichuan food";
        String testTwo = "5";
        ArrayList<String> list1 = lastCommand.treatInput(testOne);
        ArrayList<String> list2 = lastCommand.treatInput(testTwo);

        assertEquals("4", list1.get(0));
        assertEquals("sichuan food", list1.get(1));
        assertEquals(1, list2.size());
        assertEquals("5", list2.get(0));
    }

    @Test
    void testCheckStringisDigit() {
        String testOne = "5sichuanfood";
        String testtwo = "12345";

        assertFalse(lastCommand.CheckStringisDigit(testOne));
        assertTrue(lastCommand.CheckStringisDigit(testtwo));
    }

    @Test
    void testCovertoSting() {
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
        assertEquals(expectedString, lastCommand.covertoSting(orderItems1));
    }

    @Test
    void testMessageEmbed() {
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

        String restaurantNameOne = "testOne";
        String restaurantNameTwo = "testtwo";
        Order orderOne = new Order();
        Order orderTwo = new Order();
        ArrayList<Order> testOrders = new ArrayList<>();
        orderOne.setOrderItems(orderItems1);
        orderOne.setRestaurantName(restaurantNameOne);
        orderItems1.remove(0);
        orderTwo.setOrderItems(orderItems1);
        orderTwo.setRestaurantName(restaurantNameTwo);
        //    need to fix

    }
    // void testCheckIfRestrantInDataBase() {
    //     // not yet figure out where to test this method.
    // }
}
