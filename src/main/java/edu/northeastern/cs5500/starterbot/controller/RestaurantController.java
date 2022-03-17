package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.Restaurant;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.HashMap;
import javax.inject.Inject;

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
        return null;
    }

    /**
     * TO DO: check dish number is included in restaurant menu
     *
     * @param dishNumber
     * @param restaurantName
     * @return HashMap contains dish name and dish price
     */
    @Nullable
    public HashMap<String, Double> getDish(Integer dishNumber, String restaurantName) {
        return null;
    }
}
