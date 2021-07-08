package com.gomdolstudio.musicapp_assistedinjection.ui.player

import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.gomdolstudio.musicapp_assistedinjection.data.MusicRetrofitService
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Song
import com.gomdolstudio.musicapp_assistedinjection.di.factory.AssistedSavedStateViewModelFactory
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

    var count = 0
        set(value){
            savedStateHandle.set("count", value)
            field = value
        }

    init {
        savedStateHandle.get<Int>("count")?.run {
            count = this
        }
    }
    private var liveSong: MutableLiveData<Song> = MutableLiveData()
    private var singer: LiveData<String> = Transformations.map(liveSong){
            song -> song.singer
    }
    private var album: LiveData<String> = Transformations.map(liveSong){
            song -> song.album
    }
    private var title: LiveData<String> = Transformations.map(liveSong){
            song -> song.title
    }
    private var fileUrl: String = ""
    private var playBtnClickEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    fun loadMusic(){
        compositeDisposable.add(musicRetrofitService.getMusic()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(liveSong::setValue))
    }
    fun getPlayBtnClickEvent(): MutableLiveData<Boolean>{
        return playBtnClickEvent
    }

    /**
     * playBtn 클릭 이벤트 구현
     */
    fun onPlayBtnClick(){
        // Fragment로 이벤트를 전달하도록
        // MutableLiveData의 값을 변경한다.
        playBtnClickEvent.value = playBtnClickEvent.value == false

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