package fr.masciulli.drinks.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;

import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.drinks.ReadableDrinksDataSource;
import fr.masciulli.drinks.net.liquors.ReadableLiquorsDataSource;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Client implements ReadableDrinksDataSource, ReadableLiquorsDataSource {
    private static final String SERVER_BASE_URL = "http://drinks-api.appspot.com";

    private static Client instance;

    private WebApi retrofit;

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client(SERVER_BASE_URL);
        }
        return instance;
    }

    Client(String baseUrl) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebApi.class);
    }

    @Override
    public Observable<List<Drink>> getDrinks() {
        return retrofit.getDrinks();
    }

    @Override
    public Observable<List<Liquor>> getLiquors() {
        return retrofit.getLiquors();
    }
}
