package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.DishUserPair;
import edu.northeastern.cs5500.starterbot.model.GuildShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;

public class GuildShoppingCartController {
    GenericRepository<GuildShoppingCart> guildShoppingCartRepository;

    @Inject
    GuildShoppingCartController(GenericRepository<GuildShoppingCart> guildShoppingCartRepository) {
        this.guildShoppingCartRepository = guildShoppingCartRepository;
    }

    /**
     * Check if the given guild already has on going order
     *
     * @param String the id of the given guild
     * @return true or false whether the guild has an ongoing shopping cart
     */
    @Nonnull
    public Boolean isGuildInShoppingCart(String guildId) {
        Collection<GuildShoppingCart> carts = guildShoppingCartRepository.getAll();
        for (GuildShoppingCart cart : carts) {
            if (cart.getGuildId().equals(guildId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a shopping cart with the given information
     *
     * @param guildId the id of the discord guild
     * @param restaurantName the name of the restaurant the guild ordering at
     * @param createdUserId the discord id of the user who started the group order
     */
    public void createCart(String guildId, String restaurantName, String createdUserId) {
        GuildShoppingCart newCart = new GuildShoppingCart();
        newCart.setGuildId(guildId);
        newCart.setRestaurantName(restaurantName);
        newCart.setCreatedUserId(createdUserId);
        newCart.setDishes(new ArrayList<DishUserPair>());
        this.guildShoppingCartRepository.add(newCart);
    }

    /**
     * get the shopping cart for the given guild
     *
     * @param guildId the id of the guild
     * @return the GuildShoppingCart Object
     */
    @Nullable
    public GuildShoppingCart getCart(String guildId) {
        Collection<GuildShoppingCart> carts = guildShoppingCartRepository.getAll();
        for (GuildShoppingCart cart : carts) {
            if (cart.getGuildId().equals(guildId)) {
                return cart;
            }
        }
        return null;
    }

    /**
     * Add the given dish object into the shopping cart for the given guild
     *
     * @param dish the dish needed to be added
     * @param guildId the id of the guild
     * @param userId the id of the discord user who ordered this dish
     * @param username the username of the discord user who ordered this dish
     * @return an arrayList of DishUserPair object representing all the dishes ordered by the guild
     *     members
     */
    @Nullable
    public ArrayList<DishUserPair> addDish(
            DishObject dish, String guildId, String userId, String username) {
        Collection<GuildShoppingCart> carts = guildShoppingCartRepository.getAll();
        for (GuildShoppingCart cart : carts) {
            if (cart.getGuildId().equals(guildId)) {
                DishUserPair newPair = new DishUserPair();
                newPair.setDish(dish);
                newPair.setUserId(userId);
                newPair.setUsername(username);
                cart.getDishes().add(newPair);
                guildShoppingCartRepository.update(cart);
                return cart.getDishes();
            }
        }
        return null;
    }
}
