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
}
