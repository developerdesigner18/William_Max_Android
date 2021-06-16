package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable
{
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("eventname")
    @Expose
    private String eventname;

    @SerializedName("company_id")
    @Expose
    private int companyId;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("termscondition")
    @Expose
    private String termscondition;

    @SerializedName("organizer")
    @Expose
    private String organizer;

    @SerializedName("contactno")
    @Expose
    private String contactno;

    @SerializedName("max_no_ticket")
    @Expose
    private int maxNoTicket;

    @SerializedName("imagepath")
    @Expose
    private String imagepath;



    @SerializedName("flyerimage")
    @Expose
    private String flyerimage;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("eventimages")
    @Expose
    private List<EventImage> eventimages = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTermscondition() {
        return termscondition;
    }

    public void setTermscondition(String termscondition) {
        this.termscondition = termscondition;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public int getMaxNoTicket() {
        return maxNoTicket;
    }

    public void setMaxNoTicket(int maxNoTicket) {
        this.maxNoTicket = maxNoTicket;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getFlyerimage() {
        return flyerimage;
    }

    public void setFlyerimage(String flyerimage) {
        this.flyerimage = flyerimage;
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

    public List<EventImage> getEventimages() {
        return eventimages;
    }

    public void setEventimages(List<EventImage> eventimages) {
        this.eventimages = eventimages;
    }

}
