package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.DiscordGuildController;
import edu.northeastern.cs5500.starterbot.controller.GuildShoppingCartController;
import edu.northeastern.cs5500.starterbot.model.DishUserPair;
import edu.northeastern.cs5500.starterbot.model.GuildShoppingCart;
import java.awt.Color;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class GroupCheckoutCommand implements SlashCommandHandler {
    @Inject GuildShoppingCartController guildShoppingCartController;
    @Inject DiscordGuildController discordGuildController;

    @Inject
    public GroupCheckoutCommand() {}

    @Override
    public String getName() {
        return "groupcheckout";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "check out your guild's group order");
    }

    /**
     * Respond to user's command input
     *
     * @param event, SlashCommandEvent
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /groupcheckout");
        User user = event.getUser();
        String guildId = event.getGuild().getId();
        GuildShoppingCart shoppingCart = guildShoppingCartController.getCart(guildId);

        if (shoppingCart == null) {
            event.reply("You don't have an order! Please user '/grouporder' to begin group order")
                    .setEphemeral(true)
                    .queue();
        } else if (!guildShoppingCartController.matchCreateUserId(user.getId(), guildId)) {
            event.reply(
                            "you are not allowed to checkout the order, please wait the order creator finish order")
                    .setEphemeral(true)
                    .queue();
        } else {
            MessageEmbed eb = buildEB(shoppingCart.getDishes(), shoppingCart.getRestaurantName());
            discordGuildController.addOrder(shoppingCart);
            guildShoppingCartController.deleteCart(guildId);
            event.replyEmbeds(eb).queue();
        }
    }

    /**
     * Build a reply embed to show the order after the user check out the current group order.
     *
     * @param orderedDishes the dishes the users in the discord guild have ordered
     * @param restaurantName the restaurant the guild is ordering at
     * @return a MessageEmbed object
     */
    protected MessageEmbed buildEB(ArrayList<DishUserPair> orderedDishes, String restaurantName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":grin: Thanks for ordering!");
        eb.setDescription("Your guild order at **" + restaurantName + "** includes:");

        Double totalPrice = 0.0;

        for (int i = 0; i < orderedDishes.size(); i++) {
            DishUserPair curPair = orderedDishes.get(i);
            String dish = curPair.getDish().getDish();
            Double price = curPair.getDish().getPrice();
            String username = curPair.getUsername();
            totalPrice += price;

            String strForDish = String.format("%d. %s: $%s", i + 1, dish, price.toString());
            String strForPrice = String.format("add by %s", username);

            eb.addField(strForDish, strForPrice, false);
        }
        eb.addField(":receipt: Total:", "$" + Math.round(totalPrice * 100.0) / 100.0, false);
        eb.setColor(Color.PINK);
        return eb.build();
    }
}
