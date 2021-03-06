package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import java.awt.Color;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class MenuCommand implements SlashCommandHandler {

    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public MenuCommand() {}

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "menu")
                .addOptions(
                        new OptionData(OptionType.STRING, "content", "show menu")
                                .setRequired(false));
    }

    /**
     * Respond to user's command input
     *
     * @param event, SlashCommandEvent
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /menu");
        User user = event.getUser();

        String userInput = null;
        if (event.getOption("content") != null) {
            userInput = event.getOption("content").getAsString();
        }

        String restaurantName =
                (userInput != null)
                        ? restaurantController.getRestaurantName(userInput)
                        : shoppingCartController.getRestaurantName(user.getId());

        if (restaurantName == null) {
            event.reply(
                            ":slight_frown: cannot find the restaurant you want to check. Please type in "
                                    + "'/menu' to check the restaurant you are ordering at or '/menu restaurant_name' to check the given restaurant")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        ArrayList<DishObject> resultMenu = restaurantController.getMenu(restaurantName);
        event.replyEmbeds(buildReplyEmbed(resultMenu, restaurantName)).queue();
    }

    /**
     * Return MessageEmbed object with Menu and restaurant name info which users can choose
     *
     * @param resultMenu - DishObject, which is for use to choose
     * @param restaurantName - String, which is the restaurant name
     * @return MessageEmbed object with Menu and restaurant name
     */
    protected MessageEmbed buildReplyEmbed(
            ArrayList<DishObject> resultMenu, String restaurantName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":scroll: " + restaurantName + "'s menu: ");

        for (int i = 0; i < resultMenu.size(); i++) {
            String strForDish = String.format("%d. %s:", i + 1, resultMenu.get(i).getDish());
            String strForPrice = String.format("$%s", resultMenu.get(i).getPrice().toString());
            eb.addField(strForDish, strForPrice, true);
        }
        eb.setColor(Color.BLUE);
        return eb.build();
    }
}
