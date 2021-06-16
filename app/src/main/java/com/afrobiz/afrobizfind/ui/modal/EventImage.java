package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventImage implements Serializable
{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("event_id")
    @Expose
    private int eventId;
    @SerializedName("eventimage")
    @Expose
    private String eventimage;
    @SerializedName("imagepath")
    @Expose
    private String imagepath;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventimage() {
        return eventimage;
    }

    public void setEventimage(String eventimage) {
        this.eventimage = eventimage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
