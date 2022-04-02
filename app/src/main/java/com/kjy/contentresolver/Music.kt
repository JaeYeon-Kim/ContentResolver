package com.kjy.contentresolver

import android.net.Uri
import android.provider.MediaStore

class Music(id: String, title: String?, artist: String?, albumId: String?, duration: Long?) {

    // 각 프로퍼티들 정의
    var id: String = ""
    var title: String?
    var artist: String?
    var albumId: String?
    var duration: Long?

    init {
        this.id = id
        this.title = title
        this.artist = artist
        this.albumId = albumId
        this.duration = duration
    }

    // 음원의 URI를 생성하는 getMusicUri() 메서드 정의.
    // 음원 URI는 기본 MediaStore의 주소와 음원 ID를 조합해서 만들기 때문에 메서드로 만들어 놓는 것이 편함.
    fun getMusicUri(): Uri {
        return Uri.withAppendedPath(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
        )
    }

    // 음원 파일 별로 썸네일 지정 = 앨범 아트(앨범 이미지 사용)
    // Uri문자열을 직접 접근을 위한 Uri.parse 형식으로 URI 생성.
    fun getAlbumUri(): Uri {
        return Uri.parse(
            "content://media/external/audio/albumart/" + albumId
        )
    }
}