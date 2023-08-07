package com.example.disign.component.video;

import android.media.AudioDeviceInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.disign.R;

import java.util.List;

public class VideoFragment extends Fragment {
    private Uri uri;

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public VideoFragment(Uri uri) {
        this.setUri(uri);
    }

    public static VideoFragment newInstance(Uri uri) {
        return new VideoFragment(uri);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_video, container, false);
        PlayerView imageView=root.findViewById(R.id.playerView);
        Player player = new ExoPlayer.Builder(this.getContext()).build();

        MediaItem mediaItem =MediaItem.fromUri(uri);
        player.addMediaItem(mediaItem);
        player.prepare();
        imageView.setPlayer(player);

        return root;
    }
}