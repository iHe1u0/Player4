package cc.imorning.player;

import android.util.Log;

public class Jni {

    private static final String TAG = "JniLoader";

    static {
        final String[] libs = {
                "player",
                "avfilter-7",
                "avformat-58",
                "avcodec-58",
                "swscale-5",
                "swresample-3",
                "postproc-55",
                "avutil-56",
                "avdevice-58"

        };
        for (String lib : libs) {
            try {
                System.loadLibrary(lib);
            } catch (Throwable throwable) {
                Log.e(TAG, "load library " + lib + " failed", throwable);
            }
        }
    }
}
