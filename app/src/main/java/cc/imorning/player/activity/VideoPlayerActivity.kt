package cc.imorning.player.activity

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import cc.imorning.player.databinding.ActivityMainBinding
import cc.imorning.player.databinding.ActivityVideoBinding
import cc.imorning.player.player.video.IJKCorePlayer
import cn.jzvd.JzvdStd
import java.io.File

class VideoPlayerActivity : Activity() {

    private lateinit var binding: ActivityVideoBinding

    private val rootPath = Environment.getExternalStorageDirectory().path.plus(File.separator)

    private val audioFile: String = rootPath.plus("audio.mp3")
    private val videoFile: String = rootPath.plus("DayNight.mp4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoPlayer = binding.mainVideoPlayer
        videoPlayer.setUp(videoFile, videoFile, JzvdStd.FULLSCREEN_ORIENTATION, IJKCorePlayer::class.java)
        videoPlayer.startVideo()
    }


    companion object {
        private const val TAG = "VideoPlayerActivity"
    }
}