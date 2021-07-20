package com.gomdolstudio.musicapp_assistedinjection.di.modules.service

import com.gomdolstudio.musicapp_assistedinjection.data.service.MusicService
import com.gomdolstudio.musicapp_assistedinjection.di.scope.ActivityScope
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [(MusicServiceModule::class)])
    abstract fun getService() : MusicService
}