package com.afrobiz.afrobizfind.ui.mActivity.MyCompanies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.addCompany.AddCompanyActivity;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MyCompaniesActivity extends AppCompatActivity implements GetAllCompany1 {
    ProgressDialog pd;

    ImageView Ivback;
    RecyclerView RvMyCompany;
    TextView tv_empty;
    String token = null;
    Users currentUser;

    boolean from_add_company = false;
    MyCompaniesAdapter adapter;
    LinearLayoutManager layoutManager;
    ApiInterface objInterface;
    private final List<String> list = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_companies);

        RvMyCompany = (RecyclerView) findViewById(R.id.RvMyCompany);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        tv_empty = (TextView) findViewById(R.id.tv_empty);

        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            from_add_company = b.getBoolean("from_add_company");
        }

//        objInterface = ApiClient.create_InstanceAuth(token);

        currentUser = new PrefrenceManager(MyCompaniesActivity.this).getCurrentuser();

        if (currentUser != null)
        {
            token = currentUser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null) {
                if (ApiClient.isNetworkAvailable(MyCompaniesActivity.this)) {
                    new MyCompaniesAsyncTask(MyCompaniesActivity.this, MyCompaniesActivity.this).execute();
//                    new GetAllCompanies().execute();
                } else {
                    Toast.makeText(MyCompaniesActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        RvMyCompany.setVisibility(View.GONE);
        tv_empty.setVisibility(View.VISIBLE);

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (from_add_company == true)
                {
                    Intent intent = new Intent(MyCompaniesActivity.this, AddCompanyActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(MyCompaniesActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void getAll_Comp(List<Company> listCompany)
    {
        Log.e("company_size", "" + listCompany.size());
        if (listCompany != null && listCompany.size() > 0)
        {
            Log.e("company_size", "" + listCompany.size());

            layoutManager = new LinearLayoutManager(MyCompaniesActivity.this);
            adapter = new MyCompaniesAdapter(MyCompaniesActivity.this, listCompany);
            RvMyCompany.setAdapter(adapter);

            RvMyCompany.setLayoutManager(layoutManager);
            adapter.notifyDataSetChanged();

            runLayoutAnimation(RvMyCompany);

            RvMyCompany.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            RvMyCompany.setVisibility(View.GONE);
        }
    }


    //    private class GetAllCompanies extends AsyncTask<Void,Void,Void>
//    {
//
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//            pd = new ProgressDialog(MyCompaniesActivity.this);
//            pd.setCancelable(false);
//            pd.setMessage(getString(R.string.please_wait));
//            pd.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids)
//        {
//            get_all_companies();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid)
//        {
//            super.onPostExecute(aVoid);
//        }
//    }
//    public void get_all_companies()
//    {
//        try
//        {
//            Call<Response> getall = objInterface.GetAllCompany();
//
//            Log.e("call" , ""+getall.request());
//
//            getall.enqueue(new Callback<Response>()
//            {
//                @Override
//                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
//                {
//                    Log.e("call11" , ""+call.request());
////                    Log.e("response5", ""+response.code());
//
//                    if(response != null && response.isSuccessful())
//                    {
//                        Log.e("response beauty", new Gson().toJson(response.body()));
//
//                        Log.e("response" , ""+response.body().getCompanies());
//
//                        if(response.body().getResult() == 1)
//                        {
//                            Log.e("all_response" , ""+new Gson().toJson(response.body()));
//
//                            if(pd != null && pd.isShowing())
//                            {
//                                pd.dismiss();
//                            }
//                            listCompany = response.body().getCompanies();
//
//                            if(listCompany != null && listCompany.size() >0)
//                            {
//                                    Log.e("company_size" , ""+listCompany.size());
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
//                        }
//                        else
//                        {
//                            if(pd != null && pd.isShowing())
//                            {
//                                pd.dismiss();
//                            }
//                            Log.e("all_response" , ""+new Gson().toJson(response.body()));
////                            Toast.makeText(MyCompaniesActivity.this ,response.body().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    else
//                    {
//                        if(pd != null && pd.isShowing())
//                        {
//                            pd.dismiss();
//                        }
//                        Toast.makeText(MyCompaniesActivity.this ,getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Response> call, Throwable t)
//                {
//                    Log.e("call344" , ""+call.request());
//                    if(pd != null && pd.isShowing())
//                    {
//                        pd.dismiss();
//                    }
//                    Log.e("report" , ""+t.toString());
//                    Toast.makeText( MyCompaniesActivity.this , t.toString(), Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//        catch(Exception e)
//        {
//            Log.e("exception" , ""+e.toString());
//        }
//    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from_add_company == true) {
            Intent intent = new Intent(MyCompaniesActivity.this, AddCompanyActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MyCompaniesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void runLayoutAnimation(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
