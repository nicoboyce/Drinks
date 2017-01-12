package fr.masciulli.drinks.drinks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.drinks.DrinksRepository;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DrinksPresenterTest {
    private DrinksPresenter presenter;
    @Mock
    private DrinksRepository drinksRepository;
    @Mock
    private DrinksContract.View drinksView;

    @Captor
    private ArgumentCaptor<List<Drink>> drinksCaptor;

    private static final Drink DRINK = Drink.create(
            "name",
            "http://url.com/image.jpg",
            "history",
            "http://wikipedia.org/wiki",
            "instructions",
            Arrays.asList("an ingredient"));

    private static final Drink DRINK1 = Drink.create(
            "another Name",
            "http://url.com/image.jpg",
            "history",
            "http://wikipedia.org/wiki",
            "instructions",
            new ArrayList<>());

    private static final Drink DRINK2 = Drink.create(
            "something Else",
            "http://url.com/image.jpg",
            "history",
            "http://wikipedia.org/wiki",
            "instructions",
            Arrays.asList("an ingredient"));

    private static final List<Drink> DRINKS = Arrays.asList(DRINK, DRINK1, DRINK2);

    @Before
    public void setUp() {
        initMocks(this);
        when(drinksRepository.getDrinks()).thenReturn(Observable.just(DRINKS));

        presenter = new DrinksPresenter(drinksRepository, drinksView, Schedulers.immediate(), Schedulers.immediate());
    }

    @Test
    public void testThatStartRetrievesDrinksAndPassesThemToView() {
        presenter.start();

        verify(drinksView).showLoading();
        verify(drinksRepository).getDrinks();
        verify(drinksView).showDrinks(DRINKS);
    }

    @Test
    public void testThatStartShowsViewErrorWhenErrorWhileGettingDrinks() {
        when(drinksRepository.getDrinks())
                .thenReturn(Observable.error(new RuntimeException("Something bad has happened!")));

        presenter.start();

        verify(drinksView).showLoading();
        verify(drinksRepository).getDrinks();
        verify(drinksView).showLoadingError();
    }

    @Test
    public void testThatRefreshRetrievesDrinksAndPassesThemToView() {
        presenter.refreshDrinks();

        verify(drinksRepository).getDrinks();
        verify(drinksView).showLoading();
        verify(drinksView).showDrinks(DRINKS);
    }

    @Test
    public void testThatRefreshShowsViewErrorWhenErrorWhileGettingDrinks() {
        when(drinksRepository.getDrinks())
                .thenReturn(Observable.error(new RuntimeException("Something bad has happened!")));

        presenter.start();

        verify(drinksRepository).getDrinks();
        verify(drinksView).showLoading();
        verify(drinksView).showLoadingError();
    }

    @Test
    public void testThatOpenDrinkCallsOpenDrinkOnView() {
        presenter.openDrink(0, DRINK);
        verify(drinksView).openDrink(0, DRINK);
    }

    @Test
    public void testThatFilterWithNameCallsViewShowDrinksWithCorrectDrinks() {
        presenter.filter("name");
        verify(drinksView).showDrinks(drinksCaptor.capture());

        List<Drink> filteredDrinks = drinksCaptor.getValue();
        assertThat(filteredDrinks).isEqualTo(Arrays.asList(DRINK, DRINK1));
    }

    @Test
    public void testThatFilterWithIngredientCallsViewShowDrinksWithCorrectDrinks() {
        presenter.filter("ingredient");
        verify(drinksView).showDrinks(drinksCaptor.capture());

        List<Drink> filteredDrinks = drinksCaptor.getValue();
        assertThat(filteredDrinks).isEqualTo(Arrays.asList(DRINK, DRINK2));
    }

    @Test
    public void testThatFilterCallsViewShowEmptyIfNoMatchingDrinks() {
        presenter.filter("thisdoesntmatchanything");
        verify(drinksView).showDrinks(drinksCaptor.capture());
        verify(drinksView).showEmpty();

        List<Drink> filteredDrinks = drinksCaptor.getValue();
        assertThat(filteredDrinks).isEmpty();
    }

    @Test
    public void testThatClearFilterShowsAllDrinks() {
        presenter.filter("thisdoesntmatchanything");
        presenter.clearFilter();
        verify(drinksView).showDrinks(DRINKS);
    }
}
