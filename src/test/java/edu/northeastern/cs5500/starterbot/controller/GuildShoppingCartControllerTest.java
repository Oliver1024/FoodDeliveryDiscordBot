package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.DishUserPair;
import edu.northeastern.cs5500.starterbot.model.GuildShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class GuildShoppingCartControllerTest {

    GuildShoppingCartController guildShoppingCartController =
            new GuildShoppingCartController(new InMemoryRepository<>());

    @Test
    void testIsGuildInShoppingCart() {
        String testGuildId = "12345";
        assertFalse(guildShoppingCartController.isGuildInShoppingCart(testGuildId));

        String restaurantName = "restaurant1";
        String createdUserId = "1234";
        guildShoppingCartController.createCart(testGuildId, restaurantName, createdUserId);
        assertTrue(guildShoppingCartController.isGuildInShoppingCart(testGuildId));
        assertFalse(guildShoppingCartController.isGuildInShoppingCart("111"));
    }

    @Test
    void testGetCart() {
        GuildShoppingCart actual1 = guildShoppingCartController.getCart("123");
        assertNull(actual1);

        String guildId = "guild12345";
        String restaurantName = "restaurant1";
        String createdUserId = "user1234";

        guildShoppingCartController.createCart(guildId, restaurantName, createdUserId);
        GuildShoppingCart actual2 = guildShoppingCartController.getCart(guildId);
        assertEquals(guildId, actual2.getGuildId());
        assertEquals(restaurantName, actual2.getRestaurantName());
        assertEquals(createdUserId, actual2.getCreatedUserId());

        GuildShoppingCart actual3 = guildShoppingCartController.getCart("111");
        assertNull(actual3);
    }

    @Test
    void testAddDish() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        String guildId = "guild12345";
        String restaurantName = "restaurant1";
        String createdUserId = "user1234";
        guildShoppingCartController.createCart(guildId, restaurantName, createdUserId);

        String orderUserId = "user22";
        String orderUsername = "username";

        assertEquals(0, guildShoppingCartController.getCart(guildId).getDishes().size());
        ArrayList<DishUserPair> actual =
                guildShoppingCartController.addDish(dish1, guildId, orderUserId, orderUsername);
        assertEquals(1, actual.size());
        assertEquals("dish1", actual.get(0).getDish().getDish());
        assertEquals(9.9, actual.get(0).getDish().getPrice());
        assertEquals(orderUserId, actual.get(0).getUserId());
        assertEquals(orderUsername, actual.get(0).getUsername());

        ArrayList<DishUserPair> actual2 =
                guildShoppingCartController.addDish(dish1, "123", orderUserId, orderUsername);
        assertNull(actual2);
    }

    @Test
    void testMatchCreateUserId() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        String guildId = "guild12345";
        String restaurantName = "restaurant1";
        String createdUserId = "user1234";
        guildShoppingCartController.createCart(guildId, restaurantName, createdUserId);
        String createdUserId2 = "user12344";

        assertTrue(guildShoppingCartController.matchCreateUserId(createdUserId, guildId));
        assertFalse(guildShoppingCartController.matchCreateUserId(createdUserId2, guildId));
    }

    @Test
    void testDeleteCart() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        String guildId = "guild12345";
        String restaurantName = "restaurant1";
        String createdUserId = "user1234";
        guildShoppingCartController.createCart(guildId, restaurantName, createdUserId);
        assertNotNull(guildShoppingCartController.getCart(guildId));
        guildShoppingCartController.deleteCart(guildId);
        assertNull(guildShoppingCartController.getCart(guildId));
    }
}
