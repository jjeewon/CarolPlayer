package com.gomdolstudio.musicapp_assistedinjection.di

import com.gomdolstudio.musicapp_assistedinjection.data.service.MusicService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRemoteDataSource @Inject constructor(
    private val musicService: MusicService
){
}