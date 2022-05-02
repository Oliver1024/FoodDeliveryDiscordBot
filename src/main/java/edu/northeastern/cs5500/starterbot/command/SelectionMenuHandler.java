package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public interface SelectionMenuHandler {

    /**
     * Get name
     *
     * @return String, name
     */
    @Nonnull
    public String getName();

    /**
     * Provide a dropdown feature to users that can choose one of the menu
     *
     * @param event, SelectionMenuEvent
     */
    public void onSelectionMenu(@Nonnull SelectionMenuEvent event);
}
