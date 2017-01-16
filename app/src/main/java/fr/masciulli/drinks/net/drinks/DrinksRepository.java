package fr.masciulli.drinks.net.drinks;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;

public class DrinksRepository implements ReadableDrinksDataSource {
    private static DrinksRepository instance;

    private final ReadableDrinksDataSource remoteSource;
    private final WritableDrinksDataSource localSource;

    private List<Drink> cached;

    public static DrinksRepository getInstance(ReadableDrinksDataSource remoteSource, WritableDrinksDataSource localSource) {
        if (instance == null) {
            instance = new DrinksRepository(remoteSource, localSource);
        }
        return instance;
    }

    DrinksRepository(ReadableDrinksDataSource remoteSource, WritableDrinksDataSource localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
    }

    @Override
    public Observable<List<Drink>> getDrinks() {
        if (cached != null) {
            return Observable.just(cached);
        }

        return remoteSource.getDrinks()
                .map(this::cacheDrinks)
                .flatMap(localSource::putDrinks)
                .onErrorResumeNext(localSource.getDrinks());
    }

    private List<Drink> cacheDrinks(List<Drink> drinks) {
        cached = drinks;
        return cached;
    }
}
