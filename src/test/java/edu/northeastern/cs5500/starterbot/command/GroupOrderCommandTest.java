package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.DishUserPair;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.junit.jupiter.api.Test;

public class GroupOrderCommandTest {
    GroupOrderCommand groupOrderCommand = new GroupOrderCommand();

    @Test
    void testGetname() {
        String expected = "grouporder";
        assertEquals(expected, groupOrderCommand.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData commandData = groupOrderCommand.getCommandData();
        String expectedDescription = "Start a group order at the current discord guild";
        assertEquals(expectedDescription, commandData.getDescription());
    }

    @Test
    void testReplyMessForSelection() {
        String guildId = "guild123";
        String userId = "user123";
        String restaurant = "restaurant1";
        String dish = "dish1";

        String expected1 = "click 'Submit' to order at restaurant1";
        String actual1 =
                groupOrderCommand.replyMessForSelection(false, restaurant, guildId, userId);
        assertEquals(expected1, actual1);

        String expected2 = "click 'Submit' to order dish1";
        String actual2 = groupOrderCommand.replyMessForSelection(true, dish, guildId, userId);
        assertEquals(expected2, actual2);
    }

    @Test
    void testBuildRestaurantSelection() {
        ArrayList<String> restaurantNames = new ArrayList<>();
        restaurantNames.add("restaurant1");
        restaurantNames.add("restaurant2");
        restaurantNames.add("restaurant3");

        SelectionMenu actual = groupOrderCommand.buildRestaurantSelection(restaurantNames);

        String expectedId = "grouporder";
        String expectedPlaceHolder = "Choose the restaurant your guild want to order";

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
    void testBuildDishSelection() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        ArrayList<DishObject> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);

        SelectionMenu actual = groupOrderCommand.buildDishSelection(dishes);

        assertEquals("grouporder", actual.getId());
        assertEquals(
                "Group Order! Please Choose the dish you want to order", actual.getPlaceholder());

        assertEquals(2, actual.getOptions().size());
        assertEquals("dish1: 💲9.9", actual.getOptions().get(0).getLabel());
        assertEquals("dish1", actual.getOptions().get(0).getValue());

        assertEquals("dish2: 💲19.9", actual.getOptions().get(1).getLabel());
        assertEquals("dish2", actual.getOptions().get(1).getValue());
    }

    @Test
    void testBuildReplyEmbed() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishUserPair pair1 = new DishUserPair();
        pair1.setDish(dish1);
        pair1.setUserId("userId1");
        pair1.setUsername("username1");

        DishUserPair pair2 = new DishUserPair();
        pair2.setDish(dish2);
        pair2.setUserId("userId2");
        pair2.setUsername("username2");

        ArrayList<DishUserPair> dishes = new ArrayList<>();
        dishes.add(pair1);
        dishes.add(pair2);

        MessageEmbed actual = groupOrderCommand.buildReplyEmbed(dishes, "restaurant1");

        String expectedTitle = "🛒 Guild shopping cart:";
        String expectedDescription =
                "**username2** has added **dish2** into the guild shopping cart at **restaurant1**";

        assertEquals(expectedTitle, actual.getTitle());
        assertEquals(expectedDescription, actual.getDescription());
        assertEquals(3, actual.getFields().size());

        assertEquals("1. dish1: 💲9.9", actual.getFields().get(0).getName());
        assertEquals("add by username1", actual.getFields().get(0).getValue());

        assertEquals("2. dish2: 💲19.9", actual.getFields().get(1).getName());
        assertEquals("add by username2", actual.getFields().get(1).getValue());

        assertEquals("🧾 Total:", actual.getFields().get(2).getName());
        assertEquals("💲29.8", actual.getFields().get(2).getValue());
    }
}
