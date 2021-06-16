package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Offers implements Serializable
{

    @SerializedName("company_id")
    @Expose
    private int companyId;

    @SerializedName("offer_number")
    @Expose
    private String offer_number;
    @SerializedName("offer_code")
    @Expose
    private String offerCode;
    @SerializedName("offer_details")
    @Expose
    private String offerDetails;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("customer_only")
    @Expose
    private int customerOnly;
//    @SerializedName("mobile_number")
//    @Expose
//    private String mobileNumber;
    @SerializedName("active")
    @Expose
    private int active;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("company_name")
    @Expose
    private String companyName;

    @SerializedName("company")
    @Expose
    private Company company;

    public String getOffer_number() {
        return offer_number;
    }

    public void setOffer_number(String offer_number) {
        this.offer_number = offer_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(String offerDetails) {
        this.offerDetails = offerDetails;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getCustomerOnly() {
        return customerOnly;
    }

    public void setCustomerOnly(int customerOnly) {
        this.customerOnly = customerOnly;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company)
    {
        this.company = company;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
