package fr.masciulli.drinks.drink;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.drinks.DrinksRepository;
import rx.Observable;
import rx.Scheduler;
import timber.log.Timber;

public class DrinkPresenter implements DrinkContract.Presenter {
    private static final String TAG = DrinkPresenter.class.getSimpleName();

    private final DrinksRepository repository;
    private final DrinkContract.View view;
    private final String drinkName;
    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;

    public DrinkPresenter(DrinksRepository repository, DrinkContract.View view, String drinkName, Scheduler subscribeScheduler, Scheduler observeScheduler) {
        this.repository = repository;
        this.view = view;
        this.drinkName = drinkName;
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
    }

    @Override
    public void start() {
        loadDrink();
    }

    private void loadDrink() {
        repository.getDrinks()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .flatMap(Observable::from)
                .filter(drink -> drink.getName().equals(drinkName))
                .single()
                .subscribe(this::drinkLoaded, this::errorLoadingDrink);
    }

    private void drinkLoaded(Drink drink) {
        view.showDrink(drink);
    }

    private void errorLoadingDrink(Throwable throwable) {
        Timber.e(TAG, "Couldn't load drink", throwable);
        view.showError();
    }
}
