package fr.masciulli.drinks.net.liquors;

import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import rx.Observable;

public class LocalLiquorsDataSource implements WritableLiquorsDataSource {
    private static LocalLiquorsDataSource instance;

    public static LocalLiquorsDataSource getInstance() {
        if (instance == null) {
            instance = new LocalLiquorsDataSource();
        }
        return instance;
    }

    @Override
    public Observable<List<Liquor>> getLiquors() {
        return null;
    }

    @Override
    public Observable<List<Liquor>> putLiquors(List<Liquor> liquors) {
        //TODO implement
        return Observable.just(liquors);
    }
}
