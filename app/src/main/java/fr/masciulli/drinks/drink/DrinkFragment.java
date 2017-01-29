package fr.masciulli.drinks.drink;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.ui.EnterPostponeTransitionCallback;

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
        getActivity().setTitle(drink.getName());

        Picasso.with(getActivity())
                .load(drink.getImageUrl())
                .noFade()
                .into(imageView, new EnterPostponeTransitionCallback(getActivity()));

        historyView.setText(drink.getHistory());
        instructionsView.setText(drink.getInstructions());
        ingredientsView.setText(parseIngredients(drink));
        wikipediaButton.setText(getString(R.string.wikipedia, drink.getName()));
        wikipediaButton.setOnClickListener(v -> presenter.showWikipedia());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Spanned parseIngredients(Drink drink) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String ingredient : drink.getIngredients()) {
            builder.append("&#8226; ");
            builder.append(ingredient);
            if (++i < drink.getIngredients().size()) {
                builder.append("<br>");
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(builder.toString());
        } else {
            return Html.fromHtml(builder.toString(), 0);
        }
    }

    @Override
    public void showError() {
        //TODO implement
    }

    @Override
    public void openWikipedia(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
