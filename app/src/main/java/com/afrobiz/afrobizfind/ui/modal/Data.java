package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data  implements Serializable
{
    @SerializedName("user")
    @Expose
    private Users user;

    public Users getUser()
    {
        return user;
    }

    public void setUser(Users user)
    {
        this.user = user;
    }

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("control_settings")
    @Expose
    private String controlSettings;
    @SerializedName("on_off")
    @Expose
    private int onOff;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getControlSettings() {
        return controlSettings;
    }

    public void setControlSettings(String controlSettings) {
        this.controlSettings = controlSettings;
    }

    public int getOnOff() {
        return onOff;
    }

    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }

}
