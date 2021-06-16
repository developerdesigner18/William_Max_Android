package com.afrobiz.afrobizfind.ui.inventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Inventory.InventoryActivity;
import com.afrobiz.afrobizfind.ui.modal.Inventory;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class InventoryHistoryActivity extends AppCompatActivity
{
    TextView tv_empty;
    RecyclerView rcv_inventory_history;
    ImageView Ivback;
//    List<Inventory> listProduct = new ArrayList<>();
    InventoryHistoryAdapter adapter;
    Users currentuser;
    ProgressDialog pd3;
    int company_id =0;
    String token = null;
    ApiInterface objInterface;
    List<Inventory> listInventory = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inventory_history);

        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_inventory_history = (RecyclerView) findViewById(R.id.rcv_inventory_history);
        Ivback = (ImageView) findViewById(R.id.Ivback);


        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com",""+company_id);
        }
        currentuser = new PrefrenceManager(InventoryHistoryActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(InventoryHistoryActivity.this))
                {
                    Log.e("company_id", "" + company_id);
                    new GetInventoryHistoryList().execute();
                }
                else
                {
                    Toast.makeText(InventoryHistoryActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        adapter = new InventoryHistoryAdapter(InventoryHistoryActivity.this, listInventory);
        LinearLayoutManager manager = new LinearLayoutManager(InventoryHistoryActivity.this);
        rcv_inventory_history.setLayoutManager(manager);
        rcv_inventory_history.setAdapter(adapter);

        runLayoutAnimation(rcv_inventory_history);

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(InventoryHistoryActivity.this, InventoryActivity.class);
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
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom_scale);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public class GetInventoryHistoryList extends AsyncTask<Void, Void, Void>
    {
//        int id= 0 ;

//        public GetCurrentInventoryList(int company_id)
//        {
//            this.id = company_id;
//        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(InventoryHistoryActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getInventoryHistory();
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getInventoryHistory()
    {
        Call<Response> call = objInterface.InventoryHistory();

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

                        listInventory = response.body().getInventoryList();

                        if(listInventory !=null && listInventory.size() >0)
                        {
                            Log.e("productlist",""+listInventory.size());
                            tv_empty.setVisibility(View.GONE);
                            rcv_inventory_history.setVisibility(View.VISIBLE);

                            adapter = new InventoryHistoryAdapter(InventoryHistoryActivity.this, listInventory);
                            LinearLayoutManager manager = new LinearLayoutManager(InventoryHistoryActivity.this);
                            rcv_inventory_history.setLayoutManager(manager);
                            rcv_inventory_history.setAdapter(adapter);

                            runLayoutAnimation(rcv_inventory_history);

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            tv_empty.setVisibility(View.VISIBLE);
                            rcv_inventory_history.setVisibility(View.GONE);
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
                        Toast.makeText(InventoryHistoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(InventoryHistoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(InventoryHistoryActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(InventoryHistoryActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(InventoryHistoryActivity.this, InventoryActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();

    }
}
