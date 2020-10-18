package com.example.smartageliketool.di.views.main;


import com.example.smartageliketool.di.scope.PerActivity;
import com.example.smartageliketool.view.main.MainActivity;

import dagger.Subcomponent;


@PerActivity
@Subcomponent (modules = MainActivityModule.class)
public interface MainActivitySubcomponent {

    void inject(MainActivity converterActivity);
}
