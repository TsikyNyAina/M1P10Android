package com.example.disign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disign.model.User;
import com.example.disign.service.ApiManager;
import com.example.disign.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends BaseActivity {

    private TextView btnConnexion;
    private EditText editNom;
    private EditText editPassword;
    private TextView erreur;
    private Button inscription;
    private AlertDialog alertdialog;

    private ApiService apiService;

    public SharedPreferences sharedpreferences;
    public static final String SHARED_PREFS = "shared_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        btnConnexion = findViewById(R.id.btnConnexion);

        erreur = findViewById(R.id.error_inscription);

        editNom = findViewById(R.id.nom_inscription);
        editPassword = findViewById(R.id.mdp_inscription);
        inscription = findViewById(R.id.inscription_button);

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((TextUtils.isEmpty(editNom.getText().toString()))
                        || (TextUtils.isEmpty(editPassword.getText().toString()))){
                    String myString = getResources().getString(R.string.champ_vide);
                    Toast.makeText(InscriptionActivity.this, myString, Toast.LENGTH_SHORT).show();
                }else{
                    String nom = editNom.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();
                    User user = new User(nom, password);

                    showProgressDialog();
                    createUSer(user);
                }
            }
        });


        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(InscriptionActivity.this, LoginActivity.class);
                startActivity(refresh);
                finish();
            }
        });
    }

    private void createUSer(User user) {
        Call<User> call = ApiManager.apiService.createUser(user);


        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressDialog();
                if(response.isSuccessful()){
                    String title = getResources().getString(R.string.success_title);
                    String message = getResources().getString(R.string.success_message);
                    User user1 = response.body();
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_done_24);
                    alertdialog = message(title,message,icon,1, user1);
                    alertdialog.show();
                }else{
                    erreur.setVisibility(View.VISIBLE);
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_message);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon,0, null);
                    alertdialog.show();
                }



            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressDialog();
                Log.e("erreur" , t.getMessage());
                String messageErreur = t.getMessage();
                hideProgressDialog();
                assert messageErreur != null;
                if(messageErreur.contains("connect")){
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_net);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_no_internet);
                    alertdialog = message(title,message,icon,0, null);
                    alertdialog.show();
                }else{
                    erreur.setVisibility(View.VISIBLE);
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.erreur_message);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon,0,null);
                    alertdialog.show();
                }
            }
        });
    }

    private AlertDialog message(String title, String message, Drawable icon, int type,User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        InscriptionActivity.this.alertdialog.cancel();
                        if(type==1){
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putLong("idUser", user.getId());
                            editor.putString("nameUser", user.getUsername());
                            editor.apply();
                            Intent loginActivityIntent = new Intent(InscriptionActivity.this, MainActivity.class);
                            startActivity(loginActivityIntent);
                            finish();
                        }

                    }
                });
        return builder.create();
    }

    private ProgressDialog progressDialog;

    // Call this method to show the ProgressDialog
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
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
}