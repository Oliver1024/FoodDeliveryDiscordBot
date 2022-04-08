package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.model.User;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;

public class UserController {
    GenericRepository<User> userRepository;

    @Inject
    UserController(GenericRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * add the order into the user's list of history orders in the database
     *
     * @param userId the user's discord id
     * @param username the user's discord username
     * @param restaurantName the restaurant that the user ordered at
     * @param orderItems all the ordered dishes
     */
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

    /**
     * Helper function which creates a User object with the given information
     *
     * @param userId the discord id for the new user
     * @param username the discord username for the new user
     * @return the User object
     */
    private User createUser(String userId, String username) {
        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setUsername(username);
        newUser.setOrders(new ArrayList<Order>());

        this.userRepository.add(newUser);
        return newUser;
    }

    /**
     * Helper function which creates a new Order object with the given information
     *
     * @param restaurantName the restaurant that user ordered at
     * @param orderItems the arraylist of all the ordered dishes
     * @return a new Order object
     */
    private Order createNewOrder(String restaurantName, ArrayList<DishObject> orderItems) {
        LocalDateTime curTime = LocalDateTime.now();
        Order newOrder = new Order();
        newOrder.setRestaurantName(restaurantName);
        newOrder.setIsDelivered(false);
        newOrder.setOrderTime(curTime);
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }

    /**
     * Get all orders of the given user that are not delivered yet
     *
     * @param userId the user we will check
     * @return an arraylist of all the filtered orders
     */
    @Nullable
    public ArrayList<Order> getUndelivedOrders(String userId) {
        ArrayList<Order> undeliveredOrders = new ArrayList<Order>();
        Collection<User> users = userRepository.getAll();
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                for (Order order : user.getOrders()) {
                    if (!order.getIsDelivered()) {
                        undeliveredOrders.add(order);
                    }
                }
                return undeliveredOrders;
            }
        }
        return null;
    }

    /**
     * For the given user, change isDelivered value of the orders that should be delivered from
     * false to true.
     *
     * @param userId the discord id of the user
     */
    public void deliverOrders(String userId) {
        Collection<User> users = userRepository.getAll();
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                for (Order order : user.getOrders()) {
                    if (!order.getIsDelivered() && shouldOrderBeDelivered(order)) {
                        order.setIsDelivered(true);
                    }
                }
                userRepository.update(user);
            }
        }
    }

    /**
     * Check the order status of the given order based on the order time and curent time
     *
     * @param order the order we will check
     * @param ORDER_STATUS the options of order statuses.
     * @return the order status of the given order
     */
    @Nonnull
    private boolean shouldOrderBeDelivered(Order order) {
        int TIME_PER_DISH = 5;
        int TIME_FOR_DELIVER = 20;
        long passedMins = Duration.between(order.getOrderTime(), LocalDateTime.now()).toMinutes();
        long prepareTime = order.getOrderItems().size() * TIME_PER_DISH;
        return (passedMins > prepareTime + TIME_FOR_DELIVER);
    }
}
