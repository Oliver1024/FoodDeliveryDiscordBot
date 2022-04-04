package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
