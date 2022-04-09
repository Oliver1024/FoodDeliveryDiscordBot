package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        restaurantTest.setName(restaurant_name_one);

        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(
                restaurantController.getRestaurantName(restaurant_name_one).equals("Sichuan food"));
        assertNull(restaurantController.getRestaurantName(restaurant_name_two));
    }

    @Test
    void testgetDish() {
        // setup
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName(restaurant_name_one);
        DishObject testDishOne = new DishObject();
        testDishOne.setDish("testOne");
        testDishOne.setPrice(1.11);
        ArrayList<DishObject> menus = new ArrayList<DishObject>();
        menus.add(testDishOne);
        restaurantTest.setMenu(menus);

        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(
                restaurantController.getDish(1, restaurant_name_one).getDish().equals("testOne"));
        assertFalse(restaurantController.getDish(1, restaurant_name_one).getPrice() == 1.12);
        assertNull(restaurantController.getDish(2, restaurant_name_one));
        assertNull(restaurantController.getDish(-1, restaurant_name_one));
        assertNull(restaurantController.getDish(1, restaurant_name_two));
    }

    @Test
    void testGetAllRestaurantsName() {
        // setup
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName(restaurant_name_one);
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
        restaurantTest.setName(restaurant_name_one);
        DishObject testDishOne = new DishObject();
        testDishOne.setDish("testOne");
        testDishOne.setPrice(1.11);
        ArrayList<DishObject> menus = new ArrayList<DishObject>();
        menus.add(testDishOne);
        restaurantTest.setMenu(menus);
        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);

        assertTrue(
                restaurantController.randomDish(restaurant_name_one).getDish().equals("testOne"));
        assertNull(restaurantController.randomDish(restaurant_name_two));
    }

    @Test
    void testGetMenu() {
        // setup
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName(restaurant_name_one);
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
        assertEquals(1, restaurantController.getMenu(restaurant_name_one).size());
    }

    @Test
    void testGetRestaurant() {
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName(restaurant_name_one);
        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);
        assertEquals(restaurantTest, restaurantController.getRestaurant(restaurant_name_one));
        assertNull(restaurantController.getRestaurant(restaurant_name_two));
    }

    @Test
    void testFilterRestaurantByCuisine() {
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName(restaurant_name_one);
        String inputCuisineTypeOne = "inputCuisineTypeOne";
        ArrayList<String> cuisineTypes = new ArrayList<>();
        cuisineTypes.add(inputCuisineTypeOne);
        restaurantTest.setCuisineType(cuisineTypes);
        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);
        assertEquals(
                restaurant_name_one, restaurantController.filterRestaurantByCuisine(null).get(0));
        assertEquals(
                restaurant_name_one,
                restaurantController.filterRestaurantByCuisine(inputCuisineTypeOne).get(0));
    }

    @Test
    void testfindDishNumber() {
        Restaurant restaurantTest = new Restaurant();
        restaurantTest.setName(restaurant_name_one);
        DishObject testDishOne = new DishObject();
        testDishOne.setDish("testOne");
        testDishOne.setPrice(1.11);
        DishObject testDishTwo = new DishObject();
        testDishTwo.setDish("testTwo");
        testDishTwo.setPrice(2.22);
        ArrayList<DishObject> menus = new ArrayList<DishObject>();
        menus.add(testDishOne);
        menus.add(testDishTwo);
        restaurantTest.setMenu(menus);
        RestaurantController restaurantController = getRestaurantController();
        restaurantController.restaurantRepository.add(restaurantTest);
        assertEquals(1, restaurantController.findDishNumber(restaurant_name_one, testDishOne));
        assertNotEquals(3, restaurantController.findDishNumber(restaurant_name_one, testDishTwo));
    }
}
