package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.DiscordGuild;
import edu.northeastern.cs5500.starterbot.model.GuildShoppingCart;
import edu.northeastern.cs5500.starterbot.model.Restaurant;
import edu.northeastern.cs5500.starterbot.model.ShoppingCart;
import edu.northeastern.cs5500.starterbot.model.User;
import edu.northeastern.cs5500.starterbot.model.UserPreference;

@Module
public class RepositoryModule {
    @Provides
    public GenericRepository<UserPreference> provideUserPreferencesRepository(
            MongoDBRepository<UserPreference> repository) {
        return repository;
    }

    @Provides
    public Class<UserPreference> provideUserPreference() {
        return UserPreference.class;
    }

    @Provides
    public GenericRepository<Restaurant> provideRestaurantRepository(
            MongoDBRepository<Restaurant> repository) {
        return repository;
    }

    @Provides
    public Class<Restaurant> provideRestaurant() {
        return Restaurant.class;
    }

    @Provides
    public GenericRepository<User> provideUserRepository(MongoDBRepository<User> repository) {
        return repository;
    }

    @Provides
    public Class<User> provideUser() {
        return User.class;
    }

    @Provides
    public GenericRepository<ShoppingCart> provideShoppingCartRepository(
            MongoDBRepository<ShoppingCart> repository) {
        return repository;
    }

    @Provides
    public Class<ShoppingCart> provideShoppingCart() {
        return ShoppingCart.class;
    }

    @Provides
    public GenericRepository<DiscordGuild> provideDiscordGuildRepository(
            MongoDBRepository<DiscordGuild> repository) {
        return repository;
    }

    @Provides
    public Class<DiscordGuild> provideDiscordGuild() {
        return DiscordGuild.class;
    }

    @Provides
    public GenericRepository<GuildShoppingCart> provideGuildShoppingCartRepository(
            MongoDBRepository<GuildShoppingCart> repository) {
        return repository;
    }

    @Provides
    public Class<GuildShoppingCart> provideGuildShoppingCart() {
        return GuildShoppingCart.class;
    }
}
