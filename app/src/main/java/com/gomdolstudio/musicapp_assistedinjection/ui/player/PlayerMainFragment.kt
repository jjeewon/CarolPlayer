package com.gomdolstudio.musicapp_assistedinjection.ui.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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
    private lateinit var viewModel: PlayerViewModel

    private lateinit var job: Job
    private lateinit var seekBarJob: Job
    private val scope = CoroutineScope(Dispatchers.Main)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            viewModel.isItBound = true
            job.start()
            seekBarJob.start()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            viewModel.isItBound = false
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding.아이디.로 조작...
        if (savedInstanceState == null) {
            viewModel.loadMusic()
        }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.lyricRecyclerView.adapter = lyricsAdapter
        binding.lyricRecyclerView.layoutManager = layoutManager


         viewModel.getLiveMusic()
                .observe(
                        viewLifecycleOwner, androidx.lifecycle.Observer { song: Song? ->
                    setSongInfos(song!!)
                }
                )



        viewModel.getLiveLyricList()
                .observe(
                        viewLifecycleOwner, androidx.lifecycle.Observer { lyricsList: ArrayList<Lyric>? ->
                    lyricsAdapter.setItems(lyricsList)
                }
                )

        viewModel.getPlayBtnClickEvent()
                .observe(
                        viewLifecycleOwner, Observer { startOrStop: Boolean -> startOrStopMusicService(startOrStop) }
                )
        viewModel.getLyricsItemClickEvent()
                .observe(
                        viewLifecycleOwner, Observer { clicked: Boolean -> moveFragment(clicked)}
                )

        music_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, userClicked: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val milSec = seekBar.progress * musicService.duration / 100
                viewModel.timer.value = milSec
                musicService.movePlayerTimeBySeekerBar(milSec)
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider.get(PlayerViewModel::class.java)
    }

    fun moveFragment(clicked: Boolean){
        if (clicked) (activity as PlayerActivity).moveFragment(R.id.container, PlayerLyricsFragment())
    }

    fun startOrStopMusicService(startOrStop: Boolean) {

        if (viewModel.getFileUrl() != "") {
            val intent = Intent(activity, MusicService::class.java)
            val lyricsList = lyricsAdapter.getLyricsList()
            if (startOrStop == true) {
                intent.action = Actions.START_FOREGROUND
                intent.putExtra("url", viewModel.getFileUrl())
                intent.putExtra("time", viewModel.timer.value)
                intent.putExtra("duration", viewModel.getDuration().value)
                activity?.startService(intent)
                activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
                job = lyricsCoroutine()
                seekBarJob = seekBarCoroutine()

            } else {
                job.cancel()
                seekBarJob.cancel()
                viewModel.timer.value = musicService.millSec
                intent.action = Actions.STOP_FOREGROUND
                activity?.stopService(intent)
                activity?.unbindService(connection)
            }
        }

    }

    fun setSongInfos(song: Song) {
        setSongLyrics(song.lyrics)
        setSongImage(song.image)
    }

    fun setSongLyrics(lyricsString: String) {
        viewModel.setLyrics(lyricsString)
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
            delay(100)
        }

    }

    fun lyricsCoroutine(): Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        var result: Int = 0
        val array = viewModel.getLiveLyricList().value
        if (array != null) array.add((Lyric(musicService.duration, "",viewModel)))
        while (musicService.millSec < musicService.duration) {

            Log.d("fragment", "${musicService.millSec}, : ${musicService.duration}")
            val newResult = binarySearchForLyricsPosition(array!!, musicService.millSec)
            if (newResult != result) {
                scope.launch {
                    array.get(newResult).highlight = true
                    lyricsAdapter.setItems(arrayListOf(array.get(newResult), array.get(newResult + 1)))
                    delay(50)
                }
                result = newResult
            }
            delay(100)
        }
    }


}
