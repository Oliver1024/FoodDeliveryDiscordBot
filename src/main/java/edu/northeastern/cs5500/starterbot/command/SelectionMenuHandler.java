package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public interface SelectionMenuHandler {

    /**
     * Get the name of the command
     *
     * @return String, the name of the command
     */
    @Nonnull
    public String getName();

    /**
     * Provide a dropdown selection menu for users
     *
     * @param event, SelectionMenuEvent
     */
    public void onSelectionMenu(@Nonnull SelectionMenuEvent event);
}
