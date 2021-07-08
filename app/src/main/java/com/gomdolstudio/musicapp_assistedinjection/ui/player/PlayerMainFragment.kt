package com.gomdolstudio.musicapp_assistedinjection.ui.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gomdolstudio.musicapp_assistedinjection.data.entity.Song
import com.gomdolstudio.musicapp_assistedinjection.data.service.Actions
import com.gomdolstudio.musicapp_assistedinjection.data.service.MusicService
import com.gomdolstudio.musicapp_assistedinjection.databinding.FragmentMainBinding
import dagger.android.support.DaggerFragment


import javax.inject.Inject
import javax.inject.Named

class PlayerMainFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider
    @Inject
    lateinit var binding: FragmentMainBinding


    private lateinit var viewModel : PlayerViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding.아이디.로 조작...
        if (savedInstanceState == null){
            viewModel.loadMusic()
        }
        binding.lifecycleOwner = this
        binding.viewModel =viewModel

        viewModel.getLiveMusic()
                .observe(
                        viewLifecycleOwner, androidx.lifecycle.Observer { song: Song? -> setSongImage(song!!.image) }
                )

        viewModel.getPlayBtnClickEvent()
                .observe(
                        viewLifecycleOwner, Observer{ startOrStop: Boolean -> startOrStopMusicService(startOrStop) }
                )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider.get(PlayerViewModel::class.java)

    }

    fun startOrStopMusicService(startOrStop: Boolean){
        if (viewModel.getFileUrl() != ""){
            val intent = Intent(activity, MusicService::class.java)
            if (startOrStop == true){
                intent.action = Actions.START_FOREGROUND
                intent.putExtra("url",viewModel.getFileUrl())
                activity?.startService(intent)
            }else{
                intent.action = Actions.STOP_FOREGROUND
                activity?.stopService(intent)
            }
        }



    }

    fun setSongImage(imageUrl: String) {
        Glide.with(context)
                .load(imageUrl)
                .into(binding.thumbnailImageView)
    }





    }