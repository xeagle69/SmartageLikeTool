package com.example.smartageliketool;


import android.app.Application;

import com.example.smartageliketool.data.remote.RemoteModule;
import com.example.smartageliketool.di.AppComponent;
import com.example.smartageliketool.di.AppModule;
import com.example.smartageliketool.di.DaggerAppComponent;


public class BaseApplication extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .remoteModule(new RemoteModule())
                .build();
        appComponent.inject(this);
    }
}
