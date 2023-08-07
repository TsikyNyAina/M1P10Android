package com.example.disign.component.chooseFile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.disign.R;
import com.example.disign.component.imageView.SelectableImageView;
import com.example.disign.model.MediaPart;
import com.example.disign.util.UriToMultipartPartConverter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

public class ChooseFile extends Fragment {
    private LinearLayout imageContainer;
    private String type;
    private static final int PICK_FILE_REQUEST_CODE = 1001; // Code de requête pour la sélection du fichier
    private List<MediaPart> mediaPartList= new ArrayList<>();
    private ChooseFileViewModel mViewModel;

    public static ChooseFile newInstance(String type) {
        return new ChooseFile(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<MediaPart> getMediaPartList() {
        return mediaPartList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_choose_file, container, false);

        AppCompatImageButton btnChooseFile = rootView.findViewById(R.id.chooseFile);



        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        imageContainer = rootView.findViewById(R.id.imageContainer);

        LinearLayout addUri=rootView.findViewById(R.id.webUriInput);
        addUri.setOnClickListener(event->{
            AlertDialog.Builder builder=openCustomDialog(rootView);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Handle the user input when OK is clicked
                String userInput = editTextCustom.getText().toString();
                MediaPart mediaPart=new MediaPart();
                mediaPart.setWebUri(userInput);
                this.getMediaPartList().add(mediaPart);

                SelectableImageView customImageView=new SelectableImageView();
                customImageView.setMedia(mediaPart);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(imageContainer.getId(),customImageView );
                fragmentTransaction.commit();

                customImageView.setCloseButtonListener((e)->{
                    this.getMediaPartList().remove(mediaPart);
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.remove(customImageView);
                    fragmentTransaction2.commit();

                });



            });



            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Handle cancel, if needed

                dialog.dismiss();
            });




            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });






        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChooseFileViewModel.class);
        // TODO: Use the ViewModel


    }
    private void chooseFile() {
        // Créez un intent pour ouvrir le sélecteur de fichiers
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(this.getType());
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Lancez l'activité pour choisir le fichier
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedFileUri = data.getData();

            MediaPart mediaPart=new MediaPart();
            mediaPart.setUri(selectedFileUri);
            MultipartBody.Part t= UriToMultipartPartConverter.convertToMultipartPart( this.getContext(),selectedFileUri,"file");
            mediaPart.setPart(t);

            this.getMediaPartList().add(mediaPart);
            SelectableImageView customImageView=new SelectableImageView();
            customImageView.setMedia(mediaPart );
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(imageContainer.getId(),customImageView );
            fragmentTransaction.commit();

            customImageView.setCloseButtonListener((event)->{
                this.getMediaPartList().remove(mediaPart);
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                fragmentTransaction2.remove(customImageView);
                fragmentTransaction2.commit();

            });
        }
    }
    public ChooseFile(String type){
        this.setType(type);
    }


    private EditText editTextCustom;
    public AlertDialog.Builder openCustomDialog(View view) {
        // Inflate the custom dialog layout

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.customdialogue, null);

        // Initialize the EditText in the custom dialog layout
        EditText editTextCustom = dialogView.findViewById(R.id.dialogueTextInput);
        this.editTextCustom=editTextCustom;
        // Create an AlertDialog to show the custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(dialogView);
        builder.setTitle("Ajouter un URL"); // Set the dialog title


        return builder;
    }








}




