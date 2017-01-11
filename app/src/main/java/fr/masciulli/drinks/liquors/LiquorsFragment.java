package fr.masciulli.drinks.liquors;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.ui.activity.LiquorActivity;
import fr.masciulli.drinks.ui.adapter.ItemClickListener;
import fr.masciulli.drinks.ui.adapter.LiquorsAdapter;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;

public class LiquorsFragment extends Fragment implements ItemClickListener<Liquor>, LiquorsContract.View {
    private static final String STATE_LIQUORS = "state_liquors";

    private LiquorsContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private View errorView;
    private LiquorsAdapter adapter;

    @Override
    public void setPresenter(LiquorsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_liquors, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        errorView = rootView.findViewById(R.id.error);
        Button refreshButton = (Button) rootView.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(v -> presenter.refreshLiquors());

        adapter = new LiquorsAdapter();
        adapter.setItemClickListener(this);
        recyclerView.setLayoutManager(adapter.craftLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            presenter.start();
        } else {
            List<Liquor> liquors = savedInstanceState.getParcelableArrayList(STATE_LIQUORS);
            showLiquors(liquors);
        }

        return rootView;
    }

    @Override
    public void onItemClick(int position, Liquor liquor) {
        presenter.openLiquor(position, liquor);
    }

    @Override
    public void openLiquor(int position, Liquor liquor) {
        Intent intent = new Intent(getActivity(), LiquorActivity.class);
        intent.putExtra(LiquorActivity.EXTRA_LIQUOR, liquor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TileViewHolder holder = (TileViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            String transition = getString(R.string.transition_liquor);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(getActivity(), holder.getImageView(), transition);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_LIQUORS, adapter.getLiquors());
    }

    @Override
    public void showLiquors(List<Liquor> liquors) {
        adapter.setLiquors(liquors);

        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingError() {
        errorView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
}
