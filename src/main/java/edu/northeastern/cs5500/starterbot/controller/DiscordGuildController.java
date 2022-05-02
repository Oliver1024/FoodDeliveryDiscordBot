package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DiscordGuild;
import edu.northeastern.cs5500.starterbot.model.GuildOrder;
import edu.northeastern.cs5500.starterbot.model.GuildShoppingCart;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

public class DiscordGuildController {
    GenericRepository<DiscordGuild> discordGuildRepository;

    @Inject
    DiscordGuildController(GenericRepository<DiscordGuild> discordGuildRepository) {
        this.discordGuildRepository = discordGuildRepository;
    }

    /**
     * put the new guildShoppingCart into the guilds
     *
     * @param guildShoppingCart the target guildShoppingCart that we need to add into collections.
     */
    public void addOrder(GuildShoppingCart guildShoppingCart) {
        Collection<DiscordGuild> guilds = discordGuildRepository.getAll();
        DiscordGuild discordGuild = null;
        for (DiscordGuild guild : guilds) {
            if (guild.getGuildId().equals(guildShoppingCart.getGuildId())) {
                discordGuild = guild;
                break;
            }
        }
        if (discordGuild == null) {
            discordGuild = createGuild(guildShoppingCart.getGuildId());
        }
        GuildOrder newGuildOrder = createNewGuildOrder(guildShoppingCart);

        ArrayList<GuildOrder> allOrders = discordGuild.getGuildOrders();
        allOrders.add(newGuildOrder);
        discordGuild.setGuildOrders(allOrders);

        this.discordGuildRepository.update(discordGuild);
    }

    /**
     * Helper function which creates a new guildOrder object with the given information
     *
     * @param guildShoppingCart the target shopping cart that user need to add
     * @return a new guild order.
     */
    private GuildOrder createNewGuildOrder(GuildShoppingCart guildShoppingCart) {
        LocalDateTime curTime = LocalDateTime.now();
        GuildOrder newGuildOrder = new GuildOrder();
        newGuildOrder.setDishes(guildShoppingCart.getDishes());
        newGuildOrder.setRestaurantName(guildShoppingCart.getRestaurantName());
        newGuildOrder.setOrderTime(curTime);
        newGuildOrder.setIsDelivered(false);

        return newGuildOrder;
    }

    /**
     * Helper function which creates a new Discord guild object with the given information
     *
     * @param GuildId the target the groupId that user need to add
     * @return a new discord guild
     */
    private DiscordGuild createGuild(String guildId) {
        DiscordGuild guild = new DiscordGuild();
        guild.setGuildId(guildId);
        guild.setGuildOrders(new ArrayList<GuildOrder>());
        return guild;
    }
}
