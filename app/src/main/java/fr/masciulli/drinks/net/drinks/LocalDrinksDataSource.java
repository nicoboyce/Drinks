package fr.masciulli.drinks.net.drinks;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;

public class LocalDrinksDataSource implements WritableDrinksDataSource {
    private static LocalDrinksDataSource instance;

    public static LocalDrinksDataSource getInstance() {
        if (instance == null) {
            instance = new LocalDrinksDataSource();
        }
        return instance;
    }

    @Override
    public Observable<List<Drink>> getDrinks() {
        //TODO implement
        return null;
    }

    @Override
    public Observable<List<Drink>> putDrinks(List<Drink> drinks) {
        //TODO implement
        return Observable.just(drinks);
    }
}
