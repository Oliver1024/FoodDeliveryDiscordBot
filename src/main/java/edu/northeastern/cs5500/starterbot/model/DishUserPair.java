package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;

@Data
public class DishUserPair {
    DishObject dish;
    String userId;
    String username;
}
