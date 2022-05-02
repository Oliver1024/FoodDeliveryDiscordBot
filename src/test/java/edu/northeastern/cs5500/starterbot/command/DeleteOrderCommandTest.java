package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.dv8tion.jda.api.entities.Message;
import org.junit.jupiter.api.Test;

public class DeleteOrderCommandTest {
    DeleteOrderCommand deleteOrderCommand = new DeleteOrderCommand();

    @Test
    void testGetName() {
        String expected = "deleteorder";
        assertEquals(expected, deleteOrderCommand.getName());
    }

    @Test
    void testGetCommandData() {
        String expectedDescrption = "delete your current shopping cart";
        assertEquals(expectedDescrption, deleteOrderCommand.getCommandData().getDescription());
    }

    @Test
    void testBuildButton() {
        String restaurantName = "restaurant1";
        Message actual = deleteOrderCommand.buildButton(restaurantName);

        String expectedContent =
                "Do you really want to delete the current shopping cart at **restaurant1**?";
        assertEquals(expectedContent, actual.getContentRaw());

        assertEquals(2, actual.getActionRows().get(0).getButtons().size());

        assertEquals("deleteorder:yes", actual.getActionRows().get(0).getButtons().get(0).getId());
        assertEquals("Yes", actual.getActionRows().get(0).getButtons().get(0).getLabel());

        assertEquals("deleteorder:no", actual.getActionRows().get(0).getButtons().get(1).getId());
        assertEquals("No", actual.getActionRows().get(0).getButtons().get(1).getLabel());
    }

    @Test
    void testReplyMessForButton() {
        String test1 = deleteOrderCommand.replyMessForButton(true, "Yes");
        String expected1 = ":ballot_box_with_check: Deleted your shopping cart";

        String test2 = deleteOrderCommand.replyMessForButton(false, "Yes");
        String expected2 = ":exclamation: You already deleted your shopping cart!";

        String test3 = deleteOrderCommand.replyMessForButton(true, "No");
        String expected3 = ":ok_hand: OK, won't delete your shopping cart";

        assertEquals(expected1, test1);
        assertEquals(expected2, test2);
        assertEquals(expected3, test3);
    }
}
