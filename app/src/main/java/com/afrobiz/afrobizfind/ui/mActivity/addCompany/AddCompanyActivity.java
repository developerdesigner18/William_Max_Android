package com.afrobiz.afrobizfind.ui.mActivity.addCompany;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.ui.modal.Data;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.CropImageActivity;
import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.RequestPermissionHandler;
import com.afrobiz.afrobizfind.StoreData;
import com.afrobiz.afrobizfind.adapter.CompanyImageAdapter;
import com.afrobiz.afrobizfind.adapter.SpinAdapter;
import com.afrobiz.afrobizfind.adapter.UpdatedImageaAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Category;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.paypal.PaypalPaymentActivity;
import com.afrobiz.afrobizfind.ui.mActivity.LoginActivity;
import com.afrobiz.afrobizfind.ui.mActivity.MyCompanies.MyCompaniesActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;

public class AddCompanyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    AlertDialog alert;
    StringBuffer deletImageBuffer =  null;
    List<Image>   imglist;
    List<MultipartBody.Part> parts = new ArrayList<>();
    List<MultipartBody.Part> update_parts = new ArrayList<>();
    Cursor cursor;
    public static final int PICK_IMAGE_FROM_GALLERY = 12, PICK_LOGO_FROM_GALLERY = 122, CROP_IMAGE=909;
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.fab_gallery)
    ImageView fab_gallery;

    @BindView(R.id.tv_delete)
    TextView tv_delete;

    @BindView(R.id.tv_Suspend)
    TextView tv_Suspend;

    @BindView(R.id.tv_Active)
    TextView tv_Active;

    @BindView(R.id.tv_Monday_opentime)
    TextView tvMondayOpentime;

    @BindView(R.id.tv_Monday_closetime)
    TextView tvMondayClosetime;

    @BindView(R.id.tv_Tuesday_opentime)
    TextView tvTuesdayOpentime;

    @BindView(R.id.tv_Tuesday_closetime)
    TextView tvTuesdayClosetime;

    @BindView(R.id.tv_Wednesday_opentime)
    TextView tvWednesdayOpentime;

    @BindView(R.id.tv_Wednesday_closetime)
    TextView tvWednesdayClosetime;

    @BindView(R.id.tv_Thusday_opentime)
    TextView tvThursdayOpentime;

    @BindView(R.id.tv_Thusday_closetime)
    TextView tvThursdayClosetimeme;

    @BindView(R.id.tv_Friday_opentime)
    TextView tvFridayOpentime;

    @BindView(R.id.tv_Friday_closetime)
    TextView tvFridayClosetime;

    @BindView(R.id.tv_Saturday_opentime)
    TextView tvSaturdayOpentime;

    @BindView(R.id.tv_Saturday_closetime)
    TextView tvSaturdayClosetime;

    @BindView(R.id.tv_Sunday_opentime)
    TextView tvSundayOpentime;

    @BindView(R.id.tv_Sunday_closetime)
    TextView tvSundayClosetime;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    SpinAdapter adapter;
    @BindView(R.id.Sp_compnayCategory)
    Spinner SpCompnayCategory;

    @BindView(R.id.et_company_ethos)
    EditText et_company_ethos;

    @BindView(R.id.et_company_name)
    EditText et_company_name;

    @BindView(R.id.et_country)
    EditText et_country;

    @BindView(R.id.et_company_app_link)
    EditText et_company_app_link;

    String companyLogoPath = null , updatelogopath = null;

    @BindView(R.id.et_building_no_name)
    EditText et_building_no_name;

    @BindView(R.id.et_city)
    EditText et_city;

    @BindView(R.id.et_address)
    EditText et_address;

    @BindView(R.id.et_postcode)
    EditText et_postcode;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_contact_number)
    EditText et_contact_number;

    @BindView(R.id.et_website)
    EditText et_website;

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.et_customer_number)
    EditText et_customer_number;

    @BindView(R.id.rcv_update)
    RecyclerView rcv_update;

    @BindView(R.id.rcv_add)
    RecyclerView rcv_add;

    @BindView(R.id.tv_empty)
    TextView tv_empty;

    @BindView(R.id.card)
    CardView card;

    @BindView(R.id.tv_paypal)
    TextView tv_paypal;

    private int mHour, mMinute;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private RequestPermissionHandler mRequestPermissionHandler;
    String[] permissions =
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
    String CompanyCategory = null;

    int cat_id = 0 ;

    @BindView(R.id.tvMyCompanies)
    TextView tvMyCompanies;

    @BindView(R.id.tv_toolbar)
    TextView tv_toolbar;

    @BindView(R.id.frame_logo_image)
    RelativeLayout frame_logo_image;

    @BindView(R.id.img_logo)
    ImageView img_logo;

    @BindView(R.id.img_cancel)
    ImageView img_cancel;

    @BindView(R.id.tv_paypal_mandatory)
    TextView tv_paypal_mandatory;

    @BindView(R.id.fab_logo_gallery)
    FloatingActionButton fab_logo_gallery;

    ApiInterface objInterface;
    ApiInterface apiWithoutAuth;
    boolean isPayMandatory = false;

    boolean update = false;
    List<Category> categoryList = new ArrayList<>();
    String CompanyName, building_number, address_line_1, city, contact_number, email, app_link,country , customer_number,
            website, company_ethos, userName, postcode, expiryDate, paymentNonce;

    ProgressDialog pd, pd1, pd2, pd3, pd4, pd5, pd6;
    int company_id = 0;
    String token = null;
    Users currentuser;

    @BindView(R.id.layout_delete_suspend)
            LinearLayout layout_delete_suspend;

    EditText etpass;

    TextView tv_title1,tvcancel , tv_title, tv_suspend, tv_active_btn;

    UpdatedImageaAdapter updateAdapter;
    CompanyImageAdapter imagesAdapter;
    int updateCompanyId =0, updateCat_id= 0 ;
    int cyear , cmonth, cday;

    @BindView(R.id.img_reset_mon)
    ImageView img_reset_mon;
    @BindView(R.id.img_reset_tue)
    ImageView img_reset_tue;
    @BindView(R.id.img_reset_wed)
    ImageView img_reset_wed;
    @BindView(R.id.img_reset_thu)
    ImageView img_reset_thu;
    @BindView(R.id.img_reset_fri)
    ImageView img_reset_fri;
    @BindView(R.id.img_reset_sat)
    ImageView img_reset_sat;
    @BindView(R.id.img_reset_sun)
    ImageView img_reset_sun;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    DisplayMetrics metrics;
    int screenwidth, screenheight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        metrics = getResources().getDisplayMetrics();
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;

        Calendar c= Calendar.getInstance();
        cyear = c.get(Calendar.YEAR);
        cmonth = c.get(Calendar.MONTH)+1;
        cday = c.get(Calendar.DAY_OF_MONTH);
        Log.e("calender",""+cyear+"/"+cmonth+"/"+cday);

        apiWithoutAuth = ApiClient.create_Istance();

        mRequestPermissionHandler = new RequestPermissionHandler();

        currentuser = new PrefrenceManager(AddCompanyActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token" , ""+token);
            objInterface  = ApiClient.create_InstanceAuth(token);
            
            if(apiWithoutAuth != null)
            {
                if(ApiClient.isNetworkAvailable(AddCompanyActivity.this))
                {
                    new CategoryList().execute();
                }
                else
                {
                    Toast.makeText(AddCompanyActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            update = b.getBoolean("update");
            company_id = b.getInt("company_id");
        }

        if(new PrefrenceManager(AddCompanyActivity.this).getLogout()== true)
        {
            setContentView(R.layout.activity_add_company);
            ButterKnife.bind(this);

            setParam();
            SpCompnayCategory.setOnItemSelectedListener(this);
        }
        else
        {
            showDialog();
        }
        if(ApiClient.isNetworkAvailable(AddCompanyActivity.this))
        {
            new GetPaymentStatus().execute();
        }
        else
        {
            Toast.makeText(AddCompanyActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

        if(update == true)
        {
            tv_toolbar.setText("Update Company");
            layout_delete_suspend.setVisibility(View.VISIBLE);

            if (ApiClient.isNetworkAvailable(AddCompanyActivity.this))
            {
                Log.e("company_id",""+company_id);
                if(objInterface != null)
                {
                    new GetCompany(company_id).execute();
                }
            }
            else
            {
                Toast.makeText(AddCompanyActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            tv_toolbar.setText("Add Company");
            layout_delete_suspend.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();

//        if(update == true)
//        {
//            tv_toolbar.setText("Update Company");
//            layout_delete_suspend.setVisibility(View.VISIBLE);
//
//            if (ApiClient.isNetworkAvailable(AddCompanyActivity.this))
//            {
//                Log.e("company_id",""+company_id);
//                if(objInterface != null)
//                {
//                    new GetCompany(company_id).execute();
//                }
//            }
//            else
//            {
//                Toast.makeText(AddCompanyActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    @OnClick(R.id.tv_paypal)
    public void openPaypal()
    {
        if(update == false && company_id == 0)
        {
            Toast.makeText(AddCompanyActivity.this, "Please save company details first.",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent i_paypal = new Intent(AddCompanyActivity.this , PaypalPaymentActivity.class);
            if(update == true)
            {
                i_paypal.putExtra("com_id",updateCompanyId);
            }
            else
            {
                i_paypal.putExtra("com_id",company_id);
            }
            startActivity(i_paypal);
        }
    }






    public void setParam()
    {
        CardView.LayoutParams p1  = (CardView.LayoutParams) img_logo.getLayoutParams();
        p1.width = screenwidth * 250/1080;
        p1.height = screenheight * 250 / 1920;
//            p1.setMargins(0, screenheight*50/1920 , screenwidth * 20 /1080 , 0);
        img_logo.setLayoutParams(p1);

        RelativeLayout.LayoutParams pa1  = (RelativeLayout.LayoutParams) card.getLayoutParams();
        pa1.width = screenwidth * 250/1080;
        pa1.height = screenheight * 250 / 1920;
        pa1.setMargins(0, screenheight*20/1920 , screenwidth * 20 /1080 , 0);
        card.setLayoutParams(pa1);
//            img_path1.setLayoutParams(p1);

        RelativeLayout.LayoutParams p1s  = (RelativeLayout.LayoutParams) img_cancel.getLayoutParams();
        p1s.width = screenwidth * 70/1080;
        p1s.height = screenheight * 70 / 1920;
        img_cancel.setLayoutParams(p1s);

        RelativeLayout.LayoutParams p1zs  = (RelativeLayout.LayoutParams) frame_logo_image.getLayoutParams();
        p1zs.width = screenwidth * 250/1080;
        p1zs.height = screenheight * 250 / 1920;
        frame_logo_image.setLayoutParams(p1zs);
    }
    public void removeForUpdateImage(int position)
    {
        if(deletImageBuffer == null)
        {
            deletImageBuffer  = new StringBuffer();
            deletImageBuffer.append(imglist.get(position).getId());
        }
        else
        {
            deletImageBuffer.append(","+ imglist.get(position).getId());
        }

        imglist.remove(position);

        Log.e("dlete",""+deletImageBuffer.toString()+"   "+imglist.size());

        runOnUiThread(new Runnable()
        {
            public void run()
            {
                updateAdapter.notifyDataSetChanged();
            }
        });
    }

    public void check_list_fill()
    {
        if(StoreData.companyImagelist != null && StoreData.companyImagelist.size() > 0)
        {
            rcv_add.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
            rcv_update.setVisibility(View.GONE);

            imagesAdapter = new CompanyImageAdapter(AddCompanyActivity.this , StoreData.companyImagelist);
            rcv_add.setAdapter(imagesAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(AddCompanyActivity.this , LinearLayoutManager.HORIZONTAL , false);
            rcv_add.setLayoutManager(manager);
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_update.setVisibility(View.GONE);
            rcv_add.setVisibility(View.GONE);
        }
    }




    public class GetPaymentStatus extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd6 = new ProgressDialog(AddCompanyActivity.this);
            pd6.setCancelable(false);
            pd6.setMessage(getString(R.string.please_wait));
            pd6.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getPaymentStatus();
            return null;
        }

        private void getPaymentStatus()
        {
            Call<Response> call = apiWithoutAuth.Get_PaymentStatus();
            Log.e("call",""+call.request());
            call.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {
                    Log.e("response555", ""+response.code());

                    if(response != null && response.isSuccessful())
                    {
                        Log.e("response beauty", new Gson().toJson(response.body()));

                        Log.e("response" , ""+response.body().getResult());

                        if(response.body().getResult() == 1)
                        {

                            Data data = response.body().getData();
                            if(data != null)
                            {
                                int code = data.getOnOff();
                                if(code == 0)
                                {
                                    isPayMandatory = false;
                                    tv_paypal_mandatory.setText("PAYMENT NOT MANDATORY");
//                                    tv_paypal.setEnabled(false);
//                                    Toast.makeText(AddCompanyActivity.this, "PAYMENT NOT MANDATORY", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    isPayMandatory = true;
                                    tv_paypal_mandatory.setText("PAYMENT MANDATORY");
//                                    tv_paypal.setEnabled(true);
//                                    Toast.makeText(AddCompanyActivity.this, "PAYMENT MANDATORY", Toast.LENGTH_SHORT).show();
                                }


                            }

                            if(pd6 != null && pd6.isShowing())
                            {
                                pd6.dismiss();
                            }
                        }
                        else if(response.body().getResult() == 0)
                        {
                            if(pd6 != null && pd6.isShowing())
                            {
                                pd6.dismiss();
                            }
                            Toast.makeText(AddCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(pd6 != null && pd6.isShowing())
                            {
                                pd6.dismiss();
                            }
                            Toast.makeText(AddCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if(pd6 != null && pd6.isShowing())
                        {
                            pd6.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd6 != null && pd6.isShowing())
                    {
                        pd6.dismiss();
                    }
                    Toast.makeText(AddCompanyActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public class CategoryList extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(AddCompanyActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            ListCategory();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void ListCategory()
    {
        Call<Response> call = apiWithoutAuth.GetAll_categories();
        Log.e("call" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("response555", ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("response beauty", new Gson().toJson(response.body()));

                    Log.e("response" , ""+response.body().getResult());
                    if(response.body().getResult() == 1)
                    {
                        categoryList = response.body().getCategories();
                        if(categoryList != null && categoryList.size() > 0)
                        {

                            categoryList.remove(0);
                            adapter = new SpinAdapter(AddCompanyActivity.this,
                                    android.R.layout.simple_spinner_item, categoryList);
                            SpCompnayCategory.setAdapter(adapter);
                        }

                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }

                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }

                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }

                    Toast.makeText(AddCompanyActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(AddCompanyActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    @OnClick(R.id.img_cancel)
    public void removeLogo()
    {
        companyLogoPath = null;
        frame_logo_image.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvMyCompanies)
    public void ClickMyCompanies()
    {
        Intent intent = new Intent(AddCompanyActivity.this, MyCompaniesActivity.class);
        intent.putExtra("from_add_company", true);
        startActivity(intent);
        finish();
    }

    public class ActiveSuspendCompany extends AsyncTask<Void, Void, Void>
    {
        int id= 0,  status = 0 ;

        public ActiveSuspendCompany(int id, int sttaus)
        {
            this.id = id;
            this.status = sttaus;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd5 = new ProgressDialog(AddCompanyActivity.this);
            pd5.setCancelable(false);
            pd5.setMessage(getString(R.string.please_wait));
            pd5.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            active_suspend(id, status);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public void active_suspend(int id, int status)
    {
        Company objOffers = new Company();
        objOffers.setId(id);
        objOffers.setStatus(status);

        Call<Response>  call = objInterface.ActiveSuspendCompany(objOffers);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {
                        Toast.makeText(AddCompanyActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        if(status == 1)
                        {
                            tv_Active.setVisibility(View.VISIBLE);
                            tv_Suspend.setVisibility(View.GONE);
                        }
                        else
                        {
                            tv_Suspend.setVisibility(View.VISIBLE);
                            tv_Active.setVisibility(View.GONE);
                        }
//                        Intent i_start = new Intent(AddCompanyActivity.this , MainActivity.class);
//                        startActivity(i_start);
//                        finish();
                        if(pd5 != null & pd5.isShowing())
                        {
                            pd5.dismiss();
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd5 != null & pd5.isShowing())
                        {
                            pd5.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd5 != null & pd5.isShowing())
                        {
                            pd5.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd5 != null & pd5.isShowing())
                    {
                        pd5.dismiss();
                    }
                    Toast.makeText(AddCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd5 != null & pd5.isShowing())
                {
                    pd5.dismiss();
                }
                Toast.makeText(AddCompanyActivity.this , t.toString() , Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.tv_Active)
    public void ActiveCompany()
    {
        ActiveDialog();
    }

    @OnClick(R.id.tv_Suspend)
    public void SuspendCompany()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_suspend_account, null);

         etpass = dialogLayout.findViewById(R.id.etPass);
         tvcancel = dialogLayout.findViewById(R.id.tv_cancel);
         tv_title = dialogLayout.findViewById(R.id.tv_title);
         tv_title1 = dialogLayout.findViewById(R.id.tv_title1);
         tv_suspend = dialogLayout.findViewById(R.id.tv_suspend);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        alert = builder.create();

        tvcancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                etpass.setText("");
                alert.cancel();
            }
        });
        tv_suspend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String password = etpass.getText().toString().trim();
                if(!TextUtils.isEmpty(password))
                {
                    if(password.equals(currentuser.getPassword()))
                    {
//                        new DeleteCompany(company_id).execute();

                        tv_title.setText("Your Company Has been Suspended.");

                        etpass.setVisibility(View.GONE);

                        tvcancel.setVisibility(View.GONE);
                        tv_suspend.setVisibility(View.GONE);
                        tv_title1.setVisibility(View.GONE);

                        if(ApiClient.isNetworkAvailable(AddCompanyActivity.this))
                        {
                            if(objInterface != null)
                            {
                                new ActiveSuspendCompany(company_id, 1).execute();
                            }
                        }
                        else
                        {
                            Toast.makeText(AddCompanyActivity.this,getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                        }


//                        tv_Active.setVisibility(View.VISIBLE);
//                        tv_suspend.setVisibility(View.GONE);

                        etpass.setText("");

                        Handler h = new Handler();
                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if(alert != null && alert.isShowing())
                                {
                                    alert.cancel();
                                    alert.dismiss();
                                }

                                Intent i_start = new Intent(AddCompanyActivity.this , MainActivity.class);
                                startActivity(i_start);
                                finish();
                            }
                        },2000);

                    }
                    else
                    {
                        tv_title.setText("Incorrect Password");
                        etpass.setText("");
                    }
                }
                else
                {
                    Toast.makeText(AddCompanyActivity.this, "Please enter password first", Toast.LENGTH_SHORT).show();
                }
//                alert.cancel();
//                etpass.setText("");
            }
        });


        alert.show();

    }

    @OnClick(R.id.tv_delete)
    public void DeleteCompany()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        builder.setMessage("Write your message here.");
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_delete_company, null);
        final EditText etpass = dialogLayout.findViewById(R.id.etPass);
        final TextView tvcancel = dialogLayout.findViewById(R.id.tv_cancel);
        final TextView tv_title = dialogLayout.findViewById(R.id.tv_title);
        final TextView tv_delete = dialogLayout.findViewById(R.id.tv_delete);

        builder.setView(dialogLayout);
        builder.setCancelable(true);

        alert = builder.create();

        tvcancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                etpass.setText("");
                alert.cancel();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String password = etpass.getText().toString().trim();
                if(!TextUtils.isEmpty(password))
                {
                    Log.e("current_pass",""+currentuser.getPassword());

                    if(password.equals(currentuser.getPassword()))
                    {

                        if(ApiClient.isNetworkAvailable(AddCompanyActivity.this))
                        {
                            if(objInterface != null)
                            {
                                new DeleteCompany(company_id).execute();
                            }
                        }
                        else
                        {
                            Toast.makeText(AddCompanyActivity.this,getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        tv_title.setText("Incorrect Password");
                        etpass.setText("");
                    }
                }
                else
                {
                    Toast.makeText(AddCompanyActivity.this, "Please enter password first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(alert != null && (alert.isShowing()== false))
        {
            alert.show();
        }
    }
    public void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        builder.setMessage("Write your message here.");
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_company_option, null);
        final TextView tv_ok = dialogLayout.findViewById(R.id.tv_ok);
        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert1 = builder.create();

        tv_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alert1.cancel();

                Intent i_go = new Intent(AddCompanyActivity.this , LoginActivity.class);
                startActivity(i_go);
                finish();
            }
        });

        alert1.show();
    }
    public void ActiveDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_active, null);

        etpass = dialogLayout.findViewById(R.id.etPass);
        tvcancel = dialogLayout.findViewById(R.id.tv_cancel);
        tv_title = dialogLayout.findViewById(R.id.tv_title);
        tv_title1 = dialogLayout.findViewById(R.id.tv_title1);
        tv_active_btn = dialogLayout.findViewById(R.id.tv_active_btn);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        alert = builder.create();

        tvcancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                etpass.setText("");
                alert.cancel();
            }
        });
        tv_active_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String password = etpass.getText().toString().trim();
                if(!TextUtils.isEmpty(password))
                {
                    if(password.equals(currentuser.getPassword()))
                    {
//                        new DeleteCompany(company_id).execute();

                        if(ApiClient.isNetworkAvailable(AddCompanyActivity.this))
                        {
                            if(objInterface != null)
                            {
                                new ActiveSuspendCompany(company_id, 0).execute();
                            }
                        }
                        else
                        {
                            Toast.makeText(AddCompanyActivity.this,getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                        }



                        tv_Active.setVisibility(View.GONE);
                        tv_Suspend.setVisibility(View.VISIBLE);

                        tv_title.setText("Your Company Has been Activated.");

                        etpass.setVisibility(View.GONE);

                        tvcancel.setVisibility(View.GONE);
                        tv_active_btn.setVisibility(View.GONE);
                        tv_title1.setVisibility(View.GONE);

                        etpass.setText("");

                        Handler h = new Handler();
                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {


                                if(alert != null && alert.isShowing())
                                {
                                    alert.cancel();
                                    alert.dismiss();
                                }

                                Intent i_start = new Intent(AddCompanyActivity.this , MainActivity.class);
                                startActivity(i_start);
                                finish();
                            }
                        },2000);

                    }
                    else
                    {
                        tv_title.setText("Incorrect Password");
                        etpass.setText("");
                    }
                }
                else
                {
                    Toast.makeText(AddCompanyActivity.this, "Please enter password first", Toast.LENGTH_SHORT).show();
                }
//                alert.cancel();
//                etpass.setText("");
            }
        });

        alert.show();

    }

    @OnClick({R.id.Ivback, R.id.tv_Monday_opentime,
            R.id.tv_Monday_closetime, R.id.tv_Tuesday_opentime,
            R.id.tv_Tuesday_closetime, R.id.tv_Wednesday_opentime,
            R.id.tv_Wednesday_closetime, R.id.tv_Thusday_opentime,
            R.id.tv_Thusday_closetime, R.id.tv_Friday_opentime,
            R.id.tv_Friday_closetime, R.id.tv_Saturday_opentime,
            R.id.tv_Saturday_closetime, R.id.tv_Sunday_opentime,
            R.id.tv_Sunday_closetime, R.id.img_reset_mon, R.id.img_reset_tue, R.id.img_reset_wed, R.id.img_reset_thu,
            R.id.img_reset_fri, R.id.img_reset_sat, R.id.img_reset_sun})

    public void onViewClicked(View view)
    {
        switch (view.getId()) {
            case R.id.Ivback:
                break;
            case R.id.tv_Monday_opentime:
                TimeDialog(tvMondayOpentime);
                break;
            case R.id.tv_Monday_closetime:
                TimeDialog(tvMondayClosetime);
                break;
            case R.id.img_reset_mon :
                tvMondayOpentime.setText("00:00 AM");
                tvMondayClosetime.setText("00:00 PM");
                break;
            case R.id.tv_Tuesday_opentime:
                TimeDialog(tvTuesdayOpentime);
                break;
            case R.id.tv_Tuesday_closetime:
                TimeDialog(tvTuesdayClosetime);
                break;
            case R.id.img_reset_tue :
                tvTuesdayOpentime.setText("00:00 AM");
                tvTuesdayClosetime.setText("00:00 PM");
                break;
            case R.id.tv_Wednesday_opentime:
                TimeDialog(tvWednesdayOpentime);
                break;
            case R.id.tv_Wednesday_closetime:
                TimeDialog(tvWednesdayClosetime);
                break;
            case R.id.img_reset_wed :
                tvWednesdayOpentime.setText("00:00 AM");
                tvWednesdayClosetime.setText("00:00 PM");
                break;
            case R.id.tv_Thusday_opentime:
                TimeDialog(tvThursdayOpentime);
                break;
            case R.id.tv_Thusday_closetime:
                TimeDialog(tvThursdayClosetimeme);
                break;
            case R.id.img_reset_thu :
                tvThursdayOpentime.setText("00:00 AM");
                tvThursdayClosetimeme.setText("00:00 PM");
                break;
            case R.id.tv_Friday_opentime:
                TimeDialog(tvFridayOpentime);
                break;
            case R.id.tv_Friday_closetime:
                TimeDialog(tvFridayClosetime);
                break;
            case R.id.img_reset_fri :
                tvFridayOpentime.setText("00:00 AM");
                tvFridayClosetime.setText("00:00 PM");
                break;
            case R.id.tv_Saturday_opentime:
                TimeDialog(tvSaturdayOpentime);
                break;
            case R.id.tv_Saturday_closetime:
                TimeDialog(tvSaturdayClosetime);
                break;
            case R.id.img_reset_sat:
                tvSaturdayOpentime.setText("00:00 AM");
                tvSaturdayClosetime.setText("00:00 PM");
                break;
            case R.id.tv_Sunday_opentime:
                TimeDialog(tvSundayOpentime);
                break;
            case R.id.tv_Sunday_closetime:
                TimeDialog(tvSundayClosetime);
                break;
            case R.id.img_reset_sun :
                tvSundayOpentime.setText("00:00 AM");
                tvSundayClosetime.setText("00:00 PM");
                break;
        }
    }

    private void TimeDialog(TextView textView) {

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        boolean isPM = (hourOfDay >= 12);
                        textView.setText(String.format("%02d:%02d %s",
                                (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        CompanyCategory = parent.getItemAtPosition(position).toString();

        Category v  = (Category) parent.getItemAtPosition(position);

        if(update == true)
        {
            updateCat_id = v.getId();
        }
        else
        {
            cat_id = v.getId();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick(R.id.tv_save)
    public void saveCompanyDetails()
    {
        validation();
    }

    public void validation()
    {
        if (et_company_name.getText().toString().equals(""))
        {
            et_company_name.setError("Enter Company Name");
            return;
        }
        else
        {
            et_company_name.setError(null);
        }


        if (et_building_no_name.getText().toString().equals("")) {
            et_building_no_name.setError("Enter Building Number Or Name");
            return;
        } else {
            et_building_no_name.setError(null);
        }
        if (et_address.getText().toString().equals("")) {
            et_address.setError("Enter Address");
            return;
        } else {
            et_address.setError(null);
        }

        if (et_city.getText().toString().equals("")) {
            et_city.setError("Enter City");
            return;
        } else {
            et_city.setError(null);
        }
        if (et_postcode.getText().toString().equals("")) {
            et_postcode.setError("Enter Postcode");
            return;
        } else {
            et_postcode.setError(null);
        }

        if (et_country.getText().toString().equals("")) {
            et_country.setError("Enter Country");
            return;
        } else {
            et_country.setError(null);
        }

        if (et_email.getText().toString().equals("")) {
            et_email.setError("Enter Email ");
            return;
        } else {
            et_email.setError(null);
        }
        if (!et_email.getText().toString().matches(emailPattern)) {
            et_email.setError("Enter Proper Email");
            return;
        } else {
            et_email.setError(null);
        }

        if (et_contact_number.getText().toString().equals("")) {
            et_contact_number.setError("Enter Contact Number");
            return;
        } else {
            et_contact_number.setError(null);
        }
//        if (et_website.getText().toString().equals("")) {
//            et_website.setError("Enter website");
//            return;
//        } else {
//            et_website.setError(null);
//        }

        CompanyName = et_company_name.getText().toString().trim();
        building_number = et_building_no_name.getText().toString().trim();
        city = et_city.getText().toString().trim();
        address_line_1 = et_address.getText().toString().trim();
        postcode = et_postcode.getText().toString().trim();
        userName = et_username.getText().toString().trim();
        contact_number = et_contact_number.getText().toString().trim();
        company_ethos = et_company_ethos.getText().toString().trim();
        website = et_website.getText().toString().trim();
        email = et_email.getText().toString().trim();
        customer_number = et_customer_number.getText().toString().trim();
        country = et_country.getText().toString().trim();
        app_link = et_company_app_link.getText().toString().trim();

        if (ApiClient.isNetworkAvailable(AddCompanyActivity.this))
        {
            if(objInterface != null)
            {
                if (update == true)
                {
                    new UpdateCompany().execute();
                }
                else
                {
                    new CreateCompany().execute();
                }
            }
        }
        else
        {
            Toast.makeText(AddCompanyActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
        }
    }
    public class CreateCompany extends AsyncTask<Void ,Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd4 = new ProgressDialog(AddCompanyActivity.this);
            pd4.setCancelable(false);
            pd4.setMessage(getString(R.string.please_wait));
            pd4.show();
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
        String address = CompanyName+", "+building_number+", "+address_line_1+", "+city+", "+postcode+", "+country;

        LatLng objlatlong = getLocationFromAddress(AddCompanyActivity.this, address);

        RequestBody r_catid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(cat_id));
        RequestBody rCompanyName = RequestBody.create(MediaType.parse("multipart/form-data"), CompanyName);
        RequestBody rBuildingNumber = RequestBody.create(MediaType.parse("multipart/form-data"), building_number);
        RequestBody rAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address_line_1);
        RequestBody rPostcode = RequestBody.create(MediaType.parse("multipart/form-data"), postcode);
        RequestBody rCountry = RequestBody.create(MediaType.parse("multipart/form-data"), country);
        RequestBody rCity = RequestBody.create(MediaType.parse("multipart/form-data"), city);
        RequestBody rEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody rWebsite = RequestBody.create(MediaType.parse("multipart/form-data"), website);
        RequestBody rAppLink = RequestBody.create(MediaType.parse("multipart/form-data"), app_link);
        RequestBody rContactNumber = RequestBody.create(MediaType.parse("multipart/form-data"), contact_number);
        RequestBody rCompanyEthos = RequestBody.create(MediaType.parse("multipart/form-data"), company_ethos);
        RequestBody rMondayOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvMondayOpentime.getText().toString());
        RequestBody rMondayClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvMondayClosetime.getText().toString());
        RequestBody rTuesOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvTuesdayOpentime.getText().toString());
        RequestBody rTuesClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvTuesdayClosetime.getText().toString());
        RequestBody rWedOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvWednesdayOpentime.getText().toString());
        RequestBody rWedClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvWednesdayClosetime.getText().toString());
        RequestBody rThursOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvThursdayOpentime.getText().toString());
        RequestBody rThursClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvThursdayClosetimeme.getText().toString());
        RequestBody rFriOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvFridayOpentime.getText().toString());
        RequestBody rFriClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvFridayClosetime.getText().toString());
        RequestBody rSatOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvSaturdayOpentime.getText().toString());
        RequestBody rSatClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvSaturdayClosetime.getText().toString());
        RequestBody rSunOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvSundayOpentime.getText().toString());
        RequestBody rSunClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvSundayClosetime.getText().toString());
        RequestBody rExpiryDate = null, rPaymentNonce = null;

        if(expiryDate != null)
        {
            rExpiryDate = RequestBody.create(MediaType.parse("multipart/form-data"), expiryDate);
        }

        if(paymentNonce != null)
        {
             rPaymentNonce = RequestBody.create(MediaType.parse("multipart/form-data"), paymentNonce);
        }

        Log.e("monday",""+rMondayOpening.toString());
        RequestBody rLat=null, rLong=null;
        if(objlatlong != null)
        {
            Log.e("latitude",""+objlatlong.latitude);
            Log.e("longitude",""+objlatlong.longitude);

            rLat = RequestBody.create(MediaType.parse("multipart/form-data"), Double.toString(objlatlong.latitude));
            rLong = RequestBody.create(MediaType.parse("multipart/form-data"), Double.toString(objlatlong.longitude));
        }

        if(StoreData.companyImagelist != null && StoreData.companyImagelist.size() > 0)
        {
            for(int i=0 ; i < StoreData.companyImagelist.size() ; i++)
            {
                parts.add(prepareFilePart("images[]", StoreData.companyImagelist.get(i)));
                Log.e("parts" , ""+parts.get(i).body().toString());
            }
        }

        MultipartBody.Part companyLogo = null;
        if(companyLogoPath != null)
        {
           companyLogo = prepareFilePart("image", companyLogoPath);
        }

        Call<Response> call = objInterface.Create_Company(companyLogo,parts , r_catid , rCompanyName , rBuildingNumber ,  rAddress ,
                rCity, rPostcode,rCountry,rMondayOpening,rMondayClosing,
                rTuesOpening,rTuesClosing, rWedOpening,rWedClosing,rThursOpening,rThursClosing,rFriOpening,rFriClosing,
                rSatOpening,rSatClosing,rSunOpening,rSunClosing, rEmail, rContactNumber, rWebsite,rAppLink,
                rLat,rLong,rCompanyEthos,rPaymentNonce,rExpiryDate);

        Log.e("call" , ""+call.request());

        Log.e("body" , ""+bodyToString(call.request()));

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

                    if(response.body().getErrors() != null)
                    {
                        Log.e("response error", new Gson().toJson(response.body().getErrors()));
                    }

                    if(response.body().getResult() == 1)
                    {
                        Company resultCompany = response.body().getCompany();

                        if(resultCompany != null)
                        {

                            if(pd4 != null && pd4.isShowing())
                            {
                                pd4.dismiss();
                            }
                            StoreData.companyImagelist.clear();

                            Toast.makeText(AddCompanyActivity.this , "Company Added Successfully" , Toast.LENGTH_SHORT).show();

                            if(isPayMandatory == false)
                            {
                                Intent i_save = new Intent(AddCompanyActivity.this , AddCompanyActivity.class);
                                i_save.putExtra("company_id",resultCompany.getId());
                                i_save.putExtra("update",true);
                                startActivity(i_save);
                                finish();
                            }
                            else
                            {
                                Intent i_save = new Intent(AddCompanyActivity.this , SavedCompanyDetailsActivity.class);
                                i_save.putExtra("company_id",resultCompany.getId());
                                startActivity(i_save);
                                finish();
                            }
                        }
                        else
                        {
                            if(pd4 != null && pd4.isShowing())
                            {
                                pd4.dismiss();
                            }
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd4 != null && pd4.isShowing())
                        {
                            pd4.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd4 != null && pd4.isShowing())
                    {
                        pd4.dismiss();
                    }
                    Toast.makeText(AddCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd4 != null && pd4.isShowing())
                {
                    pd4.dismiss();
                }
                Toast.makeText(AddCompanyActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
    private MultipartBody.Part prepareFilePart(String partName, String filepath)
    {
        File file = new File(filepath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
    @OnClick(R.id.Ivback)
    public void onBack()
    {
        StoreData.companyImagelist.clear();
        if(update == true)
        {
            Intent i_back = new Intent(AddCompanyActivity.this , SavedCompanyDetailsActivity.class);
            i_back.putExtra("company_id",company_id);
            startActivity(i_back);
            finish();
        }
        else
        {
            Intent i_bacl = new Intent(AddCompanyActivity.this , MainActivity.class);
            startActivity(i_bacl);
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        StoreData.companyImagelist.clear();
        if(update == true)
        {
            Intent i_back = new Intent(AddCompanyActivity.this , SavedCompanyDetailsActivity.class);
            i_back.putExtra("company_id",company_id);
            startActivity(i_back);
            finish();
        }
        else
        {
            Intent i_bacl = new Intent(AddCompanyActivity.this , MainActivity.class);
            startActivity(i_bacl);
            finish();
        }
    }

    @OnClick(R.id.tv_cancel)
    public void onCancel()
    {
        StoreData.companyImagelist.clear();
        if(update == true)
        {
            Intent i_back = new Intent(AddCompanyActivity.this , SavedCompanyDetailsActivity.class);
            i_back.putExtra("company_id",company_id);
            startActivity(i_back);
            finish();
        }
        else
        {
            Intent i_bacl = new Intent(AddCompanyActivity.this , MainActivity.class);
            startActivity(i_bacl);
            finish();
        }
    }

    public class UpdateCompany extends AsyncTask<Void ,Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd1 = new ProgressDialog(AddCompanyActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Update_Company();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public  void Update_Company()
    {
        RequestBody r_companyid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(updateCompanyId));
        RequestBody r_catid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(updateCat_id));
        RequestBody rCompanyName = RequestBody.create(MediaType.parse("multipart/form-data"), CompanyName);
        RequestBody rBuildingNumber = RequestBody.create(MediaType.parse("multipart/form-data"), building_number);
        RequestBody rAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address_line_1);
        RequestBody rPostcode = RequestBody.create(MediaType.parse("multipart/form-data"), postcode);
        RequestBody rCity = RequestBody.create(MediaType.parse("multipart/form-data"), city);
        RequestBody rCountry = RequestBody.create(MediaType.parse("multipart/form-data"), country);
        RequestBody rEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody rWebsite = RequestBody.create(MediaType.parse("multipart/form-data"), website);
        RequestBody rAppLink = RequestBody.create(MediaType.parse("multipart/form-data"), app_link);
        RequestBody rContactNumber = RequestBody.create(MediaType.parse("multipart/form-data"), contact_number);
        RequestBody rCompanyEthos = RequestBody.create(MediaType.parse("multipart/form-data"), company_ethos);
        RequestBody rMondayOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvMondayOpentime.getText().toString());
        RequestBody rMondayClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvMondayClosetime.getText().toString());
        RequestBody rTuesOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvTuesdayOpentime.getText().toString());
        RequestBody rTuesClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvTuesdayClosetime.getText().toString());
        RequestBody rWedOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvWednesdayOpentime.getText().toString());
        RequestBody rWedClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvWednesdayClosetime.getText().toString());
        RequestBody rThursOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvThursdayOpentime.getText().toString());
        RequestBody rThursClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvThursdayClosetimeme.getText().toString());
        RequestBody rFriOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvFridayOpentime.getText().toString());
        RequestBody rFriClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvFridayClosetime.getText().toString());
        RequestBody rSatOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvSaturdayOpentime.getText().toString());
        RequestBody rSatClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvSaturdayClosetime.getText().toString());
        RequestBody rSunOpening = RequestBody.create(MediaType.parse("multipart/form-data"), tvSundayOpentime.getText().toString());
        RequestBody rSunClosing = RequestBody.create(MediaType.parse("multipart/form-data"), tvSundayClosetime.getText().toString());

        RequestBody rExpiryDate= null, rPaymentNonce = null;
        if(expiryDate != null)
        {
                    rExpiryDate = RequestBody.create(MediaType.parse("multipart/form-data"), expiryDate);
        }

        if(paymentNonce != null)
        {
            rPaymentNonce = RequestBody.create(MediaType.parse("multipart/form-data"), paymentNonce);
        }
