package com.gomdolstudio.musicapp_assistedinjection.di.modules.player

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gomdolstudio.musicapp_assistedinjection.R
import com.gomdolstudio.musicapp_assistedinjection.databinding.FragmentMainBinding
import com.gomdolstudio.musicapp_assistedinjection.di.ApplicationContext
import com.gomdolstudio.musicapp_assistedinjection.di.factory.AssistedSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.di.factory.InjectingSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.di.scope.FragmentScope
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerActivity
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerMainFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.http.Field
import javax.inject.Named

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


    /*

     */


    @Provides
    @FragmentScope
    fun provideViewModelProvider(fragment:PlayerMainFragment, viewModelFactory: InjectingSavedStateViewModelFactory):ViewModelProvider{
        return ViewModelProvider(fragment, viewModelFactory.create(fragment))
    }

    @Provides
    @FragmentScope
    fun provideLinearLayoutManager(activity: PlayerActivity): LinearLayoutManager{
        return LinearLayoutManager(activity)
    }



}