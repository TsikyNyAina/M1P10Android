package com.example.disign.component.Event;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.disign.R;
import com.example.disign.component.chooseFile.ChooseFile;
import com.example.disign.model.Event;
import com.example.disign.model.Media;
import com.example.disign.service.ApiManager;
import com.example.disign.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEvent extends Fragment {


    public static CreateEvent newInstance() {
        return new CreateEvent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_create_event, container, false);
        ChooseFile chooseFile=new ChooseFile("image/*");
        Button publishButton =rootView.findViewById(R.id.publishButton);
        TextInputEditText description=rootView.findViewById(R.id.description);
        TextInputEditText lieu=rootView.findViewById(R.id.lieu);
        TextInputEditText titre=rootView.findViewById(R.id.titre);



        publishButton.setOnClickListener ((event)-> {
            Event e=new Event();
            e.setDescription(description.getText().toString());
            e.setLieu(lieu.getText().toString());
            e.setTitre(titre.getText().toString());
            e.setUserId(5);





            List<Media> mediaList=chooseFile.getMediaPartList().stream().map((f)->{
                Media media=new Media();
                media.setPart(f.getPart());
                media.setWebUrl(f.getWebUri());
                return media;
            }).collect(Collectors.toList());;
            e.setMedia(mediaList);
            e.save(this.getContext()).subscribe((f)->{
                Toast.makeText(this.getContext(), "Event created Successfully", Toast.LENGTH_SHORT).show();
            });
            Navigation.findNavController(rootView).navigate(R.id.nav_home);




        });

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.container_fragment, chooseFile);
        transaction.commit();


        return rootView;
    }


}