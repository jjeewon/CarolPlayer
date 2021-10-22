package com.gomdolstudio.musicapp_assistedinjection.di.factory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * SavedStateHandle을 포함하는 ViewModel을 생성하는 Factory
 * 이 Factory는 하나의 InjectingSavedStateViewModelFactory에 모든 ViewModel을 가질 수 있게 만들어줌
 */
interface AssistedSavedStateViewModelFactory<T: ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): T // ViewModel을 확장한 제네릭 T를 반환
    // SavedStateHandle은 Dagger Graph에 포함시키지 못하는 동적 파라미터이므로 create의 매개변수로 받음
}

