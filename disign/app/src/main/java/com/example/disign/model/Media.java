package com.example.disign.model;
import android.content.Context;
import android.net.Uri;

import com.example.disign.service.ApiManager;
import com.example.disign.util.CallBackToObservableConverter;
import com.example.disign.util.UriToMultipartPartConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Media implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("eventId")
    @Expose
    private long eventId;

    private Event event;

    private FileInfo fileInfo;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("webUrl")
    @Expose
    private String webUrl;

    private MultipartBody.Part part;

    public void setType(String type) {
        this.type = type;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public long getId() {
        return id;
    }

    public long getEventId() {
        return eventId;
    }

    public Event getEvent() {
        return event;
    }

    public MultipartBody.Part getPart() {
        return part;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }




    public Observable<Media> save(Context context )  {
        if(this.getPart()!=null){
            RequestBody eventId = RequestBody.create(MediaType.parse("text/plain"), this.getEvent().getId()+"");
            RequestBody type = RequestBody.create(MediaType.parse("text/plain"), this.getType()+"");
            Call <Media>call=ApiManager.apiService.createMedia(getPart(),eventId,type);


            Observable<Media> save=CallBackToObservableConverter.getObservable(call).map(media -> {
                this.setId(media.getId());
                return this;
            });
            return save;
        } else if (this.getWebUrl()!=null) {
            this.setEventId(this.getEvent().getId());
            this.setType("web");

            Media medias=new Media();
            medias.setType("web");
            medias.setEventId(this.getEvent().getId());
            medias.setWebUrl(this.getWebUrl());
            Call <Media>call=ApiManager.apiService.createMedia(medias);
            Observable<Media> save=CallBackToObservableConverter.getObservable(call).map(media -> {
                this.setId(media.getId());
                return this;
            });
            return save;
        }
        return null;




    }







}
