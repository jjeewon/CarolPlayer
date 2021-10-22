package com.gomdolstudio.musicapp_assistedinjection.di.modules

import android.app.Application
import android.content.Context
import com.gomdolstudio.musicapp_assistedinjection.App
import com.gomdolstudio.musicapp_assistedinjection.data.MusicRetrofitService
import com.gomdolstudio.musicapp_assistedinjection.di.ApplicationContext
import com.gomdolstudio.musicapp_assistedinjection.di.Named
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class), (RetrofitModule::class)])
class AppModule {

    @Singleton
    @Provides
    internal fun provideMusicRetrofitService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApp(app: App): Application{
        return app
    }

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(app: App): Context{
        return app
    }
}