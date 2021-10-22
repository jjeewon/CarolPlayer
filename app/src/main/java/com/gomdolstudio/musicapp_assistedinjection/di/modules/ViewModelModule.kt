package com.gomdolstudio.musicapp_assistedinjection.di.modules

import androidx.lifecycle.ViewModel
import com.gomdolstudio.musicapp_assistedinjection.di.ViewModelKey
import com.gomdolstudio.musicapp_assistedinjection.di.factory.AssistedSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerLyricsViewModel
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerMainViewModel
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerViewModel
import com.squareup.inject.assisted.AssistedInject
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.Multibinds
import javax.inject.Provider

@AssistedModule
@Module(includes = [AssistedInject_ViewModelModule::class])
abstract class ViewModelModule {

    // 일반 뷰모델들의 멀티 바인딩
    @Multibinds
    abstract fun bindsViewModels(): Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModel>

    // AssistedInject로 관리하는 ViewModel Factory 멀티 바인딩
    @Multibinds
    abstract fun bindAssistedViewModels(): Map<Class<out ViewModel>, @JvmSuppressWildcards AssistedSavedStateViewModelFactory<out ViewModel>>

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun bindsPlayerViewModel(factory: PlayerViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(PlayerMainViewModel::class)
    abstract fun bindsPlayerMainViewModel(factory: PlayerMainViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(PlayerLyricsViewModel::class)
    abstract fun bindsPlayerLyricsViewModel(factory: PlayerLyricsViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

}

