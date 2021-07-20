package com.gomdolstudio.musicapp_assistedinjection.di.modules.player

import androidx.databinding.DataBindingUtil
import com.gomdolstudio.musicapp_assistedinjection.R
import com.gomdolstudio.musicapp_assistedinjection.databinding.FragmentLyricsBinding
import com.gomdolstudio.musicapp_assistedinjection.databinding.FragmentMainBinding
import com.gomdolstudio.musicapp_assistedinjection.di.scope.FragmentScope
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerActivity
import dagger.Module
import dagger.Provides

@Module
class PlayerLyricsFragmentModule {
    @Provides
    @FragmentScope
    fun providePlayerMainFragmentBinding(activity: PlayerActivity): FragmentLyricsBinding {
        return DataBindingUtil.inflate<FragmentLyricsBinding>(
                activity.layoutInflater,
                R.layout.fragment_lyrics,
                null,
                false
        )
    }
}