package fr.masciulli.drinks.liquors;

import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.liquors.LiquorsRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LiquorsPresenter implements LiquorsContract.Presenter {
    private final LiquorsRepository liquorsRepository;
    private final LiquorsContract.View view;

    public LiquorsPresenter(LiquorsRepository liquorsRepository, LiquorsContract.View view) {
        this.liquorsRepository = liquorsRepository;
        this.view = view;
    }

    @Override
    public void start() {
        loadLiquors();
    }

    private void loadLiquors() {
        view.showLoading();
        liquorsRepository.getLiquors()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::liquorsLoaded, this::errorLoadingLiquors);
    }

    private void liquorsLoaded(List<Liquor> liquors) {
        view.showLiquors(liquors);
    }

    private void errorLoadingLiquors(Throwable throwable) {
        Timber.e(throwable, "Error loading liquors");
        view.showLoadingError();
    }

    @Override
    public void refreshLiquors() {
        loadLiquors();
    }

    @Override
    public void openLiquor(int position, Liquor liquor) {
        view.openLiquor(position, liquor);
    }
}
