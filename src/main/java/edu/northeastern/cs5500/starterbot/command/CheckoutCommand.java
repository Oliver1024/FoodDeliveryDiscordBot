package edu.northeastern.cs5500.starterbot.command;

import java.awt.Color;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class CheckoutCommand implements Command {

    @Inject
    public CheckoutCommand() {}

    @Override
    public String getName() {
        return "checkout";
    }

    @Override
    public CommandData getCommandData() {
        // return new CommandData(getName(), "aaa")
        //         .addOptions(new OptionData(OptionType.STRING, "content",
        // "bbb").setRequired(false));
        return new CommandData(getName(), "press enter to checkout");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /checkout");
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Thanks for ordering!");
        eb.addField("Restaurant: ", "Sam's Food", true);
        eb.addField("Username: ", "Wen", true);
        eb.addField("dish1", "9.9", false);
        eb.addField("dish2", "9.9", false);
        eb.setColor(Color.PINK);
        event.replyEmbeds(eb.build()).queue();
    }
}