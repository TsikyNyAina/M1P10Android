package com.example.projetm1;

import android.app.Application;
import android.app.LocaleManager;
import android.content.Context;
import android.content.res.Configuration;

import com.example.projetm1.helper.LocaleHelper;

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
