package com.gomdolstudio.musicapp_assistedinjection.util

import android.util.Log
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun convertMillSecToString(time: Int, isItMillSec: Boolean): String{
    var totalSec: Int = time
    if (isItMillSec) totalSec = time / 10
    val min: Int = totalSec / 60
    val sec: Int = totalSec % 60
    var minString = min.toString()
    if (min < 10) minString = "0${minString}"
    var secString = sec.toString()
    if (sec < 10) secString = "0${secString}"
    return "${minString}:${secString}"
}

fun convertTimeToInt(time: String): Int {
    val array = time.split(":")
    val minToMillSec = Integer.parseInt(array[0].get(0).toString())*10 + Integer.parseInt(array[0].get(1).toString())
    val secToMillSec = Integer.parseInt(array[1].get(0).toString())*10 + Integer.parseInt(array[1].get(1).toString())
    var millSec = Integer.parseInt(array[2].get(0).toString())*100 + Integer.parseInt(array[2].get(1).toString())*10 + Integer.parseInt(array[2].get(2).toString())
    return minToMillSec * 600 + secToMillSec * 10 + millSec/100
}

fun binarySearchForLyricsPosition(lyricsArray: ArrayList<Lyric>, millSec: Int): Int{
    var start: Int = 0
    var end: Int = lyricsArray.size - 1
    var result: Int = 0
    while ( start <= end ){
        val mid: Int = (start + end) / 2
        if (lyricsArray[mid].millSec <= millSec){
            if (result < mid) result = mid
            start = mid + 1
        }
        else {
            end = mid - 1
        }
    }
    return result
}

