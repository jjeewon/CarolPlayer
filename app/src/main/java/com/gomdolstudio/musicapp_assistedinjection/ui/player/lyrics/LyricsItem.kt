package com.gomdolstudio.musicapp_assistedinjection.ui.player.lyrics

import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric

class LyricsItem constructor(
        private var lyric: Lyric,
        private var eventListener: EventListener){

    fun getEventListener(): EventListener{
        return eventListener
    }
    interface EventListener{
        fun onUserClick(millSec: Int)
    }


}