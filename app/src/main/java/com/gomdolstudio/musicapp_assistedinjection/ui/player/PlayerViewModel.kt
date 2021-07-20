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

) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var timer = MutableLiveData<Int>(0)
        set(time){
            savedStateHandle.set("timer",time)
            field = time
        }
    var isItBound = false
        set(bound){
            savedStateHandle.set("isItBound", bound)
            field = bound
        }

    var isItPlaying = false
        set(play){
            savedStateHandle.set("isItPlaying", play)
            field = play
        }


    init {
        savedStateHandle.get<Boolean>("isItPlaying")?.run {
            isItPlaying = this
        }
    }

    init {
        savedStateHandle.get<Boolean>("isItBound")?.run {
            isItBound = this
        }
    }

    init {
        savedStateHandle.get<Int>("timer")?.run {
            timer.value = this
        }
    }

    private var liveSong: MutableLiveData<Song> = MutableLiveData()
    private lateinit var liveLyrics: Lyrics
    private var liveLyricList: MutableLiveData<ArrayList<Lyric>> = MutableLiveData()
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

    private var playBtnClickEvent: MutableLiveData<Boolean> = MutableLiveData(isItPlaying)
    fun loadMusic(){
        compositeDisposable.add(musicRetrofitService.getMusic()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(liveSong::setValue))
    }
    fun getPlayBtnClickEvent(): MutableLiveData<Boolean>{
        return playBtnClickEvent
    }
    fun getBound(): Boolean{
        return isItBound
    }
    fun getTime() : MutableLiveData<Int>{
        return timer
    }

    fun getLiveLyricList(): MutableLiveData<ArrayList<Lyric>>{
        return liveLyricList
    }

    fun setLyrics(lyricsString: String){
        var lyricList : ArrayList<Lyric> = arrayListOf()
        var lyrics = mutableMapOf<Int,String>()
        var timeList: ArrayList<Int> = arrayListOf()

        val array: List<String> = lyricsString.split("\n")
        for (a in array) {
            val timeLyrics = a.split("]")
            val time = convertTimeToInt(timeLyrics[0].substring(1, timeLyrics[0].length))
            lyrics[time] = timeLyrics[1]
            timeList.add(time)
            lyricList.add(Lyric(time,timeLyrics[1]))
        }
        liveLyricList.value = lyricList
        liveLyrics = Lyrics(lyrics,timeList)
    }

    /**
     * playBtn 클릭 이벤트 구현
     */
    fun onPlayBtnClick(){
        // Fragment로 이벤트를 전달하도록
        // MutableLiveData의 값을 변경한다.
        isItPlaying = (!isItPlaying)
        playBtnClickEvent.value = isItPlaying

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
        /*
         val total = Integer.parseInt(duration.value!!)
        val min: Int = total / 60
        val sec = total % 60
         */
        //if (duration.value != null) durationInt = Integer.parseInt(duration.value!!)

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