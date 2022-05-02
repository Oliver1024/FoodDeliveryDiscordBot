package edu.northeastern.cs5500.starterbot.command;

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
        return new CommandData(getName(), "press enter to groupcheckout");
    }

    protected MessageEmbed buildEB(
            String restaurantName, ArrayList<DishUserPair> orderedDishes, String createUserId) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Thanks for ordering! :grin:");
        eb.setDescription(createUserId + "'s order at **" + restaurantName + "** includes:");

        Double totalPrice = 0.0;

        for (int i = 0; i < orderedDishes.size(); i++) {
            eb.addField(
                    (i + 1)
                            + ". "
                            + orderedDishes.get(i).getDish().getDish()
                            + ":heavy_dollar_sign:"
                            + orderedDishes.get(i).getDish().getPrice().toString(),
                    " add by " + orderedDishes.get(i).getUsername(),
                    false);
            totalPrice += orderedDishes.get(i).getDish().getPrice();
        }
        eb.addField(
                ":receipt: Total:",
                ":heavy_dollar_sign:" + Math.round(totalPrice * 100.0) / 100.0,
                false);
        eb.setColor(Color.PINK);
        return eb.build();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /groupcheckout");
        User user = event.getUser();
        String guildId = event.getGuild().getId();
        GuildShoppingCart shoppingCart = guildShoppingCartController.getCart(guildId);

        if (shoppingCart == null) {
            event.reply("You don't have an order! Please user /grouporder to begin group order")
                    .queue();
        } else if (!guildShoppingCartController.matchCreateUserId(user.getId(), guildId)) {
            event.reply(
                            "you are not allowed to checkout the order, please wait the order creator finish order")
                    .queue();
        } else {
            MessageEmbed eb =
                    buildEB(
                            shoppingCart.getRestaurantName(),
                            shoppingCart.getDishes(),
                            shoppingCart.getCreatedUserId());
            discordGuildController.addOrder(shoppingCart);
            guildShoppingCartController.deleteCart(guildId);
            event.replyEmbeds(eb).queue();
        }
    }
}
