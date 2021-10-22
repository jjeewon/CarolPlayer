# CarolPlayer - Android App
Play carol song with CarolPlayer🎅🎺
  
## About

<img width="740" alt="스크린샷 2021-10-20 오전 11 14 33" src="https://user-images.githubusercontent.com/18653295/138435904-f3e37d5a-a92a-479f-aaa3-fbffdba070b2.png"> 

CarolPlayer has been made for you to play 「We with you a Merry Christmas 」 🎵

## Features
The android app lets you:
* View the currently playing lyrics and the next playing lyrics over two lines
* Move the playback section through the Seekbar
* View the currently playing lyrics highlighted in the 'full lyrics screen'  
* View an auto-scrolling screen with the lyrics positioned above the center
* Play a specific lyric section If you touch the area
* Completely ad-free

## ScreenShots
작성중..
## Permissions
On Android versions prior to Android 6.0, CovidInfoApp requires the following permissions:
* Full Network Access.

## Music data request API
<h4> COVID-19 Vaccination Status API(코로나 백신 예방 접종 통계) </h4> 

```html
GET /2020-flo/song.json
```  

```kotlin
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
```


<!-- Table -->
|Song|Type|Description|
|--|--|--|
|`singer`|String||
|`album`|String||
|`title`|string||
|`duration`|string||
|`image`|string||
|`file`|string||
|`lyrics`|string||


