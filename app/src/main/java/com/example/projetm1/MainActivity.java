package com.example.projetm1;

import androidx.appcompat.app.AlertDialog;
import androidx.core.splashscreen.SplashScreen;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetm1.apiManager.ApiManager;
import com.example.projetm1.modele.User;
import com.example.projetm1.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private ImageButton settingButton;
    private TextView btnCreerCompte;
    private Button btnConnexion;
    private EditText nomEdit;
    private EditText passwordedit;
    private TextView erreur;
    private AlertDialog alertdialog;

    private ApiService apiService;

    public SharedPreferences sharedpreferences;
    public static final String SHARED_PREFS = "shared_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Long idUser = sharedpreferences.getLong("idUser",0);
        String nomUser = sharedpreferences.getString("nameUser","");

        if(idUser!=0 && !nomUser.equalsIgnoreCase("")){
            Intent loginActivityIntent = new Intent(MainActivity.this, ListeActivity.class);
            startActivity(loginActivityIntent);
            finish();
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        nomEdit = findViewById(R.id.nomEditText);
        passwordedit = findViewById(R.id.passwordEdittext);
        erreur = findViewById(R.id.errorConnect);

        btnCreerCompte = findViewById(R.id.btnCreateAccount);
        btnCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(MainActivity.this, InscriptionActivity.class);
                startActivity(refresh);
                finish();
            }
        });

        settingButton = findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(refresh);
            }
        });

        btnConnexion = findViewById(R.id.connectBtn);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((TextUtils.isEmpty(nomEdit.getText().toString()))
                        || (TextUtils.isEmpty(passwordedit.getText().toString()))){
                    String myString = getResources().getString(R.string.champ_vide);
                    Toast.makeText(MainActivity.this, myString, Toast.LENGTH_SHORT).show();
                }else{
                    apiService = ApiManager.getApiService().create(ApiService.class);
                    String nom = nomEdit.getText().toString().trim();
                    String password = passwordedit.getText().toString().trim();
                    User user = new User(nom, password);

                    showProgressDialog();

                    //fonction chercher user
                    Log.e("option", "option");
                    getOneUser(user);
                }
            }
        });
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

    private void getOneUser(User user) {
        String option = "{\"where\":{\"username\":\""+user.getUsername()+"\",\"password\":\""+user.getPassword()+"\"},\"relations\":[]}";


        Call<List<User>> call = apiService.getOneUser(option);


        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                hideProgressDialog();
                if(response.isSuccessful()){
                    List<User> listeuser = response.body();
                    User user1 = listeuser.get(0);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putLong("idUser", user1.getId());
                    editor.putString("nameUser", user1.getUsername());
                    editor.apply();
                    Intent loginActivityIntent = new Intent(MainActivity.this, ListeActivity.class);
                    startActivity(loginActivityIntent);
                    finish();

                }else{
                    erreur.setVisibility(View.VISIBLE);
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.connexion_error);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }



            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                hideProgressDialog();
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
                    erreur.setVisibility(View.VISIBLE);
                    String title = getResources().getString(R.string.title);
                    String message = getResources().getString(R.string.connexion_message);
                    Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    alertdialog = message(title,message,icon);
                    alertdialog.show();
                }
            }
        });
    }

    private AlertDialog message(String title, String message, Drawable icon){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        MainActivity.this.alertdialog.cancel();
                    }
                });
        return builder.create();
    }

}