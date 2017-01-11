package fr.masciulli.drinks.net.drinks;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.Client;
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
