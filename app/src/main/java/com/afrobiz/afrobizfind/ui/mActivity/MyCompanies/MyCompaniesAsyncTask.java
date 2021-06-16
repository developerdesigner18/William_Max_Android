package com.afrobiz.afrobizfind.ui.mActivity.MyCompanies;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyCompaniesAsyncTask extends AsyncTask<Void,Void,Void>
{
    Context context;
    List<Company> listCompany = new ArrayList<>();
    GetAllCompany1 objInterface;
    ProgressDialog pd;
    ApiInterface apiInterface;

    public MyCompaniesAsyncTask(Context context , GetAllCompany1 objInterface1)
    {
        this.context = context;
//        this.listCompany = list;

        objInterface = objInterface1;
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
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
//        objInterface.getAll_Comp(listCompany);
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        get_all_companies();
        return null;
    }
    public void get_all_companies()
    {
        try
        {
            Users currentUser = new PrefrenceManager(context).getCurrentuser();

            if(currentUser != null)
            {
                String token = currentUser.getToken();

                Log.e("token", "" + token);
                apiInterface = ApiClient.create_InstanceAuth(token);

            }
                Call<Response> getall = apiInterface.GetAllCompany();

            Log.e("call" , ""+getall.request());

            getall.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {
                    Log.e("call11" , ""+call.request());
//                    Log.e("response5", ""+response.code());

                    if(response != null && response.isSuccessful())
                    {
                        Log.e("response beauty", new Gson().toJson(response.body()));

                        Log.e("response" , ""+response.body().getCompanies());

                        if(response.body().getResult() == 1)
                        {
                            Log.e("all_response" , ""+new Gson().toJson(response.body()));

                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            listCompany = response.body().getCompanies();
                            objInterface.getAll_Comp(listCompany);
//                            if(listCompany != null && listCompany.size() >0)
//                            {
//                                Log.e("company_size" , ""+listCompany.size());
//
//                                layoutManager = new LinearLayoutManager(MyCompaniesActivity.this);
//                                adapter = new MyCompaniesAdapter(MyCompaniesActivity.this, listCompany);
//                                RvMyCompany.setAdapter(adapter);
//                                RvMyCompany.setLayoutManager(layoutManager);
//                                adapter.notifyDataSetChanged();
//
//                                RvMyCompany.setVisibility(View.VISIBLE);
//                                tv_empty.setVisibility(View.GONE);
//                            }
//                            else
//                            {
//                                tv_empty.setVisibility(View.VISIBLE);
//                                RvMyCompany.setVisibility(View.GONE);
//                            }
                        }
                        else
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            Log.e("all_response" , ""+new Gson().toJson(response.body()));
//                            Toast.makeText(MyCompaniesActivity.this ,response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(context ,context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    Log.e("call344" , ""+call.request());
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Log.e("report" , ""+t.toString());
                    Toast.makeText( context , t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e)
        {
            Log.e("exception" , ""+e.toString());
        }
    }
}
