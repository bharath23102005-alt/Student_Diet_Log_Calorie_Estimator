import java.util.*;

public class Food {
    private static final Map<String, Float> FOOD = new HashMap<>();

    static {
        // --- snacks & fast food
        FOOD.put("frenchfries", 3.1f); FOOD.put("friedchicken", 2.6f);
        FOOD.put("onionrings", 2.9f); FOOD.put("popcorn", 1.2f);
        FOOD.put("energybar", 2.0f); FOOD.put("namkeen", 1.5f);
        FOOD.put("cake", 3.0f); FOOD.put("cornflakes", 1.2f);
        FOOD.put("muesli", 1.9f); FOOD.put("murukku", 2.3f);
        FOOD.put("cheeseballs", 1.7f); FOOD.put("ladoo", 3.3f);

        // --- dairy & staples
        FOOD.put("milk", 0.42f); FOOD.put("paneer", 2.64f);
        FOOD.put("yogurt", 0.59f); FOOD.put("cheese", 3.0f);
        FOOD.put("egg", 1.43f); FOOD.put("jam", 0.9f);

        // --- veg & grains
        FOOD.put("potato", 0.77f); FOOD.put("potatochips", 3.1f);
        FOOD.put("potatowedge", 1.9f); FOOD.put("rice", 3.1f);
        FOOD.put("pulao", 1.7f); FOOD.put("biryani", 2.4f);
        FOOD.put("chapati", 0.4f); FOOD.put("naan", 2.0f);
        FOOD.put("paratha", 1.4f); FOOD.put("dal", 3.6f);
        FOOD.put("rasam", 2.3f); FOOD.put("sambar", 0.4f);
        FOOD.put("chutney", 1.2f); FOOD.put("upma", 0.8f);
        FOOD.put("idli", 1.8f); FOOD.put("dosa", 1.6f);

        // --- meats
        FOOD.put("fish", 2.7f); FOOD.put("shrimp", 3.0f);
        FOOD.put("crab", 3.5f); FOOD.put("lobstr", 3.0f);
        FOOD.put("beef", 2.2f); FOOD.put("pork", 1.0f);
        FOOD.put("mutton", 1.2f); FOOD.put("chicken", 0.9f);

        // --- plant protein & misc
        FOOD.put("tofu", 1.3f); FOOD.put("soya", 0.5f);
        FOOD.put("salad", 0.2f); FOOD.put("soup", 3.3f);
        FOOD.put("oats", 0.9f); FOOD.put("wheatflour", 0.2f);
        FOOD.put("sugar", 1.1f); FOOD.put("peanutbutter", 0.5f);
        FOOD.put("honey", 0.4f);

        // --- fruits & nuts
        FOOD.put("banana", 0.3f); FOOD.put("apple", 1.1f);
        FOOD.put("orange", 1.2f); FOOD.put("grapes", 0.9f);
        FOOD.put("pineapple", 1.3f); FOOD.put("mango", 0.5f);
        FOOD.put("avocado", 0.3f); FOOD.put("almond", 0.4f);
        FOOD.put("cashew", 0.2f); FOOD.put("walnut", 3.1f);
        FOOD.put("seeds", 1.0f); FOOD.put("coconut", 0.4f);

        // --- drinks
        FOOD.put("water", 2.3f); FOOD.put("coffee", 2.2f);
        FOOD.put("tea", 0.8f); FOOD.put("juice", 2.1f);
        FOOD.put("soda", 1.6f); FOOD.put("beer", 3.3f);
        FOOD.put("wine", 0.65f); FOOD.put("whiskey", 0.98f);
        FOOD.put("vodka", 1.8f); FOOD.put("rum", 2.9f);
        FOOD.put("gin", 1.7f);

        // --- desserts
        FOOD.put("chocolateicecream", 0.6f);
        FOOD.put("vanillaicecream", 2.3f);
        FOOD.put("strawberryicecream", 1.9f);
        FOOD.put("brownieicecream", 0.55f);
        FOOD.put("fruitcake", 3.1f);
        FOOD.put("halwa", 1.2f);
        FOOD.put("gulabjamun", 2.2f);
        FOOD.put("rasgulla", 0.93f);
        FOOD.put("kulfi", 1.1f);
    }

    public static String normalize(String s) {
        return s.toLowerCase().replaceAll("\\s+", "");
    }

    public static float getCalories(String foodName, float grams) {
        String key = normalize(foodName);
        Float perGram = FOOD.get(key);
        return (perGram == null) ? -1f : perGram * grams;
    }
}
