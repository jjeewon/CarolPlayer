package com.gomdolstudio.musicapp_assistedinjection.di.modules

import com.gomdolstudio.musicapp_assistedinjection.data.MusicRetrofitService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit

@Module
class RetrofitModule {
    @Provides
    @Reusable
    fun provideMusicRetrofitService(retrofit: Retrofit): MusicRetrofitService{
        return retrofit.create(MusicRetrofitService::class.java)
    }
}

