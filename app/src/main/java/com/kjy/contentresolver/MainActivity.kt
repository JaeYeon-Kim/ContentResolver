package com.kjy.contentresolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjy.contentresolver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // storagePermission 런처 선언
    lateinit var storagePermission: ActivityResultLauncher<String>

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // registerForActivityResult런처를 storagePermission에 저장 하는 메서드
        storagePermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            // true면 startProcess() 호출 하고 false면 앱 종료
            if (isGranted) {
                startProcess()
            } else {
                    Toast.makeText(baseContext, "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }
    // 권한 요청 승인시 호출되는 실행 메서드
    fun startProcess() {
        // 생성한 어댑터 화면 데이터를 가져오는 메서드를 연결하는 코드 작성.
        val adapter = MusicRecyclerAdapter()
        adapter.musicList.addAll(getMusicList())

        // 데이터가 담긴 adapter를 리사이클러뷰에 연결하고 레이아웃 매니저를 설정
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // 음원을 읽어오는 메서드
    fun getMusicList(): List<Music> {
        // 음원 정보의 주소를 listUrl 변수에 저장.
        val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        // 음원 정보 테이블에서 읽어올 컬럼명을 배열로 정의.
        // MediaStore에 상수로 이미 정의되어 있음.
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
        )
        // 콘텐트 리졸버의 query() 메서드 앞에서 설정한 주소와 컬럼명을 담아서 호출하면 실행결과를 커서로 반환해줌.
        // 세번째, 네번째, 다섯번째 파라미터는 쿼리에 조건을 설정하는 옵션. 'null'을 입력하면 전체 데이터를 읽어옴.
        val cursor = contentResolver.query(listUrl, proj, null, null, null)

        // 커서로 전달받은 데이터를 꺼내서 저장할 목록 변수를 하나 만듬.
        val musicList = mutableListOf<Music>()

        /*
          반복문으로 커서를 이동하면서 데이터를 한줄씩 읽음. 읽은 데이터를 Music 클래스에 옮긴 후 앞에
          서 만들어둔 musicList에 하나씩 담음.

          getLong()일 때는 컬럼 타입이 숫자일 때 사용 가능.
          getString()과 getLong()에 입력되는 숫자는 커서에 있는 컬럼 데이터의 순서인데 앞에서 proj 변수에 저장해두었던
          컬럼의 순서와 같음.
         */
        while (cursor?.moveToNext() == true) {
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val albumId = cursor.getString(3)
            val duration = cursor.getLong(4)

            val music = Music(id, title, artist, albumId, duration)
            musicList.add(music)
        }
        return musicList
    }
}