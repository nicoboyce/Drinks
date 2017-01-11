package fr.masciulli.drinks;

import android.app.Application;
import android.content.Context;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import fr.masciulli.drinks.net.Client;
import timber.log.Timber;

public class DrinksApplication extends Application {
    //TODO remove client from here once repositories are all singletons
    private Client client;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        Stetho.initializeWithDefaults(this);

        client = Client.getInstance();

        Timber.plant(new Timber.DebugTree());
    }

    public Client getClient() {
        return client;
    }

    public static DrinksApplication get(Context context) {
        return (DrinksApplication) context.getApplicationContext();
    }
}
