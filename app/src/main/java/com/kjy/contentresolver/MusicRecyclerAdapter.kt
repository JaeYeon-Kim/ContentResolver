package com.kjy.contentresolver

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kjy.contentresolver.databinding.ItemRecyclerBinding


// 리사이클러뷰에 사용할 어댑터 생성
// Adapter 클래스를 상속받고 제네릭으로 위에서 만들어둔 Holder 지정
class MusicRecyclerAdapter: RecyclerView.Adapter<MusicRecyclerAdapter.Holder>() {
    // 음악 목록을 저장해둘 변수 생성.
    // 제네릭으로 Music을 사용하는 컬렉션
    var musicList = mutableListOf<Music>()
    // MediaPlayer를 담아두는 mediaPlayer 변수를 선언.
    var mediaPlayer: MediaPlayer? = null


    // 아이템 레이아웃 바인딩 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return Holder(binding)

    }

    // 아이템 레이아웃에 데이터 출력
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = musicList.get(position)
        holder.setMusic(music)
    }

    // 목록의 개수를 알려줌.
    override fun getItemCount(): Int {
        return musicList.size
    }

    inner class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root) {

        // 클릭시 음원이 플레이를 위한 변수 설정. Music 클래스가 가지고 있는 Uri를 저장.
        var musicUri: Uri? = null

        // 생성자로 넘어온 itemView에 클릭리스너를 연결해줌.
        init {
            binding.root.setOnClickListener {
                if (mediaPlayer != null) {
                    mediaPlayer?.release()
                    mediaPlayer = null
                }

                // MediaPlayer에 사용할 음원의 Uri로 설정하고 시작 메서드를 호출.
                // 클릭시 음원이 플레이
                mediaPlayer = MediaPlayer.create(binding.root.context, musicUri)
                mediaPlayer?.start()
            }
        }

        // setMusic() 메서드의 파라미터로 넘어온 music은 메서드가 실행되는 순간에만 사용할 수 있음.
        fun setMusic(music:Music) {
            binding.run {
                // setImageUri를 사용해서 이미지를 세팅하고, 각각의 텍스트뷰, Artist, Title, duration의 text속성에도 값을 입력.
                imageAlbum.setImageURI(music.getAlbumUri())
                textArtist.text = music.artist
                textTitle.text = music.title

                // 음악 재생 시간은 SimpleDateFormat을 사용해서 '분:초' 형태로 변환해서 사용.
                val duration = SimpleDateFormat("mm:ss").format(music.duration)
                textDuration.text = duration
            }
            this.musicUri = music.getMusicUri()
        }



    }

}

