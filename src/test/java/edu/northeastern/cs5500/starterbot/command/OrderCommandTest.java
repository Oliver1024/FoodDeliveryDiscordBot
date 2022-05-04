package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.junit.jupiter.api.Test;

public class OrderCommandTest {
    OrderCommand orderCommand = new OrderCommand();

    @Test
    void testGetname() {
        String expected = "order";
        assertEquals(expected, orderCommand.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData commandData = orderCommand.getCommandData();
        String expectedDescription = "Add a new dish into your cart";
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

        ArrayList<DishObject> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);

        MessageEmbed actual = orderCommand.buildReplyEmbed(dishes, "restaurant1");

        String expectedTitle = ":shopping_cart: Shopping cart:";
        String expectedDescription =
                "**dish2** has been added! Your cart at **restaurant1** includes:";

        assertEquals(expectedTitle, actual.getTitle());
        assertEquals(expectedDescription, actual.getDescription());
        assertEquals(3, actual.getFields().size());

        assertEquals("1. dish1:", actual.getFields().get(0).getName());
        assertEquals("$9.9", actual.getFields().get(0).getValue());

        assertEquals("2. dish2:", actual.getFields().get(1).getName());
        assertEquals("$19.9", actual.getFields().get(1).getValue());
    }

    @Test
    void testBuildSelectionMenu() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        ArrayList<DishObject> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);

        SelectionMenu actual = orderCommand.buildSelectionMenu(dishes);

        assertEquals("order", actual.getId());
        assertEquals("Choose the dish you want to order", actual.getPlaceholder());

        assertEquals(3, actual.getOptions().size());
        assertEquals("Random dish", actual.getOptions().get(0).getLabel());
        assertEquals("random", actual.getOptions().get(0).getValue());

        assertEquals("dish1: $9.9", actual.getOptions().get(1).getLabel());
        assertEquals("dish1", actual.getOptions().get(1).getValue());

        assertEquals("dish2: $19.9", actual.getOptions().get(2).getLabel());
        assertEquals("dish2", actual.getOptions().get(2).getValue());
    }
}
