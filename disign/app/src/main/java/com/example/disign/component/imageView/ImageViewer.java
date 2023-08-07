package com.example.disign.component.imageView;
import android.widget.ImageView;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.disign.R;

public class ImageViewer extends Fragment {


    private Uri uri;

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public ImageViewer(Uri uri) {
        setUri(uri);
    }


    public static ImageViewer newInstance( Uri uri ) {
        return  new ImageViewer(uri);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_image_viewer, container, false);
        ImageView imageView=root.findViewById(R.id.imageContnent);
        Glide.with(this)
                .load(this.getUri()) .into(imageView);

        return root;
    }
}