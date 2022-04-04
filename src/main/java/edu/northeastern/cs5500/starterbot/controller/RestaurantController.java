package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Restaurant;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import net.dv8tion.jda.internal.utils.tuple.Pair;

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
    @Nullable
    public Pair<String, Double> getDish(Integer dishNumber, String restaurantName) {
        Collection<Restaurant> AllRestaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : AllRestaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                ArrayList<DishObject> menu = restaurant.getMenu();
                if (dishNumber > 0 && dishNumber <= menu.size()) {
                    String name = menu.get(dishNumber - 1).getDish();
                    double price = menu.get(dishNumber - 1).getPrice();
                    return Pair.of(name, price);
                }
            }
        }
        return null;
    }

    /**
     * Gets all the names of restaurants in our database
     *
     * @return the arraylist with all the restaurants' names
     */
    @Nonnull
    public ArrayList<String> getAllRestaurantsName() {
        ArrayList<String> restaurantsName = new ArrayList<>();
        Collection<Restaurant> restaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : restaurants) {
            restaurantsName.add(restaurant.getName());
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

    /**
     * from the name that the user provide, we get the menu from the target name, then return for
     * user to use
     *
     * @param restaurantName String, the use is provided by user input and checked by other method.
     * @return return the List of the menu for use
     */
    @Nullable
    public ArrayList<DishObject> getMenu(String restaurantName) {
        Collection<Restaurant> allRestaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                return restaurant.getMenu();
            }
        }
        return null;
    }

    /**
     * Find restaurant object whose name is equal to the input restaurantName
     *
     * @param restaurantName the restaurant name we are examining
     * @return restaurant object with the name equal to the input name
     */
    @Nullable
    public Restaurant getRestaurant(String restaurantName) {
        Collection<Restaurant> allRestaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                return restaurant;
            }
        }
        return null;
    }

    /**
     * Get all the restaurants whose cuisine type is the input type
     *
     * @param inputCuisineType the cuisine type we want to used for filtering restaurants
     * @return the arraylist of all the name sof all the qualified restaurants
     */
    @Nonnull
    public ArrayList<String> getRestaurantsNameWithCertainCusineType(String inputCuisineType) {
        if (inputCuisineType == null) {
            return getAllRestaurantsName();
        }
        ArrayList<String> restaurantsName = new ArrayList<>();
        Collection<Restaurant> restaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : restaurants) {
            for (String cuisineType : restaurant.getCuisineType()) {
                if (cuisineType.equalsIgnoreCase(inputCuisineType)) {
                    restaurantsName.add(restaurant.getName());
                    break;
                }
            }
        }
        return restaurantsName;
    }
}
