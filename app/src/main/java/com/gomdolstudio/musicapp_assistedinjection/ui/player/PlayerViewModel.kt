package com.gomdolstudio.musicapp_assistedinjection.ui.player

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.gomdolstudio.musicapp_assistedinjection.data.MusicRetrofitService
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyrics
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Song
import com.gomdolstudio.musicapp_assistedinjection.di.factory.AssistedSavedStateViewModelFactory
import com.gomdolstudio.musicapp_assistedinjection.util.convertTimeToInt
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlayerViewModel @AssistedInject
constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val musicRetrofitService: MusicRetrofitService

) : ViewModel(){
    private val compositeDisposable = CompositeDisposable()
    var timer = MutableLiveData<Int>(0)
        set(time){
            savedStateHandle.set("timer",time)
            field = time
        }




    var isItComback = false
        set(comback){
            savedStateHandle.set("isItComback",comback)
            field = comback
    }





    init {
        savedStateHandle.get<Int>("timer")?.run {
            timer.value = this
        }
    }

    init {
        savedStateHandle.get<Boolean>("isItComback")?.run {
            isItComback = this
        }
    }
    private var currentPos: Int = 0
    private var liveSong: MutableLiveData<Song> = MutableLiveData()

    private var liveLyricsMap: MutableLiveData<Map<Int,String>> = MutableLiveData()
    private var singer: LiveData<String> = Transformations.map(liveSong){
            song -> song.singer
    }
    private var album: LiveData<String> = Transformations.map(liveSong){
            song -> song.album
    }
    private var title: LiveData<String> = Transformations.map(liveSong){
            song -> song.title
    }
    private var duration: LiveData<Int> = Transformations.map(liveSong){
        song -> Integer.parseInt(song.duration)
    }

    private var fileUrl: String = ""


    fun loadMusic(){
        compositeDisposable.add(musicRetrofitService.getMusic()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(liveSong::setValue))
    }

    fun getliveLyricsMap(): MutableLiveData<Map<Int,String>>{
        return liveLyricsMap
    }

    fun getTime() : MutableLiveData<Int>{
        return timer
    }

    fun setCurrentPos(currentPos: Int){
        this.currentPos = currentPos
    }
    fun getCurrentPos(): Int{
        return currentPos
    }

    fun setLyricsMap(lyricsString: String){
        var lyrics = mutableMapOf<Int,String>()
        val array: List<String> = lyricsString.split("\n")
        for (a in array) {
            val timeLyrics = a.split("]")
            val time = convertTimeToInt(timeLyrics[0].substring(1, timeLyrics[0].length))
            lyrics[time] = timeLyrics[1]
        }
        liveLyricsMap.value = lyrics
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getLiveMusic(): MutableLiveData<Song>{
        return liveSong
    }

    fun getSinger(): LiveData<String>{
        return singer
    }

    fun getTitle(): LiveData<String>{
        return title
    }

    fun getDuration(): LiveData<Int>{
        return duration
    }

    fun getAlbum(): LiveData<String>{
        return album
    }


    fun getFileUrl(): String{
        return if (liveSong.value != null)
            liveSong.value?.file!!
        else
            ""
    }

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<PlayerViewModel>

}