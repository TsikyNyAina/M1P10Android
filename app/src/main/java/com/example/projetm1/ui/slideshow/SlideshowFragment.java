package com.example.projetm1.ui.slideshow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projetm1.MainActivity;
import com.example.projetm1.R;
import com.example.projetm1.apiManager.ApiManager;
import com.example.projetm1.databinding.FragmentSlideshowBinding;
import com.example.projetm1.modele.Event;
import com.example.projetm1.modele.Media;
import com.example.projetm1.modele.User;
import com.example.projetm1.service.ApiService;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private static final int REQUEST_MEDIA_PICK = 1;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 101;
    private static final int RESULT_OK = -1;

    private Button select;
    private Button valider;
    private TextInputEditText description;
    private LinearLayout imagecontenair;
    private LinearLayout videocontenair;
    List<Uri> listeMedia;

    private ApiService apiService;
    private AlertDialog alertdialog;
    private ProgressDialog progressDialog;

    public SharedPreferences sharedpreferences;
    public static final String SHARED_PREFS = "shared_prefs";

    private int nbMedia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If permission is not granted, request it
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }

        description = binding.textInputLayout;
        select = binding.buttonSelect;

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, REQUEST_MEDIA_PICK);
            }
        });
        imagecontenair = binding.imageContainer;
        videocontenair =binding.videoContainer;

        valider = binding.buttonValider;


        listeMedia = new ArrayList<>();

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!description.getText().toString().equalsIgnoreCase("")){
                    apiService = ApiManager.getApiService().create(ApiService.class);

                    Event event = new Event();
                    event.setDescription(description.getText().toString());
                    Long idUser = sharedpreferences.getLong("idUser",0);
                    event.setUserId(idUser);

                    createEvent(event);
                }else{
                    Toast.makeText(getContext(), "Veuillez remplir le champ description", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_MEDIA_PICK && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                // Handle the selected image or video URI
                String mediaType = getContext().getContentResolver().getType(uri);
                listeMedia.add(uri);

                if (mediaType != null && mediaType.startsWith("image")) {
                    // Afficher l'image
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageURI(uri);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
                    layoutParams.setMargins(16, 16, 16, 16);
                    imageView.setLayoutParams(layoutParams);
                    imagecontenair.addView(imageView);
                } else if (mediaType != null && mediaType.startsWith("video")) {
                    // Afficher la vidéo


                    PlayerView videoView = new PlayerView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
                    layoutParams.setMargins(16, 16, 16, 16);
                    videoView.setLayoutParams(layoutParams);
                    SimpleExoPlayer player = new SimpleExoPlayer.Builder(getContext()).build();

                    // Bind the ExoPlayer to the PlayerView
                    videoView.setPlayer(player);

                    // Prepare the media item for the video
                    MediaItem mediaItem = MediaItem.fromUri(uri);
                    player.setMediaItem(mediaItem);

                    // Prepare the player
                    player.prepare();

                    imagecontenair.addView(videoView);
                }
            }
        }
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Action granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle this case accordingly
            }
        }
    }


    private void createEvent(Event event) {
        showProgressDialog();
        Call<Event> call = apiService.createEvent(event);


        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                if(response.isSuccessful()){
                    Event event = response.body();
                    if(listeMedia.size()==0){
                        String title = getResources().getString(R.string.insertion_title);
                        String message = getResources().getString(R.string.insertion_success);
                        Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_done_24);
                        alertdialog = message(title,message,icon);
                        alertdialog.show();
                        hideProgressDialog();
                        //enlever les images et videos de l'interface
                        imagecontenair.removeAllViewsInLayout();
                    }else{
                        nbMedia = 0;

                        for(Uri uri : listeMedia){
                            insertMedia(uri, event.getId());
                        }

                        new CountDownTimer(5000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                Log.e("milisecond", millisUntilFinished+" "+listeMedia.size()+ " "+nbMedia);
                            }

                            public void onFinish() {
                                if(nbMedia == listeMedia.size()){
                                    String title = getResources().getString(R.string.insertion_title);
                                    String message = getResources().getString(R.string.insertion_success);
                                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_done_24);
                                    alertdialog = message(title,message,icon);
                                    alertdialog.show();
                                    hideProgressDialog();
                                    //enlever les images et videos de l'interface

                                    imagecontenair.removeAllViewsInLayout();
                                }else {
                                    this.start();
                                }
                            }

                        }.start();
                    }
                }else{
                    Log.e("erreur 1", response.errorBody().toString());
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.insertion_error);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                    hideProgressDialog();
                }



            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {

                Log.e("erreur" , t.getMessage());
                String messageErreur = t.getMessage();
                hideProgressDialog();
                assert messageErreur != null;
                if(messageErreur.contains("connect")){
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_net);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_no_internet);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }else{
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.insertion_error);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }
            }
        });
    }

    // Call this method to show the ProgressDialog
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        String string = getResources().getString(R.string.traitement);
        progressDialog.setMessage(string); // Set your desired message
        progressDialog.setCancelable(false); // Set whether the dialog can be canceled by pressing back button
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

    public void insertMedia(Uri uri, Long eventId){
        File file = new File(getPathFromUri(uri));
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        //RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(uri)), file);

        // Create a MultipartBody.Part instance using the RequestBody and the field name "file"
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                eventId.toString());
        Call<Media> call = apiService.createMedia(filePart, id);

        call.enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if(response.isSuccessful()){
                    nbMedia = nbMedia +1;

                }else{
                    Log.e("erreur 1 media", response.errorBody().toString());
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.insertion_error);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                    hideProgressDialog();
                    nbMedia = nbMedia +1;
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                Log.e("erreur Media" , t.getMessage());
                String messageErreur = t.getMessage();
                hideProgressDialog();
                nbMedia = nbMedia +1;
                assert messageErreur != null;
                if(messageErreur.contains("connect")){
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_net);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_no_internet);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }else{
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.insertion_error);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }
            }
        });
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }

        return uri.getPath(); // Fallback to the original URI if cursor is null
    }
}