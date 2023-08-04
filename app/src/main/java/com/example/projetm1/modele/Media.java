package com.example.projetm1.modele;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Media implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("fileInfo")
    @Expose
    private FileInfo fileInfo;

    @SerializedName("eventId")
    @Expose
    private long eventId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

}
