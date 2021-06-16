package com.afrobiz.afrobizfind.ui.mActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobiz.afrobizfind.adapter.CurrencyAdapter;
import com.afrobiz.afrobizfind.ui.modal.Currency;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;
import com.afrobiz.afrobizfind.CropImageActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.RequestPermissionHandler;
import com.afrobiz.afrobizfind.StoreData;
import com.afrobiz.afrobizfind.adapter.ProductImagesAdapter;
import com.afrobiz.afrobizfind.adapter.UpdatedImageaAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyProductListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddProductActivity extends AppCompatActivity
{
    ProductImagesAdapter imagesAdapter;
    UpdatedImageaAdapter updateAdapter;

    private RequestPermissionHandler mRequestPermissionHandler;

    String[] permissions =
            {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    List<Image>    imglist=new ArrayList<>();
    int currencyid = 1 ;
    Cursor cursor;
    public static final int PICK_IMAGE_FROM_GALLERY = 2 , CROP_IMAGE = 10;
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.etProductDesc)
    EditText etProductDesc;

    @BindView(R.id.etProductPrice)
    EasyMoneyEditText etProductPrice;

    @BindView(R.id.spinner_currency)
    Spinner spinner_currency;

    @BindView(R.id.etProductName)
    EditText etProductName;

    @BindView(R.id.save_product)
    TextView save_product;

    @BindView(R.id.fab_gallery)
    FloatingActionButton fab_gallery;

    @BindView(R.id.relative_image)
    RelativeLayout relative_image;
    
    ProgressDialog pd , pd1;
    ApiInterface apiWithoutAuth,objInterface;
    String product_name, product_description;
    int product_price;
    int CompanyId = 0,  product_id = 0;
    List<MultipartBody.Part> update_parts = new ArrayList<>();
    List<MultipartBody.Part> parts = new ArrayList<>();
    StringBuffer deletImageBuffer =  null;
    String productid = null;

    String token = null;

    boolean update = false;

    TextView tv_toolbar;

    @BindView(R.id.rcv_update)
    RecyclerView rcv_update;

    @BindView(R.id.rcv_add)
    RecyclerView rcv_add;

    @BindView(R.id.tv_empty)
    TextView tv_empty;
    List<Currency> currencyList = new ArrayList<>();
    Users currentuser;
    CurrencyAdapter currencyAdapter;

    Product currentproduct = null;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.getSupportActionBar().hide();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);

        apiWithoutAuth = ApiClient.create_Istance();

        new GetAllCurrencies().execute();

        mRequestPermissionHandler = new RequestPermissionHandler();

        ButterKnife.bind(this);

        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);

        currentuser = new PrefrenceManager(AddProductActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token" , ""+token);
            objInterface  = ApiClient.create_InstanceAuth(token);
        }

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            CompanyId = b.getInt("company_id");

            Log.e("product_company_id",""+CompanyId);

            currentproduct = (Product) b.getSerializable("product");

            update = b.getBoolean("update");

            tv_toolbar.setText("Update Product");
        }
        else
        {
            tv_toolbar.setText("Add Product");
        }

        if(update == true)
        {
            tv_toolbar.setText("Update Product");

            if(currentproduct == null)
            {
                if(objInterface != null)
                {
                    if (ApiClient.isNetworkAvailable(AddProductActivity.this))
                    {
                        Log.e("product_id",""+product_id);

                        new GetProduct(product_id).execute();
                    }
                    else
                    {
                        Toast.makeText(AddProductActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                setProductDetails();

                Log.e("product_price",""+currentproduct.getPrice());
            }
        }
        else
        {
            tv_toolbar.setText("Add Product");
        }

        spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
            {
                Currency v  = (Currency) parent.getItemAtPosition(position);

                currencyid = v.getId();
                String name = v.getName();

                Log.e("currencyid",""+v.getId());

//                String itemName = spinner_currency.getSelectedItem().toString();
                String symbol = name.substring(name.indexOf("(")+1, name.indexOf(")"));
                etProductPrice.setCurrency(symbol);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    @OnClick(R.id.fab_gallery)
    public void addPhoto()
    {
        checkPermissions();
    }

    private void checkPermissions()
    {
        int PER_CODE = 166;

        mRequestPermissionHandler.requestPermission(this, permissions,
                PER_CODE, new RequestPermissionHandler.RequestPermissionListener()
                {
                    @Override
                    public void onSuccess()
                    {
                        openGallery();
                    }

                    @Override
                    public void onFailed()
                    {
                        Toast.makeText(AddProductActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(AddProductActivity.this)
                                .setCancelable(false)
                                .setTitle("Permission necessary")
                                .setMessage("Please grant all permissions to proceed with app.")
                                .setPositiveButton("Re-Try", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        checkPermissions();
                                    }
                                })
                                .setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", AddProductActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                        checkPermissions();
                                    }
                                })
                                .create().show();
                    }

                });

    }
    public void openGallery()
    {
        Intent i_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i_gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(i_gallery, "Select Image"),PICK_IMAGE_FROM_GALLERY);
    }
    @OnClick(R.id.Ivback)
    public void onBack()
    {
        StoreData.productList.clear();

        Log.e("compnyid",""+CompanyId);

        Intent i_back= new Intent(AddProductActivity.this, CompanyProductListActivity.class);
        i_back.putExtra("company_id", CompanyId);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        onBack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, grantResults);
    }
    public String getRealPathFromURI(Context context, Uri uri)
    {
        try
        {
            this.cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            int columnIndexOrThrow = this.cursor.getColumnIndexOrThrow("_data");
            this.cursor.moveToFirst();
            String string = this.cursor.getString(columnIndexOrThrow);
            return string;
        } finally {
            if (this.cursor != null) {
                this.cursor.close();
            }
        }
    }

    public class GetProduct extends  AsyncTask<Void , Void, Void>
    {
        int productid = 0 ;
        public GetProduct(int product_id)
        {
            this.productid = product_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(AddProductActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getProduct(productid);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getProduct(int productid1)
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
                        currentproduct = response.body().getProduct();

                        setProductDetails();

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
                        Toast.makeText(AddProductActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd != null & pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(AddProductActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null & pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(AddProductActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(AddProductActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void setProductDetails()
    {
        product_id = currentproduct.getId();
        CompanyId = currentproduct.getCompanyId();

//        int currency_id = currentproduct.getCurrency_id();


//        String symbol = .substring(name.indexOf("(")+1, name.indexOf(")"));
//        etProductPrice.setCurrency(symbol);


        etProductDesc.setText(currentproduct.getDescription());
        etProductName.setText(currentproduct.getProductName());

        Log.e("getcurencyid",""+currentproduct.getCurrency_id());

        if(currencyList != null && currencyList.size() > 0)
        {
            for(int i = 1 ; i <currencyList.size() ; i++)
            {
                if(currencyList.get(i).getId() == currentproduct.getCurrency_id())
                {
                    Log.e("curernt",""+ currencyList.get(i).getId());

                    String name = currencyList.get(i).getName();
                    spinner_currency.setSelection(currencyAdapter.getPosition(currencyList.get(i)));
                    Log.e("dfdfdff",""+ currencyAdapter.getPosition(currencyList.get(i)));

                    String symbol = name.substring(name.indexOf("(")+1, name.indexOf(")"));

                    etProductPrice.setCurrency(symbol);

                    etProductPrice.setText(String.valueOf(currentproduct.getPrice()));


                }
            }
        }

        imglist = currentproduct.getImages();

        StoreData.productList.clear();

        Log.e("imglist",""+imglist.size());

        if(imglist != null && imglist.size() > 0)
        {
            relative_image.setVisibility(View.VISIBLE);
            rcv_add.setVisibility(View. GONE);
            rcv_update.setVisibility(View.VISIBLE);
        }
        fillUpdatedImage();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PICK_IMAGE_FROM_GALLERY :

                if(resultCode == RESULT_OK)
                {
                    Uri uri = data.getData();

                    String path = getRealPathFromURI(AddProductActivity.this , uri);
                    if(path != null)
                    {
//                        galleryBmp = BitmapFactory.decodeFile(path);
//
//                        if(galleryBmp != null)
//                        {
                            Intent i_crop = new Intent(AddProductActivity.this , CropImageActivity.class);
                            i_crop.putExtra("imagepath",path);
                            i_crop.putExtra("logo",false);
                            startActivityForResult(i_crop , CROP_IMAGE);
//                        }
                    }
                    else
                    {
                        relative_image.setVisibility(View.GONE);
                    }
                }
                break;

            case CROP_IMAGE :

                if(update == true)
                {
                    if(resultCode == RESULT_OK)
                    {

                        if(data != null)
                        {
                            String s_path = data.getStringExtra("imagepath");
                            Image objimage = new Image();
                            objimage.setImagepath(s_path);

                            imglist.add(objimage);
                        }
                    }
                    else
                    {
                        if(data != null)
                        {
                            String s_path = data.getStringExtra("imagepath");

                            Image objimage = new Image();
                            objimage.setImagepath(s_path);

                            imglist.add(objimage);
                        }

                    }
                    if(updateAdapter != null)
                    {
                        updateAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        fillUpdatedImage();
                    }
                }
                else
                {

                    if(data != null)
                    {
                        if(resultCode == RESULT_OK)
                        {
                            String s_path = data.getStringExtra("imagepath");

//                        String savedImagepath = saveImage(CropImageActivity.croppedBMP);
                            Image objimage = new Image();
                            objimage.setImagepath(s_path);

                            StoreData.productList.add(s_path);
                        }
                        else
                        {
                            String s_path = data.getStringExtra("imagepath");

//                        String savedImagepath = saveImage(CropImageActivity.bmp);
                            Image objimage = new Image();
                            objimage.setImagepath(s_path);

                            StoreData.productList.add(s_path);
                        }
                    }

                    if(imagesAdapter != null)
                    {
                        imagesAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        check_list_fill();
                    }
                }
                break;
        }
    }
    public void check_list_fill()
    {
        if(StoreData.productList != null && StoreData.productList.size() > 0)
        {
            relative_image.setVisibility(View.VISIBLE);
            rcv_add.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
            rcv_update.setVisibility(View.GONE);

            imagesAdapter = new ProductImagesAdapter(AddProductActivity.this , StoreData.productList);
            rcv_add.setAdapter(imagesAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(AddProductActivity.this , LinearLayoutManager.HORIZONTAL , false);
            rcv_add.setLayoutManager(manager);
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_update.setVisibility(View.GONE);
            rcv_add.setVisibility(View.GONE);
        }
    }
    public void fillUpdatedImage()
    {
        if(imglist != null && imglist.size() > 0)
        {
            relative_image.setVisibility(View.VISIBLE);
            rcv_add.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            rcv_update.setVisibility(View.VISIBLE);

            updateAdapter = new UpdatedImageaAdapter(AddProductActivity.this , imglist);
            rcv_update.setAdapter(updateAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(AddProductActivity.this , LinearLayoutManager.HORIZONTAL , false);
            rcv_update.setLayoutManager(manager);
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_update.setVisibility(View.GONE);
            rcv_add.setVisibility(View.GONE);
        }
    }
    public void removeImage(int position)
    {
        StoreData.productList.remove(position);

        runOnUiThread(new Runnable()
        {
            public void run() {
                imagesAdapter.notifyDataSetChanged();
            }
        });
    }
    @OnClick(R.id.save_product)
    public void SaveProduct()
    {
        if (etProductName.getText().toString().equals("")) {
            etProductName.setError("Enter Product Name");
            return;
        } else {
            etProductName.setError(null);
        }
        if (etProductPrice.getText().toString().equals("")) {
            etProductPrice.setError("Enter Product Price");
            return;
        } else {
            etProductPrice.setError(null);
        }

        if (etProductDesc.getText().toString().equals("")) {
            etProductDesc.setError("Enter Product Description");
            return;
        } else {
            etProductDesc.setError(null);
        }

        if(update == true)
        {
            if(imglist != null && imglist.size() >0)
            {
                if(ApiClient.isNetworkAvailable(AddProductActivity.this))
                {
                    if(objInterface != null)
                    {
                        Log.e("product","update");
                        new UpdateProducts().execute();
                    }
                }
                else
                {
                    Toast.makeText(AddProductActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(AddProductActivity.this , "Please add at least one image of product", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            if(StoreData.productList != null && StoreData.productList.size() > 0)
            {
                if(ApiClient.isNetworkAvailable(AddProductActivity.this))
                {
                    Log.e("product","add");
                    new Add_Product().execute();
                }
                else
                {
                    Toast.makeText(AddProductActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(AddProductActivity.this , "Please add at least one image of product", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class GetAllCurrencies extends AsyncTask<Void , Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd1 = new ProgressDialog(AddProductActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getCurrencies();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getCurrencies()
    {
        try
        {
            Call<Response> getCurrency = apiWithoutAuth.GetAllCurrencies();

            getCurrency.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {
                    if(response != null && response.isSuccessful())
                    {
                        if(response.body().getResult() == 1)
                        {

                            Log.e("currency" , ""+new Gson().toJson(response.body()));
                            if(pd1 != null && pd1.isShowing())
                            {
                                pd1.dismiss();
                            }

                            currencyList = response.body().getCurrencies();

                            if(currencyList != null && currencyList.size() > 0)
                            {
                                currencyAdapter = new CurrencyAdapter(AddProductActivity.this,
                                        android.R.layout.simple_spinner_item, currencyList);
                                spinner_currency.setAdapter(currencyAdapter);
                            }

                            if(currentproduct != null)
                            {
                                if(currencyList != null && currencyList.size() > 0)
                                {
                                    for(int i = 1 ; i <currencyList.size() ; i++)
                                    {
                                        if(currencyList.get(i).getId() == currentproduct.getCurrency_id())
                                        {
                                            Log.e("curernt",""+ currencyList.get(i).getId());

                                            String name = currencyList.get(i).getName();
                                            spinner_currency.setSelection(currencyAdapter.getPosition(currencyList.get(i)));
                                            Log.e("dfdfdff",""+ currencyAdapter.getPosition(currencyList.get(i)));

                                            String symbol = name.substring(name.indexOf("(")+1, name.indexOf(")"));

                                            etProductPrice.setCurrency(symbol);

                                            etProductPrice.setText(String.valueOf(currentproduct.getPrice()));
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            if(pd1 != null && pd1.isShowing())
                            {
                                pd1.dismiss();
                            }
                        }
                    }
                    else
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(AddProductActivity.this ,getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd1 != null && pd1.isShowing())
                    {
                        pd1.dismiss();
                    }
                    Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {
            Log.e("excption" , e.toString());
        }

    }

    public class Add_Product extends AsyncTask<Void ,Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(AddProductActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            addCompany();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public  void addCompany()
    {
        try
        {
            Log.e("body","product_add body");

            String name = etProductName.getText().toString().trim();
            String description = etProductDesc.getText().toString().trim();
            String price = etProductPrice.getText().toString().trim();

            RequestBody p_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody P_desc = RequestBody.create(MediaType.parse("multipart/form-data"), description);
            RequestBody P_price = RequestBody.create(MediaType.parse("multipart/form-data"), price);
            RequestBody p_CompanyId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(CompanyId));
            RequestBody p_Currencyid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(currencyid));

            Log.e("compnyid111",""+CompanyId);

            Log.e("dck",""+p_name+" "+P_desc+" "+" "+P_price+" "+p_CompanyId+" "
                    +StoreData.productList.size() +" "+p_Currencyid);
            if(StoreData.productList != null && StoreData.productList.size() > 0)
            {
                for(int i=0 ; i < StoreData.productList.size() ; i++)
                {
                    parts.add(prepareFilePart("images[]", StoreData.productList.get(i)));
                    Log.e("parts" , ""+parts.get(i).body().toString());
                }
            }

            Call<Response> call = objInterface.Create_Product(parts, p_name,P_desc,  P_price, p_CompanyId, p_Currencyid);

            Log.e("call",""+call.request());
            call.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {
                    if(response != null && response.isSuccessful())
                    {
                        if(response.body().getResult() == 1)
                        {
                            Log.e("response beauty", new Gson().toJson(response.body()));

                            Log.e("response" , ""+response.body().getProducts());

                            Product resultProduct = response.body().getProduct();

                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            if(resultProduct != null)
                            {

                                if(pd != null && pd.isShowing())
                                {
                                    pd.dismiss();
                                }
                                StoreData.productList.clear();
                                Toast.makeText(AddProductActivity.this , "Product Added Successfully" , Toast.LENGTH_SHORT).show();

                                onBack();
                            }
                            else
                            {
                                if(pd != null && pd.isShowing())
                                {
                                    pd.dismiss();
                                }
                            }

                        }
                        else if(response.body().getResult() == 0)
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }

                            Toast.makeText(AddProductActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }

                            Toast.makeText(AddProductActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }

                        Toast.makeText(AddProductActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(AddProductActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e)
        {

        }

    }
    private MultipartBody.Part prepareFilePart(String partName,     String filepath)
    {
        File file = new File(filepath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
    public class UpdateProducts extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(AddProductActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            updateProduct();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void updateProduct()
    {
        try
        {
            String name = etProductName.getText().toString().trim();
            String description = etProductDesc.getText().toString().trim();
            String price = etProductPrice.getText().toString().trim();

            RequestBody p_product_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(product_id));
            RequestBody p_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody P_desc = RequestBody.create(MediaType.parse("multipart/form-data"), description);
            RequestBody P_price = RequestBody.create(MediaType.parse("multipart/form-data"), price);
            RequestBody p_CompanyId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(CompanyId));
            RequestBody p_deleteimage = null;
            RequestBody p_Currencyid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(currencyid));

            if(deletImageBuffer != null)
            {
                Log.e("deleteimage",""+deletImageBuffer.toString());
                p_deleteimage = RequestBody.create(MediaType.parse("multipart/form-data"), deletImageBuffer.toString());
            }

            Log.e("size" , ""+imglist.size());

            if(imglist!= null && imglist.size() > 0)
            {
                for(int i=0 ; i < imglist.size() ; i++)
                {
                    Log.e("id" , ""+imglist.get(i).getId());

                    if(imglist.get(i).getId() == 0)
                    {
                        update_parts.add(prepareFilePart("images[]", imglist.get(i).getImagepath()));
                    }
                    else
                    {

                    }
                }
            }

            Call<Response> call = objInterface.Update_Product(update_parts, p_product_id,
                    p_name,P_desc,  P_price, p_CompanyId , p_deleteimage, p_Currencyid);

            Log.e("callupdate",""+call.request());

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
                            Product resultCompany = response.body().getProduct();

                            if(resultCompany != null)
                            {
                                if(pd != null && pd.isShowing())
                                {
                                    pd.dismiss();
                                }
                                StoreData.productList.clear();

                                Toast.makeText(AddProductActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                                onBack();
                            }
                            else
                            {
                                if(pd != null && pd.isShowing())
                                {
                                    pd.dismiss();
                                }
                            }

                        }
                        else if(response.body().getResult() == 0)
                        {

                            List<String> er = response.body().getErrors().getImages();

                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            if(er != null && er.size() >0)
                            {
                                Toast.makeText(AddProductActivity.this , er.get(0) , Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(AddProductActivity.this , response.message() , Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }

                            Toast.makeText(AddProductActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();

                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }

                        Toast.makeText(AddProductActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(AddProductActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e)
        {

        }
    }
    public void removeForUpdateImage(int position)
    {
        if(deletImageBuffer == null)
        {
            deletImageBuffer  = new StringBuffer();
            deletImageBuffer.append(String.valueOf(imglist.get(position).getId()));
        }
        else
        {
            deletImageBuffer.append(","+String.valueOf(imglist.get(position).getId()));
        }

        imglist.remove(position);

        runOnUiThread(new Runnable()
        {
            public void run()
            {
                updateAdapter.notifyDataSetChanged();
            }
        });
    }

}
