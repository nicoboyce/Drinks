package fr.masciulli.drinks.liquors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.liquors.LiquorsRepository;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LiquorsPresenterTest {

    private static final Liquor LIQUOR =
            new Liquor("name", "http://url.com/image.jpg", "http://en.wikipedia.org/wiki", "history", Arrays.asList("ingredient"));

    private static final List<Liquor> LIQUORS = Arrays.asList(LIQUOR);

    private LiquorsPresenter presenter;
    @Mock
    private LiquorsRepository liquorsRepository;
    @Mock
    private LiquorsContract.View liquorsView;

    @Before
    public void setUp() {
        initMocks(this);
        when(liquorsRepository.getLiquors()).thenReturn(Observable.just(LIQUORS));

        presenter = new LiquorsPresenter(liquorsRepository, liquorsView, Schedulers.immediate(), Schedulers.immediate());
    }

    @Test
    public void testThatStartRetrievesLiquorsAndPassesThemToView() {
        presenter.start();

        verify(liquorsView).showLoading();
        verify(liquorsRepository).getLiquors();
        verify(liquorsView).showLiquors(LIQUORS);
    }

    @Test
    public void testThatStartShowsViewErrorWhenErrorWhileGettingLiquors() {
        when(liquorsRepository.getLiquors()).thenReturn(Observable.error(new RuntimeException("Something bad happened!")));

        presenter.start();

        verify(liquorsView).showLoading();
        verify(liquorsRepository).getLiquors();
        verify(liquorsView).showLoadingError();
    }

    @Test
    public void testThatRefreshRetrievesLiquorsAndPassesThemToView() {
        presenter.refreshLiquors();

        verify(liquorsView).showLoading();
        verify(liquorsRepository).getLiquors();
        verify(liquorsView).showLiquors(LIQUORS);
    }

    @Test
    public void testThatRefreshShowsViewErrorWhenErrorWhileGettingLiquors() {
        when(liquorsRepository.getLiquors()).thenReturn(Observable.error(new RuntimeException("Something bad happened!")));

        presenter.refreshLiquors();

        verify(liquorsView).showLoading();
        verify(liquorsRepository).getLiquors();
        verify(liquorsView).showLoadingError();
    }

    @Test
    public void testThatOpenLiquorCallsOpenLiquorsOnView() {
        presenter.openLiquor(0, LIQUOR);
        verify(liquorsView).openLiquor(0, LIQUOR);
    }
}
