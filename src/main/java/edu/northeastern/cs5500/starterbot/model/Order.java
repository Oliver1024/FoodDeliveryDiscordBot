package edu.northeastern.cs5500.starterbot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.Data;

@Data
public class Order {
    String restaurantName;
    LocalDateTime orderTime;
    Boolean isDelivered;
    ArrayList<DishObject> orderItems;
}
