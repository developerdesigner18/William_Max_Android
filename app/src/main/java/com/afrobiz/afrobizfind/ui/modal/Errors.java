package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Errors  implements Serializable
{

    @SerializedName("category_id")
    @Expose
    private List<String> categoryId = null;
    @SerializedName("company_name")
    @Expose
    private List<String> companyName = null;
    @SerializedName("building_number")
    @Expose
    private List<String> buildingNumber = null;
    @SerializedName("address_line_1")
    @Expose
    private List<String> addressLine1 = null;
    @SerializedName("city")
    @Expose
    private List<String> city = null;
    @SerializedName("postcode")
    @Expose
    private List<String> postcode = null;
    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("lat")
    @Expose
    private List<String> lat = null;
    @SerializedName("long")
    @Expose
    private List<String> _long = null;
    @SerializedName("telephone")
    @Expose
    private List<String> telephone = null;
    @SerializedName("website")
    @Expose
    private List<String> website = null;

    @SerializedName("images")
    @Expose
    private List<String> images = null;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getCompanyName() {
        return companyName;
    }

    public void setCompanyName(List<String> companyName) {
        this.companyName = companyName;
    }

    public List<String> getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(List<String> buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public List<String> getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(List<String> addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }

    public List<String> getPostcode() {
        return postcode;
    }

    public void setPostcode(List<String> postcode) {
        this.postcode = postcode;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getLat() {
        return lat;
    }

    public void setLat(List<String> lat) {
        this.lat = lat;
    }

    public List<String> getLong() {
        return _long;
    }

    public void setLong(List<String> _long) {
        this._long = _long;
    }

    public List<String> getTelephone() {
        return telephone;
    }

    public void setTelephone(List<String> telephone) {
        this.telephone = telephone;
    }

    public List<String> getWebsite() {
        return website;
    }

    public void setWebsite(List<String> website) {
        this.website = website;
    }

}
