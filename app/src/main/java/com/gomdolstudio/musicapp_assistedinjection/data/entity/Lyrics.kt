package com.gomdolstudio.musicapp_assistedinjection.data.entity

data class Lyrics(
        var lyricsMap: MutableMap<Int, String> = mutableMapOf<Int,String>(),
        var timeList: ArrayList<Int> = arrayListOf()
)
