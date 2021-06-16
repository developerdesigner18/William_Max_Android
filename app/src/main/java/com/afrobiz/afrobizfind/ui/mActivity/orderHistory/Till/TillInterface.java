package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Till;

import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Product;

import java.util.List;

public interface TillInterface {

    public void GetAllCompanyProductApiCalling();
    public void GetAllCompanyProductApiCallingResult(boolean IsSuccess, List<Product> Products);
    public void OnProductClose(Product product,int position);
    public void OnDatachangeListner(int position,int value,int qty);
}
