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
                        new OptionData(OptionType.STRING, "content", "show  Last orders User take")
                                .setRequired(false));
    }
    /**
     * we treat the user input string split into 2 parts. he first one we think it is a digit, the
     * rest part should be combine to the whole together for create our target name of restrant.
     * Also, it is possible that the user only has number. No matter what situation, only return the
     * list, and ther method for judging.
     *
     * @param userInput user input content
     * @return return an arraylist that we treated.
     */
    public ArrayList<String> treatInput(String userInput) {
        ArrayList<String> res = new ArrayList<>();
        // already checked at On event if userInput is null or not
        String[] splitedInput = userInput.split(" ");
        res.add(splitedInput[0]);
        if (splitedInput.length != 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < splitedInput.length - 1; i++) {
                sb.append(splitedInput[i]);
                sb.append(" ");
            }
            sb.append(splitedInput[splitedInput.length - 1]);
            res.add(sb.toString());
        }
        return res;
    }

    /**
     * check if the first word is digit ot not
     *
     * @param digit the String for check if it is digit
     * @return if all are digit return ture, else return false
     */
    public boolean CheckStringisDigit(String digit) {
        for (int i = 0; i < digit.length(); i++) {
            if (!Character.isDigit(digit.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * conver arraylist of dishObjevt to a single String.
     *
     * @param dishObjects
     * @return
     */
    public String covertoSting(ArrayList<DishObject> dishObjects) {
        String s = "";
        for (int i = 0; i < dishObjects.size() - 1; i++) {
            s += dishObjects.get(i).getDish() + ", ";
        }
        return s += dishObjects.get(dishObjects.size() - 1).getDish();
    }
    /**
     * check if the name of restrauant is really exsit in database
     *
     * @param rName the name of restrant we want to check
     * @return if exsit retrn true, else return false
     */
    public boolean checkIfRestrantInDataBase(String rName) {
        return restaurantController.getRestaurant(rName) != null ? true : false;
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /last");
        User user = event.getUser();
        String userInput;
        if (event.getOption("content") == null) {
            event.reply(
                            "you have to add at lease a number to show your hisory orders or with number and space with your target restaurant name  "
                                    + " with'/last' with an number '2' or also ' ' and combine with your target restaurant name")
                    .queue();
            return;
        } else {
            userInput = event.getOption("content").getAsString();
            ArrayList<String> splitedInput = treatInput(userInput);
            ArrayList<Order> result = new ArrayList<>();
            // if user input is not a digit at first splited String
            if (!CheckStringisDigit(splitedInput.get(0))) {
                event.reply(
                                "you have to add at lease a number to show your hisory orders, any other word is not alowed")
                        .queue();
                return;
            }
            // if user input only digit
            else if (splitedInput.size() == 1) {
                result =
                        userController.getKnumsOrders(
                                user.getId(), Integer.valueOf(splitedInput.get(0)));
            } else {
                // if user input with digit and name of restraurant

                // check if the name of restaurant we have in our dababase

                if (!checkIfRestrantInDataBase(splitedInput.get(1))) {
                    event.reply(
                                    " the name of restaurant you typed do exsit in the databse, you can type /restaurants to find")
                            .queue();
                    return;
                }

                result =
                        userController.getKnumsOrders(
                                user.getId(),
                                Integer.valueOf(splitedInput.get(0)),
                                splitedInput.get(1));
                // check if the name of restaurant the user has order
                if (result.size() == 0) {
                    event.reply(
                                    " the name of restaurant you typed do not have any order before, you can type other restaurants name to check")
                            .queue();
                    return;
                }
            }
            event.replyEmbeds(buildReplyEmbed(result, user));
        }
    }

    /**
     * Return MessageEmbed object with Arrlist of result.
     *
     * @param result the arrylist contains all order history
     * @param user the target user
     * @return MessageEmbed object
     */
    protected MessageEmbed buildReplyEmbed(ArrayList<Order> result, User user) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(user.getName() + "'s order history: ");
        for (int index = 0; index < result.size(); index++) {
            eb.addField(
                    index
                            + 1
                            + ". "
                            + result.get(index).getRestaurantName()
                            + ", "
                            + result.get(index).getOrderTime().toString()
                            + ", ",
                    covertoSting(result.get(index).getOrderItems()),
                    true);
        }
        eb.setColor(Color.BLUE);
        return eb.build();
    }
}
