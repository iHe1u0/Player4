package cc.imorning.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

import cc.imorning.player.R;
import cc.imorning.player.beans.Video;

public class VideoAdapter extends ArrayAdapter<Video> {

    private final int resId;
    private LinkedList<Video> videos;

    public VideoAdapter(@NonNull Context context, int resource, @NonNull LinkedList<Video> videos) {
        super(context, resource, videos);
        this.resId = resource;
        this.videos = videos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resId, parent, false);
        TextView videoName = view.findViewById(R.id.videoName);
        Video video = getItem(position);
        videoName.setText(video.getVideoName());
        return view;
    }

    public void setVideos(LinkedList<Video> videos) {
        this.videos=videos;
    }
}
