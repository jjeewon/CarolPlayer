package com.gomdolstudio.musicapp_assistedinjection.data

import com.gomdolstudio.musicapp_assistedinjection.data.entity.Song
import io.reactivex.Single
import retrofit2.http.GET

interface MusicRetrofitService {
    @GET("2020-flo/song.json")
    fun getMusic(): Single<Song>
}