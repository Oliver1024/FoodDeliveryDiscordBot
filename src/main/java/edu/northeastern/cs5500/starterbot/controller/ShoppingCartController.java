package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class ShoppingCartController {
    GenericRepository<ShoppingCart> shoppingCartRepository;

    @Inject
    ShoppingCartController(GenericRepository<ShoppingCart> shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * check if the user has an unfinished order.
     *
     * @param userId the user's discord id
     * @return boolean values indicating whether the user has unfinished order
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
     * @param userId the user's discord id
     * @param username the user's discord nickname
     * @param restaurantName the restaurant that the user are ordering at
     */
    public void createNewShoppingCart(String userId, String username, String restaurantName) {
        // set new object of cart;
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUserId(userId);
        newCart.setUsername(username);
        newCart.setRestaurantName(restaurantName);
        newCart.setOrderItems(new ArrayList<>());
        // add the object into repository;
        this.shoppingCartRepository.add(newCart);
    }

    /**
     * check if the userId is in the shopping cart
     *
     * @param userId the user's discord id
     * @return the restaurant name that the user are ordering at, otherwise return null
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
     * add new ordered dish into shopping cart and into the online database
     *
     * @param userId the user's discord id
     * @param newDish the new dish to be added into the shopping cart and database
     * @return Arraylist containing all dishes in the shopping cart
     */
    @Nullable
    public ArrayList<Pair<String, Double>> addDish(String userId, Pair<String, Double> newDish) {
        Collection<ShoppingCart> carts = shoppingCartRepository.getAll();
        ArrayList<Pair<String, Double>> orderedDishes = new ArrayList<>();
        for (ShoppingCart shoppingCart : carts) {
            if (shoppingCart.getUserId().equalsIgnoreCase(userId)) {
                ArrayList<DishObject> target = shoppingCart.getOrderItems();
                DishObject dishObject = new DishObject();
                dishObject.setDish(newDish.getLeft());
                dishObject.setPrice(newDish.getRight());
                target.add(dishObject);
                shoppingCart.setOrderItems(target);
                shoppingCartRepository.update(shoppingCart);
                for (DishObject dish : target) {
                    orderedDishes.add(Pair.of(dish.getDish(), dish.getPrice()));
                }
                return orderedDishes;
            }
        }
        return null;
    }
}
