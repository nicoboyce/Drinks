package fr.masciulli.drinks.drinks;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.ui.activity.DrinkActivity;
import fr.masciulli.drinks.ui.adapter.DrinksAdapter;
import fr.masciulli.drinks.ui.adapter.ItemClickListener;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;

import java.util.List;

public class DrinksFragment extends Fragment implements SearchView.OnQueryTextListener, ItemClickListener<Drink>, DrinksContract.View {
    private DrinksContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View emptyView;
    private View errorView;

    private DrinksAdapter adapter;

    @Override
    public void setPresenter(DrinksContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_drinks, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        emptyView = rootView.findViewById(R.id.empty);
        errorView = rootView.findViewById(R.id.error);
        Button refreshButton = (Button) rootView.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(v -> presenter.refreshDrinks());

        adapter = new DrinksAdapter();
        adapter.setItemClickListener(this);
        recyclerView.setLayoutManager(adapter.craftLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        presenter.start();

        return rootView;
    }

    @Override
    public void showDrinks(List<Drink> drinks) {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        adapter.setDrinks(drinks);
    }

    @Override
    public void showLoadingError() {
        errorView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(int position, Drink drink) {
        presenter.openDrink(position, drink);
    }

    @Override
    public void openDrink(int position, Drink drink) {
        Intent intent = new Intent(getActivity(), DrinkActivity.class);
        intent.putExtra(DrinkActivity.EXTRA_DRINK, drink.name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TileViewHolder holder = (TileViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            String transition = getString(R.string.transition_drink);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(getActivity(), holder.getImageView(), transition);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_drinks, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.filter(newText);
        return false;
    }

    @Override
    public void showEmpty() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // no-op
        return false;
    }

    @Override
    public void onDestroyOptionsMenu() {
        presenter.clearFilter();
        super.onDestroyOptionsMenu();
    }
}
