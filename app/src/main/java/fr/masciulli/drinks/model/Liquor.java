package fr.masciulli.drinks.model;

import java.util.List;

public class Liquor {
    private String name;
    private String imageUrl;
    private String wikipedia;
    private String history;
    private List<String> otherNames;

    public Liquor(String name, String imageUrl, String wikipedia, String history, List<String> otherNames) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.wikipedia = wikipedia;
        this.history = history;
        this.otherNames = otherNames;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public String getHistory() {
        return history;
    }

    public List<String> getOtherNames() {
        return otherNames;
    }
}
