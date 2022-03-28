package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.model.User;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

public class UserController {
    GenericRepository<User> userRepository;

    @Inject
    UserController(GenericRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public void addOrder(
            String userId,
            String username,
            String restaurantName,
            ArrayList<DishObject> orderItems) {
        Collection<User> users = userRepository.getAll();
        User userToUpdate = null;
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                userToUpdate = user;
                break;
            }
        }
        if (userToUpdate == null) {
            userToUpdate = createUser(userId, username);
        }

        Order newOrder = createNewOrder(restaurantName, orderItems);
        ArrayList<Order> allOrders = userToUpdate.getOrders();
        allOrders.add(newOrder);
        userToUpdate.setOrders(allOrders);

        this.userRepository.update(userToUpdate);
    }

    private User createUser(String userId, String username) {
        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setUsername(username);
        newUser.setOrders(new ArrayList<Order>());

        this.userRepository.add(newUser);
        return newUser;
    }

    private Order createNewOrder(String restaurantName, ArrayList<DishObject> orderItems) {
        LocalDateTime curTime = LocalDateTime.now();
        Order newOrder = new Order();
        newOrder.setRestaurantName(restaurantName);
        newOrder.setIsDelivered(false);
        newOrder.setOrderTime(curTime);
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }
}
