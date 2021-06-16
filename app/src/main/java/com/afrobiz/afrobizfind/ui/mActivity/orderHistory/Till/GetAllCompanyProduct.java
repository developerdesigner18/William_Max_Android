package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Till;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GetAllCompanyProduct extends AsyncTask<String, String, String> {

    TillInterface _interface;
    Context context;
    ProgressDialog pd;
    ApiInterface objInterface;
    List<Product> listCompany;
    int id;

    public GetAllCompanyProduct(TillInterface _interface, Context context, int id, ApiInterface objInterface) {
        this._interface = _interface;
        this.context = context;
        this.id = id;
        this.objInterface = objInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage(context.getString(R.string.please_wait));
        pd.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        getProduct(id);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    public void getProduct(int id) {
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response> call = objInterface.GetAllCompanyProducts(objCompany);

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {
                        listCompany = response.body().getProducts();

                        if (listCompany != null && listCompany.size() > 0) {
                            Log.e("productlist", "" + listCompany.size());
                            _interface.GetAllCompanyProductApiCallingResult(true, listCompany);
                            if (pd != null & pd.isShowing()) {
                                pd.dismiss();
                            }
                        } else {
                        }

                    } else if (response.body().getResult() == 0) {
                        if (pd != null & pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd != null & pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd != null & pd.isShowing()) {
                        pd.dismiss();
                    }
                    Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd != null & pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
