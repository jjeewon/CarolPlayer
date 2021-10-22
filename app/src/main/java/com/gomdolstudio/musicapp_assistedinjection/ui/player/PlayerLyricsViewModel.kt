package com.gomdolstudio.musicapp_assistedinjection.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import com.gomdolstudio.musicapp_assistedinjection.di.factory.AssistedSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.util.convertTimeToInt
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class PlayerLyricsViewModel @AssistedInject
constructor(
        @Assisted private val savedStateHandle: SavedStateHandle): ViewModel(), Lyric.EventListener {
    private var liveLyricList: MutableLiveData<ArrayList<Lyric>> = MutableLiveData()
    private var lyricsItemClickEvent: MutableLiveData<Int> = MutableLiveData(-1)
    fun getLyricsItemClickEvent(): MutableLiveData<Int>{
        return lyricsItemClickEvent
    }
    private var cancelBtnClickEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    var isItBound = false
        set(bound){
            savedStateHandle.set("isItBound", bound)
            field = bound
        }
    init {
        savedStateHandle.get<Boolean>("isItBound")?.run {
            isItBound = this
        }
    }
    fun getCancelBtnClickEvent(): MutableLiveData<Boolean>{
        return cancelBtnClickEvent
    }

    fun onCancelBtnClick(){
        cancelBtnClickEvent.value = true
    }

    fun setCancelBtnClickEvent(clicked: Boolean){
        cancelBtnClickEvent.value = clicked
    }


    fun setLyrics(lyricsMap: Map<Int, String>){
        var lyricsList : ArrayList<Lyric> = arrayListOf()
        for ((millSec, lyrics) in lyricsMap){
            lyricsList.add(Lyric(millSec,lyrics,this))
        }
        liveLyricList.value = lyricsList
    }

    fun getLiveLyricList(): MutableLiveData<ArrayList<Lyric>>{
        return liveLyricList
    }

    fun getBound(): Boolean{
        return isItBound
    }
    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<PlayerLyricsViewModel>

    override fun onItemClick(lyric: Lyric) {
        lyricsItemClickEvent.value = lyric.millSec
    }
}