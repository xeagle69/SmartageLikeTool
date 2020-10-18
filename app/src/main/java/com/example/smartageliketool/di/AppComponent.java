package com.example.smartageliketool.di;


import com.example.smartageliketool.BaseApplication;
import com.example.smartageliketool.data.remote.RemoteModule;
import com.example.smartageliketool.di.views.main.MainActivityModule;
import com.example.smartageliketool.di.views.main.MainActivitySubcomponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RemoteModule.class})
public interface AppComponent {
    void inject(BaseApplication baseApplication);

    MainActivitySubcomponent mainActivitySubcomponent(MainActivityModule mainActivityModule);


}
