package fr.masciulli.drinks.drinks;

import java.util.List;

import fr.masciulli.drinks.BasePresenter;
import fr.masciulli.drinks.BaseView;
import fr.masciulli.drinks.model.Drink;

public interface DrinksContract {
    interface Presenter extends BasePresenter {
        void refreshDrinks();
        void openDrink(int position, Drink drink);
        void filter(String filter);
        void clearFilter();
    }

    interface View extends BaseView<Presenter> {
        void showDrinks(List<Drink> drinks);
        void showLoadingError();
        void showLoading();
        void openDrink(int position, Drink drink);
        void filter(String filter);
        void clearFilter();
    }
}
