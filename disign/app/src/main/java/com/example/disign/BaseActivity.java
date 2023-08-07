package com.example.disign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.disign.helper.LocaleHelper;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Locale currentLocale = getResources().getConfiguration().locale;
        String appLocale = LocaleHelper.getLanguage(this);

        Log.i("app now",appLocale);
        Log.i("current activity", currentLocale.toString());

        if(!currentLocale.toString().equalsIgnoreCase(appLocale)){
            Intent intent = this.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.finish();
            startActivity(intent);
        }
    }

}
