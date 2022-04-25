package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import java.awt.Color;
import java.util.ArrayList;
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
    User user;
    String restaurantName;

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

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /neworder");
        user = event.getUser();
        Boolean isUserInShoppingCart = shoppingCartController.isUserInShoppingCart(user.getId());
        if (isUserInShoppingCart) {
            event.reply(
                            "You still have an unfinished order, please check out that one first to start a new order.")
                    .queue();
            return;
        }
        ArrayList<String> restaurantNames = restaurantController.getAllRestaurantsName();
        SelectionMenu restaurants = buildSelectionMenu(restaurantNames);

        event.reply("Choose the restaurant you want to order")
                .setEphemeral(true)
                .addActionRow(restaurants)
                .addActionRow(Button.primary(this.getName() + ":submit", "Submit"))
                .queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        restaurantName = event.getInteraction().getValues().get(0);
        event.reply("Click submit to start ordering at **" + restaurantName + "**.").queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (restaurantName != null) {
            shoppingCartController.createNewShoppingCart(
                    user.getId(), user.getName(), restaurantName);
            event.replyEmbeds(buildReplyEmbed(restaurantName)).queue();
            restaurantName = null;
        } else {
            event.reply("You have to type '/order' again to start a new order").queue();
        }
    }

    /**
     * Build selection menu for replying to discord user
     *
     * @param restaurantNames the arraylist of all restaurant names
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
        eb.setTitle("You start a new order at " + restaurantName + "!");
        eb.addField("/order:", "to order a dish by selecting a dish from menu", false);
        eb.addField("/menu:", "to show the menu of the current restaurant", false);
        eb.addField("/showcart:", "to show the shopping cart :shopping_cart:", false);
        eb.setColor(Color.BLUE);
        return eb.build();
    }
}
