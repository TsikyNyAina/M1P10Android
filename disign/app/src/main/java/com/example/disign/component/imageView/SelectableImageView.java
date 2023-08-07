package com.example.disign.component.imageView;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.disign.R;
import com.example.disign.model.MediaPart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectableImageView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectableImageView extends Fragment {

    private MediaPart media;
    private View.OnClickListener closeButtonListener;



    public void setCloseButtonListener(View.OnClickListener listener){
        this.closeButtonListener=listener;
    }


    public void setMedia(MediaPart media) {
        this.media = media;
    }

    public MediaPart getMedia() {
        return media;
    }

    public SelectableImageView() {
        // Required empty public constructor
    }

    public static SelectableImageView newInstance( ) {
        return new SelectableImageView();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_image_view, container, false);
        Button closeButton=rootView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this.closeButtonListener);

        ImageView imageView=rootView.findViewById(R.id.selectedImage);
        if(this.media.getUri()!=null){
            Glide.with(this)
                    .load(this.media.getUri()) .into(imageView);
        } else if (this.media.getWebUri()!=null) {
            String uri="https://static.vecteezy.com/system/resources/previews/000/381/075/original/html-vector-icon.jpg";

            Glide.with(this)
                    .load(uri) .into(imageView);
        }


        imageView.setImageDrawable(Drawable.createFromPath("/"));


        return rootView;
    }



}






