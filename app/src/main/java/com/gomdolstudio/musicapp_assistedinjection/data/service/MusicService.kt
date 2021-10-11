package com.gomdolstudio.musicapp_assistedinjection.data.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.gomdolstudio.musicapp_assistedinjection.ui.player.PlayerViewModel
import dagger.android.DaggerService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDateTime

class MusicService : DaggerService(), MediaPlayer.OnPreparedListener {

    private var mp: MediaPlayer? = null
    var url: String = ""
    var duration: Int = 0
    var millSec: Int = 0
    var isItPlaying: Boolean = false


    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MusicService = this@MusicService
    }


    override fun onBind(intent: Intent): IBinder? {
        if (!isItPlaying){
            isItPlaying = true
            startMusicTimer()
        }
        return binder
    }

    override fun onCreate() {
        mp = MediaPlayer()
        //viewModel = viewModelProvider.get(PlayerViewModel::class.java)

        super.onCreate()

    }

    // 서비스가 호출될 때마다 호출 (음악재생)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("sss","onStartCommand")

        if (intent!!.hasExtra("url"))
            url = intent.getStringExtra("url").toString()
        if (intent!!.hasExtra("time"))
            millSec = intent.getIntExtra("time",0)
        if (intent!!.hasExtra("duration"))
            duration = intent.getIntExtra("duration",0) * 10
        when (intent.action){
            Actions.START_FOREGROUND -> {
                startForegroundService(url)
            }
            Actions.STOP_FOREGROUND -> {
                stopForegroundService()
            }
        }
        return START_NOT_STICKY
    }

    // 서비스가 종료될 때 음악 종료
    override fun onDestroy() {
        Log.d("sss","onDestroy")
        super.onDestroy()
        mp?.release()
        mp = null
        isItPlaying = false
    }

    private fun startForegroundService(url: String){
        try{
            mp?.setDataSource(url)
        }catch (e: Exception){
            e.printStackTrace()
        }
        mp?.apply {
            setOnPreparedListener(this@MusicService)
            prepareAsync()
        }


        val notification = MusicNotification.createNotification(this)
        startForeground(NOTIFICATION_ID, notification)
    }

    fun stopForegroundService(){
        Log.d("ssss","왔다니까??")
        stopForeground(true)
        stopSelf()
    }

    companion object{
        const val TAG = "[MusicPlayerService]"
        const val NOTIFICATION_ID = 20
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.seekTo(millSec*100)
        mediaPlayer.start()
    }

    private fun startMusicTimer(){
        GlobalScope.launch {
            while ( this@MusicService.millSec < duration && isItPlaying){
                val time: LocalDateTime = LocalDateTime.now()
                delay(100L)
                this@MusicService.millSec += 1
            }
        }

    }

    fun movePlayerTime(millSec: Int){
            this.millSec = millSec
            mp?.seekTo(millSec*100)

    }


}