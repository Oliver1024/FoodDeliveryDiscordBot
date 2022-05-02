package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface ButtonClickHandler {

    /**
     * Get name
     *
     * @return
     */
    @Nonnull
    public String getName();

    /**
     * Provide a button feature that user can on click
     *
     * @param event, ButtonClickEvent
     */
    public void onButtonClick(@Nonnull ButtonClickEvent event);
}
