package fr.masciulli.drinks.ui.activity;

import fr.masciulli.drinks.net.Client;
import fr.masciulli.drinks.net.drinks.DrinksRepository;
import fr.masciulli.drinks.net.liquors.LiquorsRepository;

public final class Injection {
    private Injection() {
        // cannot be instantiated
    }

    public static DrinksRepository provideDrinksRepository() {
        return DrinksRepository.getInstance(Client.getInstance());
    }

    public static LiquorsRepository provideLiquorsRepository() {
        return LiquorsRepository.getInstance(Client.getInstance());
    }
}
