package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.junit.jupiter.api.Test;

public class NewOrderCommandTest {
    NewOrderCommand newOrderCommand = new NewOrderCommand();

    @Test
    void testGetname() {
        String expected = "neworder";
        assertEquals(expected, newOrderCommand.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData commandData = newOrderCommand.getCommandData();
        String expectedDescription = "Start a new order";
        assertEquals(expectedDescription, commandData.getDescription());
    }

    @Test
    void testBuildSelectionMenu() {
        ArrayList<String> restaurantNames = new ArrayList<>();
        restaurantNames.add("restaurant1");
        restaurantNames.add("restaurant2");
        restaurantNames.add("restaurant3");

        SelectionMenu actual = newOrderCommand.buildSelectionMenu(restaurantNames);

        String expectedId = "neworder";
        String expectedPlaceHolder = "Choose the restaurant you want to order";

        assertEquals(expectedId, actual.getId());
        assertEquals(expectedPlaceHolder, actual.getPlaceholder());
        assertEquals(3, actual.getOptions().size());
        assertEquals("restaurant1", actual.getOptions().get(0).getLabel());
        assertEquals("restaurant1", actual.getOptions().get(0).getValue());
        assertEquals("restaurant2", actual.getOptions().get(1).getLabel());
        assertEquals("restaurant2", actual.getOptions().get(1).getValue());
        assertEquals("restaurant3", actual.getOptions().get(2).getLabel());
        assertEquals("restaurant3", actual.getOptions().get(2).getValue());
    }

    @Test
    void buildReplyEmbed() {
        String restaurantName = "Sichuan Food";
        MessageEmbed eb = newOrderCommand.buildReplyEmbed(restaurantName);

        assertEquals("You start a new order at Sichuan Food!", eb.getTitle());

        assertEquals("/order:", eb.getFields().get(0).getName());
        assertEquals(
                "to order a dish by selecting a dish from menu", eb.getFields().get(0).getValue());

        assertEquals("/menu:", eb.getFields().get(1).getName());
        assertEquals(
                "to show the menu of the current restaurant", eb.getFields().get(1).getValue());

        assertEquals("/showcart:", eb.getFields().get(2).getName());
        assertEquals("to show the shopping cart🛒", eb.getFields().get(2).getValue());
    }
}
