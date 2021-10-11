package com.gomdolstudio.musicapp_assistedinjection.di.modules

import com.gomdolstudio.musicapp_assistedinjection.di.modules.player.PlayerModule
import com.gomdolstudio.musicapp_assistedinjection.di.scope.ActivityScope
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [(PlayerModule::class)])
    abstract fun getPlayerActivity() : PlayerActivity
}

