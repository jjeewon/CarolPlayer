package com.gomdolstudio.musicapp_assistedinjection.data

import com.gomdolstudio.musicapp_assistedinjection.data.entity.Song
import io.reactivex.Single
import javax.inject.Inject

class RemoteRepository @Inject constructor(private var retrofitService: MusicRetrofitService) {
    val songRepository: Single<Song>
        get() = retrofitService.getMusic()
}