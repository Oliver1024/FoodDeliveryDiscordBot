package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
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
        String expectedDescription = "Input the restaurant name";
        String expectedOptionDescription =
                "The bot will start a new order for you at the given restaurant";
        assertEquals(expectedDescription, commandData.getDescription());
        assertEquals("content", commandData.getOptions().get(0).getName());
        assertEquals(expectedOptionDescription, commandData.getOptions().get(0).getDescription());
    }

    @Test
    void buildReplyEmbed() {
        String restaurantName = "Sichuan Food";
        MessageEmbed eb = newOrderCommand.buildReplyEmbed(restaurantName);

        assertEquals("You start a new order at Sichuan Food!", eb.getTitle());

        assertEquals("/order dish_name:", eb.getFields().get(0).getName());
        assertEquals("to order a particular dish", eb.getFields().get(0).getValue());

        assertEquals("/order dish_num:", eb.getFields().get(1).getName());
        assertEquals("to order a dish with the given number", eb.getFields().get(1).getValue());

        assertEquals("/menu:", eb.getFields().get(2).getName());
        assertEquals(
                "to show the menu of the current restaurant", eb.getFields().get(2).getValue());

        assertEquals("/showcart:", eb.getFields().get(3).getName());
        assertEquals("to show the shopping cart", eb.getFields().get(3).getValue());
    }
}
