package fr.masciulli.drinks.net.liquors;

import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import rx.Observable;

public class LiquorsRepository implements ReadableLiquorsDataSource {
    private static LiquorsRepository instance;

    private final WritableLiquorsDataSource localSource;
    private final ReadableLiquorsDataSource remoteSource;

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
        return remoteSource.getLiquors()
                .flatMap(localSource::putLiquors)
                .onErrorResumeNext(localSource.getLiquors());
    }
}
