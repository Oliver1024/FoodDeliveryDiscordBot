package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DiscordGuild;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import javax.inject.Inject;

public class DiscordGuildController {
    GenericRepository<DiscordGuild> discordGuildRepository;

    @Inject
    DiscordGuildController(GenericRepository<DiscordGuild> discordGuildRepository) {
        this.discordGuildRepository = discordGuildRepository;
    }
}
