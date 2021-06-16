package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Till;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.paypal.PaypalPaymentActivity;
import com.afrobiz.afrobizfind.ui.inventory.NewInventoryActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;
import com.afrobiz.afrobizfind.ui.mActivity.addCompany.AddCompanyActivity;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.scanner.FullScannerActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;


public class TillActivity extends AppCompatActivity implements TillInterface {
    private static final String TAG = "TillActivity";

    ImageView Ivback, Ivsearch;
    private MySpinner<String> PartSpinner;
    List<String> PartName = new ArrayList<>();
    //    List<String> PartId;
    List<Product> listProducts = new ArrayList<>();
    List<Product> Products = new ArrayList<>();
    TillAdapter adapter;
    private List<String> provinceList;
    ProgressDialog pd3;
    String token = null;
    ApiInterface objInterface;
    Users currentuser;
    ProgressDialog pd;
    int company_id = 0;
    private static final int ZXING_CAMERA_PERMISSION = 10, GET_SCAN_PRODUCT = 866;
    RecyclerView rcv_bill;
    //    LinearLayout layout_search;
    RelativeLayout relative_top, relative_toolbar;
    TextView tv_total_price, tv_total_qty;
    ImageView img_scan;
    GetAllCompanyProduct getAllCompanyProduct;
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    HashMap<Integer, Integer> map_GrandTotal = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> map_TotalQty = new HashMap<Integer, Integer>();
    Dialog d_add;
    EditText et_cash_amt ;
    TextView tv_change, tv_card, tv_cash;

    int totalPayableAmount  = 0 ;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_till);

        et_cash_amt = (EditText) findViewById(R.id.et_cash_amt);
        tv_change = (TextView) findViewById(R.id.tv_change);

        tv_card = (TextView) findViewById(R.id.tv_card);
        tv_cash  = (TextView) findViewById(R.id.tv_cash);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        Ivsearch = (ImageView) findViewById(R.id.img_search);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_total_qty = (TextView) findViewById(R.id.tv_total_qty);
        img_scan = (ImageView) findViewById(R.id.img_scan);

//        layout_search = (LinearLayout) findViewById(R.id.layout_search);
//        relative_toolbar = (RelativeLayout) findViewById(R.id.relative_toolbar);

        relative_top = (RelativeLayout) findViewById(R.id.relative_top);

        PartSpinner = findViewById(R.id.spinnerPart);

        rcv_bill = (RecyclerView) findViewById(R.id.rcv_bill);

        Bundle b = getIntent().getExtras();

        if (b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com", "" + company_id);
        }

//        PartSpinner.setTitle("Search Product");

        currentuser = new PrefrenceManager(TillActivity.this).getCurrentuser();

        if (currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(TillActivity.this))
                {
                    Log.e("company_id", "" + company_id);
                    getAllCompanyProduct = new GetAllCompanyProduct(this, this, company_id, objInterface);
                    getAllCompanyProduct.execute();
//                    new GetCompanyProductList(company_id).execute();
                }
                else
                {
                    Toast.makeText(TillActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
//        PartName=new ArrayList<>();
//        PartId=new ArrayList<>();

//        if(listProducts != null && listProducts.size() > 0)
//        {
//
//        }

        adapter = new TillAdapter(TillActivity.this, Products, this);
        rcv_bill.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(TillActivity.this);
        rcv_bill.setLayoutManager(manager);

        tv_card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_paypal = new Intent(TillActivity.this , PaypalPaymentActivity.class);
                i_paypal.putExtra("com_id",company_id);
                startActivity(i_paypal);
            }
        });

        Ivsearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Ivback.setImageResource(R.drawable.ic_backerrow_grey);
//                relative_top.setBackgroundColor(Color.WHITE);
//                layout_search.setVisibility(View.VISIBLE);
//                relative_toolbar.setVisibility(View.VISIBLE);
//                relative_top.setBackgroundColor(Color.WHITE);
            }
        });
