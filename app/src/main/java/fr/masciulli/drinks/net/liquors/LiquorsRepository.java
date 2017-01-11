package fr.masciulli.drinks.net.liquors;

import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.Client;
import rx.Observable;

public class LiquorsRepository implements LiquorsDataSource {
    private static LiquorsRepository instance;
    private final Client client;

    public static LiquorsRepository getInstance(Client client) {
        if (instance == null) {
            instance = new LiquorsRepository(client);
        }
        return instance;
    }

    public LiquorsRepository(Client client) {
        this.client = client;
    }

    @Override
    public Observable<List<Liquor>> getLiquors() {
        return client.getLiquors();
    }
}
