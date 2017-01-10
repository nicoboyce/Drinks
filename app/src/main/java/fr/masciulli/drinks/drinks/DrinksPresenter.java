package fr.masciulli.drinks.drinks;

import android.util.Log;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.DrinksRepository;
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
}
