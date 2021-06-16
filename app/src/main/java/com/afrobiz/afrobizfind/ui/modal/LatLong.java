package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LatLong implements Serializable
{
    @SerializedName("lat")
    @Expose
    private Double latitude;
    @SerializedName("long")
    @Expose
    private Double longitude;

    @SerializedName("last50")
    @Expose
    private int last50;

    public int getLast50()
    {
        return last50;
    }

    public void setLast50(int last50)
    {
        this.last50 = last50;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
