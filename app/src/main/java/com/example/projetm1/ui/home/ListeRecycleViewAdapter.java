package com.example.projetm1.ui.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetm1.R;
import com.example.projetm1.modele.Event;
import com.example.projetm1.modele.Media;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListeRecycleViewAdapter extends RecyclerView.Adapter<ListeRecycleViewAdapter.Holder> {

    ListRecycleViewIterface listRecycleViewIterface;
    Context context;
    List<Event> eventList;

    public ListeRecycleViewAdapter(Context context, List<Event> eventList, ListRecycleViewIterface listRecycleViewIterface){
        this.context = context;
        this.eventList = eventList;
        this.listRecycleViewIterface = listRecycleViewIterface;
    }

    @NonNull
    @Override
    public ListeRecycleViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_liste_row, parent, false);
        return new ListeRecycleViewAdapter.Holder(view, listRecycleViewIterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ListeRecycleViewAdapter.Holder holder, int position) {
        Event event = eventList.get(position);
        holder.autheur.setText(event.getUser().getUsername());
        holder.createdDate.setText(event.getCreatedAt().toString());
        holder.description.setText(event.getDescription());

        List<Media> mediaList = event.getMedia();
        if(mediaList.size()>0){
            Media media = mediaList.get(0);

            String filename = media.getFileInfo().getFilename();
            String url = "https://m1p10androidnode.onrender.com/uploads/" + filename;
            if (hasImageExtension(filename)) {
                holder.playerView.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                Picasso.get().load(url)
                        .placeholder(R.drawable.ic_menu_camera)
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .into(holder.imageView);

            } else if (hasVideoExtension(filename)) {
                holder.imageView.setVisibility(View.GONE);
                holder.playerView.setVisibility(View.VISIBLE);
                SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();

                // Bind the ExoPlayer to the PlayerView
                holder.playerView.setPlayer(player);

                // Prepare the media item for the video
                MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
                player.setMediaItem(mediaItem);

                // Prepare the player
                player.prepare();


            }
        }else{
            holder.imageView.setVisibility(View.GONE);
            holder.playerView.setVisibility(View.GONE);
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
        return eventList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        TextView autheur;
        TextView createdDate;
        TextView description;
        ImageView imageView;
        PlayerView playerView;

        public Holder(@NonNull View itemView, ListRecycleViewIterface listRecycleViewIterface) {
            super(itemView);
            autheur = itemView.findViewById(R.id.autheur_event);
            createdDate = itemView.findViewById(R.id.date_event);
            description = itemView.findViewById(R.id.description_event);
            imageView = itemView.findViewById(R.id.imageView_list);
            playerView = itemView.findViewById(R.id.videoPlayerView_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listRecycleViewIterface !=null){
                        int pos = getBindingAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listRecycleViewIterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
