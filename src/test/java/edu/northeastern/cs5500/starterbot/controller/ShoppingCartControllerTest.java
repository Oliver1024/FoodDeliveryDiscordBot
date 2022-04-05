package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import java.util.Collection;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public class ShoppingCartControllerTest {
    ShoppingCartController shoppingCartController =
            new ShoppingCartController(new InMemoryRepository<>());

    @Test
    void testIsUserInShoppingCart() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);

        assertTrue(shoppingCartController.isUserInShoppingCart("user1"));
        assertFalse(shoppingCartController.isUserInShoppingCart("user2"));
    }

    @Test
    void testCreateNewShoppingCart() {
        Collection<ShoppingCart> actual = shoppingCartController.shoppingCartRepository.getAll();
        assertTrue(actual.size() == 0);

        String userId1 = "6227b0779744ecb0c23a772e";
        String username1 = "Wen";
        String restaurantName1 = "restaurant 1";
        shoppingCartController.createNewShoppingCart(userId1, username1, restaurantName1);

        actual = shoppingCartController.shoppingCartRepository.getAll();
        assertTrue(actual.size() == 1);
    }
}
