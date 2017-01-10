package fr.masciulli.drinks.net;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;

public interface DrinksRepository {
    Observable<List<Drink>> getDrinks();
}
