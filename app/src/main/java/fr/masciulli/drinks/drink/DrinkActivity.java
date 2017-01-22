package fr.masciulli.drinks.drink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.masciulli.drinks.Injection;
import fr.masciulli.drinks.R;

public class DrinkActivity extends AppCompatActivity {
    public static final String EXTRA_DRINK = "extra_drink";
    private static final String FRAGMENT_TAG_DRINK = "fragment_tag_drink";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        DrinkFragment fragment = createOrRetrieveFragment();

        String drinkName = getIntent().getStringExtra(EXTRA_DRINK);
        DrinkContract.Presenter presenter = new DrinkPresenter(
                Injection.provideDrinksRepository(),
                fragment,
                drinkName,
                Injection.provideSubscribeScheduler(),
                Injection.provideObserveScheduler());
        fragment.setPresenter(presenter);
    }

    private DrinkFragment createOrRetrieveFragment() {
        DrinkFragment fragment = (DrinkFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DRINK);
        if (fragment == null) {
            fragment = new DrinkFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, FRAGMENT_TAG_DRINK)
                    .commit();
        }
        return fragment;
    }
}
