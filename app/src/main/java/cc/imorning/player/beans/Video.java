package cc.imorning.player.beans;

import java.io.File;

public class Video {

    private File videoFile;

    private Video() {
    }

    public Video(File videoFile) {
        if (videoFile.exists() && videoFile.isFile()) {
            this.videoFile = videoFile;
        }
    }

    public Video(String parent, String videoFileName) {
        this(new File(parent, videoFileName));
    }

    public String getVideoName() {
        return this.videoFile.getName();
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoFile=" + videoFile.getAbsolutePath() +
                '}';
    }
}
