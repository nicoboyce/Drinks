package fr.masciulli.drinks.liquors;

import java.util.List;

import fr.masciulli.drinks.BasePresenter;
import fr.masciulli.drinks.BaseView;
import fr.masciulli.drinks.model.Liquor;

public interface LiquorsContract {
    interface Presenter extends BasePresenter {
        void refreshLiquors();
        void openLiquor(int position, Liquor liquor);
    }

    interface View extends BaseView<Presenter> {
        void showLiquors(List<Liquor> liquors);
        void showLoading();
        void showLoadingError();
        void openLiquor(int position, Liquor liquor);
    }
}
