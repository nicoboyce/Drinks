package fr.masciulli.drinks.drink;

import fr.masciulli.drinks.BasePresenter;
import fr.masciulli.drinks.BaseView;

public interface DrinkContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
    }
}
