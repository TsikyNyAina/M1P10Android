package com.example.projetm1.modele;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Event implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("createdAt")
    @Expose
    Timestamp createdAt;

    @SerializedName("deletedAt")
    @Expose
    Timestamp deletedAt;

    @SerializedName("userId")
    @Expose
    Long userId;

    @SerializedName("user")
    @Expose
    User user;

    @SerializedName("media")
    @Expose
    List<Media> media;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
