package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.junit.jupiter.api.Test;

public class RestaurantsCommandTest {
    RestaurantsCommand restaurantsCommand = new RestaurantsCommand();

    @Test
    void testGetName() {
        String expected = "restaurants";
        assertEquals(expected, restaurantsCommand.getName());
    }

    @Test
    void testGetCommandData() {
        String expectedDescrption =
                "'/restaurants' or '/restaurants cuisine_type' to show restaurants you can order at";
        assertEquals(expectedDescrption, restaurantsCommand.getCommandData().getDescription());

        String expectedOptionContent =
                "'/restaurants cuisine_type' to show restaurants with the particular cuisine type";
        List<OptionData> options = restaurantsCommand.getCommandData().getOptions();
        assertTrue(options.size() == 1);
        assertEquals(expectedOptionContent, options.get(0).getDescription());
    }

    @Test
    void testBuildReplyMessage() {
        ArrayList<String> testNames = new ArrayList<>();
        testNames.add("restaurant1");
        testNames.add("restaurant2");
        String expected1 =
                "```Below is the list of all restaurants you can order at:\n\nrestaurant1\nrestaurant2\n```";
        assertEquals(expected1, restaurantsCommand.buildReplyMessage(testNames, null));

        String cuisineType1 = "Asian";
        String expected2 =
                "```Below is the list of all Asian restaurants you can order at:\n\nrestaurant1\nrestaurant2\n```";
        assertEquals(expected2, restaurantsCommand.buildReplyMessage(testNames, cuisineType1));

        String cuisineType2 = "A";
        String expected3 =
                "```Below is the list of all A restaurants you can order at:\n\nrestaurant1\nrestaurant2\n```";
        assertEquals(expected3, restaurantsCommand.buildReplyMessage(testNames, cuisineType2));
    }
}
