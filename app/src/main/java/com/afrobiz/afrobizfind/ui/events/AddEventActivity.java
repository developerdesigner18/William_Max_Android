package com.afrobiz.afrobizfind.ui.events;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.CropImageActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.RequestPermissionHandler;
import com.afrobiz.afrobizfind.StoreData;
import com.afrobiz.afrobizfind.adapter.EventImagesAdapter;
import com.afrobiz.afrobizfind.adapter.UpdateEventImageAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.AddProductActivity;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.EventImage;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyProductListActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddEventActivity extends AppCompatActivity {
    ProgressDialog pd3, pd, pd2;

    @BindView(R.id.save_event)
    TextView save_event;

    @BindView(R.id.tvBuyTicket)
    TextView tvBuyTicket;

    @BindView(R.id.etEventName)
    EditText etEventName;

    @BindView(R.id.etDate)
    EditText etDate;

    @BindView(R.id.etLocation)
    EditText etLocation;

    @BindView(R.id.etEventPrice)
    EasyMoneyEditText etEventPrice;

    @BindView(R.id.etTerms)
    EditText etTerms;

    @BindView(R.id.et_MaxTicket)
    EditText et_MaxTicket;

    @BindView(R.id.etOrg_Name)
    EditText etOrg_Name;

    @BindView(R.id.etOrg_Number)
    EditText etOrg_Number;

    @BindView(R.id.fab_gallery)
    FloatingActionButton fab_gallery;

    @BindView(R.id.relative_image)
    RelativeLayout relative_image;

    @BindView(R.id.fab_gallery_logo)
    FloatingActionButton fab_gallery_logo;

    @BindView(R.id.rcv_update)
    RecyclerView rcv_update;

    @BindView(R.id.rcv_add)
    RecyclerView rcv_add;

    @BindView(R.id.tv_empty)
    TextView tv_empty;

    @BindView(R.id.frame_logo_image)
    RelativeLayout frame_logo_image;

    @BindView(R.id.img_logo)
    ImageView img_logo;

    @BindView(R.id.img_cancel)
    ImageView img_cancel;

    @BindView(R.id.card)
    CardView card;
    List<MultipartBody.Part> update_parts = new ArrayList<>();
    Cursor cursor;
    boolean update = false;
    private RequestPermissionHandler mRequestPermissionHandler;
    public static final int PICK_LOGO_FROM_GALLERY = 899, CROP_LOGO_IMAGE = 565, PICK_IMAGE_FROM_GALLERY1 = 222, CROP_IMAGE1 = 101;
    List<EventImage> imglist = new ArrayList<>();
    List<Image> Eventimglist = new ArrayList<>();
    UpdateEventImageAdapter updateAdapter;
    EventImagesAdapter imagesAdapter;
    //    UpdateEventImage adapters;
    String EventLogoPath = null, UpdateEventLogoPath = null;
    EventImagesAdapter eventAdapter;
    StringBuffer deletImageBuffer = null;
    ;
    String[] permissions =
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
    DisplayMetrics metrics;
    int screenwidth, screenheight;

    List<MultipartBody.Part> parts = new ArrayList<>();
    List<MultipartBody.Part> parts1 = new ArrayList<>();

    ApiInterface apiWithoutAuth, objInterface;
    Users currentuser;
    String token = null;
    int CompanyId = 0, event_id = 0 ;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    private Calendar myCalendar = Calendar.getInstance();
    Event currentEvent;

    @BindView(R.id.tv_toolbar)
    TextView tv_toolbar;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);

        metrics = getResources().getDisplayMetrics();
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;

        ButterKnife.bind(this);

        mRequestPermissionHandler = new RequestPermissionHandler();
        setParam();

        currentuser = new PrefrenceManager(AddEventActivity.this).getCurrentuser();

        if (currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);
        }

        Bundle b = getIntent().getExtras();

        if (b != null)
        {
            CompanyId = b.getInt("company_id");
            currentEvent = (Event) b.getSerializable("event");

            update = b.getBoolean("update");

            tv_toolbar.setText("Update Event");
        }
        else
        {
            tv_toolbar.setText("Add Event");
        }

        if(update == true)
        {
            tv_toolbar.setText("Update Event");
            tvBuyTicket.setVisibility(View.VISIBLE);

            if(currentEvent == null)
            {
                if(objInterface != null)
                {
                    if (ApiClient.isNetworkAvailable(AddEventActivity.this))
                    {
                        new GetEvent(event_id).execute();
                    }
                    else
                    {
                        Toast.makeText(AddEventActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                setEventDetails();
            }
        }
        else
        {
            tvBuyTicket.setVisibility(View.GONE);
            tv_toolbar.setText("Add Event");
        }
    }

    public class GetEvent extends  AsyncTask<Void , Void, Void>
    {
        int eventid = 0 ;
        public GetEvent(int event_id)
        {
            this.eventid = event_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd2 = new ProgressDialog(AddEventActivity.this);
            pd2.setCancelable(false);
            pd2.setMessage(getString(R.string.please_wait));
            pd2.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getEvent(eventid);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getEvent(int eventid1)
    {
        Call<Response>  call = objInterface.GetSingleEvent(eventid1);
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
                        currentEvent = response.body().getEvent();

                        setEventDetails();

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
                        Toast.makeText(AddEventActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd2 != null & pd2.isShowing())
                        {
                            pd2.dismiss();
                        }
                        Toast.makeText(AddEventActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd2 != null & pd2.isShowing())
                    {
                        pd2.dismiss();
                    }
                    Toast.makeText(AddEventActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd2 != null & pd2.isShowing())
                {
                    pd2.dismiss();
                }
                Toast.makeText(AddEventActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void setEventDetails()
    {
        event_id = currentEvent.getId();
        CompanyId = currentEvent.getCompanyId();

        etTerms.setText(currentEvent.getTermscondition());
        etLocation.setText(currentEvent.getLocation());
        etOrg_Number.setText(currentEvent.getContactno());
        etOrg_Name.setText(currentEvent.getOrganizer());
        etEventPrice.setText(""+currentEvent.getPrice());
        etDate.setText(currentEvent.getDate());
        etEventName.setText(currentEvent.getEventname());

        imglist = currentEvent.getEventimages();

        StoreData.eventList.clear();

        if(imglist != null && imglist.size() > 0)
        {
            relative_image.setVisibility(View.VISIBLE);
            rcv_add.setVisibility(View. GONE);
            rcv_update.setVisibility(View.VISIBLE);
        }
        fillUpdatedImage();
    }

    @OnClick(R.id.save_event)
    public void saveEvent()
    {
        if (etEventName.getText().toString().equals(""))
        {
            etEventName.setError("Enter Event Name");
            return;
        }
        else
        {
            etEventName.setError(null);
        }

        if (etDate.getText().toString().equals(""))
        {
            etDate.setError("Enter Event Date");
            return;
        }
        else
        {
            etDate.setError(null);
        }
        if (etEventPrice.getText().toString().equals(""))
        {
            etEventPrice.setError("Enter Event Price");
            return;
        }
        else
        {
            etEventPrice.setError(null);
        }

        if (etLocation.getText().toString().equals(""))
        {
            etLocation.setError("Enter Event Location");
            return;
        }
        else
        {
            etLocation.setError(null);
        }

        if (etTerms.getText().toString().equals(""))
        {
            etTerms.setError("Enter Event Terms & Conditions");
            return;
        }
        else
        {
            etTerms.setError(null);
        }

        if (etOrg_Name.getText().toString().equals(""))
        {
            etOrg_Name.setError("Enter Organization Name");
            return;
        }
        else
        {
            etOrg_Name.setError(null);
        }

        if (etOrg_Number.getText().toString().equals(""))
        {
            etOrg_Number.setError("Enter Organization Number");
            return;
        }
        else
        {
            etOrg_Number.setError(null);
        }

        if(update == true)
        {
            if(imglist != null && imglist.size() >0)
            {
                if(ApiClient.isNetworkAvailable(AddEventActivity.this))
                {
                    if(objInterface != null)
                    {
                        Log.e("product","update");
                        new UpdateEvent().execute();
                    }
                }
                else
                {
                    Toast.makeText(AddEventActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(AddEventActivity.this , "Please add at least one image of event", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (StoreData.eventList != null && StoreData.eventList.size() > 0)
            {
                if (ApiClient.isNetworkAvailable(AddEventActivity.this))
                {
                    Log.e("event", "add");
                    new Add_Event().execute();
                }
                else
                {
                    Toast.makeText(AddEventActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(AddEventActivity.this, "Please add at least one image of event", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Add_Event extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(AddEventActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            addEvent();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void addEvent()
    {
        try
        {
            Log.e("body", "event_add body");

            String name = etEventName.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String price = etEventPrice.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String Terms = etTerms.getText().toString().trim();
            String orgName = etOrg_Name.getText().toString().trim();
            String orgNumber = etOrg_Number.getText().toString().trim();
            String maxTicket = et_MaxTicket.getText().toString().trim();

            RequestBody p_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody p_date = RequestBody.create(MediaType.parse("multipart/form-data"), date);
            RequestBody P_price = RequestBody.create(MediaType.parse("multipart/form-data"), price);
            RequestBody p_location = RequestBody.create(MediaType.parse("multipart/form-data"), location);
            RequestBody p_Terms = RequestBody.create(MediaType.parse("multipart/form-data"), Terms);
            RequestBody p_orgName = RequestBody.create(MediaType.parse("multipart/form-data"), orgName);
            RequestBody p_orgNumber = RequestBody.create(MediaType.parse("multipart/form-data"), orgNumber);
            RequestBody p_max_ticket = RequestBody.create(MediaType.parse("multipart/form-data"), maxTicket);
            RequestBody p_CompanyId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(CompanyId));

//            Log.e("compnyid111",""+CompanyId);

            Log.e("dck", "" + p_name + " " + p_date + " " + " " + P_price + " " + p_location + " " + p_Terms + " " + p_orgName + " " + p_orgNumber + " "
                    + StoreData.eventList.size());

            if (StoreData.eventList != null && StoreData.eventList.size() > 0)
            {
                for (int i = 0; i < StoreData.eventList.size(); i++)
                {
                    parts.add(prepareFilePart("eventimage[]", StoreData.eventList.get(i)));
                    Log.e("parts", "" + parts.get(i).body().toString());
                }
            }

            MultipartBody.Part eventlogo = null;

            if (EventLogoPath != null)
            {
//                parts1.add(prepareFilePart("flyerimage", EventLogoPath));
                eventlogo = prepareFilePart("flyerimage", EventLogoPath);
            }
            Call<Response> call = objInterface.AddEvent(eventlogo, parts, p_CompanyId , p_name, p_date,
                    p_location, p_Terms, p_orgName, p_orgNumber, P_price, p_max_ticket);

            Log.e("call", "" + call.request());
            call.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {

                    if (response != null && response.isSuccessful())
                    {
                        Log.e("response", "" + new Gson().toJson(response.body()));
                        if (response.body().getResult() != 0)
                        {
                            if (pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            StoreData.eventList.clear();;
                            onBack();
                            Toast.makeText(AddEventActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            Toast.makeText(AddEventActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if (pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Log.e("AK", "onResponse: " + response.body().getMessage() + " code " + response.code());
                        Toast.makeText(AddEventActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if (pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(AddEventActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {
        }
    }

    @OnClick(R.id.Ivback)
    public void onBack() {
        StoreData.eventList.clear();

        Log.e("compnyid", "" + CompanyId);

        Intent i_back = new Intent(AddEventActivity.this, CompanyProductListActivity.class);
        i_back.putExtra("company_id", CompanyId);
        startActivity(i_back);
        finish();
    }

    private MultipartBody.Part prepareFilePart(String partName, String filepath) {
        File file = new File(filepath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @OnClick(R.id.tvBuyTicket)
    public void BuyTicket()
    {
        Intent i_buy = new Intent(AddEventActivity.this, TicketPurchaseActivity.class);
        i_buy.putExtra("event", currentEvent);
        startActivity(i_buy);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, grantResults);
    }

    public String getRealPathFromURI(Context context, Uri uri) {
        try {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PICK_LOGO_FROM_GALLERY:

                if (resultCode == RESULT_OK)
                {
                    Uri uri = data.getData();

                    String path = getRealPathFromURI(AddEventActivity.this, uri);

                    if (path != null)
                    {
                        Intent i_crop = new Intent(AddEventActivity.this, CropImageActivity.class);
                        i_crop.putExtra("imagepath", path);
                        i_crop.putExtra("logo", true);
                        startActivityForResult(i_crop, CROP_IMAGE1);
                    }
                }
                break;
//            case CROP_LOGO_IMAGE :
//
//                break ;
            case PICK_IMAGE_FROM_GALLERY1:

                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    String path = getRealPathFromURI(AddEventActivity.this, uri);
                    if (path != null) {
                        Intent i_crop = new Intent(AddEventActivity.this, CropImageActivity.class);
                        i_crop.putExtra("imagepath", path);
                        i_crop.putExtra("logo", false);
                        startActivityForResult(i_crop, CROP_IMAGE1);
                    } else {
                        relative_image.setVisibility(View.GONE);
                    }
                }
                break;

            case CROP_IMAGE1:

                if (data != null) {
                    String s_path = data.getStringExtra("imagepath");
                    boolean s_logo = data.getBooleanExtra("logo", false);

                    if (s_logo == true)
                    {
                        if (update == true)
                        {
                            if (resultCode == RESULT_OK)
                            {
                                frame_logo_image.setVisibility(View.VISIBLE);
                                UpdateEventLogoPath = s_path;
                                if (UpdateEventLogoPath != null)
                                {
                                    Glide.with(AddEventActivity.this).load(UpdateEventLogoPath).into(img_logo);
                                }
                                else
                                {
                                    frame_logo_image.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                frame_logo_image.setVisibility(View.VISIBLE);
                                UpdateEventLogoPath = s_path;
                                if (UpdateEventLogoPath != null)
                                {
                                    Glide.with(AddEventActivity.this).load(UpdateEventLogoPath).into(img_logo);
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
                                EventLogoPath = s_path;
                                if (EventLogoPath != null)
                                {
                                    Glide.with(AddEventActivity.this).load(EventLogoPath).into(img_logo);
                                }
                                else
                                {
                                    frame_logo_image.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                frame_logo_image.setVisibility(View.VISIBLE);
                                EventLogoPath = s_path;
                                if (EventLogoPath != null)
                                {
                                    Glide.with(AddEventActivity.this).load(EventLogoPath).into(img_logo);
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
                                EventImage objimage = new EventImage();
                                objimage.setEventimage(s_path);

                                if(imglist != null )
                                {
                                    imglist.add(objimage);
                                }
                            }
                            else
                            {
                                EventImage objimage = new EventImage();
                                objimage.setEventimage(s_path);

                                imglist.add(objimage);
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

                                StoreData.eventList.add(s_path);
                            }
                            else
                            {
                                Image objimage = new Image();
                                objimage.setImagepath(s_path);

                                StoreData.eventList.add(s_path);
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
        }
    }

    public void setParam() {
        CardView.LayoutParams p1 = (CardView.LayoutParams) img_logo.getLayoutParams();
        p1.width = screenwidth * 250 / 1080;
        p1.height = screenheight * 250 / 1920;
//            p1.setMargins(0, screenheight*50/1920 , screenwidth * 20 /1080 , 0);
        img_logo.setLayoutParams(p1);

        RelativeLayout.LayoutParams pa1 = (RelativeLayout.LayoutParams) card.getLayoutParams();
        pa1.width = screenwidth * 250 / 1080;
        pa1.height = screenheight * 250 / 1920;
        pa1.setMargins(0, screenheight * 20 / 1920, screenwidth * 20 / 1080, 0);
        card.setLayoutParams(pa1);
//            img_path1.setLayoutParams(p1);

        RelativeLayout.LayoutParams p1s = (RelativeLayout.LayoutParams) img_cancel.getLayoutParams();
        p1s.width = screenwidth * 70 / 1080;
        p1s.height = screenheight * 70 / 1920;
        img_cancel.setLayoutParams(p1s);

        RelativeLayout.LayoutParams p1zs = (RelativeLayout.LayoutParams) frame_logo_image.getLayoutParams();
        p1zs.width = screenwidth * 250 / 1080;
        p1zs.height = screenheight * 250 / 1920;
        frame_logo_image.setLayoutParams(p1zs);
    }


    public void check_list_fill() {
        if (StoreData.eventList != null && StoreData.eventList.size() > 0) {
            relative_image.setVisibility(View.VISIBLE);
            rcv_add.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
            rcv_update.setVisibility(View.GONE);

            imagesAdapter = new EventImagesAdapter(AddEventActivity.this, StoreData.eventList);
            rcv_add.setAdapter(imagesAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(AddEventActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rcv_add.setLayoutManager(manager);
        } else {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_update.setVisibility(View.GONE);
            rcv_add.setVisibility(View.GONE);
        }
    }

    public void fillUpdatedImage() {
        if (imglist != null && imglist.size() > 0) {
            relative_image.setVisibility(View.VISIBLE);
            rcv_add.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            rcv_update.setVisibility(View.VISIBLE);

            updateAdapter = new UpdateEventImageAdapter(AddEventActivity.this, imglist);
            rcv_update.setAdapter(updateAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(AddEventActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rcv_update.setLayoutManager(manager);
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_update.setVisibility(View.GONE);
            rcv_add.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fab_gallery)
    public void addPhoto() {
        if (StoreData.eventList != null) {
            if (StoreData.eventList.size() >= 10) {
                Toast.makeText(AddEventActivity.this, "You can select maximum 10 images", Toast.LENGTH_SHORT).show();
            } else {
                checkPermissions();
            }
        }
    }

    public void removeImage(int position) {
        StoreData.eventList.remove(position);

        runOnUiThread(new Runnable() {
            public void run() {
                imagesAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.fab_gallery_logo)
    public void addPhoto1() {
        openGalleryForLogo();
    }

    public void openGallery() {
        Intent i_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i_gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(i_gallery, "Select Image"), PICK_IMAGE_FROM_GALLERY1);
    }

    public void openGalleryForLogo() {
        Intent i_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i_gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(i_gallery, "Select Image"), PICK_LOGO_FROM_GALLERY);
    }

    @OnClick(R.id.img_cancel)
    public void removeLogo() {
        EventLogoPath = null;
        frame_logo_image.setVisibility(View.GONE);
    }

    @OnClick(R.id.etDate)
    public void clickDate() {
        showdatepicker();
    }

    private void checkPermissions() {
        int PER_CODE = 166;

        mRequestPermissionHandler.requestPermission(this, permissions,
                PER_CODE, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        openGallery();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(AddEventActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(AddEventActivity.this)
                                .setCancelable(false)
                                .setTitle("Permission necessary")
                                .setMessage("Please grant all permissions to proceed with app.")
                                .setPositiveButton("Re-Try", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkPermissions();
                                    }
                                })
                                .setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", AddEventActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                        checkPermissions();
                                    }
                                })
                                .create().show();
                    }

                });

    }

    private void checkPermissionsForLogo() {
        int PER_CODE = 166;

        mRequestPermissionHandler.requestPermission(this, permissions,
                PER_CODE, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        openGallery();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(AddEventActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(AddEventActivity.this)
                                .setCancelable(false)
                                .setTitle("Permission necessary")
                                .setMessage("Please grant all permissions to proceed with app.")
                                .setPositiveButton("Re-Try", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkPermissions();
                                    }
                                })
                                .setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", AddEventActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                        checkPermissions();
                                    }
                                })
                                .create().show();
                    }

                });

    }

    public void showdatepicker() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.show();

    }
    public class UpdateEvent extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd3 = new ProgressDialog(AddEventActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            updateEvent();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void updateEvent()
    {
        try
        {
            String name = etEventName.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String price = etEventPrice.getText().toString().trim();
            String org_name = etOrg_Name.getText().toString().trim();
            String org_contact = etOrg_Number.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String terms = etTerms.getText().toString().trim();
            String max_ticket = et_MaxTicket.getText().toString().trim();

            RequestBody p_event_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(event_id));
            RequestBody p_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody P_date = RequestBody.create(MediaType.parse("multipart/form-data"), date);
            RequestBody P_price = RequestBody.create(MediaType.parse("multipart/form-data"), price);
            RequestBody p_org_name = RequestBody.create(MediaType.parse("multipart/form-data"), org_name);
            RequestBody P_org_contact = RequestBody.create(MediaType.parse("multipart/form-data"), org_contact);
            RequestBody P_location = RequestBody.create(MediaType.parse("multipart/form-data"), location);
            RequestBody P_terms = RequestBody.create(MediaType.parse("multipart/form-data"), terms);
            RequestBody P_max_no_ticker = RequestBody.create(MediaType.parse("multipart/form-data"), max_ticket);

            RequestBody p_CompanyId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(CompanyId));
            RequestBody p_deleteimage = null;

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
                        update_parts.add(prepareFilePart("eventimage[]", imglist.get(i).getEventimage()));
                    }
                    else
                    {

                    }
                }
            }
            MultipartBody.Part eventLogo = null;
            if(UpdateEventLogoPath != null)
            {
                eventLogo = prepareFilePart("flyerimage", UpdateEventLogoPath);
            }

            Call<Response> call = objInterface.EditEvent(eventLogo, update_parts , p_CompanyId, p_event_id , p_name ,
                    P_date ,  P_location , P_terms, p_org_name , P_org_contact, P_price , P_max_no_ticker , p_deleteimage);

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
//                            Event resultCompany = response.body().getEvent();

//                            if(resultCompany != null)
//                            {
                                if(pd3 != null && pd3.isShowing())
                                {
                                    pd3.dismiss();
                                }
                                StoreData.eventList.clear();

                                Toast.makeText(AddEventActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                                onBack();
//                            }
//                            else
//                            {
//                                if(pd3 != null && pd3.isShowing())
//                                {
//                                    pd3.dismiss();
//                                }
//                            }

                        }
                        else if(response.body().getResult() == 0)
                        {

                            List<String> er = response.body().getErrors().getImages();

                            if(pd3 != null && pd3.isShowing())
                            {
                                pd3.dismiss();
                            }
                            if(er != null && er.size() >0)
                            {
                                Toast.makeText(AddEventActivity.this , er.get(0) , Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(AddEventActivity.this , response.message() , Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            if(pd3 != null && pd3.isShowing())
                            {
                                pd3.dismiss();
                            }

                            Toast.makeText(AddEventActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();

                        }
                    }
                    else
                    {
                        if(pd3 != null && pd3.isShowing())
                        {
                            pd3.dismiss();
                        }

                        Toast.makeText(AddEventActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd3 != null && pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(AddEventActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
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

