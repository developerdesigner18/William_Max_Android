package com.afrobiz.afrobizfind.ui.inventory;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.afrobiz.afrobizfind.ui.mActivity.AddProductActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Inventory.InventoryActivity;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Inventory;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.scanner.FullScannerActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class NewInventoryActivity extends AppCompatActivity
{
    private static final int ZXING_CAMERA_PERMISSION = 10, GET_SCAN_PRODUCT = 866;
    ImageView Ivback, img_scan;
    TextView tv_add, tv_empty, tv_total;
    LinearLayout layout_serach_dropdown, layout_new_list;
    RecyclerView rcv_inventory, rcv_saerch_product;
    EditText et_search;
    List<Inventory> inventoryProductList = new ArrayList<>();
    NewInventoryAdapter adapter;
    Users currentuser;
    ProgressDialog pd3, pd;
    int company_id = 0;
    List<Product> product_list = new ArrayList<>();
    String token = null;
    ApiInterface objInterface;
    List<Inventory> listInventory = new ArrayList<>();
    LinearLayout layout_header;
    Dialog d_add;

    SearchInventoryAdapter searchInventoryAdapter;
    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_inventory);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        img_scan = (ImageView) findViewById(R.id.img_scan);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_total = (TextView) findViewById(R.id.tv_total);
        et_search = (EditText) findViewById(R.id.et_search);
        rcv_inventory = (RecyclerView) findViewById(R.id.rcv_inventory);
        rcv_saerch_product = (RecyclerView) findViewById(R.id.rcv_saerch_product);
        layout_header = (LinearLayout) findViewById(R.id.layout_header);
        layout_serach_dropdown = (LinearLayout) findViewById(R.id.layout_serach_dropdown);
        layout_new_list = (LinearLayout) findViewById(R.id.layout_new_list);

        layout_serach_dropdown.setVisibility(View.GONE);
        tv_add.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();

        if (b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com", "" + company_id);
            Toast.makeText(getApplicationContext(), "comid"+company_id, Toast.LENGTH_SHORT).show();;
        }

        currentuser = new PrefrenceManager(NewInventoryActivity.this).getCurrentuser();

        if (currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(NewInventoryActivity.this))
                {
                    Log.e("company_id", "" + company_id);
//                    new GetInventoryHistoryList().execute();
                    new GetCompanyProductList(company_id).execute();
                }
                else
                {
                    Toast.makeText(NewInventoryActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (inventoryProductList != null && inventoryProductList.size() > 0)
        {
            tv_total.setText(""+inventoryProductList.size());
            rcv_inventory.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
//
//            layout_new_list.setVisibility(View.VISIBLE);

//            layout_header.setVisibility(View.GONE);
        }
        else
        {
            tv_total.setText(""+0);
            tv_empty.setVisibility(View.VISIBLE);
            rcv_inventory.setVisibility(View.GONE);
//
//            layout_new_list.setVisibility(View.INVISIBLE);
        }

        adapter = new NewInventoryAdapter(NewInventoryActivity.this, inventoryProductList);
        rcv_inventory.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(NewInventoryActivity.this);
        rcv_inventory.setLayoutManager(manager);

        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(product_list != null && product_list.size() > 0)
                {
                    if(s.length() > 0)
                    {
                        layout_serach_dropdown.setVisibility(View.VISIBLE);
                        filterProduct(s.toString());
                    }
                    else
                    {
                        layout_serach_dropdown.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Intent i_back = new Intent(NewInventoryActivity.this, AddNewInventory.class);
//                startActivity(i_back);
//                finish();
            }
        });
        img_scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
                Intent i_back = new Intent(NewInventoryActivity.this, InventoryActivity.class);
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
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_random_scale);

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
                layout_serach_dropdown.setVisibility(View.VISIBLE);
                Log.e("filter",""+new Gson().toJson(p));
                listTemp.add(p);
            }
        }
        layout_serach_dropdown.setVisibility(View.VISIBLE);
        Log.e("searclistsize",""+listTemp.size());
        searchInventoryAdapter.filterList(listTemp);
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
            case ZXING_CAMERA_PERMISSION:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case GET_SCAN_PRODUCT:
                if (resultCode == RESULT_OK)
                {
                    String productid = data.getStringExtra("scan_result");
                    if (productid != null)
                    {
                        new GetProductFromID(Integer.parseInt(productid)).execute();
                    }
                }
                break;
        }
    }

    public class GetProductFromID extends  AsyncTask<Void , Void, Void>
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

            pd = new ProgressDialog(NewInventoryActivity.this);
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
        Call<Response>  call = objInterface.GetProducts(objProduct);
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

                        showProductAddDialog(currentproduct);

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
                        Toast.makeText(NewInventoryActivity.this , response.body().getMessage() + " Product not found.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd != null & pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(NewInventoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null & pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(NewInventoryActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(NewInventoryActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addQuantityToProduct(Product product , int qunatity)
    {
        Call<Response> call = objInterface.AddNewInventory(product.getId(),
                null , qunatity);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {

            }
        });
    }
    public void showProductAddDialog(Product product)
    {
        et_search.setText("");
        layout_serach_dropdown.setVisibility(View.GONE);

        d_add = new Dialog(NewInventoryActivity.this);
        d_add.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_add.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);

        d_add.getWindow().setWindowAnimations(R.style.enter_exit_animate);

        d_add.setContentView(R.layout.dialog_add_inventory_quntity);
        EditText et_product_name = (EditText) d_add.findViewById(R.id.et_product_name);
        EditText et_qty = (EditText) d_add.findViewById(R.id.et_qty);
        TextView tv_add = (TextView) d_add.findViewById(R.id.tv_add);
        TextView tv_cancel = (TextView) d_add.findViewById(R.id.tv_cancel);

        et_product_name.setText(product.getProductName());

        tv_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (et_qty.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(NewInventoryActivity.this, "Please add quantity first.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new AddInventory(product.getId(), product.getProduct_number(), Integer.parseInt(et_qty.getText().toString())).execute();
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
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
    public void showProductByNameAddDialog(String product)
    {
        d_add = new Dialog(NewInventoryActivity.this);
        d_add.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_add.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        d_add.getWindow().setWindowAnimations(R.style.enter_exit_animate);

        d_add.setContentView(R.layout.dialog_add_inventory_quntity);
        EditText et_product_name = (EditText) d_add.findViewById(R.id.et_product_name);
        EditText et_qty = (EditText) d_add.findViewById(R.id.et_qty);
        TextView tv_add = (TextView) d_add.findViewById(R.id.tv_add);
        TextView tv_cancel = (TextView) d_add.findViewById(R.id.tv_cancel);

        et_product_name.setText(product);

        tv_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (et_qty.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(NewInventoryActivity.this, "Please add quantity first.", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    new AddInventory(product.getProductName(), product.getProduct_number(), Integer.parseInt(et_qty.getText().toString()));
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
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

            pd3 = new ProgressDialog(NewInventoryActivity.this);
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
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response> call = objInterface.GetAllCompanyProducts(objCompany);

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
//                            rcv_saerch_product.setVisibility(View.VISIBLE);

                            searchInventoryAdapter = new SearchInventoryAdapter(NewInventoryActivity.this, product_list);
                            LinearLayoutManager productlayoutManager = new LinearLayoutManager(NewInventoryActivity.this);
                            rcv_saerch_product.setLayoutManager(productlayoutManager);
                            rcv_saerch_product.setAdapter(searchInventoryAdapter);
                            adapter.notifyDataSetChanged();

                            runLayoutAnimation(rcv_saerch_product);
                        }
                        else
                        {
//                            tv_empty.setVisibility(View.VISIBLE);
                            layout_serach_dropdown.setVisibility(View.GONE);
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
                        Toast.makeText(NewInventoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(NewInventoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(NewInventoryActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(NewInventoryActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public class SearchInventoryAdapter extends RecyclerView.Adapter<SearchInventoryAdapter.MyViewHolder>
    {
        Context context;
        List<Product> list_search;

        public SearchInventoryAdapter(Context newInventoryActivity, List<Product> product_list)
        {
            context = newInventoryActivity;
            list_search = product_list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_product,parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
        {
            holder.tv_product.setText(list_search.get(position).getProductName());
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showProductAddDialog(list_search.get(position));
                }
            });
        }
        @Override
        public int getItemCount()
        {
            return list_search.size();
        }
        public void filterList(List<Product> filterdNames)
        {
            this.list_search = filterdNames;
            notifyDataSetChanged();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView tv_product;

            public MyViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tv_product = (TextView) itemView.findViewById(R.id.tv_product);
            }
        }
    }
    public class AddInventory extends AsyncTask<Void, Void, Void>
    {
        String barcode;
        int quantity;
        int product_id;

        public AddInventory(int product_id1, String barcode1, int parseInt)
        {
            product_id = product_id1;
            quantity = parseInt;
            barcode = barcode1;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(NewInventoryActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            addQuantityToProduct(product_id, barcode, quantity);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    private void addQuantityToProduct(int  productID, String barcode, int quantity)
    {
        Call<Response> call = objInterface.AddNewInventory(productID , barcode, quantity);
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null)
                {
                    if(response.isSuccessful())
                    {
                        if(response.body() != null)
                        {
                            if(response.body().getResult() == 1)
                            {
                                if(d_add != null && d_add.isShowing())
                                {
                                    d_add.dismiss();
                                }
                                if(pd3 != null & pd3.isShowing())
                                {
                                    pd3.dismiss();
                                }

                                tv_empty.setVisibility(View.GONE);
                                rcv_inventory.setVisibility(View.VISIBLE);

                                if(response.body().getInventorydata() != null)
                                {
                                    inventoryProductList.add(response.body().getInventorydata());

                                    adapter = new NewInventoryAdapter(NewInventoryActivity.this, inventoryProductList);
                                    rcv_inventory.setAdapter(adapter);
                                    LinearLayoutManager manager = new LinearLayoutManager(NewInventoryActivity.this);
                                    rcv_inventory.setLayoutManager(manager);

                                    adapter.notifyItemInserted(inventoryProductList.size()-1);
                                    adapter.notifyDataSetChanged();

                                    runLayoutAnimation(rcv_inventory);

                                    CountTotalItems();

                                    Toast.makeText(NewInventoryActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                if(d_add != null && d_add.isShowing())
                                {
                                    d_add.dismiss();
                                }
                                if(pd3 != null & pd3.isShowing())
                                {
                                    pd3.dismiss();
                                }
                                Toast.makeText(NewInventoryActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {

                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(NewInventoryActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(NewInventoryActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(NewInventoryActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void CountTotalItems()
    {
        int count = 0 ;

        if(inventoryProductList != null && inventoryProductList.size() > 0 )
        {
            for (int i = 0 ; i < inventoryProductList.size() ; i ++)
            {
                if (inventoryProductList.get(i) != null)
                {
                    count = count + inventoryProductList.get(i).getQuantity();
                }
            }
        }
        tv_total.setText(""+count);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(NewInventoryActivity.this, InventoryActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }
//    public class AddNewInventory extends AsyncTask<Void, Void, Void>
//    {
////        int id= 0 ;
//
////        public GetCurrentInventoryList(int company_id)
////        {
////            this.id = company_id;
////        }
//
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//
//            pd3 = new ProgressDialog(InventoryHistoryActivity.this);
//            pd3.setCancelable(false);
//            pd3.setMessage(getString(R.string.please_wait));
//            pd3.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids)
//        {
//            getInventoryHistory();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid)
//        {
//            super.onPostExecute(aVoid);
//        }
//    }
//    public void addNew_Inventrory()
//    {
////        Company objCompany = new Company();
////        objCompany.setId(id);
//        Call<Response> call = objInterface.AddNewInventory();
//
//        Log.e("callGet" , ""+call.request());
//        call.enqueue(new Callback<Response>()
//        {
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
//            {
//                Log.e("getcode" , ""+response.code());
//
//                if(response != null && response.isSuccessful())
//                {
//                    Log.e("getresponse beauty", new Gson().toJson(response.body()));
//
//                    Log.e("getresponse" , ""+response.body().getResult());
//
//                    if(response.body().getResult() == 1)
//                    {
//
//                        listInventory = response.body().getInventoryList();
//
//                        if(listInventory !=null && listInventory.size() >0)
//                        {
//                            Log.e("productlist",""+listInventory.size());
//                            tv_empty.setVisibility(View.GONE);
//                            rcv_inventory_history.setVisibility(View.VISIBLE);
//
//                            adapter = new InventoryHistoryAdapter(InventoryHistoryActivity.this, listInventory);
//                            LinearLayoutManager manager = new LinearLayoutManager(InventoryHistoryActivity.this);
//                            rcv_inventory_history.setLayoutManager(manager);
//                            rcv_inventory_history.setAdapter(adapter);
//
//                            adapter.notifyDataSetChanged();
//                        }
//                        else
//                        {
//                            tv_empty.setVisibility(View.VISIBLE);
//                            rcv_inventory_history.setVisibility(View.GONE);
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
//                        Toast.makeText(InventoryHistoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        if(pd3 != null & pd3.isShowing())
//                        {
//                            pd3.dismiss();
//                        }
//                        Toast.makeText(InventoryHistoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//                else
//                {
//                    if(pd3 != null & pd3.isShowing())
//                    {
//                        pd3.dismiss();
//                    }
//                    Toast.makeText(InventoryHistoryActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<Response> call, Throwable t)
//            {
//                if(pd3 != null & pd3.isShowing())
//                {
//                    pd3.dismiss();
//                }
//                Toast.makeText(InventoryHistoryActivity.this , t.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
