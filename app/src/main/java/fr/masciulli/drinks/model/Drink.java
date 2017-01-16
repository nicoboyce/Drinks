package fr.masciulli.drinks.model;

import java.util.List;

public class Drink {
    private String name;
    private String imageUrl;
    private String history;
    private String wikipedia;
    private String instructions;
    private List<String> ingredients;

    public Drink(String name, String imageUrl, String history, String wikipedia, String instructions, List<String> ingredients) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.history = history;
        this.wikipedia = wikipedia;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHistory() {
        return history;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
