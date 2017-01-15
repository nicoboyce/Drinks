package fr.masciulli.drinks.net.liquors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import fr.masciulli.drinks.model.Liquor;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LiquorsRepositoryTest {
    private static final Liquor LIQUOR =
            Liquor.create("name", "http://url.com/image.jpg", "http://en.wikipedia.org/wiki", "history", Arrays.asList("ingredient"));
    private static final Liquor LIQUOR1 =
            Liquor.create("another name", "http://url.com/image.jpg", "http://en.wikipedia.org/wiki", "history", Arrays.asList("ingredient"));

    private static final List<Liquor> LIQUORS = Arrays.asList(LIQUOR);
    private static final List<Liquor> LOCAL_LIQUORS = Arrays.asList(LIQUOR1);

    private LiquorsRepository repository;
    @Mock
    private ReadableLiquorsDataSource remoteSource;
    @Mock
    private WritableLiquorsDataSource localSource;

    @Before
    public void setUp() {
        initMocks(this);
        when(remoteSource.getLiquors()).thenReturn(Observable.just(LIQUORS));
        when(localSource.getLiquors()).thenReturn(Observable.just(LOCAL_LIQUORS));

        when(localSource.putLiquors(any())).thenAnswer(new Answer<Observable<List<Liquor>>>() {
            @Override
            public Observable<List<Liquor>> answer(InvocationOnMock invocation) throws Throwable {
                return Observable.just((List<Liquor>) invocation.getArguments()[0]);
            }
        });
        repository = new LiquorsRepository(remoteSource, localSource);
    }

    @Test
    public void testThatGetLiquorsRetrievesFromRemoteAndPutsInLocal() {
        TestSubscriber<List<Liquor>> testSubscriber = new TestSubscriber<>();

        repository.getLiquors()
                .subscribe(testSubscriber);
        List<Liquor> liquors = testSubscriber.getOnNextEvents().get(0);

        testSubscriber.assertNoErrors();
        assertThat(liquors).isEqualTo(LIQUORS);
        verify(localSource).putLiquors(LIQUORS);
    }

    @Test
    public void testThatGetLiquorsRetrievesFromLocalAndDoesntPutInLocalIfRemoteError() {
        when(remoteSource.getLiquors()).thenReturn(Observable.error(new RuntimeException("Something bad happened!")));
        TestSubscriber<List<Liquor>> testSubscriber = new TestSubscriber<>();

        repository.getLiquors()
                .subscribe(testSubscriber);
        List<Liquor> liquors = testSubscriber.getOnNextEvents().get(0);

        testSubscriber.assertNoErrors();
        assertThat(liquors).isEqualTo(LOCAL_LIQUORS);
        verify(localSource, never()).putLiquors(any());
    }
}
