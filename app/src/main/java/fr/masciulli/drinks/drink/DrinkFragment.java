package fr.masciulli.drinks.drink;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinkFragment extends Fragment implements DrinkContract.View {
    private DrinkContract.Presenter presenter;

    private ImageView imageView;
    private TextView historyView;
    private TextView instructionsView;
    private TextView ingredientsView;
    private Button wikipediaButton;

    @Override
    public void setPresenter(DrinkContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) root.findViewById(R.id.image);
        historyView = (TextView) root.findViewById(R.id.history);
        instructionsView = (TextView) root.findViewById(R.id.instructions);
        ingredientsView = (TextView) root.findViewById(R.id.ingredients);
        wikipediaButton = (Button) root.findViewById(R.id.wikipedia);

        presenter.start();
        return root;
    }

    @Override
    public void showDrink(Drink drink) {
        //TODO implement
    }

    @Override
    public void showError() {
        //TODO implement
    }
}
