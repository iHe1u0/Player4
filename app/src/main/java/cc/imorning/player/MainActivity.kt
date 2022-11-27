package cc.imorning.player

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import cc.imorning.player.databinding.ActivityMainBinding
import cc.imorning.player.player.audio.mp3.Mp3Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mp3Player: Mp3Player

    private val rootPath = Environment.getExternalStorageDirectory().path.plus(File.separator)
    private val audioFile: String = rootPath.plus("audio.mp3")
    private val videoFile: String = rootPath.plus("video.mp4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sampleText.text = stringFromJNI()
        val mp3Exist = File(audioFile).exists()
        Log.i(TAG, "onCreate: $mp3Exist")
        if (mp3Exist) {
            mp3Player = Mp3Player()
            MainScope().launch(Dispatchers.IO) {
                mp3Player.playAudio(audioFile)
            }
        }

    }

    private external fun stringFromJNI(): String

    companion object {
        private const val TAG = "MainActivity"
    }
}