package fr.masciulli.drinks.model;

import android.os.Parcelable;
import auto.parcelgson.AutoParcelGson;

import java.util.List;

@AutoParcelGson
public abstract class Liquor implements Parcelable {
    public abstract String name();

    public abstract String imageUrl();

    public abstract String history();

    public abstract String wikipedia();

    public abstract List<String> otherNames();

    public static Liquor create(String name, String imageUrl, String history, String wikipedia, List<String> otherNames) {
        return new AutoParcelGson_Liquor(name, imageUrl, history, wikipedia, otherNames);
    }
}
