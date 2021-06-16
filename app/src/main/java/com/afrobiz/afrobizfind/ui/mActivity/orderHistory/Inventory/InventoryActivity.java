package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Inventory;

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
import com.afrobiz.afrobizfind.ui.inventory.CurrentInventoryAdapter;
import com.afrobiz.afrobizfind.ui.inventory.InventoryHistoryActivity;
import com.afrobiz.afrobizfind.ui.inventory.InventorySetupActivity;
import com.afrobiz.afrobizfind.ui.inventory.NewInventoryActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;
import com.afrobiz.afrobizfind.ui.modal.Inventory;
import com.afrobiz.afrobizfind.ui.modal.Offers;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class InventoryActivity extends AppCompatActivity {
    private static final String TAG = "InventoryActivity";
    RecyclerView rcv_current_inventory;
    Users currentuser;
    TextView tv_empty, tv_inventory_setup, tv_current_inventory, tv_new_inventory, tv_inventory_history;
    ImageView Ivback;
    EditText et_search;
    CurrentInventoryAdapter adapter;
    ProgressDialog pd3;
    int company_id = 0;
    String token = null;
    List<Inventory> listInventory = new ArrayList<>();
    ApiInterface objInterface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inventory);

        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_inventory_setup = (TextView) findViewById(R.id.tv_inventory_setup);
        tv_current_inventory = (TextView) findViewById(R.id.tv_current_inventory);
        tv_new_inventory = (TextView) findViewById(R.id.tv_new_inventory);
        tv_inventory_history = (TextView) findViewById(R.id.tv_inventory_history);
        Ivback = (ImageView) findViewById(R.id.Ivback);
        et_search = findViewById(R.id.et_search);

        rcv_current_inventory = (RecyclerView) findViewById(R.id.rcv_current_inventory);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            company_id = b.getInt("company_id");
            Log.e("com", "" + company_id);
        }
        currentuser = new PrefrenceManager(InventoryActivity.this).getCurrentuser();

        adapter = new CurrentInventoryAdapter(InventoryActivity.this, listInventory);
        LinearLayoutManager manager = new LinearLayoutManager(InventoryActivity.this);
        rcv_current_inventory.setLayoutManager(manager);
        rcv_current_inventory.setAdapter(adapter);

        runLayoutAnimation(rcv_current_inventory);

        if (currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(InventoryActivity.this))
                {
                    Log.e("company_id", "" + company_id);
                    new GetCurrentInventoryList().execute();
                }
                else
                {
                    Toast.makeText(InventoryActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
        tv_new_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_back = new Intent(InventoryActivity.this, NewInventoryActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
                finish();
            }
        });

        tv_inventory_setup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(InventoryActivity.this, InventorySetupActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
                finish();
            }
        });
        tv_inventory_history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(InventoryActivity.this, InventoryHistoryActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
                finish();
            }
        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(InventoryActivity.this, SavedCompanyDetailsActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
                finish();
            }
        });

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
                if(editable.toString() != null)
                {
                    filter(editable.toString());
                }
            }
        });
    }
    private void runLayoutAnimation(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    private void filter(String text)
    {
        if (listInventory != null && listInventory.size() > 0)
        {
            List<Inventory> filterdNames = new ArrayList<>();

            for (Inventory s : listInventory)
            {
                if ((s.getProductName() !=  null && s.getProductName().toLowerCase().contains(text.toLowerCase())) ||
                        (s.getProductNumber() != null && s.getProductNumber().contains(text)))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                tv_empty.setVisibility(View.GONE);
                rcv_current_inventory.setVisibility(View.VISIBLE);
//                tv_total.setText("" + filterdNames.size());
                adapter.InventoryList(filterdNames);
            }
            else
            {
//                tv_total.setText("0");
                tv_empty.setVisibility(View.VISIBLE);
                rcv_current_inventory.setVisibility(View.GONE);
            }
        }
    }

    public class GetCurrentInventoryList extends AsyncTask<Void, Void, Void>
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

            pd3 = new ProgressDialog(InventoryActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getCurrentInventory();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getCurrentInventory()
    {
//        Company objCompany = new Company();
//        objCompany.setId(id);

        Call<Response> call = objInterface.InventoryHistory();

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {

                        listInventory = response.body().getInventoryList();

                        if (listInventory != null && listInventory.size() > 0) {
                            Log.e("productlist", "" + listInventory.size());
                            tv_empty.setVisibility(View.GONE);
                            rcv_current_inventory.setVisibility(View.VISIBLE);

                            adapter = new CurrentInventoryAdapter(InventoryActivity.this, listInventory);
                            LinearLayoutManager manager = new LinearLayoutManager(InventoryActivity.this);
                            rcv_current_inventory.setLayoutManager(manager);
                            rcv_current_inventory.setAdapter(adapter);

                            runLayoutAnimation(rcv_current_inventory);




                            adapter.notifyDataSetChanged();
                        } else {
                            tv_empty.setVisibility(View.VISIBLE);
                            rcv_current_inventory.setVisibility(View.GONE);
                        }
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                    } else if (response.body().getResult() == 0) {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(InventoryActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(InventoryActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd3 != null & pd3.isShowing()) {
                        pd3.dismiss();
                    }
                    Toast.makeText(InventoryActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd3 != null & pd3.isShowing()) {
                    pd3.dismiss();
                }
                Toast.makeText(InventoryActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i_back = new Intent(InventoryActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }
}
