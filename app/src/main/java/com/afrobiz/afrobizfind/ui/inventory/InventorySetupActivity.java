package com.afrobiz.afrobizfind.ui.inventory;

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
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Inventory.InventoryActivity;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class InventorySetupActivity extends AppCompatActivity
{
    TextView tv_empty;
    RecyclerView rcv_inventory;
    ImageView Ivback;
    EditText et_seach;
    ProgressDialog pd3;
    Users currentuser;
    int company_id = 0;
    String token = null;
    ApiInterface objInterface;
    List<Product> product_list = new ArrayList<>();
    InventorySetupAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inventory_setup);

        et_seach = (EditText) findViewById(R.id.et_search);
        Ivback = (ImageView) findViewById(R.id.Ivback);
        rcv_inventory = (RecyclerView) findViewById(R.id.rcv_inventory);
//        tv_empty = (TextView) findViewById(R.id.tv_empty);

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com",""+company_id);
        }

        currentuser = new PrefrenceManager(InventorySetupActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(InventorySetupActivity.this))
                {
                    Log.e("company_id", "" + company_id);
                    new GetCompanyProductList(company_id).execute();
                }
                else
                {
                    Toast.makeText(InventorySetupActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        et_seach.addTextChangedListener(new TextWatcher()
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
                Intent i_inventory = new Intent(InventorySetupActivity.this, InventoryActivity.class);
                i_inventory.putExtra("company_id", company_id);
                startActivity(i_inventory);
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
    public void filterProduct(String text)
    {
        List<Product> listTemp = new ArrayList<>();

        for(Product p : product_list)
        {

            if((p.getProduct_number() != null && String.valueOf(p.getProduct_number()).contains(text)) ||
                    (p.getCreatedAt() != null && p.getCreatedAt().toLowerCase().contains(text)) ||
//                    (p.get() != 0 && String.valueOf(p.getQuantity()).contains(text))||
                    (p.getPrice() != null && String.valueOf(p.getPrice()).contains(text))||
                    (p.getProductName() != null && p.getProductName().toLowerCase().contains(text)))
            {
                Log.e("filter",""+new Gson().toJson(p));
                listTemp.add(p);
            }
      }
        if(adapter != null)
        {
            adapter.filterList(listTemp);
        }
    }

    public void startDetailctivity(Product product)
    {
        Intent i_start = new Intent(InventorySetupActivity.this , InentorySetupDetailsActivity.class);
        i_start.putExtra("inventorysetup", product);
        i_start.putExtra("company_id", company_id);
        startActivity(i_start);
    }

    public class GetCompanyProductList extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;

        public GetCompanyProductList(int company_id)
        {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(InventorySetupActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getProduct(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getProduct(int id)
    {
//        Company objCompany = new Company();
//        objCompany.setId(id);
        Call<Response> call = objInterface.getAllProductForInventory(id);

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
                        product_list = response.body().getProducts();

                        if(product_list !=null && product_list.size() >0)
                        {
                            Log.e("productlist",""+product_list.size());
//                            tv_empty.setVisibility(View.GONE);
                            rcv_inventory.setVisibility(View.VISIBLE);

                            adapter = new InventorySetupAdapter(InventorySetupActivity.this, product_list);
                            LinearLayoutManager productlayoutManager = new LinearLayoutManager(InventorySetupActivity.this);
                            rcv_inventory.setLayoutManager(productlayoutManager);
                            rcv_inventory.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            runLayoutAnimation(rcv_inventory);
                        }
                        else
                        {
//                            tv_empty.setVisibility(View.VISIBLE);
                            rcv_inventory.setVisibility(View.GONE);
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
                        Toast.makeText(InventorySetupActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(InventorySetupActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(InventorySetupActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(InventorySetupActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_inventory = new Intent(InventorySetupActivity.this, InventoryActivity.class);
        i_inventory.putExtra("company_id", company_id);
        startActivity(i_inventory);
        finish();
    }
}
