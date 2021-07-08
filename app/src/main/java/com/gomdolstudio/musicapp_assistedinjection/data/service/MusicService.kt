package com.gomdolstudio.musicapp_assistedinjection.data.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service(), MediaPlayer.OnPreparedListener {
    private var mp: MediaPlayer? = null
    var url: String = ""

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        mp = MediaPlayer()
        super.onCreate()

    }

    // 서비스가 호출될 때마다 호출 (음악재생)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent!!.hasExtra("url"))
            url = intent.getStringExtra("url").toString()


        when (intent?.action){
            Actions.START_FOREGROUND -> {
                startForegroundService(url)
            }
            Actions.STOP_FOREGROUND -> {
                stopForegroundService()
            }

        }
        return START_STICKY
    }

    // 서비스가 종료될 때 음악 종료
    override fun onDestroy() {
        super.onDestroy()
        mp?.release()
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

    private fun stopForegroundService(){
        stopForeground(true)
        stopSelf()
    }

    companion object{
        const val TAG = "[MusicPlayerService]"
        const val NOTIFICATION_ID = 20
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }
}