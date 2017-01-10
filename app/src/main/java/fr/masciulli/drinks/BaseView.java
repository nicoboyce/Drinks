package fr.masciulli.drinks;

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
