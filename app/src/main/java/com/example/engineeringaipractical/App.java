package com.example.engineeringaipractical;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class App extends Application {

    private static App INSTANCE;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(App.this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = App.this;
    }


}
