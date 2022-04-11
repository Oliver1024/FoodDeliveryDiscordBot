package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.UserController;
import edu.northeastern.cs5500.starterbot.model.DishObject;
import edu.northeastern.cs5500.starterbot.model.Order;
import java.awt.Color;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class LastCommand implements Command {

    @Inject UserController userController;
    @Inject RestaurantController restaurantController;

    @Inject
    public LastCommand() {}

    @Override
    public String getName() {
        return "last";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "last")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "Show history orders you ordered by following"
                                                + "'/last num_k' or '/last num_k restaurant_name' patterns")
                                .setRequired(true));
    }
    /**
     * divide input String from user into one ot two parts.
     *
     * @param userInput user input content
     * @return return an arrayList after we divided.
     */
    public ArrayList<String> processUserInput(String userInput) {
        ArrayList<String> res = new ArrayList<>();
        userInput = userInput.trim();
        int first_space_index = userInput.indexOf(" ");
        if (first_space_index != -1) {
            res.add(userInput.substring(0, first_space_index));
            res.add(userInput.substring(first_space_index + 1));
        } else {
            res.add(userInput);
        }
        return res;
    }

    /**
     * check if the every word of target string is a valid number ot not
     *
     * @param digit the String for check if it is digit
     * @return if all character are valid digit return true, else return false
     */
    public boolean checkIfStringIsNumber(String digit) {
        for (int i = 0; i < digit.length(); i++) {
            if (!Character.isDigit(digit.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * convert dishObject arrayList to a string
     *
     * @param dishObjects the arrayList contains all information about dish
     * @return a string contain all dish names
     */
    public String buildOrderedDishesString(ArrayList<DishObject> dishObjects) {
        String s = "";
        for (int i = 0; i < dishObjects.size() - 1; i++) {
            s += dishObjects.get(i).getDish() + ", ";
        }
        return s += dishObjects.get(dishObjects.size() - 1).getDish();
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /last");
        User user = event.getUser();
        String userInput = event.getOption("content").getAsString();
        ArrayList<String> processedInput = processUserInput(userInput);
        ArrayList<Order> result = new ArrayList<>();
        //  check if user input is a invalid number
        if (!checkIfStringIsNumber(processedInput.get(0))) {
            event.reply(
                            "Invalid number input, please type '/last num_k' or '/last num_k restaurant_name' to check your history orders")
                    .queue();
            return;
        }
        // if user input only contains numbers
        else if (processedInput.size() == 1) {
            result =
                    userController.getLastKNumsOrders(
                            user.getId(), Integer.valueOf(processedInput.get(0)));
        } else {
            // if user input with number and name of restaurant
            // check if the name of restaurant we have in our database
            if (restaurantController.getRestaurantName(processedInput.get(1)) == null) {
                event.reply(
                                "The restaurant name you provide doesn't match any restaurants we have. Pease type"
                                        + "'/last num_k' or '/last num_k restaurant_name' to check your history orders.")
                        .queue();
                return;
            }
            result =
                    userController.getLastKNumsOrders(
                            user.getId(),
                            Integer.valueOf(processedInput.get(0)),
                            processedInput.get(1));
        }
        event.replyEmbeds(buildReplyEmbed(result));
    }

    /**
     * Return MessageEmbed object with ArrayList of result.
     *
     * @param result the arrayList contains all order history
     * @return MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(ArrayList<Order> result) {
        EmbedBuilder eb = new EmbedBuilder();
        // check if the name of restaurant the user has order
        if (result.size() == 0) {
            eb.setTitle(
                    "the name of restaurant you typed do not have any order before, you can type /restaurants to check other restaurants");
        } else {
            eb.setTitle("your order history: ");
            for (int index = 0; index < result.size(); index++) {
                eb.addField(
                        index
                                + 1
                                + ". "
                                + result.get(index).getRestaurantName()
                                + ", "
                                + result.get(index).getOrderTime().toString()
                                + ", ",
                        buildOrderedDishesString(result.get(index).getOrderItems()),
                        true);
            }
        }
        eb.setColor(Color.BLUE);
        return eb.build();
    }
}
