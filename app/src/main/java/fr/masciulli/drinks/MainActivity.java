package fr.masciulli.drinks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import fr.masciulli.drinks.drinks.DrinksFragment;
import fr.masciulli.drinks.drinks.DrinksPresenter;
import fr.masciulli.drinks.liquors.LiquorsFragment;
import fr.masciulli.drinks.liquors.LiquorsPresenter;

public class MainActivity extends AppCompatActivity {
    private static final int POSITION_DRINKS = 0;
    private static final int POSITION_LIQUORS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        DrinksFragment drinksFragment = retrieveOrCreateDrinksFragment();
        LiquorsFragment liquorsFragment = retrieveOrCreateLiquorsFragment();

        DrinksPresenter drinksPresenter = new DrinksPresenter(Injection.provideDrinksRepository(),
                drinksFragment,
                Injection.provideSubscribeScheduler(),
                Injection.provideObserveScheduler()
        );
        drinksFragment.setPresenter(drinksPresenter);

        LiquorsPresenter liquorsPresenter = new LiquorsPresenter(Injection.provideLiquorsRepository(),
                liquorsFragment,
                Injection.provideSubscribeScheduler(),
                Injection.provideObserveScheduler());
        liquorsFragment.setPresenter(liquorsPresenter);

        pager.setAdapter(new DrinksFragmentPagerAdapter(getSupportFragmentManager(), drinksFragment, liquorsFragment));

        tabLayout.setupWithViewPager(pager);
    }

    private DrinksFragment retrieveOrCreateDrinksFragment() {
        DrinksFragment fragment = (DrinksFragment) getSupportFragmentManager()
                .findFragmentByTag(getFragmentTag(POSITION_DRINKS));
        if (fragment == null) {
            return new DrinksFragment();
        }
        return fragment;
    }

    private LiquorsFragment retrieveOrCreateLiquorsFragment() {
        LiquorsFragment fragment = (LiquorsFragment) getSupportFragmentManager()
                .findFragmentByTag(getFragmentTag(POSITION_LIQUORS));
        if (fragment == null) {
            return new LiquorsFragment();
        }
        return fragment;
    }


    private String getFragmentTag(int fragmentId) {
        return "android:switcher:" + R.id.pager + ":" + fragmentId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                openAbout();
                return true;
            case R.id.action_feedback:
                sendFeedback();
                return true;
            case R.id.action_licenses:
                openLicenses();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openLicenses() {
        startActivity(new Intent(this, LicensesActivity.class));
    }

    private void openAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }


    private void sendFeedback() {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(getString(R.string.feedback_mail))
                + "?subject=" + Uri.encode(getString(R.string.feedback_default_subject));
        Uri uri = Uri.parse(uriText);
        sendIntent.setData(uri);
        startActivity(Intent.createChooser(sendIntent, getString(R.string.action_feedback)));
    }

    private class DrinksFragmentPagerAdapter extends FragmentPagerAdapter {
        private final DrinksFragment drinksFragment;
        private final LiquorsFragment liquorsFragment;

        public DrinksFragmentPagerAdapter(FragmentManager fragmentManager, DrinksFragment drinksFragment, LiquorsFragment liquorsFragment) {
            super(fragmentManager);
            this.drinksFragment = drinksFragment;
            this.liquorsFragment = liquorsFragment;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case POSITION_DRINKS:
                    return drinksFragment;
                case POSITION_LIQUORS:
                    return liquorsFragment;
                default:
                    throw new IndexOutOfBoundsException("No fragment for position " + position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case POSITION_DRINKS:
                    return getString(R.string.title_drinks);
                case POSITION_LIQUORS:
                    return getString(R.string.title_liquors);
                default:
                    throw new IndexOutOfBoundsException("No fragment for position " + position);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
