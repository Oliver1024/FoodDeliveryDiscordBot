package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class CommandModule {

    @Provides
    @IntoSet
    public Command provideSayCommand(SayCommand sayCommand) {
        return sayCommand;
    }

    @Provides
    @IntoSet
    public Command provideOrderCommand(OrderCommand orderCommand) {
        return orderCommand;
    }

    @Provides
    @IntoSet
    public Command provideCheckoutCommand(CheckoutCommand checkoutCommand) {
        return checkoutCommand;
    }

    @Provides
    @IntoSet
    public Command provideNewOrderCommand(NewOrderCommand newOrderCommand) {
        return newOrderCommand;
    }

    @Provides
    @IntoSet
    public Command provideRestaurantsCommand(RestaurantsCommand restaurantsCommand) {
        return restaurantsCommand;
    }

    @Provides
    @IntoSet
    public Command providePreferredNameCommand(PreferredNameCommand preferredNameCommand) {
        return preferredNameCommand;
    }

    @Provides
    @IntoSet
    public Command provideMenuCommand(MenuCommand menuCommand) {
        return menuCommand;
    }

    @Provides
    @IntoSet
    public Command provideShowCartCommand(ShowCartCommand showCartCommand) {
        return showCartCommand;
    }

    @Provides
    @IntoSet
    public Command provideInfoCommand(InfoCommand infoCommand) {
        return infoCommand;
    }

    @Provides
    @IntoSet
    public Command provideOrderStatusCommand(OrderStatusCommand orderStatusCommand) {
        return orderStatusCommand;
    }
}
