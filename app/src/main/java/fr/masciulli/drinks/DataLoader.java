package fr.masciulli.drinks;

import android.content.Context;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.Client;
import fr.masciulli.drinks.net.ConnectivityChecker;
import rx.Observable;

public class DataLoader {
    private final Client client;
    private final Database database;
    private final ConnectivityChecker connectivityChecker;

    public DataLoader(Context context) {
        client = DrinksApplication.get(context).getClient();
        database = new Database(context);
        connectivityChecker = new ConnectivityChecker(context);
    }

    public Observable<List<Drink>> getDrinks() {
        if (connectivityChecker.isConnectedOrConnecting()) {
            return loadDrinksFromNetworkAndStoreInDatabase();
        }
        return loadDrinksFromDatabase();
    }

    private Observable<List<Drink>> loadDrinksFromNetworkAndStoreInDatabase() {
        return dropDrinksFromDatabase()
                .flatMap(count -> client.getDrinks())
                .flatMap(this::storeDrinksInDatabase);
    }

    private Observable<Integer> dropDrinksFromDatabase() {
        return database.dropAllDrinks();
    }

    private Observable<List<Drink>> storeDrinksInDatabase(List<Drink> drinks) {
        return database.storeDrinks(drinks)
                .map(indexes -> drinks);
    }

    private Observable<List<Drink>> loadDrinksFromDatabase() {
        return database.getAllDrinks();
    }

    public Observable<List<Liquor>> getLiquors() {
        //TODO delegate to either client or sqlite
        return client.getLiquors()
                .map(this::storeLiquorsInDatabase);
    }

    private List<Liquor> storeLiquorsInDatabase(List<Liquor> liquors) {
        //TODO store liquors
        return liquors;
    }
}
