package com.gomdolstudio.musicapp_assistedinjection.di.modules.player

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.gomdolstudio.musicapp_assistedinjection.R
import com.gomdolstudio.musicapp_assistedinjection.databinding.FragmentMainBinding
import com.gomdolstudio.musicapp_assistedinjection.di.factory.AssistedSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.di.factory.InjectingSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.di.scope.FragmentScope
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerActivity
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerMainFragment
import dagger.Module
import dagger.Provides

@Module
class PlayerMainFragmentModule {

    @Provides
    @FragmentScope
    fun providePlayerMainFragmentBinding(activity: PlayerActivity): FragmentMainBinding {
        return DataBindingUtil.inflate<FragmentMainBinding>(
                activity.layoutInflater,
                R.layout.fragment_main,
                null,
                false
        )
    }

    @Provides
    @FragmentScope
    fun provideViewModelProvider(fragment:PlayerMainFragment, viewModelFactory: InjectingSavedStateViewModelFactory):ViewModelProvider{
        return ViewModelProvider(fragment, viewModelFactory.create(fragment))
    }


}