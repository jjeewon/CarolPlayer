package com.gomdolstudio.musicapp_assistedinjection.ui.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gomdolstudio.musicapp_assistedinjection.R
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import com.gomdolstudio.musicapp_assistedinjection.data.service.MusicService
import com.gomdolstudio.musicapp_assistedinjection.databinding.FragmentLyricsBinding
import com.gomdolstudio.musicapp_assistedinjection.ui.player.lyrics.LyricsAdapter
import com.gomdolstudio.musicapp_assistedinjection.util.binarySearchForLyricsPosition
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import javax.inject.Inject

class PlayerLyricsFragment: DaggerFragment() {

    @Inject
    lateinit var binding: FragmentLyricsBinding
    @Inject
    lateinit var viewModelProvider: ViewModelProvider
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var playerLyricsViewModel: PlayerLyricsViewModel

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var lyricsAdapter: LyricsAdapter

    private lateinit var musicService: MusicService

    private lateinit var highlightJob: Job
    private lateinit var scrollJob: Job
    private val scope = CoroutineScope(Dispatchers.Main)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            Log.d("lll","onServiceConnected")
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            playerLyricsViewModel.isItBound = true
            highlightJob.start()
            scrollJob.start()

            playerLyricsViewModel.getLyricsItemClickEvent().observe(
                    viewLifecycleOwner, Observer { millSec: Int -> movePlayerTime(millSec) }
            )
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("lll","onServiceDisconnected")

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDestroy() {
        Log.d("lll","onDestroy")
        super.onDestroy()
        playerLyricsViewModel.isItBound = false
        playerViewModel.timer.value = musicService.millSec
        highlightJob.cancel()
        scrollJob.cancel()
        activity?.unbindService(connection)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("lll","onCreateView")
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("lll","onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        // binding.아이디.로 조작...
        if (savedInstanceState == null) {

        }
        binding.lifecycleOwner = this
        binding.playerViewModel = playerViewModel
        binding.playerLyricsViewModel = playerLyricsViewModel
        binding.lyricsRecyclerView.adapter = lyricsAdapter
        binding.lyricsRecyclerView.layoutManager = layoutManager
        loadLyrics()

        val intent = Intent(activity, MusicService::class.java)
        intent.putExtra("url", playerViewModel.getFileUrl())
        intent.putExtra("time", playerViewModel.timer.value)
        intent.putExtra("duration", playerViewModel.getDuration().value)
        activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        highlightJob = highlightCoroutine()
        scrollJob = scrollCoroutine()

        playerLyricsViewModel.getCancelBtnClickEvent()
                .observe(viewLifecycleOwner, Observer { cancelBtnClicked:Boolean -> moveFragment(cancelBtnClicked) })


        playerLyricsViewModel.getLiveLyricList()
                .observe(viewLifecycleOwner, Observer { lyricsList: ArrayList<Lyric> -> lyricsAdapter.setItems(lyricsList) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lll","onCreate")
        super.onCreate(savedInstanceState)
        playerViewModel = viewModelProvider.get(PlayerViewModel::class.java)
        playerLyricsViewModel = viewModelProvider.get(PlayerLyricsViewModel::class.java)
    }

    fun loadLyrics(){
        playerLyricsViewModel.setLyrics(playerViewModel.getliveLyricsMap().value!!)
    }

    fun highlightCoroutine(): Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        var position: Int = 0
        val array = playerLyricsViewModel.getLiveLyricList().value
        if (array != null) array.add((Lyric(musicService.duration, "", playerLyricsViewModel)))
        while (musicService.millSec < musicService.duration) {
            val newPosition = binarySearchForLyricsPosition(array!!, musicService.millSec)
            if (newPosition != position) {
                playerViewModel.setCurrentPos(newPosition)
                scope.launch {
                    array.get(newPosition).highlight = true
                    lyricsAdapter.setItems(array)
                    if (newPosition != 0) array.get(newPosition - 1).highlight = false
                    delay(50)
                }
                position = newPosition
            }
            delay(50)

        }
    }

    fun scrollCoroutine(): Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        val array = playerLyricsViewModel.getLiveLyricList().value
        var position: Int = 0
        while (musicService.millSec < musicService.duration) {
            val newPosition = binarySearchForLyricsPosition(array!!, musicService.millSec)
            if (layoutManager != null){
                val midPositionInRecyclerView: Int = (layoutManager.findFirstVisibleItemPosition() + layoutManager.findLastVisibleItemPosition()) / 2
                if (newPosition != position && midPositionInRecyclerView < newPosition) {
                    scope.launch {
                        binding.lyricsRecyclerView.smoothScrollToPosition(layoutManager.findLastVisibleItemPosition()+1)
                        delay(500)
                    }
                    position = newPosition
                }
                delay(100)
            }

        }
    }

    fun movePlayerTime(millSec: Int){
        if (millSec > 0 ){
            playerLyricsViewModel.getLiveLyricList().value?.get(playerViewModel.getCurrentPos())?.highlight = false
            musicService.movePlayerTime(millSec)
        }
    }

    fun moveFragment(cancelBtnClicked: Boolean){
        Log.d("lll","설마 LYRics moveFragment왔다고?")
        if (cancelBtnClicked){
            scrollJob.cancel()
            highlightJob.cancel()
            //activity?.unbindService(connection)
            playerViewModel.isItComback = true
            playerLyricsViewModel.setCancelBtnClickEvent(false)
            (activity as PlayerActivity).moveFragment(R.id.container, PlayerMainFragment())

        }
    }




}