package fr.masciulli.drinks.drinks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.drinks.DrinksRepository;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DrinksPresenterTest {
    private DrinksPresenter presenter;
    @Mock
    private DrinksRepository drinksRepository;
    @Mock
    private DrinksContract.View drinksView;
    @Mock
    private Scheduler scheduler;

    private List<Drink> drinks = new ArrayList<>();

    @Before
    public void setUp() {
        initMocks(this);
        when(drinksRepository.getDrinks()).thenReturn(Observable.just(drinks));

        presenter = new DrinksPresenter(drinksRepository, drinksView, Schedulers.immediate(), Schedulers.immediate());
    }

    @Test
    public void testThatStartRetrievesDrinksAndPassesThemToView() {
        presenter.start();
        verify(drinksRepository).getDrinks();
        verify(drinksView).showDrinks(drinks);
    }
}
