package com.example.projetm1.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.projetm1.MainActivity;
import com.example.projetm1.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetm1.apiManager.ApiManager;
import com.example.projetm1.databinding.FragmentHomeBinding;
import com.example.projetm1.detailActivity;
import com.example.projetm1.modele.Event;
import com.example.projetm1.modele.Media;
import com.example.projetm1.modele.User;
import com.example.projetm1.service.ApiService;
import com.example.projetm1.ui.gallery.GalleryRecycleViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ListRecycleViewIterface{

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;

    private ApiService apiService;
    private AlertDialog alertdialog;

    private List<Event> eventList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerViewList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new GalleryRecycleViewAdapter(getContext(), new ArrayList<>()));

        listeEvenement();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void listeEvenement() {
        apiService = ApiManager.getApiService().create(ApiService.class);
        String option = "{\"where\":{},\"relations\":[\"user\",\"media\"]}";
        Call<List<Event>> call = apiService.listeEvent(option);


        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                if(response.isSuccessful()){
                    List<Event> listEvent = response.body();
                    eventList = listEvent;
                    ListeRecycleViewAdapter adapter = new ListeRecycleViewAdapter(getContext(), listEvent, HomeFragment.this);
                    recyclerView.setAdapter(adapter);

                    // Notify the RecyclerView that the dataset has changed
                    adapter.notifyDataSetChanged();
                }else{
                    Log.e("erreur 1", response.errorBody().toString());
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_net);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_no_internet);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

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

                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,title,icon);
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

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), detailActivity.class);
        detailIntent.putExtra("event", eventList.get(position));
        Log.e("intent", "intent");
        startActivity(detailIntent);
    }
}