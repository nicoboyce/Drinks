package fr.masciulli.drinks.drink;

public class DrinkPresenter implements DrinkContract.Presenter {
    private final DrinkContract.View view;

    public DrinkPresenter(DrinkContract.View view) {
        this.view = view;
    }
    @Override
    public void start() {

    }
}
