package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Restaurant;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import javax.inject.Inject;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class RestaurantController {
    GenericRepository<Restaurant> restaurantRepository;

    @Inject
    RestaurantController(GenericRepository<Restaurant> restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Check if the restaurant name that user input is in our database.
     *
     * @param name the restaurant name that user input
     * @return return the name of the restaurant if it exists in the database, otherwise, return
     *     null.
     */
    @Nullable
    public String getRestaurantName(String toCheckName) {
        // get all restaurants to a Collection
        Collection<Restaurant> AllRestaurants = restaurantRepository.getAll();

        // compare the name of restaurant ,  if we got the target return the name else return null
        for (Restaurant restaurant : AllRestaurants) {
            if (restaurant.getName().equalsIgnoreCase(toCheckName)) {
                return restaurant.getName();
            }
        }
        return null;
    }

    /**
     * Check dish number is included in restaurant menu
     *
     * @param dishNumber the dish number that user input
     * @param restaurantName the restaurant that the user are ordering at
     * @return HashMap containing dish names and their corresponding dish prices
     */
    @NotNull
    public HashMap<String, Double> getDish(Integer dishNumber, String restaurantName) {
        Collection<Restaurant> AllRestaurants = restaurantRepository.getAll();
        HashMap<String, Double> target = new HashMap<>();
        for (Restaurant restaurant : AllRestaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                ArrayList<DishObject> menu = restaurant.getMenu();
                if (menu.size() >= dishNumber && dishNumber > 0) {
                    String name = menu.get(dishNumber - 1).getDish();
                    double price = menu.get(dishNumber - 1).getPrice();
                    target.put(name, price);
                }
            }
        }
        return target;
    }

    /**
     * Gets all the names of restaurants in our database
     *
     * @return the string with all the restaurants' names
     */
    @NotNull
    public String getAllRestaurantsName() {
        String restaurantsName = "";
        Collection<Restaurant> restaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : restaurants) {
            restaurantsName = restaurantsName + restaurant.getName() + "\n";
        }
        return restaurantsName;
    }

    /**
     * To randomly select one dish from the given restaurant.
     *
     * @param
     * @return the pair of dish name and dish price as the random dish
     */
    @Nullable
    public Pair<String, Integer> randomDish(String restaurantName) {
        Collection<Restaurant> restaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                Integer numOfDishes = restaurant.getMenu().size();
                Random rand = new Random();
                Integer randomIndex = rand.nextInt(numOfDishes);
                String randomDish = restaurant.getMenu().get(randomIndex).getDish();
                return Pair.of(randomDish, randomIndex + 1);
            }
        }
        return null;
    }
}
