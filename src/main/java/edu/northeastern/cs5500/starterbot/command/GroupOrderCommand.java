package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.DiscordGuildController;
import edu.northeastern.cs5500.starterbot.controller.GuildShoppingCartController;
import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.DishUserPair;
import edu.northeastern.cs5500.starterbot.model.GuildShoppingCart;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
public class GroupOrderCommand
        implements SlashCommandHandler, SelectionMenuHandler, ButtonClickHandler {

    @Inject DiscordGuildController discordGuildController;
    @Inject RestaurantController restaurantController;
    @Inject GuildShoppingCartController guildShoppingCartController;

    Map<String, String> guildAndRestaurant = new HashMap<>();
    Map<String, String> userAndDish = new HashMap<>();

    @Inject
    public GroupOrderCommand() {}

    @Override
    public String getName() {
        return "grouporder";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Start a group order at the current discord guild");
    }

    /**
     * Respond to user's command input
     *
     * @param event, SlashCommandEvent
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /grouporder");
        String guildId = event.getGuild().getId();
        if (guildShoppingCartController.isGuildInShoppingCart(guildId)) {
            event.reply(
                            "Your guild has an ongoing order! Please use /groupcheckout to check out first.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        ArrayList<String> restaurantNames = restaurantController.getAllRestaurantsName();
        SelectionMenu restaurants = buildRestaurantSelection(restaurantNames);

        event.reply("Choose the restaurant that your guild wants to order")
                .setEphemeral(true)
                .addActionRow(restaurants)
                .addActionRow(Button.primary(this.getName() + ":submit", "Submit"))
                .queue();
    }

    /**
     * Respond to user's button hitting action
     *
     * @param event, ButtonClickEvent
     */
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        String guildId = event.getGuild().getId();
        User user = event.getUser();
        Boolean isStartOrdering = guildShoppingCartController.isGuildInShoppingCart(guildId);

        if (!isStartOrdering) {
            String restaurantName = guildAndRestaurant.get(guildId);

            if (restaurantName == null) {
                event.reply("Please select restaurant you want to order at")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            guildAndRestaurant.remove(guildId);
            guildShoppingCartController.createCart(guildId, restaurantName, user.getId());

            ArrayList<DishObject> menu = restaurantController.getMenu(restaurantName);
            SelectionMenu dishSelectionMenu = buildDishSelection(menu);

            event.reply(":point_down: Choose the dish you want to order")
                    .addActionRow(dishSelectionMenu)
                    .addActionRow(Button.success(this.getName() + ":submit", "Submit"))
                    .queue();
        } else {
            String dishString = userAndDish.get(user.getId());

            if (dishString == null) {
                event.reply(":exclamation: Please select dish you want to order")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            userAndDish.remove(user.getId());

            GuildShoppingCart cart = guildShoppingCartController.getCart(guildId);

            DishObject dish = restaurantController.getDish(dishString, cart.getRestaurantName());
            ArrayList<DishUserPair> orderedDishes =
                    guildShoppingCartController.addDish(
                            dish, guildId, user.getId(), user.getName());

            event.replyEmbeds(buildReplyEmbed(orderedDishes, cart.getRestaurantName())).queue();
        }
    }

    /**
     * Provide a dropdown selection menu for users
     *
     * @param event, SelectionMenuEvent
     */
    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        String guildId = event.getGuild().getId();
        User user = event.getUser();
        Boolean isStartOrdering = guildShoppingCartController.isGuildInShoppingCart(guildId);
        String selection = event.getInteraction().getValues().get(0);

        String replyMess = replyMessForSelection(isStartOrdering, selection, guildId, user.getId());
        event.reply(replyMess).setEphemeral(true).queue();
    }

    /**
     * Helper function which build corresponding reply message when user select option on dropdown
     *
     * @param isStartOrdering if the guild has already created a shopping cart
     * @param selection the string of the user's selection on dropdown
     * @param guildId the id of the guild
     * @param userId the id of the user who made the selection
     * @return a String for the reply message
     */
    protected String replyMessForSelection(
            Boolean isStartOrdering, String selection, String guildId, String userId) {
        if (!isStartOrdering) {
            guildAndRestaurant.put(guildId, selection);
            return "click 'Submit' to order at " + selection;
        }
        userAndDish.put(userId, selection);
        return "click 'Submit' to order " + selection;
    }
    /**
     * Provide general command feature
     *
     * @param event, SlashCommandEvent
     */
    /**
     * Build restaurant selection for replying to discord user
     *
     * @param restaurantNames the ArrayList of all restaurant names
     * @return a SelectionMenu object
     */
    protected SelectionMenu buildRestaurantSelection(ArrayList<String> restaurantNames) {
        ArrayList<SelectOption> options = new ArrayList<>();
        for (String restaurantName : restaurantNames) {
            SelectOption option = SelectOption.of(restaurantName, restaurantName);
            options.add(option);
        }

        SelectionMenu restaurants =
                SelectionMenu.create("grouporder")
                        .setPlaceholder("Choose the restaurant your guild want to order")
                        .addOptions(options)
                        .build();

        return restaurants;
    }

    /**
     * Build menu selection for replying to discord user
     *
     * @param menu the dishes in the restaurant
     * @return a SelectionMenu object
     */
    protected SelectionMenu buildDishSelection(ArrayList<DishObject> menu) {
        ArrayList<SelectOption> options = new ArrayList<>();
        for (DishObject dish : menu) {
            SelectOption option =
                    SelectOption.of(dish.getDish() + ": $" + dish.getPrice(), dish.getDish());
            options.add(option);
        }

        SelectionMenu dishSelectionMenu =
                SelectionMenu.create("grouporder")
                        .setPlaceholder("Choose the dish you want to order")
                        .addOptions(options)
                        .build();

        return dishSelectionMenu;
    }

    /**
     * Build a reply embed to show the current shopping cart for user after they ordered a dish.
     *
     * @param dishes the dishes the users in the discord guild have ordered
     * @param restaurantName the restaurant the guild is ordering at
     * @return a MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(ArrayList<DishUserPair> dishes, String restaurantName) {
        EmbedBuilder eb = new EmbedBuilder();
        DishUserPair dishUserPair = dishes.get(dishes.size() - 1);
        eb.setTitle(":shopping_cart: Guild shopping cart:");
        eb.setDescription(
                "**"
                        + dishUserPair.getUsername()
                        + "** has added **"
                        + dishUserPair.getDish().getDish()
                        + "** into the guild shopping cart at **"
                        + restaurantName
                        + "**");
        Double totalPrice = 0.0;
        for (int i = 0; i < dishes.size(); i++) {
            DishUserPair curPair = dishes.get(i);
            String dish = curPair.getDish().getDish();
            Double price = curPair.getDish().getPrice();
            String username = curPair.getUsername();
            totalPrice += price;
            eb.addField(
                    (i + 1) + ". " + dish + ": $" + price.toString(), "add by " + username, false);
        }
        eb.addField(":receipt: Total:", "$" + Math.round(totalPrice * 100.0) / 100.0, false);

        eb.setColor(Color.GREEN);
        return eb.build();
    }
}
