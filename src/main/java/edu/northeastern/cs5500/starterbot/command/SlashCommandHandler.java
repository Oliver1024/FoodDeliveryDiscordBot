package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommandHandler {

    /**
     * Get name
     *
     * @return String, name
     */
    @Nonnull
    public String getName();

    /**
     * Get the data that the users input corresponded command
     *
     * @return CommandData
     */
    @Nonnull
    public CommandData getCommandData();

    /**
     * Provide general command feature
     *
     * @param event, SlashCommandEvent
     */
    public void onSlashCommand(@Nonnull SlashCommandEvent event);
}
