package fr.masciulli.drinks;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class DrinksApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        Stetho.initializeWithDefaults(this);

        Timber.plant(new Timber.DebugTree());
    }

    public static DrinksApplication get(Context context) {
        return (DrinksApplication) context.getApplicationContext();
    }
}
