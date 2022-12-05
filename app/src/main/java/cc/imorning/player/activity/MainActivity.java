package cc.imorning.player.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.LinkedList;

import cc.imorning.player.R;
import cc.imorning.player.adapter.VideoAdapter;
import cc.imorning.player.beans.Video;
import cc.imorning.player.databinding.ActivityMainBinding;
import cc.imorning.player.utils.VideoScanner;

public class MainActivity extends Activity {

    private static final int VIDEO = 1;
    private static final int AUDIO = 2;

    private static final String TAG = "MainActivity";
    private LinkedList<Video> videos = new LinkedList<>();
    private ActivityMainBinding activityMainBinding;
    private VideoAdapter adapter;

    private final Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case VIDEO:
                    Log.i(TAG, "_scanner>>>" + Arrays.toString(videos.toArray()));
                    adapter.setVideos(videos);
                    adapter.notifyDataSetChanged();
                    activityMainBinding.videoList.setAdapter(adapter);
                    break;
                case AUDIO:
                    Log.i(TAG, "_scanner>>>" + Arrays.toString(videos.toArray()));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        adapter = new VideoAdapter(this, R.layout.video_item, videos);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        VideoScanner videoScanner = new VideoScanner(root);
        new Thread(() -> {
            videos = videoScanner.scanner();
            handler.sendEmptyMessage(VIDEO);
        }).start();
    }
}