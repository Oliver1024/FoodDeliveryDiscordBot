package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ShoppingCart implements Model {
    ObjectId id;
    String userId;
    String username;
    String restaurantName;
    ArrayList<DishObject> orderItems;
}
