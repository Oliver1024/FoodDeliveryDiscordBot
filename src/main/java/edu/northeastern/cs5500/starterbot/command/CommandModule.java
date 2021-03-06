package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class CommandModule {

    @Provides
    @IntoSet
    public SlashCommandHandler provideNewOrderCommand(NewOrderCommand newOrderCommand) {
        return newOrderCommand;
    }

    @Provides
    @IntoSet
    public SelectionMenuHandler provideNewOrderCommandMenuHandler(NewOrderCommand newOrderCommand) {
        return newOrderCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideNewOrderCommandClickHandler(NewOrderCommand newOrderCommand) {
        return newOrderCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideOrderCommand(OrderCommand orderCommand) {
        return orderCommand;
    }

    @Provides
    @IntoSet
    public SelectionMenuHandler provideOrderCommandMenuHandler(OrderCommand orderCommand) {
        return orderCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideOrderCommandClickHandler(OrderCommand orderCommand) {
        return orderCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideCheckoutCommand(CheckoutCommand checkoutCommand) {
        return checkoutCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideRestaurantsCommand(RestaurantsCommand restaurantsCommand) {
        return restaurantsCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideMenuCommand(MenuCommand menuCommand) {
        return menuCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideShowCartCommand(ShowCartCommand showCartCommand) {
        return showCartCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideInfoCommand(InfoCommand infoCommand) {
        return infoCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideOrderStatusCommand(OrderStatusCommand orderStatusCommand) {
        return orderStatusCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideLastCommand(LastCommand lastCommand) {
        return lastCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideDeleteCommand(DeleteCommand deleteCommand) {
        return deleteCommand;
    }

    @Provides
    @IntoSet
    public SelectionMenuHandler provideDeleteCommandMenuHandler(DeleteCommand deleteCommand) {
        return deleteCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideDeleteCommandClickHandler(DeleteCommand deleteCommand) {
        return deleteCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideDeleteOrderCommand(DeleteOrderCommand deleteOrderCommand) {
        return deleteOrderCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideDeleteOrderCommandClickHandler(
            DeleteOrderCommand deleteOrderCommand) {
        return deleteOrderCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideGroupOrderCommand(GroupOrderCommand groupOrderCommand) {
        return groupOrderCommand;
    }

    @Provides
    @IntoSet
    public SelectionMenuHandler provideGroupOrderCommandMenuHandler(
            GroupOrderCommand groupOrderCommand) {
        return groupOrderCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideGroupOrderCommandClickHandler(
            GroupOrderCommand groupOrderCommand) {
        return groupOrderCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideGroupCheckoutCommand(
            GroupCheckoutCommand groupCheckoutCommand) {
        return groupCheckoutCommand;
    }
}
