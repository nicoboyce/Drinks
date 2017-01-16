package fr.masciulli.drinks.net.drinks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.masciulli.drinks.model.Drink;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DrinksRepositoryTest {

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

    private static final List<Drink> REMOTE_DRINKS = Arrays.asList(DRINK, DRINK1);
    private static final List<Drink> LOCAL_DRINKS = Arrays.asList(DRINK1, DRINK2);

    @Captor
    private ArgumentCaptor<List<Drink>> drinksCaptor;

    private DrinksRepository repository;
    @Mock
    private ReadableDrinksDataSource remoteSource;
    @Mock
    private WritableDrinksDataSource localSource;

    @Before
    public void setUp() {
        initMocks(this);
        when(remoteSource.getDrinks()).thenReturn(Observable.just(REMOTE_DRINKS));
        when(localSource.getDrinks()).thenReturn(Observable.just(LOCAL_DRINKS));

        // when putDrinks() is called, just return what was passed as an argument, wrapped in an Observable
        when(localSource.putDrinks(any())).thenAnswer(new Answer<Observable<List<Drink>>>() {
            @Override
            public Observable<List<Drink>> answer(InvocationOnMock invocation) throws Throwable {
                return Observable.just((List<Drink>) invocation.getArguments()[0]);
            }
        });

        repository = new DrinksRepository(remoteSource, localSource);
    }

    @Test
    public void testThatGetDrinksRetrievesFromRemoteAndPutsInLocal() {
        TestSubscriber<List<Drink>> testSubscriber = new TestSubscriber<>();

        repository.getDrinks()
                .subscribe(testSubscriber);
        List<Drink> result = testSubscriber.getOnNextEvents().get(0);

        testSubscriber.assertNoErrors();
        assertThat(result).isEqualTo(REMOTE_DRINKS);
        verify(localSource).putDrinks(REMOTE_DRINKS);
    }

    @Test
    public void testThatGetDrinksRetrievesFromCacheIfDrinksAlreadyLoaded() {
        TestSubscriber<List<Drink>> remoteSubscriber = new TestSubscriber<>();
        repository.getDrinks()
                .subscribe(remoteSubscriber);
        TestSubscriber<List<Drink>> cacheSubscriber = new TestSubscriber<>();
        repository.getDrinks()
                .subscribe(cacheSubscriber);
        List<Drink> result = cacheSubscriber.getOnNextEvents().get(0);

        remoteSubscriber.assertNoErrors();
        assertThat(result).isEqualTo(REMOTE_DRINKS);
        verify(remoteSource, atMost(1)).getDrinks();
    }

    @Test
    public void testThatGetDrinksRetrievesFromLocalAndDoesntPutInLocalIfRemoteError() {
        when(remoteSource.getDrinks()).thenReturn(Observable.error(new RuntimeException("Something bad happened!")));
        TestSubscriber<List<Drink>> testSubscriber = new TestSubscriber<>();

        repository.getDrinks()
                .subscribe(testSubscriber);
        List<Drink> result = testSubscriber.getOnNextEvents().get(0);

        testSubscriber.assertNoErrors();
        assertThat(result).isEqualTo(LOCAL_DRINKS);
        verify(localSource, never()).putDrinks(any());
    }
}
