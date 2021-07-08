package com.gomdolstudio.musicapp_assistedinjection.di.modules.player

import androidx.databinding.DataBindingUtil
import com.gomdolstudio.musicapp_assistedinjection.R
import com.gomdolstudio.musicapp_assistedinjection.databinding.ActivityPlayerBinding
import com.gomdolstudio.musicapp_assistedinjection.di.scope.ActivityScope
import com.gomdolstudio.musicapp_assistedinjection.di.scope.FragmentScope
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerActivity
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerMainFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class PlayerModule {


    @Module
    companion object{
        @JvmStatic
        @Provides
        @ActivityScope
        fun providePlayerActivityBinding(activity: PlayerActivity): ActivityPlayerBinding {
            return DataBindingUtil.setContentView(activity, R.layout.activity_player)
        }
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = [(PlayerMainFragmentModule::class)])
    abstract fun getPlayerMainFragment(): PlayerMainFragment

}