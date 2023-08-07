package com.example.disign.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disign.R;
import com.example.disign.model.Media;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

// ... Your imports ...

public class GalleryRecycleViewAdapter extends RecyclerView.Adapter<GalleryRecycleViewAdapter.MyViewHolder> {

    Context context;
    List<Media> mediaList;

    List<String> listeMedia;

    /*public GalleryRecycleViewAdapter(Context context, List<Media> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }*/

    public GalleryRecycleViewAdapter(Context context, List<String> mediaList) {
        this.context = context;
        this.listeMedia = mediaList;
    }

    @NonNull
    @Override
    public GalleryRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_gallery_row, parent, false);
        return new GalleryRecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryRecycleViewAdapter.MyViewHolder holder, int position) {
        /*Media media = mediaList.get(position);*/
        /*String filename = media.getFileInfo().getFilename();*/

        String filename = listeMedia.get(position);


        String url = "https://m1p10androidnode.onrender.com/uploads/" + filename;

        if (hasImageExtension(filename)) {
            holder.videoView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(url)
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(holder.imageView);

        } else if (hasVideoExtension(filename)) {
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);

            Player player = new ExoPlayer.Builder(context).build();
            // Bind the ExoPlayer to the PlayerView
            holder.videoView.setPlayer(player);

            // Prepare the media item for the video
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
            player.setMediaItem(mediaItem);

            // Prepare the player
            player.prepare();


            holder.videoView.setPlayer(player);


        }
    }

    public boolean hasVideoExtension(String inputString) {
        String pattern = ".*\\.(mp4|avi|mov|mkv|wmv|flv|webm|3gp)$";
        return inputString.toLowerCase().matches(pattern);
    }

    public boolean hasImageExtension(String inputString) {
        String pattern = ".*\\.(jpg|jpeg|png|gif|bmp|webp|svg)$";
        return inputString.toLowerCase().matches(pattern);
    }

    @Override
    public int getItemCount() {
        return listeMedia.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        PlayerView videoView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            videoView = itemView.findViewById(R.id.videoPlayerView);
        }
    }
}