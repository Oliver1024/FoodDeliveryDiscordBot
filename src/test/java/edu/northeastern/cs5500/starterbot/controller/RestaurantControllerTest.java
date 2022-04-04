package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Restaurant;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class RestaurantControllerTest {
    static final String restaurant_name_one = "Sichuan food";
    static final String restaurant_name_two = "hunan food";
    ArrayList<DishObject> menus = new ArrayList<DishObject>();

    private RestaurantController getRestaurantController() {
        RestaurantController restaurantController =
                new RestaurantController(new InMemoryRepository<>());
        return restaurantController;
    }

    @Test
    void testGetRestaurantName() {
        // setup
        Restaurant restaurantTest = new Restaurant();

        restaurantTest.setName("Sichuan food");

        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(
                restaurantController.getRestaurantName(restaurant_name_one).equals("Sichuan food"));
        assertNull(restaurantController.getRestaurantName(restaurant_name_two));
    }

    @Test
    void testGetgetDish() {
        // setup
        Restaurant restaurantTest = new Restaurant();
        // restaurantTest.setId(new ObjectId("62279c01954170cf221a6fac"));
        restaurantTest.setName("Sichuan food");
        // restaurantTest.setAddress("123 456th 789");
        // restaurantTest.setContact((long)222222222);
        // ArrayList<String> cuisineType = new ArrayList<String>();
        // cuisineType.add("chinese");
        // restaurantTest.setCuisineType(cuisineType);

        DishObject testDishOne = new DishObject();
        testDishOne.setDish("testOne");
        testDishOne.setPrice(1.11);
        ArrayList<DishObject> menus = new ArrayList<DishObject>();
        menus.add(testDishOne);
        restaurantTest.setMenu(menus);

        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(restaurantController.getDish(1, "Sichuan food").getLeft().equals("testOne"));
        assertFalse(restaurantController.getDish(1, "Sichuan food").getRight() == 1.12);
    }

    @Test
    void testGetAllRestaurantsName() {
        // setup
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName("Sichuan food");
        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(restaurantController.getAllRestaurantsName().get(0).equals(restaurant_name_one));
        assertFalse(
                restaurantController.getAllRestaurantsName().get(0).equals(restaurant_name_two));
    }

    @Test
    void testRandomDish() {
        // setup
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName("Sichuan food");
        DishObject testDishOne = new DishObject();
        testDishOne.setDish("testOne");
        testDishOne.setPrice(1.11);
        ArrayList<DishObject> menus = new ArrayList<DishObject>();
        menus.add(testDishOne);
        restaurantTest.setMenu(menus);

        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(
                restaurantController.randomDish(restaurant_name_one).getLeft().equals("testOne"));
        assertNull(restaurantController.randomDish(restaurant_name_two));
    }

    @Test
    void testGetMenu() {
        // setup
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName("Sichuan food");
        DishObject testDishOne = new DishObject();
        testDishOne.setDish("testOne");
        testDishOne.setPrice(1.11);
        ArrayList<DishObject> menus = new ArrayList<DishObject>();
        menus.add(testDishOne);
        restaurantTest.setMenu(menus);

        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(
                restaurantController
                        .getMenu(restaurant_name_one)
                        .get(0)
                        .getDish()
                        .equals("testOne"));
        assertNull(restaurantController.getMenu(restaurant_name_two));
    }
}
