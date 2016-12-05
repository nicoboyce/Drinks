package fr.masciulli.drinks;

import android.content.Context;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.Client;
import rx.Observable;

public class DataLoader {
    private final Client client;

    public DataLoader(Context context) {
        client = DrinksApplication.get(context).getClient();
    }

    public Observable<List<Drink>> getDrinks() {
        //TODO delegate to either client or sqlite
        return client.getDrinks()
                .map(this::storeDrinksInDatabase);
    }

    private List<Drink> storeDrinksInDatabase(List<Drink> drinks) {
        //TODO store drinks
        return drinks;
    }
}
