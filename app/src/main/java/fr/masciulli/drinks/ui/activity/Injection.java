package fr.masciulli.drinks.ui.activity;

import fr.masciulli.drinks.net.Client;
import fr.masciulli.drinks.net.DrinksRepository;

public final class Injection {
    private Injection() {
        // cannot be instantiated
    }

    public static DrinksRepository provideDrinksRepository() {
        return DrinksRepository.getInstance(Client.getInstance());
    }
}
