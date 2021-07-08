package com.gomdolstudio.musicapp_assistedinjection.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Named(val value: String ="")