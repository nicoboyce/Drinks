package fr.masciulli.drinks.liquors;

import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.liquors.LiquorsRepository;
import rx.Scheduler;
import timber.log.Timber;

public class LiquorsPresenter implements LiquorsContract.Presenter {
    private final LiquorsRepository liquorsRepository;
    private final LiquorsContract.View view;
    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;

    public LiquorsPresenter(LiquorsRepository liquorsRepository,
                            LiquorsContract.View view,
                            Scheduler subscribeScheduler,
                            Scheduler observeScheduler) {
        this.liquorsRepository = liquorsRepository;
        this.view = view;
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
    }

    @Override
    public void start() {
        loadLiquors();
    }

    private void loadLiquors() {
        view.showLoading();
        liquorsRepository.getLiquors()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
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
