package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface ButtonClickHandler {

    /**
     * Get the name of the command
     *
     * @return String, the name of the command
     */
    @Nonnull
    public String getName();

    /**
     * Respond to user's button hitting action
     *
     * @param event, ButtonClickEvent
     */
    public void onButtonClick(@Nonnull ButtonClickEvent event);
}
