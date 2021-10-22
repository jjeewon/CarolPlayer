package com.gomdolstudio.musicapp_assistedinjection.ui.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import com.gomdolstudio.musicapp_assistedinjection.di.factory.AssistedSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.util.convertTimeToInt
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class PlayerMainViewModel @AssistedInject
constructor(
        @Assisted private val savedStateHandle: SavedStateHandle): ViewModel(), Lyric.EventListener {
    var isItPlaying = false
        set(play){
            savedStateHandle.set("isItPlaying", play)
            field = play
        }
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

    init {
        savedStateHandle.get<Boolean>("isItPlaying")?.run {
            isItPlaying = this
        }
    }
    fun getPlayBtnClickEvent(): MutableLiveData<Boolean>{
        return playBtnClickEvent
    }
    private var playBtnClickEvent: MutableLiveData<Boolean> = MutableLiveData(isItPlaying)
    private var liveLyricList: MutableLiveData<ArrayList<Lyric>> = MutableLiveData()
    private var lyricsItemClickEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getLyricsItemClickEvent(): MutableLiveData<Boolean>{
        return lyricsItemClickEvent
    }
    fun getLiveLyricList(): MutableLiveData<ArrayList<Lyric>>{
        return liveLyricList
    }

    fun setLyrics(lyricsMap: Map<Int, String>){
        var lyricsList : ArrayList<Lyric> = arrayListOf()
        for ((millSec, lyrics) in lyricsMap){
            lyricsList.add(Lyric(millSec,lyrics,this))
        }
        liveLyricList.value = lyricsList
    }
    fun getBound(): Boolean{
        return isItBound
    }
    fun onPlayBtnClick(){
        // Fragment로 이벤트를 전달하도록
        // MutableLiveData의 값을 변경한다.
        isItPlaying = (!isItPlaying)
        playBtnClickEvent.value = isItPlaying

    }


    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<PlayerMainViewModel>

    override fun onItemClick(lyric: Lyric) {
        Log.d("moveF",lyric.lyric)
        lyricsItemClickEvent.value = true
    }
}