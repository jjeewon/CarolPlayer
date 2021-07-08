package com.gomdolstudio.musicapp_assistedinjection.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Song(
        @Expose
        @SerializedName("singer")
        var singer: String = "",
        @Expose
        @SerializedName("album")
        var album: String = "",
        @Expose
        @SerializedName("title")
        var title: String = "",
        @Expose
        @SerializedName("duration")
        var duration: String = "",
        @Expose
        @SerializedName("image")
        var image: String = "",
        @Expose
        @SerializedName("file")
        var file: String = "",
        @Expose
        @SerializedName("lyrics")
        var lyrics: String = ""
)
