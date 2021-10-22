package com.gomdolstudio.musicapp_assistedinjection

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gomdolstudio.musicapp_assistedinjection.data.MusicRetrofitService
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerViewModel
import io.mockk.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import org.junit.Assert
@RunWith(AndroidJUnit4::class)
class ViewModelTest {
    private lateinit var viewModel: PlayerViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var musicService: MusicRetrofitService


    @Before
    fun setup(){
        savedStateHandle = SavedStateHandle()
        savedStateHandle.set("timer",0)
        savedStateHandle.set("isItComback",false)

        musicService = mockk<MusicRetrofitService>()
        every { musicService.getMusic() } returns mockk()
        viewModel = PlayerViewModel(savedStateHandle, musicService)
    }

    @Test
    fun testViewModel(){
        viewModel.loadMusic()
        verify { musicService.getMusic() }

    }

}