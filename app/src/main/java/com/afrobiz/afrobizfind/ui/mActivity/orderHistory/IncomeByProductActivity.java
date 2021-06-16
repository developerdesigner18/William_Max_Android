package com.afrobiz.afrobizfind.ui.mActivity.orderHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CompanyOrderHistory.CompanyOrderHistoryActivity;
import com.afrobiz.afrobizfind.ui.modal.IncomeByProduct;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class IncomeByProductActivity extends AppCompatActivity
{
    ImageView Ivback;
    RecyclerView rcv_by_product;
    TextView tv_empty;
    List<IncomeByProduct> list = new ArrayList<>();
    EditText et_search;
    IncomeByProductAdapter adapter;
    ProgressDialog pd3;
    int company_id = 0;
    Users currentuser;
    String token = null;
    ApiInterface objInterface;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_income_by_product);

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com",""+company_id);
        }
        currentuser = new PrefrenceManager(IncomeByProductActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();
            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if(ApiClient.isNetworkAvailable(IncomeByProductActivity.this))
                {
                    new GetIncomeByProduct(company_id).execute();
                }
                else
                {
                    Toast.makeText(IncomeByProductActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        Ivback = (ImageView) findViewById(R.id.Ivback);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_by_product = (RecyclerView) findViewById(R.id.rcv_by_product);

        adapter = new IncomeByProductAdapter(IncomeByProductActivity.this, list);
        LinearLayoutManager manager = new LinearLayoutManager(IncomeByProductActivity.this);
        rcv_by_product.setLayoutManager(manager);
        rcv_by_product.setAdapter(adapter);

        runLayoutAnimation(rcv_by_product);

        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.toString() != null)
                {
                    filterProduct(s.toString().toLowerCase());
                }
            }
        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(IncomeByProductActivity.this, CompanyOrderHistoryActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
                finish();
            }
        });
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
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(IncomeByProductActivity.this, CompanyOrderHistoryActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }
    public void filterProduct(String text)
    {
        List<IncomeByProduct> listTemp = new ArrayList<>();

        for(IncomeByProduct p : list)
        {
            if(p.getCompanyName().toLowerCase().contains(text) || p.getProductName().toLowerCase().contains(text))
            {
                listTemp.add(p);
            }
        }
        adapter.filterList(listTemp);
//        adapter.notifyDataSetChanged();
    }
    public class GetIncomeByProduct extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;

        public GetIncomeByProduct(int company_id)
        {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(IncomeByProductActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getIncomeProduct(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getIncomeProduct(int id)
    {
//        Company objCompany = new Company();
//        objCompany.setId(id);

        Call<Response> call = objInterface.IncomeByProduct(id);
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(pd3 != null && pd3.isShowing())
                {
                    pd3.dismiss();
                }
                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));
                    if(response.body().getResult() == 1)
                    {
                        list =  response.body().getIncomeproduct();
                        if(list != null && list.size() > 0)
                        {
                            tv_empty.setVisibility(View.GONE);
                            rcv_by_product.setVisibility(View.VISIBLE);

                            adapter = new IncomeByProductAdapter(IncomeByProductActivity.this, list);
                            LinearLayoutManager manager = new LinearLayoutManager(IncomeByProductActivity.this);
                            rcv_by_product.setLayoutManager(manager);
                            rcv_by_product.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            runLayoutAnimation(rcv_by_product);
                        }
                        else
                        {
                            rcv_by_product.setVisibility(View.GONE);
                            tv_empty.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        Toast.makeText(IncomeByProductActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(IncomeByProductActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null && pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(IncomeByProductActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
//        Call<Response> call = objInterface.IncomeByProduct(id);
//
//        Log.e("callGet" , ""+call.request());
//        call.enqueue(new Callback<IncomeByProduct()
//        {
//            @Override
//            public void onResponse(Call<IncomeByProduct> call, retrofit2.Response<IncomeByProduct> response)
//            {
//                Log.e("getcode" , ""+response.code());
//
//                if(response != null && response.isSuccessful())
//                {
//                    Log.e("getresponse beauty", new Gson().toJson(response.body()));
//
//                    Log.e("getresponse" , ""+response.body()..
//                    if(response.body().getResu() == 1)
//                    {
//                        list = response.body().getin
//
//                        if(list !=null && list.size() >0)
//                        {
//                            Log.e("productlist",""+list.size());
//                            tv_empty.setVisibility(View.GONE);
//                            rcv_by_product.setVisibility(View.VISIBLE);
//
//                            adapter = new IncomeByProductAdapter(IncomeByProductActivity.this, list);
//                            LinearLayoutManager productlayoutManager = new LinearLayoutManager(IncomeByProductActivity.this);
//                            rcv_by_product.setLayoutManager(productlayoutManager);
//                            rcv_by_product.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                        }
//                        else
//                        {
//                            tv_empty.setVisibility(View.VISIBLE);
//                            rcv_by_product.setVisibility(View.GONE);
//                        }
//                        if(pd3 != null & pd3.isShowing())
//                        {
//                            pd3.dismiss();
//                        }
//                    }
//                    else if(response.body().getResult() == 0)
//                    {
//                        if(pd3 != null & pd3.isShowing())
//                        {
//                            pd3.dismiss();
//                        }
//                        Toast.makeText(IncomeByProductActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        if(pd3 != null & pd3.isShowing())
//                        {
//                            pd3.dismiss();
//                        }
//                        Toast.makeText(IncomeByProductActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//                else
//                {
//                    if(pd3 != null & pd3.isShowing())
//                    {
//                        pd3.dismiss();
//                    }
//                    Toast.makeText(IncomeByProductActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<IncomeByProduct> call, Throwable t)
//            {
//                if(pd3 != null & pd3.isShowing())
//                {
//                    pd3.dismiss();
//                }
//                Toast.makeText(IncomeByProductActivity.this , t.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
