package fr.masciulli.drinks.drink;

import fr.masciulli.drinks.BasePresenter;
import fr.masciulli.drinks.BaseView;
import fr.masciulli.drinks.model.Drink;

public interface DrinkContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
        void showDrink(Drink drink);

        void showError();
    }
}
