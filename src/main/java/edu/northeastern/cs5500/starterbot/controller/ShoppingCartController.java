package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
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
     * Please implement this function. We use it to check if the user has an unfinished order.
     *
     * @param userId
     * @return boolean values
     */
    @Nonnull
    public Boolean isUserInShoppingCart(String userId) {
        return false;
    }

    /**
     * Please implement this function. we use it to create a new shopping cart for the current user.
     *
     * @param userId
     * @param username
     * @param restaurantName
     */
    public void createNewShoppingCart(String userId, String username, String restaurantName) {}

    /**
     * check user id is in the shopping cart
     *
     * @param userId
     * @return String restaurant name, otherwise return null
     */
    @Nullable
    public String getRestaurantName(String userId) {
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
        return null;
    }
}