//        PartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (listProducts.size() > 0)
//                    Log.e("Product clicked", "" + listProducts.get(position).getProductName());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(TillActivity.this, SavedCompanyDetailsActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
                finish();
            }
        });
        et_cash_amt.addTextChangedListener(new TextWatcher()
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
                if(s.toString().length() > 0)
                {
                    int givenAmount = Integer.parseInt(s.toString());
                    if(givenAmount < totalPayableAmount)
                    {
                        tv_change.setText("");
                        Toast.makeText(TillActivity.this, "Please enter total payable amount", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        int changeAmount = givenAmount - totalPayableAmount;
                        tv_change.setText(""+changeAmount);
                    }
                }
            }
        });
    }

    public void OnScannerClick(View view) {
        launchActivity();
    }

    public void launchActivity()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
        else
        {
            Intent intent = new Intent(this, FullScannerActivity.class);
            startActivityForResult(intent, GET_SCAN_PRODUCT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case ZXING_CAMERA_PERMISSION :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(this, FullScannerActivity.class);
                    startActivityForResult(intent, GET_SCAN_PRODUCT);
                }
                else
                {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    private void initSpinner()
    {
        provinceList = new ArrayList<>();
        provinceList.add("Kampong Thom");
        provinceList.add("Kampong Cham");
        provinceList.add("Kampong Chhnang");
        provinceList.add("Phnom Penh");
        provinceList.add("Kandal");
        provinceList.add("Kampot");

        PartSpinner.setItem(PartName);

        PartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(TillActivity.this, PartName.get(position), Toast.LENGTH_SHORT).show();

                Log.e(TAG, "onItemSelected: "
                        + listProducts.get(hashMap.get(PartName.get(position))).getProductName());

                if (!Products.contains(listProducts.get(hashMap.get(PartName.get(position)))))
                    Products.add(listProducts.get(hashMap.get(PartName.get(position))));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(TillActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }
    @Override
    public void OnDatachangeListner(int position, int value, int qty)
    {
        map_GrandTotal.put(position, value);
        map_TotalQty.put(position, qty);

        totalPayableAmount = GetGrandTotal();
        tv_total_price.setText("" + GetGrandTotal());
        tv_total_qty.setText("" + GetTotalQty());
//        adapter.notifyDataSetChanged();
        rcv_bill.scrollToPosition(position);
        //        adapter.notifyDataSetChanged();
    }

    @Override
    public void GetAllCompanyProductApiCalling()
    {

    }

    @Override
    public void OnProductClose(Product product, int position)
    {
        this.Products.remove(position);
//        adapter.notifyDataSetChanged();
//        rcv_bill.scrollToPosition(position);
    }

    @Override
    public void GetAllCompanyProductApiCallingResult(boolean IsSuccess, List<Product> Products)
    {
        if (IsSuccess)
        {
            listProducts.clear();
            listProducts.addAll(Products);

            for (int i = 0; i < listProducts.size(); i++)
            {
                Log.e(TAG, "GetAllCompanyProductApiCallingResult Price :-------------> " + listProducts.get(i).getPrice());
                PartName.add(listProducts.get(i).getProductName());
                PartName.add(listProducts.get(i).getProduct_number());
                hashMap.put(listProducts.get(i).getProductName(), i);
                hashMap.put(listProducts.get(i).getProduct_number(), i);
            }
            initSpinner();
//            TillSearchAdapter adapter = new TillSearchAdapter(TillActivity.this, android.R.layout.simple_spinner_item, listProducts);
//            PartSpinner.setAdapter(adapter);
            Log.e(TAG, "GetAllCompanyProductApiCallingResult size:--- " + listProducts.size());
        }
    }

    private int GetGrandTotal()
    {
        int total = 0;

        Log.e(TAG, "GetGrandTotal size:- " + map_GrandTotal.size());

        for (int i = 0; i < map_GrandTotal.size(); i++)
        {
            Log.e(TAG, "GetGrandTotal pos = " + i + " val = " + map_GrandTotal.get(i));

            if (map_GrandTotal.get(i) == null)
            {
                Log.e(TAG, "GetGrandTotal is null: ");
            }
            else
            {
                total = total + map_GrandTotal.get(i).intValue();
            }
        }
        return total;
    }

    private int GetTotalQty()
    {
        int total = 0;
        Log.e(TAG, "GetGrandTotal size:- " + map_TotalQty.size());
        for (int i = 0; i < map_TotalQty.size(); i++)
        {
            Log.e(TAG, "GetGrandTotal pos = " + i + " val = " + map_TotalQty.get(i));
            if (map_TotalQty.get(i) == null)
            {
                Log.e(TAG, "GetGrandTotal is null: ");
            }
            else
            {
                total = total + map_TotalQty.get(i).intValue();
            }
        }
        return total;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case GET_SCAN_PRODUCT:

                if (resultCode == RESULT_OK)
                {
//                    String productName = data.getStringExtra("scan_result");
                    String productid = data.getStringExtra("scan_result");
                    if (productid != null)
                    {
                        new  GetProductFromID(Integer.parseInt(productid)).execute();
                    }
//                    if (productName != null)
//                    {
////                        showProductByNameAddDialog(productName);
//                        if (PartName.contains(productName)) {
//                            if (!Products.contains(listProducts.get(hashMap.get(productName)))) {
//                                Products.add(listProducts.get(hashMap.get(productName)));
//                                adapter.notifyDataSetChanged();
//                            }
//                        }else{
//                            Toast.makeText(this, "Product is Not match", Toast.LENGTH_SHORT).show();
//                        }
//                        et_product_name.setText(productName);
//                    }
                }
                break;
        }
    }

    public class GetProductFromID extends AsyncTask<Void , Void, Void>
    {
        int productid = 0 ;
        public GetProductFromID(int product_id)
        {
            this.productid = product_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(TillActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getProductFromID(productid);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getProductFromID(int productid1)
    {
        Product objProduct = new Product();
        objProduct.setId(productid1);
        Call<Response> call = objInterface.GetProducts(objProduct);
        Log.e("callGet" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("code" , ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("response beauty", new Gson().toJson(response.body()));

                    Log.e("response" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {
                        Product currentproduct = response.body().getProduct();

                        if (!Products.contains(currentproduct))
                        {
                                Products.add(currentproduct);
                                adapter.notifyDataSetChanged();
                            }
                        if(pd != null & pd.isShowing())
                        {
                            pd.dismiss();
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd != null & pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(TillActivity.this , response.body().getMessage() + " Product not found.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd != null & pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(TillActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null & pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(TillActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(TillActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showProductByNameAddDialog(String product)
    {
        d_add = new Dialog(TillActivity.this);
        d_add.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_add.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        d_add.setContentView(R.layout.dialog_add_product_quntity_till);

        TextView et_product_name = (TextView) d_add.findViewById(R.id.et_product_name);
        EditText et_qty = (EditText) d_add.findViewById(R.id.et_qty);
        TextView tv_add = (TextView) d_add.findViewById(R.id.tv_add);
        TextView tv_cancel = (TextView) d_add.findViewById(R.id.tv_cancel);

        et_product_name.setText("" + product);

        tv_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (et_qty.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(TillActivity.this, "Please add quantity first.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Products.add(listProducts.get(hashMap.get(product)));
                    adapter.notifyDataSetChanged();
//                    new AddInventory(product.getProductName(), product.getProduct_number(), Integer.parseInt(et_qty.getText().toString()));
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_add.dismiss();
//                if (et_qty.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(NewInventoryActivity.this, "Please add quantity first.", Toast.LENGTH_SHORT).show();
//                } else {
//                    new AddInventoryQuantity(productName, Integer.parseInt(et_qty.getText().toString()));
//                }
            }
        });
        d_add.show();
    }
}

