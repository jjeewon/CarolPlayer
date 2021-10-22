package com.gomdolstudio.musicapp_assistedinjection.data.entity

data class Lyric(val millSec: Int,
                 val lyric: String,
                 val eventListener: EventListener,
                 var highlight: Boolean = false){
    interface EventListener{
        fun onItemClick(lyric: Lyric)
    }
}

