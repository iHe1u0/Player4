package cc.imorning.player.utils;

import android.text.TextUtils;

import java.io.File;

public class FileUtils {

    private final static String[] VIDEO_TYPES = new String[]{".mp4", ".mkv", ".flv"};

    public static boolean isVideoFile(File file) {
        if (file.exists() && file.isFile()) {
            return isVideoFile(file.getAbsolutePath());
        }
        return false;
    }

    public static boolean isVideoFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        boolean isVideo = false;
        for (String ends : VIDEO_TYPES) {
            if (filePath.endsWith(ends)) {
                isVideo = true;
                break;
            }
        }
        return isVideo;
    }
}
