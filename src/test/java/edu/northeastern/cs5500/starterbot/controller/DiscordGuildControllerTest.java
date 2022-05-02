package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.model.DiscordGuild;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.DishUserPair;
import edu.northeastern.cs5500.starterbot.model.GuildShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class DiscordGuildControllerTest {

    DiscordGuildController discordGuildController =
            new DiscordGuildController(new InMemoryRepository<>());

    @Test
    void TestAddOrder() {
        GuildShoppingCart guildShoppingCart = new GuildShoppingCart();

        guildShoppingCart.setCreatedUserId("createdUserId");
        guildShoppingCart.setRestaurantName("restaurantName1");
        guildShoppingCart.setGuildId("guildId");
        ArrayList<DishUserPair> list = new ArrayList<>();
        DishUserPair dishUserPair1 = new DishUserPair();
        DishObject dish1 = new DishObject();
        dish1.setDish("dish1");
        dish1.setPrice(1.11);

        dishUserPair1.setDish(dish1);
        dishUserPair1.setUserId("userId1");
        dishUserPair1.setUsername("username1");

        list.add(dishUserPair1);
        guildShoppingCart.setDishes(list);

        discordGuildController.addOrder(guildShoppingCart);
        Collection<DiscordGuild> guilds = discordGuildController.discordGuildRepository.getAll();
        ArrayList<DiscordGuild> guildsList = new ArrayList<>(guilds);
        assertEquals("guildId", guildsList.get(0).getGuildId());
        assertEquals(
                "restaurantName1", guildsList.get(0).getGuildOrders().get(0).getRestaurantName());
        assertEquals(1, guildsList.get(0).getGuildOrders().size());
        assertEquals(1, guildShoppingCart.getDishes().size());

        GuildShoppingCart guildShoppingCart1 = new GuildShoppingCart();

        guildShoppingCart1.setCreatedUserId("createdUserId2");
        guildShoppingCart1.setRestaurantName("restaurantName2");
        guildShoppingCart1.setGuildId("guildId");
        ArrayList<DishUserPair> list2 = new ArrayList<>();
        DishUserPair dishUserPair2 = new DishUserPair();
        DishObject dish2 = new DishObject();
        dish2.setDish("dish1");
        dish2.setPrice(1.11);

        dishUserPair2.setDish(dish2);
        dishUserPair2.setUserId("userId1");
        dishUserPair2.setUsername("username1");

        list2.add(dishUserPair2);
        guildShoppingCart1.setDishes(list2);

        discordGuildController.addOrder(guildShoppingCart1);
        Collection<DiscordGuild> guilds1 = discordGuildController.discordGuildRepository.getAll();
        ArrayList<DiscordGuild> guildsList1 = new ArrayList<>(guilds1);
        assertEquals(1, guildsList1.size());
        assertEquals("guildId", guildsList1.get(0).getGuildId());
        assertEquals(
                "restaurantName1", guildsList1.get(0).getGuildOrders().get(0).getRestaurantName());
        assertEquals(2, guildsList1.get(0).getGuildOrders().size());
    }
}
