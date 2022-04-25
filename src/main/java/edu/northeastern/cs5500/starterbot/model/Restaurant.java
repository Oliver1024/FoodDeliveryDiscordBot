package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Restaurant implements Model {
    ObjectId id;
    String name;
    String address;
    Long contact;
    ArrayList<String> cuisineType;
    ArrayList<DishObject> menu;
    String image;
}
