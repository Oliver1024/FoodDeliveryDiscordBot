package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Restaurant;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class RestaurantController {
    GenericRepository<Restaurant> restaurantRepository;

    @Inject
    RestaurantController(GenericRepository<Restaurant> restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * To do: Please implement this function, we need this function to check if the restaurant name
     * user input is in our database. If so, return the name of the restaurant, otherwise, return
     * null. And please ignore the case of the input. (you can use .equalsIgnorCase())
     *
     * @param name
     * @return
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
     * TO DO: check dish number is included in restaurant menu
     *
     * @param dishNumber
     * @param restaurantName
     * @return HashMap contains dish name and dish price
     */
    @NotNull
    public HashMap<String, Double> getDish(Integer dishNumber, String restaurantName) {
        // find the target object of restaurant
        // get all restaurants to a Collection

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
}
