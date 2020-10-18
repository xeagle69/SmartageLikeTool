package com.example.smartageliketool.di.views.main;


import com.example.smartageliketool.data.DataRepository;
import com.example.smartageliketool.data.DataRepositoryImpl;
import com.example.smartageliketool.di.scope.PerActivity;
import com.example.smartageliketool.view.main.MainActivity;
import com.example.smartageliketool.view.main.MainContract;
import com.example.smartageliketool.view.main.MainPresenter;

import dagger.Module;
import dagger.Provides;


@Module
public class MainActivityModule {

    private MainActivity converterActivity;

    public MainActivityModule(MainActivity converterActivity) {
        this.converterActivity = converterActivity;
    }

    @Provides
    @PerActivity
    MainContract.View provideView() {
        return converterActivity;
    }

    @Provides
    @PerActivity
    MainContract.Presenter providePresenter(MainPresenter presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    DataRepository provideLoginInteractor(DataRepositoryImpl dataManager) {
        return dataManager;
    }
}
