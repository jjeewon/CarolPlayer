package com.gomdolstudio.musicapp_assistedinjection.di

import android.content.Context
import com.gomdolstudio.musicapp_assistedinjection.App
import com.gomdolstudio.musicapp_assistedinjection.di.modules.ActivityModule
import com.gomdolstudio.musicapp_assistedinjection.di.modules.AppModule
import com.gomdolstudio.musicapp_assistedinjection.di.modules.service.ServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (ServiceModule::class), (ActivityModule::class), (AppModule::class)])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory: AndroidInjector.Factory<App>{}

}

