package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Company implements Serializable
{

    @SerializedName("category_id")
    @Expose
    private int category_id;

    @SerializedName("company_number")
    @Expose
    private String company_number;

    @SerializedName("is_favourite")
    @Expose
    private int is_favourite ;


    @SerializedName("is_customer")
    @Expose
    private int is_customer ;


    @SerializedName("is_expiry")
    @Expose
    private int is_expiry;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("mobileallowed")
    @Expose
    private int mobileallowed;

    public int getMobileallowed() {
        return mobileallowed;
    }

    public void setMobileallowed(int mobileallowed) {
        this.mobileallowed = mobileallowed;
    }

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("building_number")
    @Expose
    private String building_number;

    @SerializedName("address_line_1")
    @Expose
    private String address_line_1;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("postcode")
    @Expose
    private String postcode;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("monday_opening")
    @Expose
    private String monday_opening;

    @SerializedName("monday_closing")
    @Expose
    private String monday_closing;

    @SerializedName("tuesday_opening")
    @Expose
    private String tuesday_opening;

    @SerializedName("tuesday_closing")
    @Expose
    private String tuesday_closing;

    @SerializedName("wednesday_opening")
    @Expose
    private String wednesday_opening;

    @SerializedName("wednesday_closing")
    @Expose
    private String wednesday_closing;

    @SerializedName("thursday_opening")
    @Expose
    private String thursday_opening;

    @SerializedName("thursday_closing")
    @Expose
    private String thursday_closing;

    @SerializedName("friday_opening")
    @Expose
    private String friday_opening;

    @SerializedName("friday_closing")
    @Expose
    private String friday_closing;

    @SerializedName("saturday_opening")
    @Expose
    private String saturday_opening;

    @SerializedName("saturday_closing")
    @Expose
    private String saturday_closing;

    @SerializedName("sunday_opening")
    @Expose
    private String sunday_opening;

    @SerializedName("sunday_closing")
    @Expose
    private String sunday_closing;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("applink")
    @Expose
    private String applink;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("long")
    @Expose
    private String longi;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("imagepath")
    @Expose
    private String company_image;


    @SerializedName("ethos")
    @Expose
    private String company_ethos;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("customer_number")
    @Expose
    private String customer_number;

    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    @SerializedName("activeoffers")
    @Expose
    private List<Offers> activeoffers = null;

    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    @SerializedName("category")
    @Expose
    private Category category;

    @SerializedName("totalfavorite")
    @Expose
    private int totalfavorite;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("paypal_nonce")
    @Expose
    private String paypal_nonce;

    @SerializedName("expiry_date")
    @Expose
    private String expiry_date;

    public int getIs_expiry() {
        return is_expiry;
    }

    public void setIs_expiry(int is_expiry) {
        this.is_expiry = is_expiry;
    }

    public String getPaypal_nonce() {
        return paypal_nonce;
    }

    public void setPaypal_nonce(String paypal_nonce) {
        this.paypal_nonce = paypal_nonce;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getCompany_number() {
        return company_number;
    }

    public void setCompany_number(String company_number) {
        this.company_number = company_number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalfavorite() {
        return totalfavorite;
    }

    public void setTotalfavorite(int totalfavorite) {
        this.totalfavorite = totalfavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_favourite() {
    return is_favourite;
}

    public void setIs_favourite(int is_favourite) {
        this.is_favourite = is_favourite;
    }

    public int getIs_customer() {
        return is_customer;
    }

    public void setIs_customer(int is_customer) {
        this.is_customer = is_customer;
    }

    public String getCompany_image() {
        return company_image;
    }

    public void setCompany_image(String company_image) {
        this.company_image = company_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(String customer_number) {
        this.customer_number = customer_number;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getCompany_ethos() {
        return company_ethos;
    }

    public void setCompany_ethos(String company_ethos) {
        this.company_ethos = company_ethos;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getBuilding_number() {
        return building_number;
    }

    public void setBuilding_number(String building_number) {
        this.building_number = building_number;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getMonday_opening() {
        return monday_opening;
    }

    public void setMonday_opening(String monday_opening) {
        this.monday_opening = monday_opening;
    }

    public String getMonday_closing() {
        return monday_closing;
    }

    public void setMonday_closing(String monday_closing) {
        this.monday_closing = monday_closing;
    }

    public String getTuesday_opening() {
        return tuesday_opening;
    }

    public void setTuesday_opening(String tuesday_opening) {
        this.tuesday_opening = tuesday_opening;
    }

    public String getTuesday_closing() {
        return tuesday_closing;
    }

    public void setTuesday_closing(String tuesday_closing) {
        this.tuesday_closing = tuesday_closing;
    }

    public String getWednesday_opening() {
        return wednesday_opening;
    }

    public void setWednesday_opening(String wednesday_opening) {
        this.wednesday_opening = wednesday_opening;
    }

    public String getWednesday_closing() {
        return wednesday_closing;
    }

    public void setWednesday_closing(String wednesday_closing) {
        this.wednesday_closing = wednesday_closing;
    }

    public String getThursday_opening() {
        return thursday_opening;
    }

    public void setThursday_opening(String thursday_opening) {
        this.thursday_opening = thursday_opening;
    }

    public String getThursday_closing() {
        return thursday_closing;
    }

    public void setThursday_closing(String thursday_closing) {
        this.thursday_closing = thursday_closing;
    }

    public String getFriday_opening() {
        return friday_opening;
    }

    public void setFriday_opening(String friday_opening) {
        this.friday_opening = friday_opening;
    }

    public String getFriday_closing() {
        return friday_closing;
    }

    public void setFriday_closing(String friday_closing) {
        this.friday_closing = friday_closing;
    }

    public String getSaturday_opening() {
        return saturday_opening;
    }

    public void setSaturday_opening(String saturday_opening) {
        this.saturday_opening = saturday_opening;
    }

    public String getSaturday_closing() {
        return saturday_closing;
    }

    public void setSaturday_closing(String saturday_closing) {
        this.saturday_closing = saturday_closing;
    }

    public String getSunday_opening() {
        return sunday_opening;
    }

    public void setSunday_opening(String sunday_opening) {
        this.sunday_opening = sunday_opening;
    }

    public String getSunday_closing() {
        return sunday_closing;
    }

    public void setSunday_closing(String sunday_closing) {
        this.sunday_closing = sunday_closing;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this .lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Company()
    {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompanyName() {
        return company_name;
    }

    public void setCompanyName(String company_name) {
        this.company_name = company_name;
    }


    public String getImagepath() {
        return company_image;
    }

    public void setImagepath(String imagepath) {
        this.company_image = imagepath;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Offers> getActiveoffers() {
        return activeoffers;
    }

    public void setActiveoffers(List<Offers> activeoffers) {
        this.activeoffers = activeoffers;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}
