package com.afrobiz.afrobizfind.ui.inventory;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Inventory;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.scanner.FullScannerActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AddNewInventory extends AppCompatActivity
{
    RelativeLayout relative_add;
    TextView tv_add;
    ImageView img_scan, Ivback;
    EditText et_product_name, et_qty, et_search;
    private static final int ZXING_CAMERA_PERMISSION = 10, GET_SCAN_PRODUCT = 866;
    Users currentuser;
    ProgressDialog pd3;
    int company_id = 0;
    String token = null;
    ApiInterface objInterface;
    List<Inventory> listInventory = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_inventory);

        relative_add = (RelativeLayout) findViewById(R.id.relative_add);
        tv_add = (TextView) findViewById(R.id.tv_add);
        img_scan = (ImageView) findViewById(R.id.img_scan);
        Ivback = (ImageView) findViewById(R.id.Ivback);
        et_search = (EditText) findViewById(R.id.et_search);
        et_product_name = (EditText) findViewById(R.id.et_product_name);
        et_qty = (EditText) findViewById(R.id.et_qty);

        Bundle b = getIntent().getExtras();

        if (b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com", "" + company_id);
        }

        currentuser = new PrefrenceManager(AddNewInventory.this).getCurrentuser();
        if (currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null) {
                if (ApiClient.isNetworkAvailable(AddNewInventory.this)) {
                    Log.e("company_id", "" + company_id);
//                    new GetInventoryHistoryList().execute();
                } else {
                    Toast.makeText(AddNewInventory.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity();
//                Intent i_back = new Intent(AddNewInventory.this, FullScannerActivity.class);
//                startActivity(i_back);
//                finish();
            }
        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(AddNewInventory.this, NewInventoryActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
                finish();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public class AddInventory extends AsyncTask<Void, Void, Void> {
//        int id= 0 ;
//        public GetCurrentInventoryList(int company_id)
//        {
//            this.id = company_id;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd3 = new ProgressDialog(AddNewInventory.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            addInventory();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void addInventory()
    {
//        Company objCompany = new Company();
//        objCompany.setId(id);

        Call<Response> call = objInterface.AddNewInventory(1, "1", 1);

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
//                            tv_empty.setVisibility(View.GONE);
//                            rcv_inventory_history.setVisibility(View.VISIBLE);

//                            adapter = new InventoryHistoryAdapter(AddNewInventory.this, listInventory);
                            LinearLayoutManager manager = new LinearLayoutManager(AddNewInventory.this);
//                            rcv_inventory_history.setLayoutManager(manager);
//                            rcv_inventory_history.setAdapter(adapter);

//                            adapter.notifyDataSetChanged();
                        } else {
//                            tv_empty.setVisibility(View.VISIBLE);
//                            rcv_inventory_history.setVisibility(View.GONE);
                        }
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                    } else if (response.body().getResult() == 0) {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(AddNewInventory.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(AddNewInventory.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd3 != null & pd3.isShowing()) {
                        pd3.dismiss();
                    }
                    Toast.makeText(AddNewInventory.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd3 != null & pd3.isShowing()) {
                    pd3.dismiss();
                }
                Toast.makeText(AddNewInventory.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void launchActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, FullScannerActivity.class);
            startActivityForResult(intent, GET_SCAN_PRODUCT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, FullScannerActivity.class);
                    startActivityForResult(intent, GET_SCAN_PRODUCT);
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GET_SCAN_PRODUCT:
                if (resultCode == RESULT_OK) {
                    String productName = data.getStringExtra("scan_result");
                    if (productName != null) {
                        et_product_name.setText(productName);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i_back = new Intent(AddNewInventory.this, NewInventoryActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }
}
