package com.example.disign;

import android.content.Intent;
import android.os.Bundle;

import com.example.disign.service.SessionService;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.disign.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionService.getEventFromSource().subscribe((e)->{
            Intent intent = new Intent( this, MainActivity.class);
            startActivity(intent);
            finish();
        },error->{
            Toast.makeText(this,"ERROR",Toast.LENGTH_LONG);
        });


    }

}