package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @Test
    void testGetRestaurantNameIsNull() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        assertEquals(null, shoppingCartController.getRestaurantName("user1"));
    }

    @Test
    void testGetRestaurantNameUseIdNotMatch() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);
        assertEquals(null, shoppingCartController.getRestaurantName("user2"));
    }

    @Test
    void testGetRestaurantName() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);
        String restaurantName = "restaruant 1";
        assertEquals(restaurantName, shoppingCartController.getRestaurantName("user1"));
    }

    @Test
    void testAddDish() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);
        ArrayList<DishObject> testOrderDishes = new ArrayList<>();
        DishObject expectedDish = new DishObject();
        testOrderDishes.add(expectedDish);
        assertEquals(testOrderDishes, shoppingCartController.addDish("user1", new DishObject()));
    }

    @Test
    void testAddDishIsNull() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        assertEquals(null, shoppingCartController.addDish("user1", null));
    }

    @Test
    void testAddDishUseIdNotMatch() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);
        assertEquals(null, shoppingCartController.addDish("user2", null));
    }

    @Test
    void testGetOrderedDishesIsNull() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        assertEquals(null, shoppingCartController.getOrderedDishes("user1"));
    }

    @Test
    void testGetOrderedDishesUseIdNotMatch() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);
        assertEquals(null, shoppingCartController.getOrderedDishes("user2"));
    }

    @Test
    void testGetOrderedDishes() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);
        ArrayList<DishObject> testOrderDishes = new ArrayList<>();
        DishObject expectedDish = new DishObject();
        testOrderDishes.add(expectedDish);
        assertEquals(new ArrayList<DishObject>(), shoppingCartController.getOrderedDishes("user1"));
    }

    @Test
    void testDeleteCart() {
        Collection<ShoppingCart> actual = shoppingCartController.shoppingCartRepository.getAll();
        String userId1 = "6227b0779744ecb0c23a772e";
        String username1 = "Wen";
        String restaurantName1 = "restaurant 1";
        shoppingCartController.createNewShoppingCart(userId1, username1, restaurantName1);
        shoppingCartController.deleteCart(userId1);
        actual = shoppingCartController.shoppingCartRepository.getAll();
        assertTrue(actual.size() == 0);
    }

    @Test
    void testDeleteCartUserIdNotMatch() {
        Collection<ShoppingCart> actual = shoppingCartController.shoppingCartRepository.getAll();
        String userId1 = "6227b0779744ecb0c23a772e";
        String username1 = "Wen";
        String restaurantName1 = "restaurant 1";
        shoppingCartController.createNewShoppingCart("userId2", username1, restaurantName1);
        shoppingCartController.deleteCart(userId1);
        actual = shoppingCartController.shoppingCartRepository.getAll();
        assertFalse(actual.size() == 0);
    }

    @Test
    void testGetShoppingCart() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        cart1.setOrderItems(new ArrayList<DishObject>());
        shoppingCartController.shoppingCartRepository.add(cart1);

        assertEquals(cart1, shoppingCartController.getShoppingCart("user1"));
        assertNull(shoppingCartController.getShoppingCart("user2"));
    }

    @Test
    void testRemoveDish() {
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
        cart1.setUserId("user1");
        cart1.setUsername("Wen");
        cart1.setRestaurantName("restaruant 1");
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(1.11);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(1.11);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(1.11);

        ArrayList<DishObject> orderDishes = new ArrayList<>();
        orderDishes.add(dish1);
        orderDishes.add(dish2);
        orderDishes.add(dish3);

        cart1.setOrderItems(orderDishes);

        shoppingCartController.shoppingCartRepository.add(cart1);

        assertEquals(3, shoppingCartController.getOrderedDishes("user1").size());
        assertEquals(dish2, shoppingCartController.getOrderedDishes("user1").get(1));
        ArrayList<DishObject> removedList =
                shoppingCartController.removeDish(dish2.getDish(), "user1");

        assertEquals(2, shoppingCartController.getOrderedDishes("user1").size());
        assertNotEquals(dish2, removedList.get(1));
        assertNotEquals(dish2, removedList.get(0));
    }
}
