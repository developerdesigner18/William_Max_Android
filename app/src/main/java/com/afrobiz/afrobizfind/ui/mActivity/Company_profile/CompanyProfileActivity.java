package com.afrobiz.afrobizfind.ui.mActivity.Company_profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.afrobiz.afrobizfind.adapter.EventProfileAdapter;
import com.afrobiz.afrobizfind.ui.StreetViewActivity;
import com.afrobiz.afrobizfind.ui.events.EventImagesAdapter;
import com.afrobiz.afrobizfind.ui.events.TicketPurchaseActivity;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.EventImage;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.View_Image;
import com.afrobiz.afrobizfind.adapter.ProductDeatilsImagesAdapter;
import com.afrobiz.afrobizfind.adapter.ProductProfileAdapter;
import com.afrobiz.afrobizfind.adapter.SlidingImage_Adapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Offers;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.widgets.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class CompanyProfileActivity extends AppCompatActivity implements
        OnMapReadyCallback {
    AlertDialog alert;
    @BindView(R.id.tv_Monday_closetime)
    TextView tv_Monday_closetime;

    @BindView(R.id.tv_Monday_opentime)
    TextView tv_Monday_opentime;

    @BindView(R.id.tv_Tuesday_closetime)
    TextView tv_Tuesday_closetime;

    @BindView(R.id.tv_Tuesday_opentime)
    TextView tv_Tuesday_opentime;

    @BindView(R.id.tv_Wednesday_closetime)
    TextView tv_Wednesday_closetime;

    @BindView(R.id.tv_Wednesday_opentime)
    TextView tv_Wednesday_opentime;

    @BindView(R.id.tv_Thusday_closetime)
    TextView tv_Thusday_closetime;

    @BindView(R.id.tv_Thusday_opentime)
    TextView tv_Thusday_opentime;

    @BindView(R.id.tv_Friday_opentime)
    TextView tv_Friday_opentime;

    @BindView(R.id.tv_Friday_closetime)
    TextView tv_Friday_closetime;

    @BindView(R.id.tv_Saturday_closetime)
    TextView tv_Saturday_closetime;

    @BindView(R.id.tv_Saturday_opentime)
    TextView tv_Saturday_opentime;

    @BindView(R.id.tv_Sunday_closetime)
    TextView tv_Sunday_closetime;

    @BindView(R.id.tv_Sunday_opentime)
    TextView tv_Sunday_opentime;

    @BindView(R.id.tv_empty_offers)
    TextView tv_empty_offers;

    @BindView(R.id.tv_empty_product)
    TextView tv_empty_product;

    @BindView(R.id.tv_empty_event)
    TextView tv_empty_event;

    @BindView(R.id.tv_company_no)
    TextView tv_company_no;

    @BindView(R.id.tv_company_telephone_number)
    TextView tv_company_telephone_number;

    @BindView(R.id.tv_company_email)
    TextView tv_company_email;

    @BindView(R.id.tv_company_website)
    TextView tv_company_website;

    @BindView(R.id.tv_company_app_link)
    TextView tv_company_app_link;

    @BindView(R.id.tv_company_ethos)
    TextView tv_company_ethos;

    @BindView(R.id.RvOfferList)
    RecyclerView RvOfferList;

    @BindView(R.id.rcv_ProductList)
    RecyclerView rcv_ProductList;

    @BindView(R.id.rcv_EventList)
    RecyclerView rcv_EventList;

    @BindView(R.id.IvPlus)
    ImageView IvPlus;

    @BindView(R.id.Ivpminu)
    ImageView Ivpminu;

    @BindView(R.id.IvMessage)
    ImageView IvMessage;

    @BindView(R.id.IvDirection)
    ImageView IvDirection;

    Company currentCompany = null;

    @BindView(R.id.IvNumber_Dial)
    ImageView IvNumberDial;

    @BindView(R.id.IvaddContact)
    ImageView IvaddContact;

    private GoogleMap mMap;

    @BindView(R.id.IvBack)
    ImageView IvBack;

    @BindView(R.id.img_logo)
    ImageView img_logo;

    ProgressDialog pd2, pd1, pd3, pd4;

    @BindView(R.id.img_like)
    ImageView img_like;

    boolean isLike = false;

    ApiInterface objInterface;
    String token = null;
    int custNo = 0;

    String telephone = null, companyname = null;

    double lat;
    double longi;

    ProgressDialog pd;

    TextView tv_address, tv_company_name;

    LinearLayoutManager productlayoutManager, offerlayoutManager;

    OfferlistAdapter offerlistAdapter;

    Users currentuser;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    String companyLogo;
    LinearLayout layout_map;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    @BindView(R.id.viewpager)
    ViewPager mPager;

    @BindView(R.id.tv_likes)
    TextView tv_likes;

    int isFav = 0, totalLikes = 0, isCustomer = 0;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private List<Image> ImagesArray = new ArrayList<Image>();

    List<Offers> offersList = new ArrayList<>();
    List<Product> product_list = new ArrayList<>();
    List<Event> event_list = new ArrayList<>();
    int company_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_profile);
        ButterKnife.bind(this);

        layout_map = (LinearLayout) findViewById(R.id.layout_map);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);

        Bundle b = getIntent().getExtras();

        if (b != null)
        {
            company_id = b.getInt("company_id");
        }

        layout_map.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
