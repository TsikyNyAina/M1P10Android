package com.example.disign.model;

import android.net.Uri;

import okhttp3.MultipartBody;

public class MediaPart {
    private Uri uri;
    private MultipartBody.Part part;

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }

    public Uri getUri() {
        return uri;
    }

    public MultipartBody.Part getPart() {
        return part;
    }
    private String webUri;

    public void setWebUri(String webUri) {
        this.webUri = webUri;
    }

    public String getWebUri() {
        return webUri;
    }
}
