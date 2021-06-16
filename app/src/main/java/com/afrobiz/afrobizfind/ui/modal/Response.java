package com.afrobiz.afrobizfind.ui.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Response implements Serializable {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("user")
    @Expose
    private Users user;

    @SerializedName("users")
    @Expose
    private List<Users> users = null;

    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("company")
    @Expose
    private Company company;

    @SerializedName("versions")
    @Expose
    private List<Version> versions = null;


    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    @SerializedName("nearbyCompanies")
    @Expose
    private List<Company> nearbyCompanies = null;

    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    @SerializedName("product")
    @Expose
    private Product product;

    @SerializedName("totalcustomers")
    @Expose
    private int totalcustomers;

    @SerializedName("totalunreadnotification")
    @Expose
    private int totalunreadnotification;

    @SerializedName("totalnotifications")
    @Expose
    private int totalnotifications;

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("incomeproduct")
    @Expose
    private List<IncomeByProduct> incomeproduct = null;

    @SerializedName("incomebyday")
    @Expose
    private List<IncomeByDay> incomebyday = null;

    @SerializedName("eventdata")
    @Expose
    private List<Event> eventList = null;

    @SerializedName("event")
    @Expose
    private Event event;

    @SerializedName("ticketinfo")
    @Expose
    private Ticket ticketInfo = null;

    public Ticket getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(Ticket ticketInfoList) {
        this.ticketInfo = ticketInfoList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<IncomeByDay> getIncomebyday() {
        return incomebyday;
    }

    public void setIncomebyday(List<IncomeByDay> incomebyday) {
        this.incomebyday = incomebyday;
    }

    public List<IncomeByProduct> getIncomeproduct() {
        return incomeproduct;
    }

    public void setIncomeproduct(List<IncomeByProduct> incomeproduct) {
        this.incomeproduct = incomeproduct;
    }

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("offer")
    @Expose
    private Offers offer;

    @SerializedName("offers")
    @Expose
    private List<Offers> offers = null;

    @SerializedName("orderstatus")
    @Expose
    private List<OrderStatus> orderStatusList = null;

    @SerializedName("orderhistory")
    @Expose
    private List<OrderHistory> orderHistoryList = null;

    @SerializedName("companyorder")
    @Expose
    private List<OrderHistory> companyorderList = null;

    @SerializedName("currencies")
    @Expose
    private List<Currency> currencies = null;

    @SerializedName("inventory")
    @Expose
    private List<Inventory> inventoryList = null;

    @SerializedName("inventorydata")
    @Expose
    private Inventory inventorydata;

    @SerializedName("ticketlist")
    @Expose
    private List<Ticket> ticketList = null;

    public List<Ticket> getTicketList()
    {
        return ticketList;
    }
    public void setTicketList(List<Ticket> ticketList)
    {
        this.ticketList = ticketList;
    }

    public List<Inventory> getInventoryList()
    {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public Inventory getInventorydata() {
        return inventorydata;
    }

    public void setInventorydata(Inventory inventorydata) {
        this.inventorydata = inventorydata;
    }

    public String getSuccess() {
        return success;
    }

    public List<OrderHistory> getCompanyorderList()
    {
        return companyorderList;
    }


    public void setCompanyorderList(List<OrderHistory> companyorderList)
    {
        this.companyorderList = companyorderList;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<OrderHistory> getOrderHistoryList()
    {
        return orderHistoryList;
    }

    public void setOrderHistoryList(List<OrderHistory> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public List<Offers> getOffers() {
        return offers;
    }

    public void setOffers(List<Offers> offers) {
        this.offers = offers;
    }

    public Offers getOffer() {
        return offer;
    }

    public void setOffer(Offers offer) {
        this.offer = offer;
    }

    public List<OrderStatus> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<OrderStatus> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public int getTotalunreadnotification() {
        return totalunreadnotification;
    }

    public void setTotalunreadnotification(int totalunreadnotification) {
        this.totalunreadnotification = totalunreadnotification;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public List<Company> getNearbyCompanies() {
        return nearbyCompanies;
    }

    public void setNearbyCompanies(List<Company> nearbyCompanies) {
        this.nearbyCompanies = nearbyCompanies;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public int getTotalnotifications() {
        return totalnotifications;
    }

    public void setTotalnotifications(int totalnotifications) {
        this.totalnotifications = totalnotifications;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @SerializedName("errors")
    @Expose
    private Errors errors;

    @SerializedName("companies")
    @Expose
    private List<Company> companies = null;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public int getTotalcustomers() {
        return totalcustomers;
    }

    public void setTotalcustomers(int totalcustomers) {
        this.totalcustomers = totalcustomers;
    }
}