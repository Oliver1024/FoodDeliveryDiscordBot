package edu.northeastern.cs5500.starterbot.command;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


@Singleton
@Slf4j
public class OrderCommand implements Command {

    @Inject
    public OrderCommand() {}

    @Override
    public String getName() {
        return "order";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "please order")
                .addOptions(
                        new OptionData(OptionType.STRING, "content", "please order")
                                .setRequired(true));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /order");
        event.reply("you ordered **" + event.getOption("content").getAsString() + "**").queue();
    }
}
