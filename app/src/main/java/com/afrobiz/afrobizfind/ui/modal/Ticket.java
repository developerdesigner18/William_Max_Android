package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ticket implements Serializable
{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("customer_number")
    @Expose
    private int customerNumber;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ticket_used_date")
    @Expose
    private int ticketUsedDate;

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("purchased_date")
    @Expose
    private String purchasedDate;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;

    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;

    @SerializedName("refund")
    @Expose
    private String refund;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTicketUsedDate() {
        return ticketUsedDate;
    }

    public void setTicketUsedDate(int ticketUsedDate) {
        this.ticketUsedDate = ticketUsedDate;
    }
}
