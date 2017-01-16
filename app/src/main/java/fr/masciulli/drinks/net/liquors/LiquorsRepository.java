package fr.masciulli.drinks.net.liquors;

import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import rx.Observable;

public class LiquorsRepository implements ReadableLiquorsDataSource {
    private static LiquorsRepository instance;

    private final WritableLiquorsDataSource localSource;
    private final ReadableLiquorsDataSource remoteSource;

    private List<Liquor> cached;

    public static LiquorsRepository getInstance(ReadableLiquorsDataSource remoteSource, WritableLiquorsDataSource localSource) {
        if (instance == null) {
            instance = new LiquorsRepository(remoteSource, localSource);
        }
        return instance;
    }

    public LiquorsRepository(ReadableLiquorsDataSource remoteSource, WritableLiquorsDataSource localSource) {
        this.localSource = localSource;
        this.remoteSource = remoteSource;
    }

    @Override
    public Observable<List<Liquor>> getLiquors() {
        if (cached != null) {
            return Observable.just(cached);
        }

        return remoteSource.getLiquors()
                .map(this::cacheLiquors)
                .flatMap(localSource::putLiquors)
                .onErrorResumeNext(localSource.getLiquors());
    }

    private List<Liquor> cacheLiquors(List<Liquor> liquors) {
        cached = liquors;
        return cached;
    }
}
