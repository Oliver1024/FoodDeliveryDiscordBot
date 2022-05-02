package edu.northeastern.cs5500.starterbot.command;

import static org.junit.Assert.assertEquals;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.junit.jupiter.api.Test;

public class DeleteCommandTest {
    DeleteCommand deleteCommand = new DeleteCommand();

    @Test
    void testGetname() {
        String expected = "delete";
        assertEquals(expected, deleteCommand.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData commandData = deleteCommand.getCommandData();
        String expectedDescription = "Delete a dish from current shopping cart";
        assertEquals(expectedDescription, commandData.getDescription());
    }

    @Test
    void testBuildReplyEmbed() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(29.9);

        ArrayList<DishObject> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);

        MessageEmbed actual = deleteCommand.buildReplyEmbed("dish4", dishes, "restaurant1");
        String expectedTitle = ":shopping_cart: Shopping cart:";
        String expectedDescription =
                "**dish4** has been removed! Your cart at **restaurant1** include:";

        assertEquals(expectedTitle, actual.getTitle());
        assertEquals(expectedDescription, actual.getDescription());
        assertEquals(4, actual.getFields().size());

        assertEquals("1. dish1:", actual.getFields().get(0).getName());
        assertEquals(":heavy_dollar_sign:9.9", actual.getFields().get(0).getValue());

        assertEquals("2. dish2:", actual.getFields().get(1).getName());
        assertEquals(":heavy_dollar_sign:19.9", actual.getFields().get(1).getValue());

        assertEquals(":heavy_dollar_sign:59.7", actual.getFields().get(3).getValue());
    }

    @Test
    void testBuildSelectionMenu() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(29.9);

        ArrayList<DishObject> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);

        SelectionMenu actual = deleteCommand.buildSelectionMenu(dishes);
        assertEquals("delete", actual.getId());
        assertEquals(
                "Choose the dish you want to remove from your shopping cart",
                actual.getPlaceholder());

        assertEquals(3, actual.getOptions().size());
        assertEquals("dish1: $9.9", actual.getOptions().get(0).getLabel());
        assertEquals("dish1", actual.getOptions().get(0).getValue());

        assertEquals("dish2: $19.9", actual.getOptions().get(1).getLabel());
        assertEquals("dish2", actual.getOptions().get(1).getValue());

        assertEquals("dish3: $29.9", actual.getOptions().get(2).getLabel());
        assertEquals("dish3", actual.getOptions().get(2).getValue());
    }
}
