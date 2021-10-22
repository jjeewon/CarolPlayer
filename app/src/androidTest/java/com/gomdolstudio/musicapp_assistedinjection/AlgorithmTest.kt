package com.gomdolstudio.musicapp_assistedinjection

import com.gomdolstudio.musicapp_assistedinjection.data.entity.Lyric
import org.junit.Test
import com.gomdolstudio.musicapp_assistedinjection.util.binarySearchForLyricsPosition
import io.mockk.mockk
import org.junit.Before
import java.util.*
import kotlin.collections.ArrayList
import org.junit.Assert.*
class AlgorithmTest {
    lateinit var lyricArrayList: ArrayList<Lyric>
    lateinit var eventListener: Lyric.EventListener

    @Before
    fun 가사배열만들기(){
        eventListener = mockk<Lyric.EventListener>()
        lyricArrayList = ArrayList()
        lyricArrayList.add(Lyric(1,"one",eventListener)) //     [0]
        lyricArrayList.add(Lyric(5,"five",eventListener)) //    [1]
        lyricArrayList.add(Lyric(10,"ten",eventListener)) //    [2]
        lyricArrayList.add(Lyric(15,"fifteen",eventListener))// [3]
        lyricArrayList.add(Lyric(20,"twenty",eventListener))//  [4]
    }

    @Test
    fun 이진탐색알고리즘_테스트(){
        // 배열에서 선택한 값(재생시간)보다 크거나 같은 값을 가진 것들 중 최솟값을 가진 클래스의 _인덱스를 반환하는지 테스트

        // 1. 선택한 값과 동일한 값을 가진 클래스가 있다면 해당 클래스의 인덱스를 반환
        assertEquals(3,binarySearchForLyricsPosition(lyricArrayList, 15))

        // 2. 배열에 선택한 값과 동일한 값을 가진 클래스가 없다면 선택한 값보다 큰 것 중에서 최소값을 가진 클래스의 인덱스 반환
        assertEquals(4, binarySearchForLyricsPosition(lyricArrayList, 200))
    }
}