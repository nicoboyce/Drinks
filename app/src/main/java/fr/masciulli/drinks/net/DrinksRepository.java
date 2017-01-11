package fr.masciulli.drinks.net;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;

public class DrinksRepository implements DrinksDataSource {
    private final Client client;
    private static DrinksRepository instance;

    public static DrinksRepository getInstance(Client client) {
        if (instance == null) {
            instance = new DrinksRepository(client);
        }
        return instance;
    }

    DrinksRepository(Client client) {
        this.client = client;
    }

    @Override
    public Observable<List<Drink>> getDrinks() {
        return client.getDrinks();
    }
}
