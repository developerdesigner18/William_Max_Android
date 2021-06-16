package com.afrobiz.afrobizfind.ui.mActivity.customerList;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class CustomerListActivity extends AppCompatActivity
{
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.Rv_CustomerList)
    RecyclerView RvCustomerList;

    CustomerListAdapter adapter;

    List<Users> list;

    LinearLayoutManager layoutManager;
    
    ProgressDialog pd;
    Users currentuser;
    ApiInterface objInterface;
    String token = null;
    int company_id = 0 ;

    @BindView(R.id.tv_empty)
    TextView tv_empty;

    @BindView(R.id.tv_total)
    TextView tv_total;

    @BindView(R.id.img_search)
    ImageView img_search;

    @BindView(R.id.et_search)
    EditText et_search;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            company_id = b.getInt("company_id");
        }

        currentuser = new PrefrenceManager(CustomerListActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);

            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(CustomerListActivity.this))
                {
                    Log.e("company_id", "" + company_id);
                    new CustomerList(company_id).execute();
                }
                else
                {
                    Toast.makeText(CustomerListActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                }
            }
        }
        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                filter(editable.toString());
            }
        });
    }
    private void filter(String text)
    {
        if(list  != null && list.size() >0)
        {
            List<Users> filterdNames = new ArrayList<>();

            for (Users s : list)
            {
                if (s.getUsername().toLowerCase().contains(text.toLowerCase()))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                tv_empty.setVisibility(View.GONE);
                RvCustomerList.setVisibility(View.VISIBLE);

                tv_total.setText(""+filterdNames.size());

                adapter.filterList(filterdNames);

            }
            else
            {
                tv_total.setText("0");
                tv_empty.setVisibility(View.VISIBLE);
                RvCustomerList .setVisibility(View.GONE);
            }
        }
    }
    private void runLayoutAnimation(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_scale);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public class  CustomerList extends AsyncTask<Void, Void,Void>
    {
        int cid = 0 ;
        public CustomerList(int company_id) 
        {
            cid = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(CustomerListActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getCustomers(cid);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getCustomers(int company_id)
    {
        Company com = new Company();
        com.setId(company_id);

        Call<Response> call = objInterface.GetCompanyCustomer(com);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse" , ""+response.body().getResult());

                    if (response.body().getResult() == 1)
                    {
                        list = response.body().getUsers();

                        if(list != null && list.size() > 0)
                        {
                            tv_total.setText(""+list.size());

                            Collections.reverse(list);

                            layoutManager = new LinearLayoutManager(CustomerListActivity.this);
                            RvCustomerList.setLayoutManager(layoutManager);
                            adapter = new CustomerListAdapter(CustomerListActivity.this, list);
                            RvCustomerList.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            runLayoutAnimation(RvCustomerList);

                            RvCustomerList.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);

                            if (pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                        }
                        else
                        {
                            tv_total.setText("0");
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }

                            tv_empty.setVisibility(View.VISIBLE);
                            RvCustomerList.setVisibility(View.GONE);
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(CustomerListActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(CustomerListActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(CustomerListActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.Ivback)
    public void onViewClicked() {

        Intent i_back= new Intent(CustomerListActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id",company_id);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i_back= new Intent(CustomerListActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id",company_id);
        startActivity(i_back);
        finish();
    }
}