//                startActivity(intent);

//                Intent i_start = new Intent(CompanyProfileActivity.this, StreetViewActivity.class);
//                i_start.putExtra("lat",lat);
//                i_start.putExtra("long",longi);
//                startActivity(i_start);
            }
        });

        currentuser = new PrefrenceManager(CompanyProfileActivity.this).getCurrentuser();

        if (currentuser != null) {
            token = currentuser.getToken();
            custNo = currentuser.getUserNumber();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null) {
                if (ApiClient.isNetworkAvailable(CompanyProfileActivity.this)) {
                    Log.e("company_id", "" + company_id);
                    new GetCompany(company_id).execute();
                } else {
                    Toast.makeText(CompanyProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
//                    showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
//                    hideOption(R.id.action_info);
                }
            }
        });
    }

    @OnClick(R.id.tv_company_email)
    public void openEmail() {
        if (tv_company_email.getText().toString().trim() != null) {
//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
//            intent.putExtra(Intent.EXTRA_EMAIL, tv_company_email.getText().toString().trim());
//            intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            }

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + tv_company_email.getText().toString().trim()));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "body");
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

            startActivity(Intent.createChooser(emailIntent, "Email"));
        }

    }

    public void ShowOfferDetails(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_offer_details, null);
        final ImageView Ivback = dialogLayout.findViewById(R.id.Ivback);
        final TextView tv_offer_id = dialogLayout.findViewById(R.id.tv_offer_id);
        final TextView tv_offer_code = dialogLayout.findViewById(R.id.tv_offer_code);
        final TextView tv_offer_name = dialogLayout.findViewById(R.id.tv_offer_name);
        final TextView tv_offer_details = dialogLayout.findViewById(R.id.tv_offer_details);
        final TextView tv_offer_discount = dialogLayout.findViewById(R.id.tv_offer_discount);
        final TextView tv_start_date = dialogLayout.findViewById(R.id.tv_start_date);
        final TextView tv_end_date = dialogLayout.findViewById(R.id.tv_end_date);
        final TextView tv_company_name = dialogLayout.findViewById(R.id.tv_company_name);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        if (alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.Dialog_Offers_Animation);
        }
        Ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

        Offers objOffers = offersList.get(position);
        Company objCompany = objOffers.getCompany();
        if (objCompany != null) {
            tv_company_name.setText(objCompany.getCompany_name());
        }
        tv_offer_id.setText("" + objOffers.getOffer_number());
        tv_offer_name.setText(objOffers.getName());
        tv_offer_code.setText(objOffers.getOfferCode());
        tv_offer_details.setText(objOffers.getOfferDetails());
        tv_offer_discount.setText(objOffers.getDiscount());
        tv_start_date.setText(objOffers.getStartDate());
        tv_end_date.setText(objOffers.getEndDate());

        alert.show();
    }

    public void ShowEventDeatils(int position)
    {
        Event objevent = event_list.get(position);

        Log.e("event details",""+new Gson().toJson(objevent));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_event_details, null);

        final ImageView Ivback = dialogLayout.findViewById(R.id.Ivback);
        final ImageView img_event = dialogLayout.findViewById(R.id.img_event);
        final TextView tv_event_name = dialogLayout.findViewById(R.id.tv_event_name);
        final TextView tv_event_date = dialogLayout.findViewById(R.id.tv_event_date);
        final TextView tv_event_price = dialogLayout.findViewById(R.id.tv_event_price);
        final TextView tv_org_contact = dialogLayout.findViewById(R.id.tv_org_contact);
        final TextView tv_org = dialogLayout.findViewById(R.id.tv_org);
        final TextView tv_empty = dialogLayout.findViewById(R.id.tv_empty);
        final TextView tv_tc = dialogLayout.findViewById(R.id.tv_tc);
        final TextView tv_location = dialogLayout.findViewById(R.id.tv_location);
        final RecyclerView rcv_event_images = dialogLayout.findViewById(R.id.rcv_event_images);
        TextView tv_buy_ticket = dialogLayout.findViewById(R.id.tv_buy_ticket);

        builder.setView(dialogLayout);
        builder.setCancelable(true);

        AlertDialog alert = builder.create();

        if (alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.Dialog_Event_Animation);
        }

        List<EventImage> listimages = objevent.getEventimages();

        String imagepath = objevent.getImagepath();

        if(imagepath != null)
        {
            Glide.with(CompanyProfileActivity.this).load(imagepath).into(img_event);
        }
        if (listimages != null && listimages.size() > 0)
        {
            rcv_event_images.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);

            EventImagesAdapter imagesAdapter = new EventImagesAdapter(CompanyProfileActivity.this, listimages);
            rcv_event_images.setAdapter(imagesAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(CompanyProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rcv_event_images.setLayoutManager(manager);
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_event_images.setVisibility(View.GONE);
        }
        tv_event_date.setText("" + objevent.getDate());
        tv_event_name.setText(objevent.getEventname());
        tv_event_price.setText(""+objevent.getPrice());
        tv_location.setText("" + objevent.getLocation());
        tv_tc.setText("" + objevent.getTermscondition());
        tv_org.setText(""+objevent.getOrganizer());
        tv_org_contact.setText("" + objevent.getContactno());

        tv_buy_ticket.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_purchase = new Intent(CompanyProfileActivity.this, TicketPurchaseActivity.class);
                i_purchase.putExtra("event",objevent);
                i_purchase.putExtra("company_id",company_id);
                startActivity(i_purchase);
            }
        });

        img_event.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_purchase = new Intent(CompanyProfileActivity.this, View_Image.class);
                i_purchase.putExtra("path", imagepath);
                startActivity(i_purchase);
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

    public class GetAllCompanyProducts extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd1 = new ProgressDialog(CompanyProfileActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getAllCompanyProducts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getAllCompanyProducts()
    {
        Company objProduct = new Company();
        objProduct.setId(company_id);

        Call<Response> call = objInterface.GetAllCompanyProducts(objProduct);

        Log.e("callGet", "" + call.request());

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("code", "" + response.code());

                if (response != null && response.isSuccessful())
                {
                    Log.e("response beauty", new Gson().toJson(response.body()));

                    Log.e("response", "" + response.body().getResult());

                    if (response.body().getResult() == 1)
                    {
                        if (pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }

                    } else if (response.body().getResult() == 0)
                    {
                        if (pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if (pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if (pd1 != null && pd1.isShowing())
                    {
                        pd1.dismiss();
                    }
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if (pd1 != null && pd1.isShowing())
                {
                    pd1.dismiss();
                }
                Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else
        {
//            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(sydney).title(companyname));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

//    @OnClick({R.id.RvOfferList, R.id.rcv_ProductList})
//    public void onViewClicked(View view)
//    {
//        switch (view.getId())
//        {
//            case R.id.RvOfferList:
//                break;
//
//            case R.id.rcv_ProductList:
//                break;
//        }
//    }

    @OnClick(R.id.IvPlus)
    public void onViewClicked11()
    {
        Dialog();
    }

    @OnClick(R.id.Ivpminu)
    public void onMinusClick()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_remove_customer, null);
        final TextView tv_yes = dialogLayout.findViewById(R.id.tv_yes);
        final TextView tv_no = dialogLayout.findViewById(R.id.tv_no);

        builder.setView(dialogLayout);
        builder.setCancelable(true);

        AlertDialog alert = builder.create();

        if (alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.SlidingDialogAnimation);
        }

        tv_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CustomerRemove();

                alert.cancel();
            }

            private void CustomerRemove()
            {
                if (ApiClient.isNetworkAvailable(CompanyProfileActivity.this))
                {
                    if (objInterface != null)
                    {
                        new RemoveCustomerFromCompany(company_id).execute();
                    }
                }
                else
                {
                    Toast.makeText(CompanyProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.cancel();
            }
        });
        alert.show();
    }

    private void Dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_become_customer, null);
        final TextView tv_no = dialogLayout.findViewById(R.id.tv_no);
        final TextView tv_yes = dialogLayout.findViewById(R.id.tv_yes);
        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();

        if (alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.enter_exit_animate);
        }
//            alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;



        tv_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CustomerAdd();

                alert.cancel();
            }

            private void CustomerAdd()
            {
                if (ApiClient.isNetworkAvailable(CompanyProfileActivity.this))
                {
                    if (objInterface != null)
                    {
                        new AddCustomerInCompany(company_id, 1).execute();
                    }
                }
                else
                {
                    Toast.makeText(CompanyProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.show();
    }

    @OnClick(R.id.IvNumber_Dial)
    public void onViewClicked1()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telephone));
        startActivity(intent);
    }

    @OnClick(R.id.IvaddContact)
    public void onViewClicked2()
    {
        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, companyname)
                .putExtra(ContactsContract.Intents.Insert.PHONE, telephone);

        startActivityForResult(contactIntent, 1);
    }

    @OnClick(R.id.IvMessage)
    public void onMsgClicked() {
        openWhatsApp();
    }

    public void openWhatsApp()
    {
        PackageManager pm = getPackageManager();

        try
        {
            String toNumber = telephone;

            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + toNumber + "?body=" + ""));
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }
    }
    public class GetCompany extends AsyncTask<Void, Void, Void>
    {
        int id = 0;


        public GetCompany(int company_id) {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(CompanyProfileActivity.this);
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null)
            {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return p1;
    }
    public void getCompany(int id)
    {
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response> call = objInterface.Get_CompanyDetails(objCompany);

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {
                        currentCompany = response.body().getCompany();

                        Log.e("product beauty", new Gson().toJson(response.body().getCompany().getProducts()));
                        product_list = response.body().getCompany().getProducts();
                        event_list = response.body().getEventList();

                        if (product_list != null && product_list.size() > 0)
                        {
                            rcv_ProductList.setVisibility(View.VISIBLE);
                            tv_empty_product.setVisibility(View.GONE);

                            ProductProfileAdapter products_Adapter =
                                    new ProductProfileAdapter(CompanyProfileActivity.this, product_list);
                            LinearLayoutManager maanger = new LinearLayoutManager(CompanyProfileActivity.this,
                                    RecyclerView.HORIZONTAL, false);
                            rcv_ProductList.setLayoutManager(maanger);
                            rcv_ProductList.setAdapter(products_Adapter);
//                            productsAdapter.notifyDataSetChanged();

                            Log.e("productlist", "" + product_list.size() + " " + rcv_ProductList.getVisibility());
                        }
                        else
                        {
                            rcv_ProductList.setVisibility(View.GONE);
                            tv_empty_product.setVisibility(View.VISIBLE);
                        }

                        if (event_list != null && event_list.size() > 0)
                        {
                            rcv_EventList.setVisibility(View.VISIBLE);
                            tv_empty_event.setVisibility(View.GONE);

                            EventProfileAdapter event_Adapter = new EventProfileAdapter(CompanyProfileActivity.this, event_list);
                            LinearLayoutManager maanger = new LinearLayoutManager(CompanyProfileActivity.this, RecyclerView.HORIZONTAL, false);
                            rcv_EventList.setLayoutManager(maanger);
                            rcv_EventList.setAdapter(event_Adapter);
//                            productsAdapter.notifyDataSetChanged();

                            Log.e("rcv_EventList", "" + event_list.size() + " " + rcv_EventList.getVisibility());
                        }
                        else
                        {
                            rcv_EventList.setVisibility(View.GONE);
                            tv_empty_event.setVisibility(View.VISIBLE);
                        }

                        Log.e("offers beauty", new Gson().toJson(response.body().getCompany().getActiveoffers()));
                        offersList = response.body().getCompany().getActiveoffers();
                        if (offersList != null && offersList.size() > 0)
                        {
                            RvOfferList.setVisibility(View.VISIBLE);
                            tv_empty_offers.setVisibility(View.GONE);

                            offerlistAdapter = new OfferlistAdapter(CompanyProfileActivity.this, offersList);
                            offerlayoutManager = new LinearLayoutManager(CompanyProfileActivity.this,
                                    RecyclerView.HORIZONTAL, false);
                            RvOfferList.setLayoutManager(offerlayoutManager);
                            RvOfferList.setAdapter(offerlistAdapter);
                            offerlistAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            RvOfferList.setVisibility(View.GONE);
                            tv_empty_offers.setVisibility(View.VISIBLE);
                        }

                        lat = Double.parseDouble(currentCompany.getLat());
                        longi = Double.parseDouble(currentCompany.getLongi());

                        Log.e("latitiude", "" + lat);
                        Log.e("longitude", "" + longi);

                        telephone = currentCompany.getTelephone();
                        isFav = currentCompany.getIs_favourite();
                        totalLikes = currentCompany.getTotalfavorite();
                        isCustomer = currentCompany.getIs_customer();

                        tv_likes.setText("Liked by" + " " + totalLikes + " customers");

                        tv_company_no.setText("" + currentCompany.getCompany_number());

                        Log.e("isFav", "" + isFav);

                        if (isFav == 1)
                        {
                            isLike = true;
                            img_like.setImageResource(R.drawable.ic_favorite_white);
                        }
                        else
                        {
                            isLike = false;
                            img_like.setImageResource(R.drawable.ic_none_favorite_white);
                        }

                        if (isCustomer == 1)
                        {
                            IvPlus.setEnabled(false);
                            Ivpminu.setEnabled(true);
                            IvPlus.setImageResource(R.drawable.plus_gray);
                            Ivpminu.setImageResource(R.drawable.minus_black);
                        }
                        else
                        {
                            Ivpminu.setEnabled(false);
                            IvPlus.setEnabled(true);
                            IvPlus.setImageResource(R.drawable.plus_black);
                            Ivpminu.setImageResource(R.drawable.minus_gray);
                        }

                        tv_Monday_closetime.setText(currentCompany.getMonday_closing());
                        tv_Monday_opentime.setText(currentCompany.getMonday_opening());
                        tv_Tuesday_closetime.setText(currentCompany.getTuesday_closing());
                        tv_Tuesday_opentime.setText(currentCompany.getTuesday_opening());
                        tv_Wednesday_closetime.setText(currentCompany.getWednesday_closing());
                        tv_Wednesday_opentime.setText(currentCompany.getWednesday_opening());
                        tv_Thusday_closetime.setText(currentCompany.getThursday_closing());
                        tv_Thusday_opentime.setText(currentCompany.getThursday_opening());
                        tv_Friday_closetime.setText(currentCompany.getFriday_closing());
                        tv_Friday_opentime.setText(currentCompany.getFriday_opening());
                        tv_Saturday_closetime.setText(currentCompany.getSaturday_closing());
                        tv_Saturday_opentime.setText(currentCompany.getSaturday_opening());
                        tv_Sunday_closetime.setText(currentCompany.getSunday_closing());
                        tv_Sunday_opentime.setText(currentCompany.getSunday_opening());

                        tv_company_email.setText(currentCompany.getEmail());
//                        tv_company_ethos.setText(currentCompany.getCompany_ethos());
                        tv_company_telephone_number.setText(currentCompany.getTelephone());
                        tv_company_website.setText(currentCompany.getWebsite());

                        tv_company_app_link.setText(currentCompany.getApplink());

                        if (currentCompany.getApplink() != null)
                        {
                            tv_company_app_link.setText(currentCompany.getApplink());
                        }
                        else
                        {
                            tv_company_app_link.setText("Not Available");
                        }
                        if (currentCompany.getCompany_ethos() != null) {
                            tv_company_ethos.setText(currentCompany.getCompany_ethos());
                        } else {
                            tv_company_ethos.setText("Not Available");
                        }

                        companyname = currentCompany.getCompanyName();

                        tv_company_name.setText(currentCompany.getCompanyName());

                        tv_address.setText(currentCompany.getBuilding_number() + ", " + currentCompany.getAddress_line_1() + ", "
                                + currentCompany.getCity() + ", " + currentCompany.getPostcode());


                        LatLng latLng = getLocationFromAddress(CompanyProfileActivity.this, tv_address.getText().toString());

//                        LatLng latLng = new LatLng(lat,longi);

                        mMap.addMarker(new MarkerOptions().position(latLng).title(companyname));

                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        companyLogo = currentCompany.getCompany_image();

                        if (companyLogo != null) {
                            Glide.with(CompanyProfileActivity.this).load(companyLogo).into(img_logo);
                        }

                        ImagesArray = currentCompany.getImages();

                        if (ImagesArray != null && ImagesArray.size() > 0) {
                            initSlider();
                        }
//                        if(currentCompany != null)
//                        {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
//                        }
                    } else if (response.body().getResult() == 0) {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd3 != null & pd3.isShowing()) {
                        pd3.dismiss();
                    }
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }



            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd3 != null & pd3.isShowing()) {
                    pd3.dismiss();
                }
                Toast.makeText(CompanyProfileActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @OnClick(R.id.img_logo)
    public void onClickLogo() {
        if (companyLogo != null) {
            Intent i_image = new Intent(CompanyProfileActivity.this, View_Image.class);
            i_image.putExtra("path", companyLogo);
            startActivity(i_image);
        }
    }

    public class AddToFav extends AsyncTask<Void, Void, Void> {
        int c_id = 0;

        public AddToFav(int company_id) {
            this.c_id = company_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd1 = new ProgressDialog(CompanyProfileActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            addFav(c_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void addFav(int id)
    {
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response> call = objInterface.AddFavouriteCompany(objCompany);

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1)
                    {
                        if (pd1 != null & pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        totalLikes = totalLikes + 1;
                        tv_likes.setText("Liked by" + " " + totalLikes + " customers");
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else if (response.body().getResult() == 0)
                    {
                        if (pd1 != null & pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if (pd1 != null & pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if (pd1 != null & pd1.isShowing())
                    {
                        pd1.dismiss();
                    }
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd1 != null & pd1.isShowing()) {
                    pd1.dismiss();
                }
                Toast.makeText(CompanyProfileActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class RemoveFromFav extends AsyncTask<Void, Void, Void> {
        int c_id = 0;

        public RemoveFromFav(int company_id) {
            this.c_id = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd1 = new ProgressDialog(CompanyProfileActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            removeFav(c_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void removeFav(int id) {
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response> call = objInterface.RemoveFavouriteCompany(objCompany);

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {
                        if (pd1 != null & pd1.isShowing()) {
                            pd1.dismiss();
                        }
                        totalLikes = totalLikes - 1;
                        tv_likes.setText("Liked by" + " " + totalLikes + " customers");
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getResult() == 0) {
                        if (pd1 != null & pd1.isShowing()) {
                            pd1.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd1 != null & pd1.isShowing()) {
                            pd1.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd1 != null & pd1.isShowing()) {
                        pd1.dismiss();
                    }
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd1 != null & pd1.isShowing()) {
                    pd1.dismiss();
                }
                Toast.makeText(CompanyProfileActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public class RemoveCustomerFromCompany extends AsyncTask<Void, Void, Void> {
        int c_id = 0;

        public RemoveCustomerFromCompany(int company_id) {
            this.c_id = company_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd4 = new ProgressDialog(CompanyProfileActivity.this);
            pd4.setCancelable(false);
            pd4.setMessage(getString(R.string.please_wait));
            pd4.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("doin", "");
            removeCustomer(c_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void removeCustomer(int c_id)
    {
        Company objCompany = new Company();
        objCompany.setId(c_id);

        Call<Response> call = objInterface.RemoveCustomer(objCompany);

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {
                        if (pd4 != null & pd4.isShowing()) {
                            pd4.dismiss();
                        }
                        Ivpminu.setEnabled(false);
                        IvPlus.setEnabled(true);
                        IvPlus.setImageResource(R.drawable.plus_black);
                        Ivpminu.setImageResource(R.drawable.minus_gray);
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getResult() == 0) {
                        if (pd4 != null & pd4.isShowing()) {
                            pd4.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd4 != null & pd4.isShowing()) {
                            pd4.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd4 != null & pd4.isShowing()) {
                        pd4.dismiss();
                    }
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd4 != null & pd4.isShowing()) {
                    pd4.dismiss();
                }
                Toast.makeText(CompanyProfileActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class AddCustomerInCompany extends AsyncTask<Void, Void, Void> {
        int c_id = 0, mobileallow = 0;

        public AddCustomerInCompany(int company_id, int mobile_allow) {
            Log.e("con", "");
            this.c_id = company_id;
            mobileallow = mobile_allow;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd2 = new ProgressDialog(CompanyProfileActivity.this);
            pd2.setCancelable(false);
            pd2.setMessage(getString(R.string.please_wait));
            pd2.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("doin", "");
            addAsCustomer(c_id, mobileallow);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void addAsCustomer(int c_id, int mobileallowed) {
        Company objCompany = new Company();
        objCompany.setId(c_id);
        objCompany.setMobileallowed(mobileallowed);

        Call<Response> call = objInterface.AddCustomer(objCompany);

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {
                        if (pd2 != null & pd2.isShowing()) {
                            pd2.dismiss();
                        }
                        IvPlus.setEnabled(false);
                        Ivpminu.setEnabled(true);
                        IvPlus.setImageResource(R.drawable.plus_gray);
                        Ivpminu.setImageResource(R.drawable.minus_black);
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getResult() == 0) {
                        if (pd2 != null & pd2.isShowing()) {
                            pd2.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd2 != null & pd2.isShowing()) {
                            pd2.dismiss();
                        }
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd2 != null & pd2.isShowing()) {
                        pd2.dismiss();
                    }
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd2 != null & pd2.isShowing()) {
                    pd2.dismiss();
                }
                Toast.makeText(CompanyProfileActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.IvDirection)
    public void goToDirection() {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)",
                lat, longi, "Where the company is at");
        Log.e("diectionuri", "" + Uri.parse(uri).toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void initSlider()
    {
        mPager.setAdapter(new SlidingImage_Adapter(CompanyProfileActivity.this, ImagesArray));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        NUM_PAGES = ImagesArray.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @OnClick(R.id.IvBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.img_like)
    public void GoForLike() {
        if (isLike == false) {
            isLike = true;
            img_like.setImageResource(R.drawable.ic_favorite_white);

            if (ApiClient.isNetworkAvailable(CompanyProfileActivity.this)) {
                if (objInterface != null) {
                    Log.e("company_id", "" + company_id);
                    new AddToFav(company_id).execute();
                }
            } else {
                Toast.makeText(CompanyProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            isLike = false;
            img_like.setImageResource(R.drawable.ic_none_favorite_white);
            if (ApiClient.isNetworkAvailable(CompanyProfileActivity.this)) {
                if (objInterface != null) {
                    Log.e("company_id", "" + company_id);
                    new RemoveFromFav(company_id).execute();
                }
            } else {
                Toast.makeText(CompanyProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (alert != null && alert.isShowing()) {
            alert.cancel();
        } else {
            super.onBackPressed();
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
        final TextView tv_empty = dialogLayout.findViewById(R.id.tv_empty);
        final TextView tv_product_id = dialogLayout.findViewById(R.id.tv_product_id);
        final RecyclerView rcv_product_images = dialogLayout.findViewById(R.id.rcv_product_images);
        TextView tvOrderNow = dialogLayout.findViewById(R.id.tvOrderNow);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        if (alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.FadDialogAnimation1);
        }
        Product objProduct = product_list.get(position);
        Company objCompany = objProduct.getCompany();

        if (objCompany != null) {
            tv_company_name.setText("" + objCompany.getCompany_name());
        }
        tv_product_id.setText("" + objProduct.getProduct_number());
        List<Image> listimages = objProduct.getImages();
        if (listimages != null && listimages.size() > 0) {
            rcv_product_images.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);

            ProductDeatilsImagesAdapter imagesAdapter = new ProductDeatilsImagesAdapter(CompanyProfileActivity.this,
                    listimages);
            rcv_product_images.setAdapter(imagesAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(CompanyProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rcv_product_images.setLayoutManager(manager);

        } else {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_product_images.setVisibility(View.GONE);
        }
        tv_product_desc.setText(objProduct.getDescription());
        tv_product_name.setText(objProduct.getProductName());
        tv_product_price.setText("" + objProduct.getPrice());

        tvOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApiClient.isNetworkAvailable(CompanyProfileActivity.this)) {
                    BookOrder(objProduct, objCompany);
                } else {
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();

                }
            }
        });
        Ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });


        alert.show();
    }

    public void AddOrder(Product objProduct, Company objCompany) {
        int companyID, productID = 0, quantity = 1, orderStatus = 1, paymentmethod = 1;
        String price = null, custnum = null;

        if (objCompany != null) {
            companyID = objCompany.getId();
            custnum = objCompany.getCustomer_number();
        }
        if (objProduct != null) {
            productID = objProduct.getId();
            price = objProduct.getPrice();
        }

        String[] priceArr = price.split(" ");
        String currency = priceArr[0];
        String value = priceArr[1];
        double d = new Double(value).doubleValue();
        Log.e("price", "" + priceArr[0] + "        " + priceArr[1]);

        Log.e("TAG", "AddOrder productID:- "+productID+" custnum:- "
        +" "+" value: "+  d+" quantity: "+quantity+" orderStatus: "+orderStatus
        +" paymentmethod: "+paymentmethod);
        Call<Response> call = objInterface.AddOrder(productID, currentuser.getUserNumber() , Double.parseDouble(value), quantity, orderStatus, paymentmethod);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response != null && response.isSuccessful()) {
                    Log.e("response", "" + response.body());
                    if (response.body() != null && response.body().getResult() == 1) {
                        Toast.makeText(CompanyProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CompanyProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    public void BookOrder(Product objProduct, Company objCompany) {
        if (ApiClient.isNetworkAvailable(CompanyProfileActivity.this)) {
            AddOrder(objProduct, objCompany);
        } else {
            Toast.makeText(CompanyProfileActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }
}




