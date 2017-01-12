package fr.masciulli.drinks.model;

import android.os.Parcelable;
import auto.parcelgson.AutoParcelGson;

import java.util.List;

@AutoParcelGson
public abstract class Drink implements Parcelable {
    public abstract String name();

    public abstract String imageUrl();

    public abstract String history();

    public abstract String wikipedia();

    public abstract String instructions();

    public abstract List<String> ingredients();

    public static Drink create(String name, String imageUrl, String history, String wikipedia, String instructions, List<String> ingredients) {
        return new AutoParcelGson_Drink(name, imageUrl, history, wikipedia, instructions, ingredients);
    }
}
