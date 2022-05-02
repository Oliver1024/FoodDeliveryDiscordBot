package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
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
public class OrderCommand implements SlashCommandHandler, ButtonClickHandler, SelectionMenuHandler {

    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;
    HashMap<String, String> userAndSelectDish = new HashMap<>();

    @Inject
    public OrderCommand() {}

    @Override
    public String getName() {
        return "order";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Add a new dish into your cart");
    }

    /**
     * Provide general command feature
     *
     * @param event, SlashCommandEvent
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /order");
        User user = event.getUser();
        ShoppingCart cart = shoppingCartController.getShoppingCart(user.getId());
        if (cart == null) {
            event.reply(":slight_frown: you need to type '/neworder' to start a new order first")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        ArrayList<DishObject> menu = restaurantController.getMenu(cart.getRestaurantName());
        SelectionMenu dishSelectionMenu = buildSelectionMenu(menu);

        event.reply("Choose the dish you want to order")
                .setEphemeral(true)
                .addActionRow(dishSelectionMenu)
                .addActionRow(Button.success(this.getName() + ":submit", "Submit"))
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
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());
        String dishString = event.getInteraction().getValues().get(0);
        if (dishString.equals("random")) {
            DishObject randomDish = restaurantController.randomDish(restaurantName);
            dishString = randomDish.getDish();
        }
        userAndSelectDish.put(user.getId(), dishString);
        event.reply("click 'Submit' to order " + dishString).setEphemeral(true).queue();
    }

    /**
     * Provide a button feature to users that can click
     *
     * @param event, ButtonClickEvent
     */
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        User user = event.getUser();
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());
        String dishString = userAndSelectDish.get(user.getId());
        if (dishString == null) {
            event.reply("You should select the dish you want to order").setEphemeral(true).queue();
        } else {
            DishObject dish = restaurantController.getDish(dishString, restaurantName);
            ArrayList<DishObject> orderedDishes =
                    shoppingCartController.addDish(user.getId(), dish);
            userAndSelectDish.remove(user.getId());
            event.replyEmbeds(buildReplyEmbed(orderedDishes, restaurantName)).queue();
        }
    }

    /**
     * Build a reply embed for user after they ordered a dish.
     *
     * @param totalDishes the dishes the user ordered
     * @param restaurantName the restaurant the user is ordering at
     * @return a MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(
            ArrayList<DishObject> totalDishes, String restaurantName) {
        EmbedBuilder eb = new EmbedBuilder();
        DishObject newDish = totalDishes.get(totalDishes.size() - 1);
        eb.setTitle(":shopping_cart: Shopping cart:");
        eb.setDescription(
                "**"
                        + newDish.getDish()
                        + "** has been added! Your cart at **"
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

    /**
     * Build the selection menu with the dishes for user
     *
     * @param menu the dishes in the restaurant
     * @return a SelectionMenu object
     */
    protected SelectionMenu buildSelectionMenu(ArrayList<DishObject> menu) {
        ArrayList<SelectOption> options = new ArrayList<>();
        SelectOption randomOption = SelectOption.of("Random dish", "random");
        options.add(randomOption);

        for (DishObject dish : menu) {
            SelectOption option =
                    SelectOption.of(dish.getDish() + ": $" + dish.getPrice(), dish.getDish());
            options.add(option);
        }

        SelectionMenu dishSelectionMenu =
                SelectionMenu.create("order")
                        .setPlaceholder("Choose the dish you want to order")
                        .addOptions(options)
                        .build();

        return dishSelectionMenu;
    }
}
