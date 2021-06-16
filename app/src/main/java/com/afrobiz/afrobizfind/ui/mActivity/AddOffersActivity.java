package com.afrobiz.afrobizfind.ui.mActivity;

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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.RequestPermissionHandler;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Offers;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyOffersListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class AddOffersActivity extends AppCompatActivity
{
    public static final int PICK_IMAGE_FROM_GALLERY = 21;

    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.checkbox)
    CheckBox  checkbox;

    @BindView(R.id.etOfferDesc)
    EditText etOfferDesc;

    @BindView(R.id.etOfferPrice)
    EditText etOfferPrice;

    @BindView(R.id.etOfferCode)
    EditText etOfferCode;

    @BindView(R.id.etOfferDiscount)
    EditText etOfferDiscount;

    @BindView(R.id.etOfferName)
    EditText etOfferName;

    @BindView(R.id.etStartDate)
    EditText etStartDate;

    @BindView(R.id.etEndDate)
    EditText etEndDate;

    @BindView(R.id.save_offer)
    TextView save_offer;

    @BindView(R.id.fab_gallery)
    FloatingActionButton fab_gallery;

    @BindView(R.id.img_gallery)
    ImageView img_gallery;

    @BindView(R.id.relative_image)
    RelativeLayout relative_image;

    @BindView(R.id.rcv_update)
    RecyclerView rcv_update;

    @BindView(R.id.rcv_add)
    RecyclerView rcv_add;

    @BindView(R.id.tv_empty)
    TextView tv_empty;

    Cursor cursor;
    ProgressDialog pd1, pd2;

    @BindView(R.id.frame_offer_image)
    FrameLayout frame_offer_image;

    private RequestPermissionHandler mRequestPermissionHandler;
    String[] permissions =
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };

    TextView tv_toolbar;

    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;

    Users currentuser;
    String token = null;
    ApiInterface objInterface;

    int company_id= 0;
    int offer_id = 0;
    boolean update = false;
    String startdate, enddate,discount,offername,desc,offercode,price,mobile;

    Offers currentOffer = null;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_offers);
        mRequestPermissionHandler = new RequestPermissionHandler();

        ButterKnife.bind(this);

        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);

        etStartDate.setInputType(InputType.TYPE_NULL);
        etEndDate.setInputType(InputType.TYPE_NULL);

        currentuser = new PrefrenceManager(AddOffersActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token" , ""+token);
            objInterface  = ApiClient.create_InstanceAuth(token);
        }

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            company_id = b.getInt("company_id");

            currentOffer = (Offers) b.getSerializable("offers");

            if(currentOffer != null)
            {
                company_id = currentOffer.getCompanyId();
            }
            update = b.getBoolean("update");
            tv_toolbar.setText("Update offer");
        }
        else
        {
            tv_toolbar.setText("Add Offer");
        }
        if(update == true)
        {
            tv_toolbar.setText("Update Offer");

            if(currentOffer == null)
            {
                if(objInterface != null)
                {
                    if (ApiClient.isNetworkAvailable(AddOffersActivity.this))
                    {
                        Log.e("offer_id",""+offer_id);
//                        new GetOffer(offer_id).execute();
                    }
                    else
                    {
                        Toast.makeText(AddOffersActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                etOfferDiscount.setText(currentOffer.getDiscount());
                etOfferDesc.setText(currentOffer.getOfferDetails());
                etOfferCode.setText(currentOffer.getOfferCode());
                etStartDate.setText(currentOffer.getStartDate());
                etEndDate.setText(currentOffer.getEndDate());
            }
        }
        else
        {
            tv_toolbar.setText("Add Offer");
        }


        etStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showdatepicker(etStartDate);
                fromDatePickerDialog.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showdatepicker(etEndDate);
                fromDatePickerDialog.show();
            }
        });


    }
    public void showdatepicker(EditText v)
    {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                v.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @OnClick(R.id.Ivback)
    public void onBack()
    {
        Intent i_back= new Intent(AddOffersActivity.this, CompanyOffersListActivity.class);
        i_back.putExtra("company_id",company_id);
        startActivity(i_back);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i_back= new Intent(AddOffersActivity.this, CompanyOffersListActivity.class);
        i_back.putExtra("company_id",company_id);
        startActivity(i_back);
        finish();
    }

    @OnClick(R.id.fab_gallery)
    public void addPhoto()
    {
        checkPermissions();
    }

    @OnClick(R.id.save_offer)
    public void SaveOffer()
    {
        if (etEndDate.getText().toString().equals(""))
        {
            etEndDate.setError("Please Enter End Date of offer.");
            return;
        }
        else
        {
            etEndDate.setError(null);
        }
        if (etEndDate.getText().toString().equals(""))
        {
            etEndDate.setError("Please Enter Start Date of offer.");
            return;
        }
        else
        {
            etEndDate.setError(null);
        }

        if (etOfferName.getText().toString().equals(""))
        {
            etOfferName.setError("Please enter offer name.");
            return;
        }
        else
        {
            etOfferName.setError(null);
        }

        if (etOfferCode.getText().toString().equals(""))
        {
            etOfferCode.setError("Please enter offer code.");
        return;
        }
        else
        {
            etOfferCode.setError(null);
        }
        if (etOfferDesc.getText().toString().equals(""))
        {
            etOfferDesc.setError("Please enter offer description.");
            return;
        }
        else
        {
            etOfferDesc.setError(null);
        }
//        if (etOfferPrice.getText().toString().equals(""))
//        {
//            etOfferPrice.setError("Please enter offer price.");
//            return;
//        }
//        else
//        {
//            etOfferPrice.setError(null);
//        }
        if (etOfferDiscount.getText().toString().equals(""))
        {
            etOfferDiscount.setError("Please enter offer discount.");
            return;
        }
        else
        {
            etOfferDiscount.setError(null);
        }

//        if(checkbox.isChecked() == true)
//        {
//            checkbox.setError("Please check the offer checkbox.");
//        }
//        else
//        {
//            checkbox.setError(null);
//            return;
//        }
        discount = etOfferDiscount.getText().toString().trim();
        desc = etOfferDesc.getText().toString().trim();
        offercode = etOfferCode.getText().toString().trim();
        offername = etOfferName.getText().toString().trim();
        startdate = etStartDate.getText().toString().trim();
        enddate = etEndDate.getText().toString().trim();


        if(update == true)
        {
            if(ApiClient.isNetworkAvailable(AddOffersActivity.this))
            {
                if(objInterface != null)
                {
                    Log.e("offer","update");
                    new UpdateOffers().execute();
                }
            }
            else
            {
                Toast.makeText(AddOffersActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(ApiClient.isNetworkAvailable(AddOffersActivity.this))
            {
                if(objInterface != null)
                {
                    Log.e("offer","add");
                    new AddOffers().execute();
                }

            }
            else
            {
                Toast.makeText(AddOffersActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openGallery()
    {
        Intent i_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i_gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(i_gallery, "Select Image"),PICK_IMAGE_FROM_GALLERY);
    }
    private void checkPermissions()
    {
        int PER_CODE = 343;

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
                        Toast.makeText(AddOffersActivity.this, "Request permission failed", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(AddOffersActivity.this)
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
                                        Uri uri = Uri.fromParts("package", AddOffersActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                        checkPermissions();
                                    }
                                })
                                .create().show();
                    }

                });

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

    public class AddOffers extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd1 = new ProgressDialog(AddOffersActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            addOffers();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void addOffers()
    {
        Offers objOfferrs = new Offers();
        objOfferrs.setOfferDetails(desc);
        objOfferrs.setName(offername);
        objOfferrs.setDiscount(discount);
        objOfferrs.setOfferCode(offercode);
        objOfferrs.setStartDate(startdate);
        objOfferrs.setEndDate(enddate);
        objOfferrs.setActive(1);
        objOfferrs.setCompanyId(company_id);

        Call<Response> call = objInterface.AddOffers(objOfferrs);

        Log.e("calloffer",""+call.request());

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
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }

                        Offers resultOffer = response.body().getOffer();
                        if(resultOffer != null)
                        {
                            Toast.makeText(AddOffersActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                      onBack();
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        if(response.body().getErrors() != null)
                        {

                        }

                        Toast.makeText(AddOffersActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }

                        Toast.makeText(AddOffersActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    if(pd1 != null && pd1.isShowing())
                    {
                        pd1.dismiss();
                    }

                    Toast.makeText(AddOffersActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd1 != null && pd1.isShowing())
                {
                    pd1.dismiss();
                }
                Toast.makeText(AddOffersActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });
    }

    public class UpdateOffers extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd2 = new ProgressDialog(AddOffersActivity.this);
            pd2.setCancelable(false);
            pd2.setMessage(getString(R.string.please_wait));
            pd2.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            UpdateOffers();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void UpdateOffers()
    {
        Offers objOfferrs = new Offers();
        objOfferrs.setOfferDetails(desc);
        objOfferrs.setDiscount(discount);
        objOfferrs.setOfferCode(offercode);
        objOfferrs.setStartDate(startdate);
        objOfferrs.setEndDate(enddate);
//        objOfferrs.setMobileNumber(mobile);
        objOfferrs.setCompanyId(company_id);

        Call<Response> call = objInterface.UpdateOffers(objOfferrs);

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
                        if(pd2 != null && pd2.isShowing())
                        {
                            pd2.dismiss();
                        }

//                        Offers resultOffer = response.body().getOffer();
//                        if(resultOffer != null)
//                        {
                            Toast.makeText(AddOffersActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
//                        }

                        onBack();
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd2 != null && pd2.isShowing())
                        {
                            pd2.dismiss();
                        }

                        Toast.makeText(AddOffersActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd2 != null && pd2.isShowing())
                        {
                            pd2.dismiss();
                        }

                        Toast.makeText(AddOffersActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    if(pd2 != null && pd2.isShowing())
                    {
                        pd2.dismiss();
                    }

                    Toast.makeText(AddOffersActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd2 != null && pd2.isShowing())
                {
                    pd2.dismiss();
                }
                Toast.makeText(AddOffersActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

            }
        });
    }


}