//        RequestBody rPaymentNonce = RequestBody.create(MediaType.parse("multipart/form-data"), paymentNonce);
//        RequestBody rExpiryDate = RequestBody.create(MediaType.parse("multipart/form-data"), expiryDate);
        RequestBody rLat=null, rLong=null;

        String address = CompanyName+", "+building_number+", "+address_line_1+", "+city+", "+postcode+", "+country ;

        LatLng objlatlong = getLocationFromAddress(AddCompanyActivity.this, address);

        Log.e("latitude",""+objlatlong.latitude);
        Log.e("longitude",""+objlatlong.longitude);

        if(objlatlong != null)
        {
            Log.e("latitude",""+objlatlong.latitude);
            Log.e("longitude",""+objlatlong.longitude);

            rLat = RequestBody.create(MediaType.parse("multipart/form-data"), Double.toString(objlatlong.latitude));
            rLong = RequestBody.create(MediaType.parse("multipart/form-data"), Double.toString(objlatlong.longitude));
        }

        RequestBody p_deleteimage = null;

        if(deletImageBuffer != null)
        {
            p_deleteimage = RequestBody.create(MediaType.parse("multipart/form-data"), deletImageBuffer.toString());
        }

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
        MultipartBody.Part companyLogo = null;
        if(updatelogopath != null)
        {
            companyLogo = prepareFilePart("image", updatelogopath);
        }

        Call<Response> call = objInterface.Update_Company(companyLogo, update_parts , r_companyid, r_catid , rCompanyName , rBuildingNumber ,  rAddress ,
                rCity, rPostcode,rCountry,rMondayOpening,rMondayClosing,
                rTuesOpening,rTuesClosing, rWedOpening,rWedClosing,rThursOpening,rThursClosing,rFriOpening,rFriClosing,
                rSatOpening,rSatClosing,rSunOpening,rSunClosing, rEmail, rContactNumber, rWebsite, rAppLink,
                rLat,rLong,rCompanyEthos,rPaymentNonce, rExpiryDate, p_deleteimage);

        Log.e("callupdate",""+call.request());

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("updatecode" , ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("response beauty", new Gson().toJson(response.body()));

                    Log.e("response" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {
                        Company resultCompany = response.body().getCompany();

                        if(resultCompany != null)
                        {
                            if(pd1 != null && pd1.isShowing())
                            {
                                pd1.dismiss();
                            }

                            StoreData.companyImagelist.clear();

                            Intent i_back = new Intent(AddCompanyActivity.this , SavedCompanyDetailsActivity.class);
                            i_back.putExtra("company_id",updateCompanyId);
                            startActivity(i_back);
                            finish();

                            Toast.makeText(AddCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(pd1 != null && pd1.isShowing())
                            {
                                pd1.dismiss();
                            }
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd1 != null && pd1.isShowing())
                    {
                        pd1.dismiss();
                    }
                    Toast.makeText(AddCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd1 != null && pd1.isShowing())
                {
                    pd1.dismiss();
                }
                Log.e("onFail",""+t.toString());
//                Toast.makeText(AddCompanyActivity.this , "onfAIL"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public class DeleteCompany extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;

        public DeleteCompany(int id)
        {
            this.id = id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd2 = new ProgressDialog(AddCompanyActivity.this);
            pd2.setCancelable(false);
            pd2.setMessage(getString(R.string.please_wait));
            pd2.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            deleteCompanyFromID(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public void deleteCompanyFromID(int id)
    {
        Company objOffers = new Company();
        objOffers.setId(id);
        Call<Response>  call = objInterface.Delete_Company(objOffers);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {
                        Toast.makeText(AddCompanyActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        if(alert != null && alert.isShowing())
                        {
                            alert.dismiss();
                        }
                                Intent i_start = new Intent(AddCompanyActivity.this , MainActivity.class);
                                startActivity(i_start);
                                finish();

                            if(pd2 != null & pd2.isShowing())
                            {
                                pd2.dismiss();
                            }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd2 != null & pd2.isShowing())
                        {
                            pd2.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd2 != null & pd2.isShowing())
                        {
                            pd2.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd2 != null & pd2.isShowing())
                    {
                        pd2.dismiss();
                    }
                    Toast.makeText(AddCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd2 != null & pd2.isShowing())
                {
                    pd2.dismiss();
                }
                Toast.makeText(AddCompanyActivity.this , t.toString() , Toast.LENGTH_LONG).show();
            }
        });
    }
    public class GetCompany extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;
        public GetCompany(int company_id)
        {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(AddCompanyActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getCompany(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public void getCompany(int id)
    {
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response>  call = objInterface.Get_CompanyDetails(objCompany);

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
                        Company  resultCompany = response.body().getCompany();
                        et_website.setText(resultCompany.getWebsite());
                        et_company_app_link.setText(resultCompany.getApplink());
                        et_email.setText(resultCompany.getEmail());
                        et_postcode.setText(resultCompany.getPostcode());
                        et_company_name.setText(resultCompany.getCompanyName());
                        et_address.setText(resultCompany.getAddress_line_1());
                        et_building_no_name.setText(resultCompany.getBuilding_number());
                        et_customer_number.setText(resultCompany.getCustomer_number());
                        et_company_ethos.setText(resultCompany.getCompany_ethos());
                        et_contact_number.setText(resultCompany.getTelephone());
                        et_city.setText(resultCompany.getCity());
                        et_country.setText(resultCompany.getCountry());

                        tvMondayOpentime.setText(resultCompany.getMonday_opening());
                        tvMondayClosetime.setText(resultCompany.getMonday_closing());

                        tvTuesdayOpentime.setText(resultCompany.getTuesday_opening());
                        tvTuesdayClosetime.setText(resultCompany.getTuesday_closing());

                        tvWednesdayOpentime.setText(resultCompany.getWednesday_opening());
                        tvWednesdayClosetime.setText(resultCompany.getWednesday_closing());

                        tvThursdayOpentime.setText(resultCompany.getThursday_opening());
                        tvThursdayClosetimeme.setText(resultCompany.getThursday_closing());

                        tvFridayOpentime.setText(resultCompany.getFriday_opening());
                        tvFridayClosetime.setText(resultCompany.getFriday_closing());

                        tvSaturdayOpentime.setText(resultCompany.getSaturday_opening());
                        tvSaturdayClosetime.setText(resultCompany.getSaturday_closing());

                        tvSundayOpentime.setText(resultCompany.getSunday_opening());
                        tvSundayClosetime.setText(resultCompany.getSunday_closing());

                        int status = resultCompany.getStatus();
                        if(status == 1)
                        {
                            tv_Active.setVisibility(View.VISIBLE);
                            tv_Suspend.setVisibility(View.GONE);
                        }
                        else
                        {
                            tv_Suspend.setVisibility(View.VISIBLE);
                            tv_Active.setVisibility(View.GONE);
                        }

                        int expiry = resultCompany.getIs_expiry();

                        if(expiry == 1)
                        {
//                            tv_paypal.setEnabled(true);
                            tv_paypal.setVisibility(View.VISIBLE);
                            tv_paypal_mandatory.setVisibility(View.VISIBLE);
                        }
                        else
                        {
//                            tv_paypal.setEnabled(false);
                            tv_paypal.setVisibility(View.GONE);
                            tv_paypal_mandatory.setVisibility(View.GONE);
                        }

                        for(int i = 0 ; i <categoryList.size() ; i++)
                        {
                            if(categoryList.get(i).getId() == resultCompany.getCategory_id())
                            {
                                String catname = categoryList.get(i).getName();
                                SpCompnayCategory.setSelection(adapter.getPosition(categoryList.get(i)));
                            }
                        }

                        updateCat_id = resultCompany.getCategory_id();
                        updateCompanyId = resultCompany.getId();

                        paymentNonce = resultCompany.getPaypal_nonce();
                        expiryDate = resultCompany.getExpiry_date();

                        imglist = resultCompany.getImages();

                        if(imglist != null && imglist.size() >0)
                        {
                            rcv_add.setVisibility(View.GONE);
                            rcv_update.setVisibility(View.VISIBLE);
                            fillUpdatedImage();
                        }
                        StoreData.companyImagelist.clear();

                       companyLogoPath =  resultCompany.getImagepath();

                        frame_logo_image.setVisibility(View.VISIBLE);

                        if(companyLogoPath != null)
                        {
                            Glide.with(AddCompanyActivity.this).load(companyLogoPath).into(img_logo);
                        }
                        else
                        {
                            frame_logo_image.setVisibility(View.GONE);
                        }


                        if(resultCompany != null)
                        {
                            if(pd3 != null & pd3.isShowing())
                            {
                                pd3.dismiss();
                            }
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(AddCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(AddCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(AddCompanyActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    @OnClick(R.id.fab_gallery)
    public void addPhoto()
    {
        checkPermissions();
    }

    @OnClick(R.id.fab_logo_gallery)
    public void addLogo()
    {
        checkPermissions1();
    }

    private void checkPermissions1()
    {
        int PER_CODE = 673;

        mRequestPermissionHandler.requestPermission(this, permissions,
                PER_CODE, new RequestPermissionHandler.RequestPermissionListener()
                {
                    @Override
                    public void onSuccess()
                    {
                        openGallery1();
                    }

                    @Override
                    public void onFailed()
                    {
                        Toast.makeText(AddCompanyActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(AddCompanyActivity.this)
                                .setCancelable(false)
                                .setTitle("Permission necessary")
                                .setMessage("Please grant all permissions to proceed with app.")
                                .setPositiveButton("Re-Try", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        checkPermissions1();
                                    }
                                })
                                .setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", AddCompanyActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                        checkPermissions();
                                    }
                                })
                                .create().show();
                    }
                });
    }
    public void openGallery1()
    {
        Intent i_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i_gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(i_gallery, "Select Image"),PICK_LOGO_FROM_GALLERY);
    }
    private void checkPermissions()
    {
        int PER_CODE = 193;

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
                        Toast.makeText(AddCompanyActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(AddCompanyActivity.this)
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
                                        Uri uri = Uri.fromParts("package", AddCompanyActivity.this.getPackageName(), null);
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

                    String path = getRealPathFromURI(AddCompanyActivity.this , uri);

                    if(path != null)
                    {
                        Intent i_crop = new Intent(AddCompanyActivity.this , CropImageActivity.class);
                        i_crop.putExtra("imagepath",path);
                        i_crop.putExtra("logo",false);
                        startActivityForResult(i_crop , CROP_IMAGE);
                    }
                }
                break;

            case CROP_IMAGE :

                if(data != null)
                {
                    String s_path = data.getStringExtra("imagepath");
                    boolean s_logo = data.getBooleanExtra("logo", false);

                    if (s_logo == true)
                    {
                        if (update == true)
                        {
                            if (resultCode == RESULT_OK)
                            {
                                frame_logo_image.setVisibility(View.VISIBLE);
                                updatelogopath = s_path;
                                if(updatelogopath != null)
                                {
                                    Glide.with(AddCompanyActivity.this).load(updatelogopath).into(img_logo);
                                }
                                else
                                {
                                    frame_logo_image.setVisibility(View.GONE);
                                }

                            }
                            else
                            {
                                frame_logo_image.setVisibility(View.VISIBLE);
                                updatelogopath = s_path;
                                if(updatelogopath != null)
                                {
                                    Glide.with(AddCompanyActivity.this).load(updatelogopath).into(img_logo);
                                }
                                else
                                {
                                    frame_logo_image.setVisibility(View.GONE);
                                }
                            }
                        }
                        else
                        {
                            if (resultCode == RESULT_OK)
                            {
                                frame_logo_image.setVisibility(View.VISIBLE);
                                companyLogoPath = s_path;
                                if(companyLogoPath != null)
                                {
                                    Glide.with(AddCompanyActivity.this).load(companyLogoPath).into(img_logo);
                                }
                                else
                                {
                                    frame_logo_image.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                frame_logo_image.setVisibility(View.VISIBLE);
                                companyLogoPath = s_path;
                                if(companyLogoPath != null)
                                {
                                    Glide.with(AddCompanyActivity.this).load(companyLogoPath).into(img_logo);
                                }
                                else
                                {
                                    frame_logo_image.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                    else
                    {
                        if (update == true)
                        {
                            if (resultCode == RESULT_OK)
                            {
                                Image objimage = new Image();
                                objimage.setImagepath(s_path);

                                if(objimage != null)
                                {
                                    imglist.add(objimage);
                                }
                            }
                            else
                            {
                                Image objimage = new Image();
                                objimage.setImagepath(s_path);

                                if(objimage != null)
                                {
                                    imglist.add(objimage);
                                }
                            }
                            if (updateAdapter != null)
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
                            if (resultCode == RESULT_OK)
                            {
                                Image objimage = new Image();
                                objimage.setImagepath(s_path);

                                StoreData.companyImagelist.add(s_path);
                            }
                            else
                            {
                                Image objimage = new Image();
                                objimage.setImagepath(s_path);

                                StoreData.companyImagelist.add(s_path);
                            }
                            if (imagesAdapter != null)
                            {
                                imagesAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                check_list_fill();
                            }
                        }


                    }
                }
                break;

            case PICK_LOGO_FROM_GALLERY :

                if(resultCode == RESULT_OK)
                {
                    Uri uri = data.getData();

                    String path = getRealPathFromURI(AddCompanyActivity.this , uri);

                    if(path != null)
                    {
                        Intent i_crop = new Intent(AddCompanyActivity.this, CropImageActivity.class);
                        i_crop.putExtra("imagepath", path);
                        i_crop.putExtra("logo",true);
                        startActivityForResult(i_crop, CROP_IMAGE);
                    }
                }

                break;
        }
    }

    public void removeImage(int position)
    {
        StoreData.companyImagelist.remove(position);
        runOnUiThread(new Runnable() {
            public void run() {
                imagesAdapter.notifyDataSetChanged();
            }
        });
    }
    public void fillUpdatedImage()
    {
        if(imglist != null && imglist.size() > 0)
        {
            rcv_add.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            rcv_update.setVisibility(View.VISIBLE);

            updateAdapter = new UpdatedImageaAdapter(AddCompanyActivity.this , imglist);
            rcv_update.setAdapter(updateAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(AddCompanyActivity.this , LinearLayoutManager.HORIZONTAL , false);
            rcv_update.setLayoutManager(manager);
            updateAdapter.notifyDataSetChanged();
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_update.setVisibility(View.GONE);
            rcv_add.setVisibility(View.GONE);
        }
    }

//    SubscriptionRequest request = new SubscriptionRequest()
//            .paymentMethodToken("the_token")
//            .planId("the_plan_id");
//
//    Result<Subscription> result = gateway.subscription().create(request);

}
