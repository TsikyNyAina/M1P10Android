package com.example.projetm1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.projetm1.modele.Event;
import com.example.projetm1.modele.Media;
import com.example.projetm1.service.ApiService;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class detailActivity extends AppCompatActivity {

    private TextView writer;
    private TextView createdTime;
    private TextView description;
    LinearLayout conteneurFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        writer = findViewById(R.id.text_user_name);
        createdTime = findViewById(R.id.text_created_date);
        description = findViewById(R.id.text_description);
        conteneurFile = findViewById(R.id.file);

        Intent intent = getIntent();
        if (intent != null) {

            Event event = (Event) intent.getSerializableExtra("event");

            Log.e("event",event.getDescription());
            Log.e("user", event.getUser().getUsername());

            writer.setText(event.getUser().getUsername());
            createdTime.setText(event.getCreatedAt().toString());
            description.setText(event.getDescription());

            List<Media> listeMedia = event.getMedia();

            for(int j=0;j<listeMedia.size();j++) {
                Media media = listeMedia.get(j);
                Log.e("media", media.getFileInfo().getPath());
                String filename = media.getFileInfo().getFilename();
                String path = "http://192.168.88.245:3000/uploads/"+filename;
                if(hasImageExtension(filename)){
                    ImageView imageView = new ImageView(this);
                    Picasso.get().load(path)
                            .placeholder(R.drawable.ic_menu_camera)
                            .error(R.drawable.ic_baseline_broken_image_24)
                            .into(imageView);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
                    layoutParams.setMargins(16, 16, 16, 16);
                    imageView.setLayoutParams(layoutParams);
                    conteneurFile.addView(imageView);
                }

                if(hasVideoExtension(filename)){
                    Log.e("video","video");
                    PlayerView videoView = new PlayerView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
                    layoutParams.setMargins(16, 16, 16, 16);
                    videoView.setLayoutParams(layoutParams);
                    SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();

                    // Bind the ExoPlayer to the PlayerView
                    videoView.setPlayer(player);

                    // Prepare the media item for the video
                    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(path));
                    player.setMediaItem(mediaItem);

                    // Prepare the player
                    player.prepare();
                    conteneurFile.addView(videoView);

                }
            }
        }else{
            onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Appeler la méthode onBackPressed() pour effectuer le retour
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean hasVideoExtension(String inputString) {
        String pattern = ".*\\.(mp4|avi|mov|mkv|wmv|flv|webm|3gp)$";
        return inputString.toLowerCase().matches(pattern);
    }

    public boolean hasImageExtension(String inputString) {
        String pattern = ".*\\.(jpg|jpeg|png|gif|bmp|webp|svg)$";
        return inputString.toLowerCase().matches(pattern);
    }
}