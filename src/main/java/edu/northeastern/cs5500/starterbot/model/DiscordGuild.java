package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class DiscordGuild implements Model {
    ObjectId id;
    String guildId;
    ArrayList<GuildOrder> guildOrders;
}
