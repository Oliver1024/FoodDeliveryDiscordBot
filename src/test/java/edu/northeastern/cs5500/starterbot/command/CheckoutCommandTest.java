package edu.northeastern.cs5500.starterbot.command;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

public class CheckoutCommandTest {
    CheckoutCommand checkoutCommand = new CheckoutCommand();

    @Test
    void testGetName() {
        String expected = "checkout";
        assertEquals(expected, checkoutCommand.getName());
    }

    @Test
    void testGetCommandData() {
        String expectedDescrption = "press enter to checkout";
        assertEquals(expectedDescrption, checkoutCommand.getCommandData().getDescription());
    }

    @Test
    void testBuildEB() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        ArrayList<DishObject> orderedDishes = new ArrayList<>();
        orderedDishes.add(dish1);
        orderedDishes.add(dish2);

        MessageEmbed eb = checkoutCommand.buildEB("restaurant1", orderedDishes);

        String expectedTitle = "Thanks for ordering!";
        assertEquals(expectedTitle, eb.getTitle());

        String expectedDescrption = "Your order at **" + "restaurant1" + "** includes:";
        assertEquals(expectedDescrption, eb.getDescription());

        assertTrue(eb.getFields().size() == 3);

        String expectedFieldName1 = "1. dish1";
        String expectedFieldValue1 = "$9.9";
        String expectedFieldName2 = "2. dish2";
        String expectedFieldValue2 = "$19.9";
        String expectedFieldValue3 = "$29.8";

        assertEquals(expectedFieldName1, eb.getFields().get(0).getName());
        assertEquals(expectedFieldValue1, eb.getFields().get(0).getValue());
        assertEquals(expectedFieldName2, eb.getFields().get(1).getName());
        assertEquals(expectedFieldValue2, eb.getFields().get(1).getValue());
        assertEquals("Total:", eb.getFields().get(2).getName());
        assertEquals(expectedFieldValue3, eb.getFields().get(2).getValue());
    }
}
