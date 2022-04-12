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
        String[] userInputStrings = userInput.split(" ", 2);
        if (userInputStrings.length != 1) {
            res.add(userInputStrings[0]);
            res.add(userInputStrings[1]);
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
    public boolean checkIfStringIsNumber(String potentialNumbers) {
        for (int i = 0; i < potentialNumbers.length(); i++) {
            if (!Character.isDigit(potentialNumbers.charAt(i))) {
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

    /**
     * operate user input return different String
     *
     * @param processedInput processed ArrayList of user input String
     * @param restaurantNames contains all the name of restaurants
     * @return compare each condition, return different String.
     */
    public String checkInput(ArrayList<String> processedInput, ArrayList<String> restaurantNames) {
        boolean foundRestaurant = false;
        if (!checkIfStringIsNumber(processedInput.get(0))) {
            return "Invalid number input, please type '/last num_k' or '/last num_k restaurant_name' to check your history orders";
        } else if (processedInput.size() == 2) {
            for (String restaurantName : restaurantNames) {
                if (restaurantName.equalsIgnoreCase(processedInput.get(1))) {
                    foundRestaurant = true;
                    break;
                }
            }
            if (!foundRestaurant) {
                return "The restaurant name you provide doesn't match any restaurants we have. Pease type"
                        + "'/last num_k' or '/last num_k restaurant_name' to check your history orders.";
            }
        }
        return null;
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /last");
        User user = event.getUser();
        String userInput = event.getOption("content").getAsString();
        ArrayList<String> processedInput = processUserInput(userInput);
        ArrayList<Order> result = new ArrayList<>();
        ArrayList<String> restaurantNamesList = restaurantController.getAllRestaurantsName();
        String userInputAnswer = checkInput(processedInput, restaurantNamesList);
        if (!userInputAnswer.equalsIgnoreCase("foundTargetRestaurant")) {
            event.reply(userInputAnswer).queue();
            return;
        } else {
            result = userController.getLastKNumsOrders(user.getId(), processedInput);
            event.replyEmbeds(buildReplyEmbed(result)).queue();
        }
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
            int index = 1;
            for (int i = result.size() - 1; i >= 0; i--) {
                eb.addField(
                        index
                                + ". "
                                + result.get(i).getRestaurantName()
                                + ", "
                                + result.get(i).getOrderTime().toString()
                                + ", ",
                        buildOrderedDishesString(result.get(i).getOrderItems()),
                        false);
                index++;
            }
        }
        eb.setColor(Color.BLUE);
        return eb.build();
    }
}
