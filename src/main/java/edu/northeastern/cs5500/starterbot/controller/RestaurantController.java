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

public class RestaurantController {
    GenericRepository<Restaurant> restaurantRepository;

    @Inject
    RestaurantController(GenericRepository<Restaurant> restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Check if the restaurant name that user input is in our database.
     *
     * @param toCheckName String the restaurant name that user input
     * @return return the name of the restaurant if it exists in the database, otherwise, return
     *     null
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
     * @param dishNumber Integer the dish number that user input
     * @param restaurantName String the restaurant that the user are ordering at
     * @return HashMap containing dish names and their corresponding dish prices if it exists in the
     *     database, otherwise, return null
     */
    @Nullable
    public DishObject getDish(Integer dishNumber, String restaurantName) {
        Collection<Restaurant> AllRestaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : AllRestaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                ArrayList<DishObject> menu = restaurant.getMenu();
                if (dishNumber > 0 && dishNumber <= menu.size()) {
                    return menu.get(dishNumber - 1);
                }
            }
        }
        return null;
    }

    /**
     * get the DishObject in the given restaurant where the dish is equal to the input dishName
     *
     * @param dishName the name of the dish
     * @param restaurantName the name of the restaurant
     * @return a DishObject object
     */
    @Nullable
    public DishObject getDish(String dishName, String restaurantName) {
        Collection<Restaurant> AllRestaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : AllRestaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                ArrayList<DishObject> menu = restaurant.getMenu();
                for (DishObject dish : menu) {
                    if (dish.getDish().equals(dishName)) {
                        return dish;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets all the names of restaurants in our database
     *
     * @return the arrayList with all the name of restaurants
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
     * @param restaurantName String the name of restaurants
     * @return the pair of dish name and dish price as the random dish if it exists in the database,
     *     otherwise, return null
     */
    @Nullable
    public DishObject randomDish(String restaurantName) {
        Collection<Restaurant> restaurants = restaurantRepository.getAll();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                Integer numOfDishes = restaurant.getMenu().size();
                Random rand = new Random();
                Integer randomIndex = rand.nextInt(numOfDishes);
                return restaurant.getMenu().get(randomIndex);
            }
        }
        return null;
    }

    /**
     * get menu information when user input the name of restaurants
     *
     * @param restaurantName String the name of restaurant that user provided
     * @return return the ArrayList of the menu of the restaurant when user input the name of
     *     restaurant, otherwise, return null
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
     * @param restaurantName String the restaurant name we are examining
     * @return restaurant object with the name equal to the input name, otherwise, return null
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
     * @param inputCuisineType String the cuisine type we want to used for filtering restaurants
     * @return the arrayList of all the name of all the filtered restaurants
     */
    @Nonnull
    public ArrayList<String> filterRestaurantByCuisine(String inputCuisineType) {
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

    /**
     * find the number of dishes in the menu
     *
     * @param restaurantName String the target restaurantName
     * @param targeDishObject DishObject the target dishObject which used for compare the list of
     *     menu
     * @return get the number of target dish, the number of dishes should add 1 because there is no
     *     index 0 on the menu, start count menu at index 1
     */
    @Nonnull
    public int findDishNumber(String restaurantName, DishObject targeDishObject) {
        int dishNumber = -1;
        ArrayList<DishObject> menu = getMenu(restaurantName);
        for (int i = 0; i < menu.size(); i++) {
            if (menu.get(i).getDish().equals(targeDishObject.getDish())) {
                dishNumber = i + 1;
            }
        }
        return dishNumber;
    }
}
