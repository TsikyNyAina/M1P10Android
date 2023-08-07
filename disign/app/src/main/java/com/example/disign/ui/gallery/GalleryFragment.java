package com.example.disign.ui.gallery;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disign.R;

import com.example.disign.databinding.FragmentGalleryBinding;
import com.example.disign.service.ApiManager;
import com.example.disign.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    private AlertDialog alertdialog;
    private FragmentGalleryBinding binding;
    private ApiService apiService;
    RecyclerView recyclerView ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new GalleryRecycleViewAdapter(getContext(), new ArrayList<>()));

        getListeMedia();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getListeMedia(){
        //apiService = ApiManager.getApiService().create(ApiService.class);

        String option = "\"{\\\"where\\\":{},\\\"relations\\\":[]}\"";
        /*Call<List<Media>> call = apiService.getListeMedia(option);*/

        /*call.enqueue(new Callback<List<Media>>() {
            @Override
            public void onResponse(Call<List<Media>> call, Response<List<Media>> response) {
                if(response.isSuccessful()){
                    Log.e("media", "media");
                    List<Media> liste = response.body();
                    GalleryRecycleViewAdapter adapter = new GalleryRecycleViewAdapter(getContext(), liste);
                    recyclerView.setAdapter(adapter);

                    // Notify the RecyclerView that the dataset has changed
                    adapter.notifyDataSetChanged();
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<Media>> call, Throwable t) {
                Log.e("erreur" , t.getMessage());
                String messageErreur = t.getMessage();
                assert messageErreur != null;
                if(messageErreur.contains("connect")){
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_net);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_no_internet);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }else{
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.title);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }
            }
        });*/

        Call<List<String>> call = ApiManager.apiService.getFileListe();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    Log.e("media", "media");
                    List<String> liste = response.body();
                    GalleryRecycleViewAdapter adapter = new GalleryRecycleViewAdapter(getContext(), liste);
                    recyclerView.setAdapter(adapter);

                    // Notify the RecyclerView that the dataset has changed
                    adapter.notifyDataSetChanged();
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("erreur" , t.getMessage());
                String messageErreur = t.getMessage();
                assert messageErreur != null;
                if(messageErreur.contains("connect")){
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_net);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_no_internet);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }else{
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.title);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }
            }
        });
    }

    private AlertDialog message(String title, String message, Drawable icon){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        alertdialog.cancel();
                    }
                });
        return builder.create();
    }
}