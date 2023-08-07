package com.example.disign;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.disign.component.Event.CreateEvent;
import com.example.disign.model.Media;
import com.example.disign.util.UriToMultipartPartConverter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.disign.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private AlertDialog alertdialog;

    public SharedPreferences sharedpreferences;
    public static final String SHARED_PREFS = "shared_prefs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //add CreateEventFragment//Controller

        sharedpreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);






        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent refresh = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(refresh);
            return true;
        }
        else if(item.getItemId() == R.id.action_log_out){
            alertdialog = confirmLogOut();
            alertdialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private AlertDialog confirmLogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        String title = getResources().getString(R.string.action_logout);
        String confirmation = getResources().getString(R.string.log_out_confirmation);
        String message = getResources().getString(R.string.log_out_message);
        String btnAnnuler = getResources().getString(R.string.btn_annuler);
        builder.setTitle(title)
                .setMessage(confirmation)
                .setPositiveButton(title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        MainActivity.this.alertdialog.cancel();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear().apply();
                        //Redirect to view login
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginActivityIntent);
                        finish();
                    }
                })
                .setNegativeButton(btnAnnuler, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.alertdialog.cancel();
                    }
                });
        return builder.create();
    }
}