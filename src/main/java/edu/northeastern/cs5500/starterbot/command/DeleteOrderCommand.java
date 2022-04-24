package edu.northeastern.cs5500.starterbot.command;

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
        return new CommandData(getName(), "delete your current shopping cart🛒");
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /deleteorder");
        User user = event.getUser();
        String restaurantName = shoppingCartController.getRestaurantName(user.getId());
        if (restaurantName == null) {
            event.reply("Your don't have an order, please type '/neworder' to start a new order");
            return;
        }
        event.reply(buildButton(restaurantName)).setEphemeral(true).queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        String choice = event.getButton().getLabel();
        User user = event.getUser();
        Boolean isUserInShoppingCart = shoppingCartController.isUserInShoppingCart(user.getId());
        if (!isUserInShoppingCart) {
            event.reply("You already deleted your shopping cart🛒!").queue();
        } else if (choice.equals("Yes")) {
            shoppingCartController.deleteCart(user.getId());
            event.reply("Deleted your shopping cart🛒").queue();
        } else {
            event.reply("🆗, won't delete your shopping cart🛒").queue();
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
                        "Do you really want to delete the current shopping cart🛒 at **"
                                + restaurantName
                                + "**?");

        return messageBuilder.build();
    }
}
