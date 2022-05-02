package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

@Singleton
@Slf4j
public class NewOrderCommand
        implements SlashCommandHandler, SelectionMenuHandler, ButtonClickHandler {

    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;
    HashMap<String, String> userAndRestaurantName = new HashMap<>();

    @Inject
    public NewOrderCommand() {}

    @Override
    public String getName() {
        return "neworder";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Start a new order");
    }

    /**
     * Provide general command feature
     *
     * @param event, SlashCommandEvent
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /neworder");
        User user = event.getUser();
        Boolean isUserInShoppingCart = shoppingCartController.isUserInShoppingCart(user.getId());
        if (isUserInShoppingCart) {
            event.reply(
                            ":slight_frown: You still have an unfinished order, please check out that one first to start a new order.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        ArrayList<String> restaurantNames = restaurantController.getAllRestaurantsName();
        SelectionMenu restaurants = buildSelectionMenu(restaurantNames);

        event.reply(":point_down: Choose the restaurant you want to order")
                .setEphemeral(true)
                .addActionRow(restaurants)
                .addActionRow(Button.primary(this.getName() + ":submit", "Submit"))
                .queue();
    }

    /**
     * Provide a dropdown feature to users that can choose one of the menu
     *
     * @param event, SelectionMenuEvent
     */
    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        User user = event.getUser();
        String restaurantName = event.getInteraction().getValues().get(0);
        userAndRestaurantName.put(user.getId(), restaurantName);
        event.reply("Click submit to start ordering at **" + restaurantName + "**.")
                .setEphemeral(true)
                .queue();
    }

    /**
     * Provide a button feature that user can on click
     *
     * @param event, ButtonClickEvent
     */
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        User user = event.getUser();
        String restaurantName = userAndRestaurantName.get(user.getId());
        if (restaurantName != null) {
            shoppingCartController.createNewShoppingCart(
                    user.getId(), user.getName(), restaurantName);
            event.replyEmbeds(buildReplyEmbed(restaurantName)).queue();
            userAndRestaurantName.remove(user.getId());
        } else {
            event.reply("You have to type '/order' again to start a new order")
                    .setEphemeral(true)
                    .queue();
        }
    }

    /**
     * Build selection menu for replying to discord user
     *
     * @param restaurantNames the ArrayList of all restaurant names
     * @return a SelectionMenu object
     */
    protected SelectionMenu buildSelectionMenu(ArrayList<String> restaurantNames) {
        ArrayList<SelectOption> options = new ArrayList<>();
        for (String restaurantName : restaurantNames) {
            SelectOption option = SelectOption.of(restaurantName, restaurantName);
            options.add(option);
        }

        SelectionMenu restaurants =
                SelectionMenu.create("neworder")
                        .setPlaceholder("Choose the restaurant you want to order")
                        .addOptions(options)
                        .build();

        return restaurants;
    }

    /**
     * Return MessageEmbed object with restaurant name
     *
     * @param restaurantName String, restaurant name which is the input where user will order
     * @return MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(String restaurantName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":clap: You start a new order at " + restaurantName + "!");
        eb.addField(
                ":point_right: /order:", "to order a dish by selecting a dish from menu", false);
        eb.addField(":point_right: /menu:", "to show the menu of the current restaurant", false);
        eb.addField(":point_right: /showcart:", "to show the shopping cart", false);
        eb.setColor(Color.BLUE);
        return eb.build();
    }
}
