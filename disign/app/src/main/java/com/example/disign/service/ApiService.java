package com.example.disign.service;

import com.example.disign.model.Event;
import com.example.disign.model.Media;
import com.example.disign.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("user")
    Call<User> createUser(@Body User user);

    @GET("user/{option}")
    Call<List<User>> getOneUser(@Path("option") String option);

    @GET("event/{option}")
    Call<List<Event>> listeEvent(@Path("option") String option);

    @Multipart
    @POST("media")
    Call<Media> createMedia(@Part MultipartBody.Part file, @Part("eventId") RequestBody eventId, @Part("type") RequestBody type);
    @POST("media/weburl")
    Call<Media> createMedia(@Body Media media);

    @POST("event")
    Call<Event> createEvent(@Body Event event);

    @GET("media/{option}")
    Call<List<Media>> getListeMedia(@Path("option") String option);

    @GET("media/fileList")
    Call<List<String>> getFileListe();
}
