package fr.masciulli.drinks.net.drinks;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;

public interface ReadableDrinksDataSource {
    Observable<List<Drink>> getDrinks();
}
