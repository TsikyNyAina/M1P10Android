package com.example.projetm1.modele;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FileInfo implements Serializable {
    @SerializedName("fieldname")
    @Expose
    String fieldname;

    @SerializedName("originalname")
    @Expose
    String originalname;

    @SerializedName("encoding")
    @Expose
    String encoding;

    @SerializedName("mimetype")
    @Expose
    String mimetype;

    @SerializedName("destination")
    @Expose
    String destination;

    @SerializedName("filename")
    @Expose
    String filename;

    @SerializedName("path")
    @Expose
    String path;

    @SerializedName("size")
    @Expose
    Long size;

    public String getFieldname() {
        return fieldname;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public String getDestination() {
        return destination;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }


}
