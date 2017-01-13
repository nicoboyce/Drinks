package fr.masciulli.drinks.net.drinks;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;

public interface WritableDrinksDataSource extends ReadableDrinksDataSource {
    Observable<List<Drink>> putDrinks(List<Drink> drinks);
}
