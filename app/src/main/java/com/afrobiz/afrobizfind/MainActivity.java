package com.afrobiz.afrobizfind;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afrobiz.afrobizfind.adapter.NearByCompanyEventsAdapter;
import com.afrobiz.afrobizfind.ui.category.CategoryDetailActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CustomerOrders.CustomerOrderHistoryActivity;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.widgets.MaterialBadgeTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.adapter.CategoryWithCompanyAdapter;
import com.afrobiz.afrobizfind.adapter.CompanyNearestAdapter;
import com.afrobiz.afrobizfind.adapter.NearByCompanyProductsAdapter;
import com.afrobiz.afrobizfind.adapter.ProductDeatilsImagesAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.currentLocation.GpsTracker;
import com.afrobiz.afrobizfind.ui.modal.Category;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.LatLong;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.category.CategoryActivity;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;
import com.afrobiz.afrobizfind.ui.mActivity.LoginActivity;
import com.afrobiz.afrobizfind.ui.mActivity.MyCompanies.MyCompaniesActivity;
import com.afrobiz.afrobizfind.ui.mActivity.ProfileDetailActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SettingActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SignupActivity;
import com.afrobiz.afrobizfind.ui.mActivity.addCompany.AddCompanyActivity;
import com.afrobiz.afrobizfind.ui.mActivity.favcompany.FavoriteCompanyActivity;
import com.afrobiz.afrobizfind.ui.widgets.RevealAnimation;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
{
    public static final String EXTRA_COLLECT_DEVICE_DATA = "collect_device_data";

    @BindView(R.id.rcv_nearest_company)
    RecyclerView rcv_nearest_company;

    @BindView(R.id.rcv_category_company)
    RecyclerView rcv_category_company;

    CategoryWithCompanyAdapter catcompanyAdapter;

    @BindView(R.id.rcv_products)
    RecyclerView rcv_products;

    @BindView(R.id.rcv_events)
    RecyclerView rcv_events;

    @BindView(R.id.Title)
    TextView Title;
    TextView tv_productlist, tv_companylist, tv_eventlist ;

    @BindView(R.id.fab_category)
    FloatingActionButton fab_category;

    @BindView(R.id.EtSearch)
    EditText EtSearch;

    @BindView(R.id.Ivsearch)
    ImageView Ivsearch;

    @BindView(R.id.tv_country)
    TextView tv_country;

    @BindView(R.id.tv_total_customers)
    TextView tv_total_customers;

    int notificationCount = 0;

    @BindView(R.id.layout_login)
    LinearLayout layout_login;

    @BindView(R.id.layout_signout)
    LinearLayout layout_signout;

    LinearLayout layout_order_history, layout_help, layout_setting;

    boolean isSerach = false;

    ProgressDialog pd, pd1,pd2,pd3;

    double currentLat = 0;
    double currentLong = 0;

    ApiInterface apiWithoutAuth , objInterface;

    CompanyNearestAdapter companyNearestAdapter;

    NearByCompanyProductsAdapter nearbycompanyProductsAdapter;

    NearByCompanyEventsAdapter nearbycompanyEventsAdapter ;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

    TextView tvMenuLogin, tvPersonalDetailMenu, tvSignupMenu, tvfavopurite_companies,
            tvAddCompanyMenu, tvmy_companies;
    ImageView IvopenDrawer;
    DrawerLayout drawer;
    List<Company> nearBycompnies;
    GpsTracker gpsTracker;
    public static List<Category> listCategory;
    List<Product> listProduct;
    List<Event> listEvent;
    String unit = null;
    String title = null;
    int last50 = 0;

    ApiInterface apiInterface = null;
    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private RequestPermissionHandler mRequestPermissionHandler;

    String[] permissions =
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };

    boolean alreadyRun = false;
    String company_id1;
    boolean isGPS = false;
    Users currentuser;
    @BindView(R.id.badge_notification)
    MaterialBadgeTextView badge_notification;
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        title = null;
        Log.e("newintent","newintent");
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            if(extras.containsKey("last50"))
            {
                String  str_last50 = extras.getString("last50");
                if(str_last50 != null && str_last50.equals("1"))
                {
                    last50 = 1;
                }
                else
                {
                    last50 =0;
                }
                Log.e("last50_new",""+last50);

                title = extras.getString("title");
                company_id1 = extras.getString("company_id");
            }
            if(extras.containsKey("title"))
            {
                title = extras.getString("title");
            }
            if(extras.containsKey("company_id"))
            {
                company_id1 = extras.getString("company_id");
            }
            if(last50 == 1)
            {
                Intent intent1 = new Intent(MainActivity.this, CategoryDetailActivity.class);
                intent1.putExtra("categoryid" , 0);
                startActivity(intent1);
//                refresh_Page();
            }
        }
        Log.e("title_new",""+title +" id "+company_id1 );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.e("create","");

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onNewIntent(getIntent());

        Log.e("last50_create",""+last50);
        Log.e("title_create",""+title);

        unit = new PrefrenceManager(MainActivity.this).getUnit();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task)
            {
                if (!task.isSuccessful())
                {
                    return;
                }
                String token = task.getResult().getToken();

                Log.e("fb", "onComplete: in app ================> " + token);
            }
        });

        apiWithoutAuth = ApiClient.create_Istance();

        currentuser = new PrefrenceManager(MainActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            String token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);
            apiInterface = objInterface;
        }
        else
        {
            apiInterface = apiWithoutAuth;
        }

        if(ApiClient.isNetworkAvailable(MainActivity.this))
        {
            new CategoryList().execute();
        }
        else
        {
            Toast.makeText(MainActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
        }

        mRequestPermissionHandler = new RequestPermissionHandler();
        checkPermissions();

        new GpsUtils(MainActivity.this).turnGPSOn(new GpsUtils.onGpsListener()
        {
            @Override
            public void gpsStatus(boolean isGPSEnable)
            {
                // turn on GPS
                isGPS = isGPSEnable;

                if(isGPS == true)
                {
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        IvopenDrawer = findViewById(R.id.Iv_openNavDrawer);
//        tv_product_list = findViewById(R.id.tv_product_list);

        tv_productlist = findViewById(R.id.tv_productlist);
        tv_eventlist = findViewById(R.id.tv_eventlist);
        tv_companylist = findViewById(R.id.tv_companylist);

        layout_setting = (LinearLayout) findViewById(R.id.layout_setting);
        layout_help = (LinearLayout) findViewById(R.id.layout_help);
        layout_order_history = (LinearLayout) findViewById(R.id.layout_order_history);

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share)
//                .setDrawerLayout(drawer)
//                .build();

        if (isSerach == false)
        {
            Title.setVisibility(View.VISIBLE);
            EtSearch.setVisibility(View.GONE);
        }
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refresh_Page();
            }
        });

        Title.setText("AFROBIZFIND");

        IvopenDrawer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        tvMenuLogin = findViewById(R.id.tvMenuLogin);
        tvPersonalDetailMenu = findViewById(R.id.tvPersonalDetailMenu);
        tvSignupMenu = findViewById(R.id.tvSignupMenu);
        tvAddCompanyMenu = findViewById(R.id.tvAddCompanyMenu);
        tvfavopurite_companies = findViewById(R.id.tvfavopurite_companies);
        tvmy_companies = findViewById(R.id.tvmy_companies);

        if (new PrefrenceManager(MainActivity.this).getLogout() == true)
        {
//            layout_login.setVisibility(View.GONE);
            layout_signout.setVisibility(View.VISIBLE);
        }
        else
        {
            layout_signout.setVisibility(View.GONE);
//            layout_login.setVisibility(View.VISIBLE);
        }

        layout_signout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                displayLogoutDialog();
            }
        });
        tvSignupMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);

                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        layout_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layout_help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        layout_order_history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, CustomerOrderHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvAddCompanyMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);

                if (new PrefrenceManager(MainActivity.this).getLogout() == true)
                {
                    Intent intent = new Intent(MainActivity.this, AddCompanyActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    displayLoginDialog();
                }
            }
        });

        tvfavopurite_companies.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);

                if (new PrefrenceManager(MainActivity.this).getLogout() == true)
                {
                    Intent intent = new Intent(MainActivity.this, FavoriteCompanyActivity.class);
                    intent.putExtra("from_profile",false);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    displayLoginDialog();
                }
            }
        });
        tvmy_companies.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);

                if (new PrefrenceManager(MainActivity.this).getLogout() == true)
                {
                    Intent intent = new Intent(MainActivity.this, MyCompaniesActivity.class);
                    intent.putExtra("from_add_company", false);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    displayLoginDialog();
                }
            }
        });
        tvPersonalDetailMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);

                if (new PrefrenceManager(MainActivity.this).getLogout() == true)
                {
                    Intent intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    displayLoginDialog();
                }

            }
        });

        tvMenuLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawer.closeDrawer(Gravity.LEFT);

                if(new PrefrenceManager(MainActivity.this) .getLogout() == true)
                {
                }
                else
                {
                    Intent i_edit = new Intent(MainActivity.this , LoginActivity.class);
                    startActivity(i_edit);
                }
            }
        });

        EtSearch.addTextChangedListener(new TextWatcher()
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
                filterCompany(s.toString());

                filterCategory(s.toString());
                filterProduct(s.toString());

                filterEvent(s.toString());
            }
        });
    }

    public void ClickNearByCompanyEvents(int companyId)
    {
        Log.e("companyid",""+companyId);
        if (new PrefrenceManager(MainActivity.this).getLogout() == true)
        {
            Intent intent = new Intent(MainActivity.this, CompanyProfileActivity.class);
            intent.putExtra("company_id", companyId);
            startActivity(intent);
//            finish();
        }
        else
        {
            displayLoginDialog();
        }
    }

    public class Logout extends AsyncTask<Void , Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd3 = new ProgressDialog(MainActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            doLogout();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void  doLogout()
    {
        Call<Response> call = objInterface.LogOut();

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {
                        if(pd3 != null && pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();

                        new PrefrenceManager(MainActivity.this).logout(false);
                        new PrefrenceManager(MainActivity.this).clearValue();





                        Intent i_edit = new Intent(MainActivity.this , LoginActivity.class);
                        startActivity(i_edit);
//                        finish();
                    }
                    else
                    {
                        if(pd3 != null && pd3.isShowing())
                        {
                            pd3.dismiss();
                        }

                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    if(pd3 != null && pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                }
                else
                {
                    if(pd3 != null && pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(MainActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null && pd3.isShowing())
                {
                    pd3.dismiss();
                }

                Toast.makeText(MainActivity.this  , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void refresh_Page()
    {
        if (EtSearch.getVisibility() == View.VISIBLE)
        {
            Title.setVisibility(View.VISIBLE);
            EtSearch.setVisibility(View.GONE);
            EtSearch.setText("");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                checkPermissions();
                new GpsUtils(MainActivity.this).turnGPSOn(new GpsUtils.onGpsListener()
                {
                    @Override
                    public void gpsStatus(boolean isGPSEnable)
                    {
                        // turn on GPS
                        isGPS = isGPSEnable;
                    }
                });
                swiperefresh.setRefreshing(false);
            }
        },1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, grantResults);
    }

    private void checkLocationPermissiion()
    {
        try
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String getCountryName(Context context, double latitude, double longitude)
    {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try
        {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty())
            {
                return addresses.get(0).getCountryName();
            }
            return null;
        }
        catch (IOException ignored)
        {
        }
        return null;

    }
    public void getLocation()
    {
        gpsTracker = new GpsTracker(MainActivity.this);

        Log.e("gpstracker"," "+gpsTracker.canGetLocation());

        if (gpsTracker.canGetLocation())
        {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            currentLat = latitude;
            currentLong = longitude;

            String country_name = getCountryName(MainActivity.this , currentLat , currentLong);

            tv_country.setText(country_name);

            Log.e("currentlat", "" + currentLat);
            Log.e("cuurent_lang", "" + currentLong);
        }
        else
        {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent , 2);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    private void filterEvent(String text)
    {
        if(listEvent != null && listEvent.size() >0)
        {
            ArrayList<Event> filterdNames = new ArrayList<>();

            for (Event s : listEvent)
            {
                if (s.getEventname().toLowerCase().contains(text.toLowerCase()))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                nearbycompanyEventsAdapter.filterList1(filterdNames);
                tv_eventlist.setVisibility(View.GONE);
                rcv_events.setVisibility(View.VISIBLE);
            }
            else
            {
                tv_eventlist.setVisibility(View.VISIBLE);
                rcv_events.setVisibility(View.GONE);
            }
        }
    }
    private void filterProduct(String text)
    {
        if(listProduct != null && listProduct.size() >0)
        {
            ArrayList<Product> filterdNames = new ArrayList<>();

            for (Product s : listProduct)
            {
                if (s.getProductName().toLowerCase().contains(text.toLowerCase()))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                nearbycompanyProductsAdapter.filterList1(filterdNames);
                tv_productlist.setVisibility(View.GONE);
                rcv_products.setVisibility(View.VISIBLE);
            }
            else
            {
                tv_productlist.setVisibility(View.VISIBLE);
                rcv_products.setVisibility(View.GONE);
            }
        }
    }
    private void filterCompany(String text)
    {
        if(nearBycompnies != null && nearBycompnies.size() >0)
        {
            ArrayList<Company> filterdNames = new ArrayList<>();

            for (Company s : nearBycompnies)
            {
                if (s.getCompanyName().toLowerCase().contains(text.toLowerCase()))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                companyNearestAdapter.filterList(filterdNames);
                tv_companylist.setVisibility(View.GONE);
                rcv_nearest_company.setVisibility(View.VISIBLE);
            }
            else
            {
                tv_companylist.setVisibility(View.VISIBLE);
                rcv_nearest_company.setVisibility(View.GONE);
            }
        }
    }

    private void filterCategory(String text)
    {
        if(listCategory != null  && listCategory.size() >0)
        {
            ArrayList<Category> filterdNames = new ArrayList<>();

            for (Category s : listCategory)
            {
                if (s.getName().toLowerCase().contains(text.toLowerCase()))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                if(catcompanyAdapter != null)
                {
                    catcompanyAdapter.filterList(filterdNames);
                }

            }
            else
            {

            }
        }
    }

    @OnClick(R.id.fab_category)
    public void OpenCategoryActivity()
    {
        if(listCategory != null && listCategory.size() >0)
        {
            startRevealActivity(fab_category);
        }

    }

    private void startRevealActivity(View v)
    {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates

        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        Bundle args = new Bundle();
        intent.putExtra("list_category" , (Serializable) listCategory);
//        args.putSerializable("list_category",(Serializable)listCategory);
//        intent.putExtra("bundle",args);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    @OnClick(R.id.Ivsearch)
    public void ClickSearch() {
        if (isSerach == false) {
            isSerach = true;
            Title.setVisibility(View.GONE);
            EtSearch.setVisibility(View.VISIBLE);

        } else {
            isSerach = false;
            Title.setVisibility(View.VISIBLE);
            EtSearch.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed()
    {
        if (EtSearch.getVisibility() == View.VISIBLE)
        {
            Title.setVisibility(View.VISIBLE);
            EtSearch.setVisibility(View.GONE);
        }
        else
        {
            super.onBackPressed();

            Intent i_main = new Intent(Intent.ACTION_MAIN);
//            i_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            i_main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i_main.addCategory(Intent.CATEGORY_HOME);
            i_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i_main);
            finish();
        }

    }


//    @Override
//    public boolean onSupportNavigateUp()
//    {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

//    @OnClick({R.id.tvtemp_compnayprofile, R.id.tvsavedCompanyDetails})
//    public void onViewClicked(View view)
//    {
//        switch (view.getId())
//        {
//            case R.id.tvtemp_compnayprofile:
//                drawer.closeDrawer(Gravity.LEFT);
//                if (new PrefrenceManager(MainActivity.this).getLogout() == true)
//                {
//                    Intent intent = new Intent(MainActivity.this, CompanyProfileActivity.class);
//                    startActivity(intent);
////                    finish();
//                } else {
//                    displayLoginDialog();
//                }
//
//                break;
//            case R.id.tvsavedCompanyDetails:
//                drawer.closeDrawer(Gravity.LEFT);
//                if (new PrefrenceManager(MainActivity.this).getLogout() == true) {
//                    Intent intent1 = new Intent(MainActivity.this, SavedCompanyDetailsActivity.class);
//                    startActivity(intent1);
//                    finish();
//                } else {
//                    displayLoginDialog();
//                }
//
//                break;
//        }
//    }
    public void displayLogoutDialog()
    {
        Dialog d_login = new Dialog(MainActivity.this);
        d_login.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_login.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        d_login.setContentView(R.layout.dialog_logout);

        TextView tv_yes = (TextView) d_login.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) d_login.findViewById(R.id.tv_no);

        tv_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                d_login.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentuser = new PrefrenceManager(MainActivity.this).getCurrentuser();

                if(currentuser != null)
                {
                    String token = currentuser.getToken();

                    Log.e("token" , ""+token);
                    objInterface  = ApiClient.create_InstanceAuth(token);

                    if(objInterface != null)
                    {
                        if(ApiClient.isNetworkAvailable(MainActivity.this))
                        {
                            new Logout().execute();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, getString(R.string.no_connection) , Toast.LENGTH_LONG).show();
                        }
                    }
                }
                d_login.dismiss();
            }
        });
        d_login.show();
    }
    public void displayLoginDialog()
    {
        Dialog d_login = new Dialog(MainActivity.this);
        d_login.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_login.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        d_login.getWindow().setWindowAnimations(R.style.enter_exit_animate);
        d_login.setContentView(R.layout.dialog_login);

        TextView tv_login = (TextView) d_login.findViewById(R.id.tv_login);
        TextView tv_cancel = (TextView) d_login.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                d_login.dismiss();
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_start = new Intent(MainActivity.this, LoginActivity.class);
//                i_start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i_start);
//                finish();
                d_login.dismiss();
            }
        });
        d_login.show();
    }

    @OnClick({R.id.rcv_nearest_company, R.id.rcv_products})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.rcv_nearest_company:
                break;
            case R.id.rcv_products:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == StoreData.GPS_REQUEST)
            {
                isGPS = true; // flag maintain before get location
                Log.e("gps",""+isGPS);

                getLocation();

                if(currentLat != 0 && currentLong != 0)
                {
                    if(ApiClient.isNetworkAvailable(MainActivity.this))
                    {
//                        if(alreadyRun == true)
//                        {
//
//                        }
//                        else
//                        {
                        new GetHomePageData().execute();
                        new NearByCompnies().execute();
//                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }
    private void checkPermissions()
    {
        int PER_CODE = 777;

        mRequestPermissionHandler.requestPermission(this, permissions,
                PER_CODE, new RequestPermissionHandler.RequestPermissionListener()
                {
                    @Override
                    public void onSuccess()
                    {
                        Log.e("success","location success");
                        getLocation();

                        if(currentLat != 0 && currentLong != 0)
                        {
                            if(ApiClient.isNetworkAvailable(MainActivity.this))
                            {
//                                if(alreadyRun == true)
//                                {
//
//                                }
//                                else
//                                {
                                new GetHomePageData().execute();
                                new NearByCompnies().execute();
//                                }

                            }
                            else
                            {
                                Toast.makeText(MainActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailed()
                    {
                        Toast.makeText(MainActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(MainActivity.this)
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
                                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);

                                        checkPermissions();
                                    }
                                })
                                .create().show();
                    }

                });

    }
    public void ClickCompanyProducts(int id)
    {
        Log.e("companyid",""+id);
        if (new PrefrenceManager(MainActivity.this).getLogout() == true) {
            Intent intent = new Intent(MainActivity.this, CompanyProfileActivity.class);
            intent.putExtra("company_id", id);
            startActivity(intent);
//            finish();
        }
        else
        {
            displayLoginDialog();
        }

    }
    public void ClickNearByCompanyProducts(int id)
    {
        Log.e("companyid",""+id);
        if (new PrefrenceManager(MainActivity.this).getLogout() == true)
        {
            Intent intent = new Intent(MainActivity.this, CompanyProfileActivity.class);
            intent.putExtra("company_id", id);
            startActivity(intent);
//            finish();
        }
        else
        {
            displayLoginDialog();
        }

    }
    public void ClickNearCompany(int id)
    {
        if (new PrefrenceManager(MainActivity.this).getLogout() == true) {
            Intent intent = new Intent(MainActivity.this, CompanyProfileActivity.class);
            intent.putExtra("company_id", id);
            startActivity(intent);
//            finish();
        }
        else
        {
            displayLoginDialog();
        }
    }

    public class GetHomePageData extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getHomePageData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if(pd != null && pd.isShowing())
            {
                pd.dismiss();
            }
            alreadyRun = false;
        }
    }
    public class CategoryList extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd1 = new ProgressDialog(MainActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
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
            if(pd1 != null && pd1.isShowing())
            {
                pd1.dismiss();
            }
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
//                Log.e("response555", ""+response.code());

                if(response != null && response.isSuccessful())
                {
//                    Log.e("response beauty", new Gson().toJson(response.body()));

//                    Log.e("response" , ""+response.body().getResult());
                    if(response.body().getResult() == 1)
                    {
                        StoreData.categoryList = response.body().getCategories();

                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }

                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }

                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }

                    if(pd1 != null && pd1.isShowing())
                    {
                        pd1.dismiss();
                    }

                }
                else
                {
                    if(pd1 != null && pd1.isShowing())
                    {
                        pd1.dismiss();
                    }

                    Toast.makeText(MainActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd1 != null && pd1.isShowing())
                {
                    pd1.dismiss();
                }
                Toast.makeText(MainActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public void getHomePageData()
    {
        LatLong lat = new LatLong();
        lat.setLatitude(currentLat);
        lat.setLongitude(currentLong);
        lat.setLast50(last50);
        Log.e("last50",""+last50);

        Call<Response> call = apiInterface.Get_HomePageData(lat);
        Log.e("call" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
//                Log.e("responsHome", ""+response.code());

                if(response != null && response.isSuccessful())
                {

                    Log.e("responseHOME", new Gson().toJson(response.body()));

//                    Log.e("responseHome" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {

                        notificationCount = response.body().getTotalunreadnotification();

                        if(notificationCount > 0)
                        {
                            Log.e("notification_count",""+notificationCount);
                            badge_notification.setVisibility(View.VISIBLE);
                            badge_notification.setText(String.valueOf(notificationCount));
                        }
                        else
                        {
                            badge_notification.setVisibility(View.GONE);
                        }

                        listCategory = response.body().getCategories();
                        listProduct = response.body().getProducts();
                        listEvent = response.body().getEventList();

                        Log.e("CAT" , ""+listCategory.get(0).getName()+" size"+listCategory.size());
                        tv_total_customers.setText("Connecting "+response.body().getTotalcustomers()+" Customers to experts");

                        if(listCategory != null && listCategory.size() >0)
                        {
                            catcompanyAdapter = new CategoryWithCompanyAdapter(MainActivity.this, listCategory);
                            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                            rcv_category_company.setLayoutManager(manager);
                            rcv_category_company.setAdapter(catcompanyAdapter);
                        }
                        if(listProduct != null && listProduct.size() >0)
                        {
                            tv_productlist.setVisibility(View.GONE);
                            rcv_products.setVisibility(View.VISIBLE);
                            nearbycompanyProductsAdapter = new NearByCompanyProductsAdapter(MainActivity.this, listProduct);
                            LinearLayoutManager manager2 = new LinearLayoutManager(MainActivity.this,
                                    RecyclerView.HORIZONTAL, false);
                            rcv_products.setLayoutManager(manager2);
                            rcv_products.setAdapter(nearbycompanyProductsAdapter);
                        }
                        else
                        {
                            rcv_products.setVisibility(View.GONE);
                            tv_productlist.setVisibility(View.VISIBLE);
                        }

                        if(listEvent != null && listEvent.size() >0)
                        {
                            tv_eventlist.setVisibility(View.GONE);
                            rcv_events.setVisibility(View.VISIBLE);
                            nearbycompanyEventsAdapter = new NearByCompanyEventsAdapter(MainActivity.this, listEvent);
                            LinearLayoutManager manager2 = new LinearLayoutManager(MainActivity.this,
                                    RecyclerView.HORIZONTAL, false);
                            rcv_events.setLayoutManager(manager2);
                            rcv_events.setAdapter(nearbycompanyEventsAdapter);
                        }
                        else
                        {
                            rcv_events.setVisibility(View.GONE);
                            tv_eventlist.setVisibility(View.VISIBLE);
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

                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }

                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(MainActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(MainActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void AddOrder(Product objProduct, Company objCompany)
    {
        int  companyID, productID= 0, quantity=1, orderStatus =1, paymentmethod = 1;
        String price = null, custnum= null;

        if(objCompany != null)
        {
            companyID = objCompany.getId();
            custnum = objCompany.getCustomer_number();
        }
        if(objProduct != null)
        {
            productID = objProduct.getId();
            price = objProduct.getPrice();
        }
        String[] priceArr = price.split(" ");
        String currency = priceArr[0];
        String value = priceArr[1];


        Call<Response> call = objInterface.AddOrder(productID, currentuser.getUserNumber(),  Double.parseDouble(value),quantity,  orderStatus, paymentmethod);
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    Log.e("response",""+response.body());
                    if(response.body() != null && response.body().getResult() == 1)
                    {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {

            }
        });
    }
    public void BookOrder(Product objProduct, Company objCompany)
    {
        if(ApiClient.isNetworkAvailable(MainActivity.this))
        {
            AddOrder(objProduct, objCompany);
        }
        else
        {
            Toast.makeText(MainActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }



    public void ShowProductsDetails(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_product_details, null);
        final ImageView Ivback = dialogLayout.findViewById(R.id.Ivback);
        final TextView tv_product_desc = dialogLayout.findViewById(R.id.tv_product_desc);
        final TextView tv_product_price = dialogLayout.findViewById(R.id.tv_product_price);
        final TextView tv_product_name = dialogLayout.findViewById(R.id.tv_product_name);
        final TextView tv_company_name = dialogLayout.findViewById(R.id.tv_company_name);
        final TextView tv_product_id = dialogLayout.findViewById(R.id.tv_product_id);
        final RecyclerView rcv_product_images = dialogLayout.findViewById(R.id.rcv_product_images);
        TextView tvOrderNow = dialogLayout.findViewById(R.id.tvOrderNow);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();

        Product objProduct = listProduct.get(position);
        Company objCompany = objProduct.getCompany();

        if(objCompany != null)
        {
            tv_company_name.setText(""+objCompany.getCompany_name());
        }
        tv_product_id.setText(""+objProduct.getProduct_number());
        List<Image> listimages = objProduct.getImages();
        if(listimages != null && listimages.size() > 0)
        {
            ProductDeatilsImagesAdapter imagesAdapter = new ProductDeatilsImagesAdapter(MainActivity.this ,
                    listimages);
            rcv_product_images.setAdapter(imagesAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this , LinearLayoutManager.HORIZONTAL , false);
            rcv_product_images.setLayoutManager(manager);
        }
        tv_product_desc.setText(objProduct.getDescription());
        tv_product_name.setText(objProduct.getProductName());
        tv_product_price.setText(""+objProduct.getPrice());
        tv_company_name.setText(""+objProduct.getCompanyId());

        tvOrderNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BookOrder(objProduct, objCompany);
            }
        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alert.cancel();
            }
        });
        alert.show();
    }
    public class NearByCompnies extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd2 =new ProgressDialog(MainActivity.this);
            pd2.setMessage(getString(R.string.please_wait));
            pd2.setCancelable(false);
            pd2.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getNearByCompanies();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if(pd2 != null && pd2.isShowing())
            {
                pd2.dismiss();
            }
        }
    }
    public void getNearByCompanies()
    {
        Company objuser = new Company();
        objuser.setLat(Double.toString(currentLat));
        objuser.setLongi(Double.toString(currentLong));
        objuser.setUnit(unit);
        Call<Response> call = apiWithoutAuth.NearByCompanies(objuser);
//        Log.e("call1",""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
//                Log.e("responsHome1", ""+response.code());

                if(response != null && response.isSuccessful())
                {
//                    Log.e("response beautyhome1", new Gson().toJson(response.body()));
//
//                    Log.e("responseHome1", "" + response.body().getResult());

                    if (response.body().getResult() == 1)
                    {
                        nearBycompnies = response.body().getNearbyCompanies();

                        if (nearBycompnies != null && nearBycompnies.size() > 0)
                        {
                            tv_companylist.setVisibility(View.GONE);
                            rcv_nearest_company.setVisibility(View.VISIBLE);
                            companyNearestAdapter = new CompanyNearestAdapter(MainActivity.this, nearBycompnies);
                            LinearLayoutManager manager3 = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
                            rcv_nearest_company.setLayoutManager(manager3);
                            rcv_nearest_company.setAdapter(companyNearestAdapter);
                        }
                        else
                        {
                            rcv_nearest_company.setVisibility(View.GONE);
                            tv_companylist.setVisibility(View.VISIBLE);
                        }
                        if(pd2 != null && pd2.isShowing())
                        {
                            pd2.dismiss();
                        }
                    }
                    else
                    {
                        if(pd2 != null && pd2.isShowing())
                        {
                            pd2.dismiss();
                        }
                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    if(pd2 != null && pd2.isShowing())
                    {
                        pd2.dismiss();
                    }
                }
                else
                {
                    if(pd2 != null && pd2.isShowing())
                    {
                        pd2.dismiss();
                    }
                    Toast.makeText(MainActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd2 != null && pd2.isShowing())
                {
                    pd2.dismiss();
                }
                Toast.makeText(MainActivity.this  , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
