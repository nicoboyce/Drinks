package fr.masciulli.drinks.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;

@AutoParcelGson
public abstract class Liquor {
    public abstract String name();

    public abstract String imageUrl();

    public abstract String wikipedia();

    public abstract String history();

    public abstract List<String> otherNames();

    public static Liquor create(String name, String imageUrl, String wikipedia, String history, List<String> otherNames) {
        return new AutoParcelGson_Liquor(name, imageUrl, wikipedia, history, otherNames);
    }
}
