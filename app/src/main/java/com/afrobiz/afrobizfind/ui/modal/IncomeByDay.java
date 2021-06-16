package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IncomeByDay implements Serializable
{
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("totaldayincome")
    @Expose
    private String totaldayincome;

    @SerializedName("totalorder")
    @Expose
    private int totalorder;

    @SerializedName("totalproduct")
    @Expose
    private String totalproduct;

    public int getTotalorder()
    {
        return totalorder;
    }

    public void setTotalorder(int totalorder)
    {
        this.totalorder = totalorder;
    }

    public String getTotalproduct()
    {
        return totalproduct;
    }

    public void setTotalproduct(String totalproduct)
    {
        this.totalproduct = totalproduct;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTotaldayincome()
    {
        return totaldayincome;
    }

    public void setTotaldayincome(String totaldayincome) {
        this.totaldayincome = totaldayincome;
    }

//    String daydate, incomeAccount;
//    int noOfProducts, noOfOrders ;
//
//    public String getDaydate() {
//        return daydate;
//    }
//
//    public void setDaydate(String daydate) {
//        this.daydate = daydate;
//    }
//
//    public String getIncomeAccount() {
//        return incomeAccount;
//    }
//
//    public void setIncomeAccount(String incomeAccount) {
//        this.incomeAccount = incomeAccount;
//    }
//
//    public int getNoOfProducts() {
//        return noOfProducts;
//    }
//
//    public void setNoOfProducts(int noOfProducts) {
//        this.noOfProducts = noOfProducts;
//    }
//
//    public int getNoOfOrders() {
//        return noOfOrders;
//    }
//
//    public void setNoOfOrders(int noOfOrders) {
//        this.noOfOrders = noOfOrders;
//    }
}
