package cc.imorning.player;

import android.util.Log;

public class Jni {

    private static final String TAG = "JniLoader";

    static {
        final String[] libs = {
                "player",
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
