package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.model.User;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public class UserControllerTest {
    UserController userController = new UserController(new InMemoryRepository<>());

    // @Test
    // void testAddOrder() {
    //     ShoppingCartController shoppingCartController =
    //             new ShoppingCartController(new InMemoryRepository<>());
    //     ShoppingCart shoppingCart1 = new ShoppingCart();
    //     shoppingCart1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
    //     shoppingCart1.setUserId("user1");
    //     shoppingCart1.setUsername("Wen");
    //     shoppingCart1.setRestaurantName("restaruant 1");
    //     shoppingCart1.setOrderItems(new ArrayList<DishObject>());
    //     shoppingCartController.shoppingCartRepository.add(shoppingCart1);
    //     // userController.addOrder(shoppingCart1);

    //     DishObject dish1 = new DishObject();
    //     dish1.setDish("dish1");
    //     dish1.setPrice(9.9);

    //     DishObject dish2 = new DishObject();
    //     dish2.setDish("dish2");
    //     dish2.setPrice(19.9);

    //     ArrayList<DishObject> orderItems1 = new ArrayList<>();
    //     orderItems1.add(dish1);
    //     orderItems1.add(dish2);

    //     Order order1 = new Order();
    //     order1.setIsDelivered(false);
    //     order1.setOrderTime(LocalDateTime.now().minusMinutes(2));
    //     order1.setOrderItems(orderItems1);
    //     order1.setRestaurantName("restaurant1");

    //     ArrayList<Order> orders = new ArrayList<>();
    //     orders.add(order1);

    //     User user1 = new User();
    //     user1.setId(new ObjectId("6227b0779744ecb0c23a772e"));
    //     user1.setUserId("user1");
    //     user1.setUsername("Wen");
    //     user1.setOrders(orders);

    //     User user2 = new User();
    //     user1.setId(new ObjectId("623fc4508e303b6fce523819"));
    //     user1.setUserId("user3");
    //     user1.setUsername("a");
    //     user1.setOrders(orders);

    //     ShoppingCart shoppingCart2 = new ShoppingCart();
    //     shoppingCart2.setId(new ObjectId("623fc4508e303b6fce523819"));
    //     shoppingCart2.setUserId("user2");
    //     shoppingCart2.setUsername("A");
    //     shoppingCart2.setRestaurantName("restaruant 2");
    //     shoppingCart2.setOrderItems(new ArrayList<DishObject>());
    //     shoppingCartController.shoppingCartRepository.add(shoppingCart2);

    //     userController.userRepository.add(user1);
    //     userController.addOrder(shoppingCart1);
    //     assertTrue(user1.getUserId().equals(shoppingCart1.getUserId()));
    //     assertFalse(user2.getUserId().equals(shoppingCart2.getUserId()));
    // }

    @Test
    void testGetUndeliveredOrders() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(7.9);

        ArrayList<DishObject> orderItems1 = new ArrayList<>();
        orderItems1.add(dish1);
        orderItems1.add(dish2);

        ArrayList<DishObject> orderItems2 = new ArrayList<>();
        orderItems2.add(dish3);

        Order order1 = new Order();
        order1.setIsDelivered(false);
        order1.setOrderTime(LocalDateTime.now().minusMinutes(2));
        order1.setOrderItems(orderItems1);
        order1.setRestaurantName("restaurant1");

        Order order2 = new Order();
        order2.setIsDelivered(true);
        order2.setOrderTime(LocalDateTime.now().minusMinutes(30));
        order2.setOrderItems(orderItems2);
        order2.setRestaurantName("restaurant2");

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        User user1 = new User();
        user1.setId(new ObjectId("623fc4508e303b6fce523819"));
        user1.setUserId("user1");
        user1.setUsername("Wen");
        user1.setOrders(orders);

        userController.userRepository.add(user1);
        ArrayList<Order> undeliveredOrders1 = userController.getUndeliveredOrders("user1");

        assertTrue(undeliveredOrders1.size() == 1);
        assertEquals(order1, undeliveredOrders1.get(0));

        ArrayList<Order> undeliveredOrders2 = userController.getUndeliveredOrders("user2");
        assertNull(undeliveredOrders2);
    }

    @Test
    void testDeliverOrders() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(7.9);

        DishObject dish4 = new DishObject();
        dish4.setDish("dish4");
        dish4.setPrice(22.9);

        ArrayList<DishObject> orderItems1 = new ArrayList<>();
        orderItems1.add(dish1);
        orderItems1.add(dish2);

        ArrayList<DishObject> orderItems2 = new ArrayList<>();
        orderItems2.add(dish3);

        ArrayList<DishObject> orderItems3 = new ArrayList<>();
        orderItems3.add(dish4);

        Order order1 = new Order();
        order1.setIsDelivered(false);
        order1.setOrderTime(LocalDateTime.now().minusMinutes(2));
        order1.setOrderItems(orderItems1);
        order1.setRestaurantName("restaurant1");

        Order order2 = new Order();
        order2.setIsDelivered(false);
        order2.setOrderTime(LocalDateTime.now().minusMinutes(30));
        order2.setOrderItems(orderItems2);
        order2.setRestaurantName("restaurant2");

        Order order3 = new Order();
        order3.setIsDelivered(true);
        order3.setOrderTime(LocalDateTime.now().minusMinutes(40));
        order3.setOrderItems(orderItems3);
        order3.setRestaurantName("restaurant3");

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        User user1 = new User();
        user1.setId(new ObjectId("623fc4508e303b6fce523819"));
        user1.setUserId("user1");
        user1.setUsername("Wen");
        user1.setOrders(orders);

        userController.userRepository.add(user1);
        userController.deliverOrders("user1");
        assertFalse(order1.getIsDelivered());
        assertTrue(order2.getIsDelivered());
        userController.deliverOrders("user2");
    }

    @Test
    void testGetLastKNumsOrders() {
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(9.9);

        DishObject dish2 = new DishObject();
        dish2.setDish("dish2");
        dish2.setPrice(19.9);

        DishObject dish3 = new DishObject();
        dish3.setDish("dish3");
        dish3.setPrice(7.9);

        DishObject dish4 = new DishObject();
        dish4.setDish("dish4");
        dish4.setPrice(22.9);

        ArrayList<DishObject> orderItems1 = new ArrayList<>();
        orderItems1.add(dish1);
        orderItems1.add(dish2);
        orderItems1.add(dish3);
        ArrayList<DishObject> orderItems2 = new ArrayList<>();
        orderItems2.add(dish3);
        orderItems1.add(dish4);
        ArrayList<DishObject> orderItems3 = new ArrayList<>();
        orderItems3.add(dish4);

        Order order1 = new Order();
        order1.setIsDelivered(false);
        order1.setOrderTime(LocalDateTime.now().minusMinutes(2));
        order1.setOrderItems(orderItems1);
        order1.setRestaurantName("restaurant1");

        Order order2 = new Order();
        order2.setIsDelivered(false);
        order2.setOrderTime(LocalDateTime.now().minusMinutes(30));
        order2.setOrderItems(orderItems2);
        order2.setRestaurantName("restaurant1");

        Order order3 = new Order();
        order3.setIsDelivered(true);
        order3.setOrderTime(LocalDateTime.now().minusMinutes(40));
        order3.setOrderItems(orderItems3);
        order3.setRestaurantName("restaurant3");

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order3);

        User user1 = new User();
        user1.setId(new ObjectId("623fc4508e303b6fce523819"));
        user1.setUserId("user1");
        user1.setUsername("Wen");
        user1.setOrders(orders);

        ArrayList<String> testStringOne = new ArrayList<>();
        testStringOne.add("5");
        testStringOne.add("restaurant1");
        ArrayList<String> testStringTwo = new ArrayList<>();
        testStringTwo.add("2");
        ArrayList<String> testStringThree = new ArrayList<>();
        testStringThree.add("1");
        testStringThree.add("restaurant1");
        ArrayList<String> testStringFour = new ArrayList<>();
        testStringFour.add("3");

        userController.userRepository.add(user1);
        ArrayList<Order> testList = userController.getLastKNumsOrders("user1", testStringOne);
        assertEquals(order2, testList.get(testList.size() - 1));
        assertEquals(2, testList.size());
        assertEquals(order1, testList.get(0));

        ArrayList<Order> testList1 = userController.getLastKNumsOrders("user1", testStringTwo);
        assertEquals(order3, testList1.get(testList1.size() - 1));
        assertEquals(order3, testList1.get(1));
        assertEquals(2, testList1.size());

        ArrayList<Order> testList2 = userController.getLastKNumsOrders("user1", testStringThree);
        assertEquals(order2, testList2.get(0));
        assertEquals(1, testList2.size());

        ArrayList<Order> testList3 = userController.getLastKNumsOrders("user2", testStringThree);
        assertEquals(new ArrayList<>(), testList3);

        ArrayList<Order> testList4 = userController.getLastKNumsOrders("user1", testStringFour);
        assertEquals(order2, testList4.get(0));
        assertEquals(order3, testList4.get(1));
        assertEquals(3, testList4.size());
    }
}
