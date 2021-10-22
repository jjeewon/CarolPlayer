package com.gomdolstudio.musicapp_assistedinjection

import com.gomdolstudio.musicapp_assistedinjection.data.MusicRetrofitService
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Song
import io.reactivex.Single

class MockMusicRetrofitService : MusicRetrofitService {
    override fun getMusic(): Single<Song> {
        val song = Song("bts","Map of the Soul: 7","ON","298","https://image","https://file","lyrics")
        return Single.just(song)
    }
}