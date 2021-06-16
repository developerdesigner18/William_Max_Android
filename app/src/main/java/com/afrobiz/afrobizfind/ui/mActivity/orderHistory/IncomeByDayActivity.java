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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CompanyOrderHistory.CompanyOrderHistoryActivity;
import com.afrobiz.afrobizfind.ui.modal.IncomeByDay;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class IncomeByDayActivity extends AppCompatActivity
{
    ImageView Ivback;
    RecyclerView rcv_by_day;
    TextView tv_empty;
    List<IncomeByDay> list = new ArrayList<>();
    IncomeByDayAdapter adapter;
    EditText et_search;
    TextView tv_day_date, tv_income_account, tv_no_orders, tv_no_products;
    int company_id = 0;
    Users currentuser;
    String  token = null;
    ApiInterface objInterface;
    ProgressDialog pd3;
    boolean ascending = false , noProdctSort = false, noOrdersSort = false, incomeAmountSort = false;


    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_income_by_day);

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com",""+company_id);
        }

        tv_day_date = (TextView) findViewById(R.id.tv_day_date);
        tv_income_account = (TextView) findViewById(R.id.tv_income_account);
        tv_no_orders = (TextView) findViewById(R.id.tv_no_orders);
        tv_no_products = (TextView) findViewById(R.id.tv_no_products);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_by_day = (RecyclerView) findViewById(R.id.rcv_by_day);
        et_search = (EditText) findViewById(R.id.et_search);

        currentuser = new PrefrenceManager(IncomeByDayActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(IncomeByDayActivity.this))
                {
                    Log.e("company_id", "" + company_id);
                    new GetIncomeByDay(company_id).execute();
                }
                else
                {
                    Toast.makeText(IncomeByDayActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }


//        for(int i = 0 ; i < 10 ; i ++)
//        {
//            IncomeByDay objDay = new IncomeByDay();
//            objDay.setDaydate("2021-03-"+i+3+" 17:06:58");
//            objDay.setIncomeAccount("zakiya"+i);
//            objDay.setNoOfOrders(i*2);
//            objDay.setNoOfProducts(i*3);
//
//            list.add(objDay);
//        }

        adapter = new IncomeByDayAdapter(IncomeByDayActivity.this, list);
        LinearLayoutManager manager = new LinearLayoutManager(IncomeByDayActivity.this);
        rcv_by_day.setLayoutManager(manager);
        rcv_by_day.setAdapter(adapter);
        runLayoutAnimation(rcv_by_day);

        tv_day_date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(ascending == false)
                {
                    ascending = true;
                    SortDayDate(ascending);
                    tv_day_date.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_ascending), null);
                }
                else
                {
                    ascending = false;
                    SortDayDate(ascending);
                    tv_day_date.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_descending), null);
                }
            }
        });
        tv_income_account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(incomeAmountSort == false)
                {
                    incomeAmountSort = true;
                    SortInComeAmount(incomeAmountSort);
                    tv_income_account.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_ascending), null);
                }
                else
                {
                    incomeAmountSort = false;
                    SortInComeAmount(incomeAmountSort);
                    tv_income_account.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_descending), null);
                }
            }
        });
        tv_no_orders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(noOrdersSort == false)
                {
                    noOrdersSort = true;
                    SortOrderNo(noOrdersSort);
                    tv_no_orders.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_ascending), null);
                }
                else
                {
                    noOrdersSort = false;
                    SortOrderNo(noOrdersSort);
                    tv_no_orders.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_descending), null);
                }
            }
        });
        tv_no_products.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(noProdctSort == false)
                {
                    noProdctSort = true;
                    SortProductNo(noProdctSort);
                    tv_no_products.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_ascending), null);
                }
                else
                {
                    noProdctSort = false;
                    SortProductNo(noProdctSort);
                    tv_no_products.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(IncomeByDayActivity.this,R.drawable.img_descending), null);
                }
            }
        });

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
                if(s != null)
                {
                    FilterData(s.toString().toLowerCase());
                }
            }
        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(IncomeByDayActivity.this, CompanyOrderHistoryActivity.class);
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
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom_random);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public class GetIncomeByDay extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;

        public GetIncomeByDay(int company_id)
        {
            this.id = company_id;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(IncomeByDayActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getIncomePerDay(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getIncomePerDay(int id)
    {
        Call<Response> call = objInterface.IncomeByDay(id);

        Log.e("callGet" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("getcode" , ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {

                        list = response.body().getIncomebyday();

                        if(list !=null && list.size() >0)
                        {
                            Log.e("productlist",""+list.size());
                            tv_empty.setVisibility(View.GONE);
                            rcv_by_day.setVisibility(View.VISIBLE);

//                            tv_total.setText(""+product_list.size());


                            adapter = new IncomeByDayAdapter(IncomeByDayActivity.this, list);
                            LinearLayoutManager manager = new LinearLayoutManager(IncomeByDayActivity.this);
                            rcv_by_day.setLayoutManager(manager);
                            rcv_by_day.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            runLayoutAnimation(rcv_by_day);
                        }
                        else
                        {
//                            tv_total.setText("0");
                            tv_empty.setVisibility(View.VISIBLE);
                            rcv_by_day.setVisibility(View.GONE);
                        }
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(IncomeByDayActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(IncomeByDayActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(IncomeByDayActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(IncomeByDayActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void FilterData(String text)
    {
        if(list != null && list.size() > 0)
        {
            List<IncomeByDay> listTemp = new ArrayList<>();
            for(IncomeByDay objIncome : list)
            {
                if((objIncome.getTotaldayincome() != null && objIncome.getTotaldayincome().toLowerCase().contains(text))||
                        (objIncome.getCreatedAt() != null && objIncome.getCreatedAt().toLowerCase().contains(text)) ||
                                (objIncome.getTotalorder() != 0 && String.valueOf(objIncome.getTotalorder()).toLowerCase().contains(text)) ||
                (objIncome.getTotalproduct() != null && objIncome.getTotalproduct().toLowerCase().contains(text)) )
                {
                    listTemp.add(objIncome);
                }
            }
            adapter.filterList(listTemp);
        }

    }
    public void SortOrderNo(boolean ascen)
    {
        Collections.sort(list, new Comparator<IncomeByDay>()
        {
            @Override
            public int compare(IncomeByDay o1, IncomeByDay o2)
            {
                if(ascen == true)
                {
                    return Integer.valueOf(o1.getTotalorder()).compareTo(o2.getTotalorder());
                }
                else
                {
                    return Integer.valueOf(o2.getTotalorder()).compareTo(o1.getTotalorder());
                }
            }
        });

        adapter .notifyDataSetChanged();
    }
    public void SortProductNo(boolean ascen)
    {
        Collections.sort(list, new Comparator<IncomeByDay>()
        {
            @Override
            public int compare(IncomeByDay o1, IncomeByDay o2)
            {
                if(ascen == true)
                {
                    return Integer.valueOf(o1.getTotalproduct()).compareTo(Integer.valueOf(o2.getTotalproduct()));
                }
                else
                {
                    return Integer.valueOf(o2.getTotalproduct()).compareTo(Integer.valueOf(o1.getTotalproduct()));
                }
            }
        });

        adapter .notifyDataSetChanged();
    }
    public void SortInComeAmount(boolean ascen)
    {
        Collections.sort(list, new Comparator<IncomeByDay>()
        {
            @Override
            public int compare(IncomeByDay o1, IncomeByDay o2)
            {
                if(ascen == true)
                {
                    return o1.getTotaldayincome().compareTo(o2.getTotaldayincome());
                }
               else
                {
                    return o2.getTotaldayincome().compareTo(o1.getTotaldayincome());
                }
            }
        });

        adapter .notifyDataSetChanged();
    }
    public void SortDayDate(boolean ascen)
    {
        Collections.sort(list, new Comparator<IncomeByDay>()
        {
            @Override
            public int compare(IncomeByDay o1, IncomeByDay o2)
            {
                if(ascen == true)
                {
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
                else
                {
                    return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(IncomeByDayActivity.this, CompanyOrderHistoryActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }
}
