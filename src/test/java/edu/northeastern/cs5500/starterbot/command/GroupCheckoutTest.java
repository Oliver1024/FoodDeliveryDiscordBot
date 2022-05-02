package edu.northeastern.cs5500.starterbot.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.DishUserPair;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

public class GroupCheckoutTest {
    GroupCheckoutCommand groupCheckoutCommand = new GroupCheckoutCommand();

    @Test
    void TestGetName() {
        String expected = "groupcheckout";
        assertEquals(expected, groupCheckoutCommand.getName());
    }

    @Test
    void TestGetCommandData() {
        String expectedDescription = "press enter to groupcheckout";
        assertEquals(expectedDescription, groupCheckoutCommand.getCommandData().getDescription());
    }

    @Test
    void TestBuildEB() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishUserPair pair1 = new DishUserPair();
        pair1.setDish(dish1);
        pair1.setUserId("userId1");
        pair1.setUsername("username1");

        DishUserPair pair2 = new DishUserPair();
        pair2.setDish(dish2);
        pair2.setUserId("userId2");
        pair2.setUsername("username2");

        ArrayList<DishUserPair> dishes = new ArrayList<>();
        dishes.add(pair1);
        dishes.add(pair2);

        MessageEmbed actual = groupCheckoutCommand.buildEB("restaurant1", dishes);
        String expectedTitle = "Thanks for ordering! :grin:";
        String expectedDescription = "Your order at **restaurant1** includes:";

        assertEquals(expectedTitle, actual.getTitle());
        assertEquals(expectedDescription, actual.getDescription());
        assertTrue(actual.getFields().size() == 3);
        assertEquals("1. dish1. Order by username1", actual.getFields().get(0).getName());
        assertEquals(":heavy_dollar_sign:9.9", actual.getFields().get(0).getValue());
        assertEquals(":receipt: Total:", actual.getFields().get(2).getName());
        assertEquals(":heavy_dollar_sign:29.8", actual.getFields().get(2).getValue());
    }
}
