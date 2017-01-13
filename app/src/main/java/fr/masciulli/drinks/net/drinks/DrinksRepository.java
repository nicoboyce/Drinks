package fr.masciulli.drinks.net.drinks;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;

public class DrinksRepository implements ReadableDrinksDataSource {
    private static DrinksRepository instance;

    private final ReadableDrinksDataSource remoteSource;
    private final WritableDrinksDataSource localSource;

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
        return remoteSource.getDrinks()
                .flatMap(localSource::putDrinks)
                .onErrorResumeNext(localSource.getDrinks());
    }
}
