package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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
        String expected = "Show the list of all restaurants you can order at";
        assertEquals(expected, restaurantsCommand.getCommandData().getDescription());
    }

    @Test
    void testBuildReplyMessage() {
        ArrayList<String> testNames = new ArrayList<>();
        testNames.add("restaurant1");
        testNames.add("restaurant2");
        String expected =
                "```Below is the list of all restaurants you can order at:\n\nrestaurant1\nrestaurant2\n```";
        assertEquals(expected, restaurantsCommand.buildReplyMessage(testNames));
    }
}
