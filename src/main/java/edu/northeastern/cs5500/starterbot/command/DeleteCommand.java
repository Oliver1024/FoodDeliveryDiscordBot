package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
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
public class DeleteCommand
        implements SlashCommandHandler, SelectionMenuHandler, ButtonClickHandler {
    @Inject ShoppingCartController shoppingCartController;
    HashMap<String, String> userAndSelectDish = new HashMap<>();

    @Inject
    public DeleteCommand() {}

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Delete a dish from current shopping cart");
    }

    /**
     * Respond to user's command input
     *
     * @param event, SlashCommandEvent
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /delete");
        User user = event.getUser();
        ShoppingCart cart = shoppingCartController.getShoppingCart(user.getId());
        if (cart == null) {
            event.reply(
                            ":slight_frown: You don't have an order, please type '/neworder' to begin a new order.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        ArrayList<DishObject> shoppingCartDishes =
                shoppingCartController.getOrderedDishes(user.getId());
        SelectionMenu dishes = buildSelectionMenu(shoppingCartDishes);

        event.reply("Choose the dish you want to delete")
                .setEphemeral(true)
                .addActionRow(dishes)
                .addActionRow(Button.primary(this.getName() + ":submit", "Submit"))
                .queue();
    }

    /**
     * Build selection menu for replying to discord user
     *
     * @param shoppingCartDishes the arrayList of all dishes the user have
     * @return a SelectionMenu object
     */
    protected SelectionMenu buildSelectionMenu(ArrayList<DishObject> shoppingCartDishes) {
        ArrayList<SelectOption> options = new ArrayList<>();

        for (DishObject dish : shoppingCartDishes) {
            SelectOption option =
                    SelectOption.of(dish.getDish() + ": $" + dish.getPrice(), dish.getDish());
            options.add(option);
        }
        SelectionMenu dishesList =
                SelectionMenu.create("delete")
                        .setPlaceholder(
                                "Choose the dish you want to remove from your shopping cart")
                        .addOptions(options)
                        .build();

        return dishesList;
    }

    /**
     * Provide a dropdown feature to users that can choose one of the menu
     *
     * @param event, SelectionMenuEvent
     */
    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        String dishString = event.getInteraction().getValues().get(0);
        userAndSelectDish.put(event.getUser().getId(), dishString);
        event.reply("Click submit to delete dish **" + dishString + "**.")
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
        String dishName = userAndSelectDish.get(user.getId());
        if (dishName == null) {
            event.reply("You should select the dish you want to remove").queue();
        } else {
            ArrayList<DishObject> totalDishes =
                    shoppingCartController.removeDish(dishName, user.getId());
            ;
            userAndSelectDish.remove(user.getId());
            String restaurantName = shoppingCartController.getRestaurantName(user.getId());
            event.replyEmbeds(buildReplyEmbed(dishName, totalDishes, restaurantName)).queue();
        }
    }

    /**
     * Return MessageEmbed object with dishTarget name
     *
     * @param dishName String, dish name which is the input where user want to delete
     * @return MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(
            String dishName, ArrayList<DishObject> totalDishes, String restaurantName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(":shopping_cart: Shopping cart:");
        eb.setDescription(
                "**"
                        + dishName
                        + "** has been removed! Your cart at **"
                        + restaurantName
                        + "** include:");

        Double totalPrice = 0.0;
        for (int i = 0; i < totalDishes.size(); i++) {
            DishObject curDish = totalDishes.get(i);
            String dish = curDish.getDish();
            Double price = curDish.getPrice();
            totalPrice += price;
            eb.addField(
                    (i + 1) + ". " + dish + ":", ":heavy_dollar_sign:" + price.toString(), false);
        }
        eb.addField(
                ":receipt: Total:",
                ":heavy_dollar_sign:" + Math.round(totalPrice * 100.0) / 100.0,
                false);

        eb.setColor(Color.GREEN);
        return eb.build();
    }
}
