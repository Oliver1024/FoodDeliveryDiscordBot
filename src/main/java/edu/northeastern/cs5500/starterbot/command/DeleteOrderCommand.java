package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class DeleteOrderCommand implements SlashCommandHandler, ButtonClickHandler {

    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public DeleteOrderCommand() {}

    @Override
    public String getName() {
        return "deleteorder";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "delete your current shopping cart");
    }

    /**
     * Respond to user's command input
     *
     * @param event, SlashCommandEvent
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /deleteorder");
        User user = event.getUser();
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());
        if (restaurantName == null) {
            event.reply(
                            ":exclamation: Your don't have an order, please type '/neworder' to start a new order")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        event.reply(buildButton(restaurantName)).setEphemeral(true).queue();
    }

    /**
     * Respond to user's button hitting action
     *
     * @param event, ButtonClickEvent
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        String choice = event.getButton().getLabel();
        User user = event.getUser();
        Boolean isUserInShoppingCart = shoppingCartController.isUserInShoppingCart(user.getId());
        if (choice.equals("Yes")) {
            shoppingCartController.deleteCart(user.getId());
        }
        event.reply(replyMessForButton(isUserInShoppingCart, choice)).setEphemeral(true).queue();
    }

    /**
     * Helper function for onButtonClick
     *
     * @param isUserInShoppingCart whether the user has an ongoing order
     * @param choice which button the user hit
     * @return a string for replying to user
     */
    protected String replyMessForButton(Boolean isUserInShoppingCart, String choice) {
        if (!isUserInShoppingCart) {
            return ":exclamation: You already deleted your shopping cart!";
        } else if (choice.equals("Yes")) {
            return ":ballot_box_with_check: Deleted your shopping cart";
        } else {
            return ":ok_hand: OK, won't delete your shopping cart";
        }
    }

    /**
     * Build the message with button function used to confirm if the user really want to delete the
     * current shopping cart.
     *
     * @param restaurantName the name of the restaurant the user are ordering at
     * @return a Message Object
     */
    protected Message buildButton(String restaurantName) {
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder =
                messageBuilder.setActionRows(
                        ActionRow.of(
                                Button.primary(this.getName() + ":yes", "Yes"),
                                Button.danger(this.getName() + ":no", "No")));
        messageBuilder =
                messageBuilder.setContent(
                        String.format(
                                "Do you really want to delete the current shopping cart at **%s**?",
                                restaurantName));

        return messageBuilder.build();
    }
}
