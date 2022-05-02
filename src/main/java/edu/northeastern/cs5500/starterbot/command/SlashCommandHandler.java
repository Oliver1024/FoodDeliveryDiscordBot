package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommandHandler {

    /**
     * Get the name of the command
     *
     * @return String, the name of the command
     */
    @Nonnull
    public String getName();

    /**
     * Get the command data for users input
     *
     * @return CommandData
     */
    @Nonnull
    public CommandData getCommandData();

    /**
     * Respond to user's command input
     *
     * @param event, SlashCommandEvent
     */
    public void onSlashCommand(@Nonnull SlashCommandEvent event);
}
