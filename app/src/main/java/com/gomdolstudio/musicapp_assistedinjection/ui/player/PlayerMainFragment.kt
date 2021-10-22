package com.gomdolstudio.musicapp_assistedinjection.ui.player

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gomdolstudio.musicapp_assistedinjection.R
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Song
import com.gomdolstudio.musicapp_assistedinjection.data.service.Actions
import com.gomdolstudio.musicapp_assistedinjection.data.service.MusicService
import com.gomdolstudio.musicapp_assistedinjection.databinding.FragmentMainBinding
import com.gomdolstudio.musicapp_assistedinjection.ui.player.lyrics.LyricsAdapter
import com.gomdolstudio.musicapp_assistedinjection.util.binarySearchForLyricsPosition
import com.gomdolstudio.musicapp_assistedinjection.util.convertMillSecToString
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_player.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*


import javax.inject.Inject

class PlayerMainFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider

    @Inject
    lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var lyricsAdapter: LyricsAdapter


    private lateinit var musicService: MusicService
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var playerMainViewModel: PlayerMainViewModel
    private lateinit var serviceBinder: IBinder

    private lateinit var job: Job
    private lateinit var seekBarJob: Job
    private val scope = CoroutineScope(Dispatchers.Main)


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            playerMainViewModel.isItBound = true
            job = lyricsCoroutine()
            seekBarJob = seekBarCoroutine()
            job.start()
            seekBarJob.start()
            Log.d("jjj","${musicService} : ")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("jjj","onServiceDisconnected")

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("sss","onAttach")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("sss","onCreateView")

        if (playerViewModel.isItComback){
            Log.d("comback","온거맞는데?")
            val intent = Intent(activity, MusicService::class.java)
            //intent.action = Actions.START_FOREGROUND
            intent.putExtra("url", playerViewModel.getFileUrl())
            intent.putExtra("time", playerViewModel.timer.value)
            intent.putExtra("duration", playerViewModel.getDuration().value)
            //activity?.startService(intent)
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        }

        Log.d("pppViewModel","${playerMainViewModel.isItPlaying}")

        return binding.root
    }

    override fun onDestroy() {
        playerMainViewModel.isItBound = false
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding.아이디.로 조작...
        if (savedInstanceState == null) {
            playerViewModel.loadMusic()

        }
        binding.lifecycleOwner = this
        binding.playerMainViewModel = playerMainViewModel
        binding.playerViewModel = playerViewModel
        binding.lyricRecyclerView.adapter = lyricsAdapter
        binding.lyricRecyclerView.layoutManager = layoutManager

        if (playerViewModel.isItComback){
            play_button.setBackgroundResource(R.drawable.ic_pause)
        }

         playerViewModel.getLiveMusic()
                .observe(
                        viewLifecycleOwner, androidx.lifecycle.Observer { song: Song? ->
                    setSongInfos(song!!)
                }
                )


        playerViewModel.getliveLyricsMap()
                .observe(
                        viewLifecycleOwner, androidx.lifecycle.Observer { map: Map<Int,String>? ->
                    playerMainViewModel.setLyrics(map!!)
                }
                )


        /*
         viewModel.getLiveLyricList()
                .observe(
                        viewLifecycleOwner, androidx.lifecycle.Observer { lyricsList: ArrayList<Lyric>? -> /* */
                    lyricsAdapter.setItems(lyricsList)
                }
                )
         */


        playerMainViewModel.getPlayBtnClickEvent()
                .observe(
                        viewLifecycleOwner, Observer { startOrStop: Boolean -> startOrStopMusicService(startOrStop) }
                )
        playerMainViewModel.getLyricsItemClickEvent()
                .observe(
                        viewLifecycleOwner, Observer { clicked: Boolean -> moveFragment(clicked)}
                )

        music_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, userClicked: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (playerMainViewModel.isItBound){
                    val milSec = seekBar.progress * musicService.duration / 100
                    playerViewModel.timer.value = milSec
                    musicService.movePlayerTime(milSec)
                }
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = viewModelProvider.get(PlayerViewModel::class.java)
        playerMainViewModel = viewModelProvider.get(PlayerMainViewModel::class.java)
    }

    fun moveFragment(clicked: Boolean){
        Log.d("moveFragment","${clicked}")
        if (clicked == true && !playerViewModel.isItComback){
            Log.d("jjj 시작할","${musicService} : ${connection}")
            seekBarJob.cancel()
            job.cancel()
            (activity as PlayerActivity).moveFragment(R.id.container, PlayerLyricsFragment())

        }
        playerViewModel.isItComback = false

    }

    fun startOrStopMusicService(startOrStop: Boolean) {

        if (playerViewModel.getFileUrl() != "" && !playerViewModel.isItComback) {
            val intent = Intent(activity, MusicService::class.java)
            if (startOrStop == true) {
                Log.d("frfr","여길 들어온다고??")
                intent.action = Actions.START_FOREGROUND
                intent.putExtra("url", playerViewModel.getFileUrl())
                intent.putExtra("time", playerViewModel.timer.value)
                intent.putExtra("duration", playerViewModel.getDuration().value)
                activity?.startService(intent)
                activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
                play_button.setBackgroundResource(R.drawable.ic_pause)

            } else {
                Log.d("jjj","나 여기 왔다니까???")
                Log.d("jjj","끝낼 때 ... ${musicService} : ${connection}")
                job.cancel()
                seekBarJob.cancel()
                playerViewModel.timer.value = musicService.millSec
                intent.action = Actions.STOP_FOREGROUND
                activity?.unbindService(connection)
                activity?.stopService(intent)
                play_button.setBackgroundResource(R.drawable.ic_play)
            }
        }

    }

    fun setSongInfos(song: Song) {
        setSongLyrics(song.lyrics)
        setSongImage(song.image)
        playerViewModel.setDuration(song.duration.toInt())
        val array = playerMainViewModel.getLiveLyricList().value
        lyricsAdapter.setItems(arrayListOf(array?.get(0)!!, array.get(1)))
        //playerViewModel.durationString.value = convertMillSecToString(song.duration.toInt())
    }

    fun setSongLyrics(lyricsString: String) {
        playerViewModel.setLyricsMap(lyricsString)
    }

    fun setSongImage(imageUrl: String) {
        Glide.with(context)
                .load(imageUrl)
                .into(binding.thumbnailImageView)
    }

    fun seekBarCoroutine(): Job = scope.launch(start = CoroutineStart.LAZY) {
        val duration = musicService.duration
        val conversionFactorForProgressBar: Double = 100.0 / duration.toDouble()
        while (musicService.millSec < duration) {
            music_seek_bar.progress = (musicService.millSec.toDouble() * conversionFactorForProgressBar).toInt()
            playerViewModel.timeString.value = convertMillSecToString(musicService.millSec, true)
            delay(100)
        }

    }

    fun lyricsCoroutine(): Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        Log.d("pppStartOrStop","${musicService} : ${musicService.duration}")
        var position: Int = 0
        val array = playerMainViewModel.getLiveLyricList().value
        if (array != null) array.add((Lyric(musicService.duration, "",playerMainViewModel)))
        while (musicService.millSec < musicService.duration) {
            val newPosition = binarySearchForLyricsPosition(array!!, musicService.millSec)
            if (newPosition != position) {
                playerViewModel.setCurrentPos(newPosition)
                scope.launch {
                    array.get(newPosition).highlight = true
                    if (newPosition != 0) array.get(newPosition-1).highlight = false
                    lyricsAdapter.setItems(arrayListOf(array.get(newPosition), array.get(newPosition + 1)))
                    delay(50)
                }
                position = newPosition
            }
            delay(100)
        }
    }



}

