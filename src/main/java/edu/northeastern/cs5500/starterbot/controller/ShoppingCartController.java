package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
     * @param userId
     * @return boolean values
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
     * @param userId
     * @param username
     * @param restaurantName
     */
    public void createNewShoppingCart(String userId, String username, String restaurantName) {
        // set new object of cart;
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUserId(userId);
        newCart.setUsername(username);
        newCart.setRestaurantName(restaurantName);
        // add the object into repository;
        this.shoppingCartRepository.add(newCart);
    }

    /**
     * check user id is in the shopping cart
     *
     * @param userId
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
     * @param userId
     * @param newDish
     * @return hashMap contains all dishes in the shopping cart
     */
    @Nullable
    public HashMap<String, Double> addDish(String userId, HashMap<String, Double> newDish) {
        Collection<ShoppingCart> carts = shoppingCartRepository.getAll();
        HashMap<String, Double> orderedDishes = newDish;
        for (ShoppingCart shoppingCart : carts) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                ArrayList<DishObject> target = shoppingCart.getOrderItems();
                for (DishObject dish : target) {
                    orderedDishes.put(dish.getDish(), dish.getPrice());
                }
                return orderedDishes;
            }
        }

        return null;
    }
}
