package com.example.disign.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.disign.service.ApiManager;
import com.example.disign.util.CallBackToObservableConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Call;

public class Event implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("userId")
    @Expose
    private long userId;
    private User user;

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("titre")
    @Expose
    private  String titre;
    @SerializedName("lieu")
    @Expose
    private  String lieu;

    @JsonIgnore
    private List<Media> media=new ArrayList<>();

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLieu() {
        return lieu;
    }

    public String getTitre() {
        return titre;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @JsonIgnore
    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public List<Media> getMedia() {
        return media;
    }

    public User getUser() {
        return user;
    }

    @SuppressLint("CheckResult")
    public Observable<Event> save(Context context){
        Call<Event> call = ApiManager.apiService.createEvent(this);


        Observable<Event> observable= CallBackToObservableConverter.getObservable(call).map((e)->{
            this.setId(e.getId());
            return e;
        });







        return Observable.create((emitter)->{
            observable.subscribe((event)->{
                if(getMedia().size()>0){
                    List<Observable> mediaEvent= getMedia().stream().map((m)->{
                        m.setEvent (this );
                        return m.save(context);
                    }).collect(Collectors.toList());


                    Observable last =Observable.concatArray(mediaEvent.toArray(new Observable[mediaEvent.size()]));
                    last.subscribe((media)->{
                        emitter.onNext(this);
                        emitter.onComplete();
                    });
                    return ;
                }
                else{
                    emitter.onNext(event);
                    emitter.onComplete();
                }
            });
        });





    }




}
