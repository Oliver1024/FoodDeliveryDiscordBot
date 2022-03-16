package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
public class Order {
    String restaurantName;
    Date orderTime;
    Boolean isDelivered;
    ArrayList<DishObject> orderItems;
}
