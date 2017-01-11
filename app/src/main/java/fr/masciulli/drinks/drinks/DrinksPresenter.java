package fr.masciulli.drinks.drinks;

import android.util.Log;

import java.util.List;
import java.util.Locale;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.drinks.DrinksRepository;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DrinksPresenter implements DrinksContract.Presenter {
    private static final String TAG = DrinksPresenter.class.getSimpleName();

    private final DrinksRepository drinksRepository;
    private final DrinksContract.View view;

    public DrinksPresenter(DrinksRepository drinksRepository, DrinksContract.View view) {
        this.drinksRepository = drinksRepository;
        this.view = view;
    }

    @Override
    public void start() {
        loadDrinks();
    }

    private void loadDrinks() {
        view.showLoading();
        drinksRepository.getDrinks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::drinksLoaded, this::errorLoadingDrinks);
    }

    private void drinksLoaded(List<Drink> drinks) {
        view.showDrinks(drinks);
    }

    private void errorLoadingDrinks(Throwable throwable) {
        Log.e(TAG, "Error loading drinks", throwable);
        view.showLoadingError();
    }

    @Override
    public void refreshDrinks() {
        loadDrinks();
    }

    @Override
    public void openDrink(int position, Drink drink) {
        view.openDrink(position, drink);
    }

    @Override
    public void filter(String filter) {
        drinksRepository.getDrinks()
                .flatMap(Observable::from)
                .filter(drink -> drinkMatchesFilter(drink, filter))
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::drinksLoaded, this::errorLoadingDrinks);
    }

    @Override
    public void clearFilter() {
        loadDrinks();
    }

    private boolean drinkMatchesFilter(Drink drink, String filter) {
        if (drink.name().toLowerCase(Locale.US).contains(filter.toLowerCase())) {
            return true;
        } else {
            for (String ingredient : drink.ingredients()) {
                if (ingredient.toLowerCase(Locale.US).contains(filter)) {
                    return true;
                }
            }
        }
        return false;
    }
}
