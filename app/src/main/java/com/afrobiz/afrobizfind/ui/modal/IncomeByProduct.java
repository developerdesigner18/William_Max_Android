package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IncomeByProduct implements Serializable
{
    @SerializedName("company_name")
    @Expose
    private String companyName;

    @SerializedName("product_name")
    @Expose
    private String productName;

    @SerializedName("totalproductincome")
    @Expose
    private String totalproductincome;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getTotalproductincome() {
        return totalproductincome;
    }

    public void setTotalproductincome(String totalproductincome) {
        this.totalproductincome = totalproductincome;
    }

}