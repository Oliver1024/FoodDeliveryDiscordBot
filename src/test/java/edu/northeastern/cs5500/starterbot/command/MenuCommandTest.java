package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class MenuCommandTest {
    MenuCommand menuCommands = new MenuCommand();

    @Test
    void testGetname() {
        String expected = "menu";
        assertEquals(expected, menuCommands.getName());
    }

    @Test
    void testGetCommandData() {
        String name = menuCommands.getName();
        CommandData commandData = menuCommands.getCommandData();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testMessageEmbed() {
        ArrayList<DishObject> menu = new ArrayList<>();
        DishObject dishObject = new DishObject();
        dishObject.setDish("dish1");
        dishObject.setPrice(11.99);
        menu.add(dishObject);
        String restaurantName = "Sichuan Food";
        String expectedTitle = ":scroll: Sichuan Food's menu: ";

        MessageEmbed eb = menuCommands.buildReplyEmbed(menu, restaurantName);

        assertTrue(eb.getFields().size() == 1);
        assertEquals(expectedTitle, eb.getTitle());
        assertEquals("1. dish1:", eb.getFields().get(0).getName());
        assertEquals("$11.99", eb.getFields().get(0).getValue());
        assertTrue(eb.getFields().get(0).isInline());
    }
}
