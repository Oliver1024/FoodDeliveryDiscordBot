package edu.northeastern.cs5500.starterbot.command;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import edu.northeastern.cs5500.starterbot.model.Restaurant;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.junit.jupiter.api.Test;

public class InfoCommandTest {
    InfoCommand infoCommand = new InfoCommand();

    @Test
    void testGetName() {
        String expected = "info";
        assertEquals(expected, infoCommand.getName());
    }

    @Test
    void testGetCommandData() {
        String expectedDescrption = "/info to check the info of the restaurant you are ordering at";
        assertEquals(expectedDescrption, infoCommand.getCommandData().getDescription());

        String expectedOptionContent =
                "'/info restaurant_name' to check the info of the given restaurant";
        List<OptionData> options = infoCommand.getCommandData().getOptions();
        assertTrue(options.size() == 1);
        assertEquals(expectedOptionContent, options.get(0).getDescription());
    }

    @Test
    void testBuildReplyEmbed() {
        ArrayList<String> cuisineTypes = new ArrayList<>();
        cuisineTypes.add("Asian");
        cuisineTypes.add("Chinese");
        Restaurant restaurant = new Restaurant();
        restaurant.setName("resaurant1");
        restaurant.setContact((long) 2061112222);
        restaurant.setAddress("222 Ave");
        restaurant.setCuisineType(cuisineTypes);

        MessageEmbed eb = infoCommand.buildReplyEmbed(restaurant);

        String expectedTitle = "resaurant1's info";
        assertEquals(expectedTitle, eb.getTitle());

        assertEquals("Contact:", eb.getFields().get(0).getName());
        assertEquals("2061112222", eb.getFields().get(0).getValue());
        assertFalse(eb.getFields().get(0).isInline());

        assertEquals("Address:", eb.getFields().get(1).getName());
        assertEquals("222 Ave", eb.getFields().get(1).getValue());
        assertFalse(eb.getFields().get(1).isInline());

        assertEquals("Cuisine Type:", eb.getFields().get(2).getName());
        assertEquals("Asian, Chinese", eb.getFields().get(2).getValue());
        assertFalse(eb.getFields().get(2).isInline());
    }
}
