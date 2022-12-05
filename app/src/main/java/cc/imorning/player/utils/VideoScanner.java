package cc.imorning.player.utils;

import android.text.TextUtils;

import java.io.File;
import java.util.LinkedList;

import cc.imorning.player.beans.Video;

public class VideoScanner {


    private static final String TAG = "VideoScanner";
    private String rootPath;

    public VideoScanner(String root) {
        if (TextUtils.isEmpty(root)) {
            return;
        }
        this.rootPath = root;
    }

    public LinkedList<Video> scanner() {
        LinkedList<Video> videos = new LinkedList<>();
        File root = new File(rootPath);
        if (!root.exists()) {
            return null;
        }
        File[] files = root.listFiles();
        if (files == null || files.length <= 0) {
            return null;
        }
        for (File file : files) {
            if (!file.canRead()) {
                continue;
            }
            if (file.isFile()) {
                if (FileUtils.isVideoFile(file)) {
                    videos.add(new Video(file));
                }
            } else {
                VideoScanner videoScanner = new VideoScanner(file.getAbsolutePath());
                LinkedList<Video> _list = videoScanner.scanner();
                if (_list != null) {
                    for (Video video : _list) {
                        videos.addLast(video);
                    }
                }
            }
        }
        return videos;
    }
}
