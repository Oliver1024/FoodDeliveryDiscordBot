package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

public class ShowCartCommandTest {
    ShowCartCommand showCartCommand = new ShowCartCommand();

    @Test
    void testGetName() {
        String expected = "showcart";
        assertEquals(expected, showCartCommand.getName());
    }

    @Test
    void testGetCommandData() {
        String expected = "Press enter to show your shopping cart :shopping_cart:";
        assertEquals(expected, showCartCommand.getCommandData().getDescription());
    }

    @Test
    void testOrderDishIsEmpty() {
        ArrayList<DishObject> testOrderDishes = new ArrayList<>();
        String testRestaurantName = "restaurant1";
        String expectedTitle = "Shopping cart :shopping_cart::";
        String expectedDescription = "Your order at **restaurant1** includes:";
        MessageEmbed embed = showCartCommand.buildEB(testRestaurantName, testOrderDishes);
        assertEquals(expectedTitle, embed.getTitle());
        assertEquals(expectedDescription, embed.getDescription());
        String expectedName = ":receipt: Your shopping cart :shopping_cart: is empty. Total:";
        assertEquals(expectedName, embed.getFields().get(0).getName());
        String expectedValue = ":heavy_dollar_sign: 0.0";
        assertEquals(expectedValue, embed.getFields().get(0).getValue());
        Boolean expectedBoolean = false;
        assertEquals(expectedBoolean, embed.getFields().get(0).isInline());
    }

    @Test
    void testOrderDishIsNotEmpty() {
        String testRestaurantName = "restaurant2";
        ArrayList<DishObject> testOrderDishes = new ArrayList<>();
        DishObject expectedDish = new DishObject();
        expectedDish.setDish("hotpot");
        expectedDish.setPrice(29.9);
        testOrderDishes.add(expectedDish);
        MessageEmbed embed = showCartCommand.buildEB(testRestaurantName, testOrderDishes);
        String expectedName = "1. hotpot:";
        assertEquals(expectedName, embed.getFields().get(0).getName());
        String expectedValue = ":heavy_dollar_sign:29.9";
        assertEquals(expectedValue, embed.getFields().get(0).getValue());
        Boolean expectedBoolean = false;
        assertEquals(expectedBoolean, embed.getFields().get(0).isInline());
    }
}
