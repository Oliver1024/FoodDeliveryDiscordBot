package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;

public class ShoppingCartController {
    GenericRepository<ShoppingCart> shoppingCartRepository;

    @Inject
    ShoppingCartController(GenericRepository<ShoppingCart> shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * check if the user has an unfinished order.
     *
     * @param userId String user ID
     * @return boolean values If find user in chopping cart return true, otherwise return false
     */
    @Nonnull
    public Boolean isUserInShoppingCart(String userId) {
        Collection<ShoppingCart> carts = shoppingCartRepository.getAll();
        // Iterate all carts, if find return true;
        for (ShoppingCart shoppingCart : carts) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * create a new shopping cart for the current user.
     *
     * @param userId String user ID
     * @param userName String name of user
     * @param restaurantName string name of restaurant
     */
    public void createNewShoppingCart(String userId, String userName, String restaurantName) {
        // set new object of cart;
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUserId(userId);
        newCart.setUsername(userName);
        newCart.setRestaurantName(restaurantName);
        newCart.setOrderItems(new ArrayList<>());
        // add the object into repository;
        this.shoppingCartRepository.add(newCart);
    }

    /**
     * check user id is in the shopping cart
     *
     * @param userId String user ID
     * @return String restaurant name, otherwise return null
     */
    @Nullable
    public String getRestaurantName(String userId) {
        Collection<ShoppingCart> curr = shoppingCartRepository.getAll();
        // Iterate all object of shoppingCart,  then return name if we find;
        for (ShoppingCart shoppingCart : curr) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                return shoppingCart.getRestaurantName();
            }
        }
        return null;
    }

    /**
     * add new order dishes into shopping cart
     *
     * @param userId String user ID
     * @param newDish DishOjbect ArrayList of dishes
     * @return ArrayList contains all dishes in the shopping cart, otherwise return null
     */
    @Nullable
    public ArrayList<DishObject> addDish(String userId, DishObject newDish) {
        Collection<ShoppingCart> carts = shoppingCartRepository.getAll();
        for (ShoppingCart shoppingCart : carts) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                ArrayList<DishObject> dishes = shoppingCart.getOrderItems();
                dishes.add(newDish);
                shoppingCart.setOrderItems(dishes);
                shoppingCartRepository.update(shoppingCart);
                return dishes;
            }
        }
        return null;
    }

    /**
     * get ordered dishes from the shopping cart
     *
     * @param userId String user ID
     * @return ArrayList contains all ordered dishes in the shopping cart, otherwise, return null
     */
    @Nullable
    public ArrayList<DishObject> getOrderedDishes(String userId) {
        Collection<ShoppingCart> carts = shoppingCartRepository.getAll();
        for (ShoppingCart shoppingCart : carts) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                return shoppingCart.getOrderItems();
            }
        }
        return null;
    }

    /**
     * delete shopping cart
     *
     * @param userId String user ID
     */
    public void deleteCart(String userId) {
        Collection<ShoppingCart> carts = shoppingCartRepository.getAll();
        for (ShoppingCart shoppingCart : carts) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                shoppingCartRepository.delete(shoppingCart.getId());
            }
        }
    }

    /**
     * Get the shopping cart object where the user is the same as the input user
     *
     * @param userId the user we want to check
     * @return a shopping cart object
     */
    @Nullable
    public ShoppingCart getShoppingCart(String userId) {
        Collection<ShoppingCart> carts = shoppingCartRepository.getAll();
        for (ShoppingCart shoppingCart : carts) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                return shoppingCart;
            }
        }
        return null;
    }
}
