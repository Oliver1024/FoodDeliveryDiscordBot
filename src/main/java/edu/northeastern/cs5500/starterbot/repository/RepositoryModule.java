package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.Restaurant;

@Module
public class RepositoryModule {

    @Provides
    public GenericRepository<Restaurant> provideRestaurantRepository(
            MongoDBRepository<Restaurant> repository) {
        return repository;
    }

    @Provides
    public Class<Restaurant> provideRestaurant() {
        return Restaurant.class;
    }
}
