package com.example.disign.component.web;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.disign.R;
import com.example.disign.component.video.VideoFragment;

public class WebFragment extends Fragment {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public WebFragment(String url) {
        this.setUrl(url);
    }

    public static WebFragment newInstance(String url) {
        return new WebFragment(url);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_web, container, false);
        WebView webView=root.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(this.getUrl());
        System.out.println(url);
        return root;
    }
}