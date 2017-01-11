package fr.masciulli.drinks.net.liquors;

import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import rx.Observable;

public interface LiquorsDataSource {
    Observable<List<Liquor>> getLiquors();
}
