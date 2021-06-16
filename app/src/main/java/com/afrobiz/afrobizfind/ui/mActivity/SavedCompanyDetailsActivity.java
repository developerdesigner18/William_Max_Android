package com.afrobiz.afrobizfind.ui.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CompanyOrderHistory.CompanyOrderHistoryActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Inventory.InventoryActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Till.TillActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.StoreData;
import com.afrobiz.afrobizfind.View_Image;
import com.afrobiz.afrobizfind.adapter.CompanyImagesAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Offers;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyOffersListActivity;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyProductListActivity;
import com.afrobiz.afrobizfind.ui.mActivity.MyCompanies.MyCompaniesActivity;
import com.afrobiz.afrobizfind.ui.mActivity.addCompany.AddCompanyActivity;
import com.afrobiz.afrobizfind.ui.mActivity.customerList.CustomerListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class SavedCompanyDetailsActivity extends AppCompatActivity {

    private static final String TAG = "SavedCompanyDetailsActi";

    @BindView(R.id.TvProductList)
    TextView TvProductList;
    @BindView(R.id.Ivback)
    ImageView Ivback;
    @BindView(R.id.offers)
    TextView offers;
    @BindView(R.id.tv_company_id)
    TextView tv_company_id;
    @BindView(R.id.customers)
    TextView customers;

    @BindView(R.id.tv_Edit)
    TextView tv_Edit;

    ApiInterface objInterface;

    String token = null;

    ProgressDialog pd, pd3;

    int company_id = 0;
    String company_number = null;

    Double lat, longi;

    List<Image> imglist;
    Company currentCompany;
    int offerId = 0;

    @BindView(R.id.tv_company_name)
    TextView tv_company_name;

    @BindView(R.id.tv_ethos)
    TextView tv_ethos;

    @BindView(R.id.tv_contact_number)
    TextView tv_contact_number;

    @BindView(R.id.tv_building_number)
    TextView tv_building_number;

    @BindView(R.id.tv_address1)
    TextView tv_address1;

    @BindView(R.id.tv_city)
    TextView tv_city;

    @BindView(R.id.tv_postcode)
    TextView tv_postcode;

    @BindView(R.id.tv_email)
    TextView tv_email;

    @BindView(R.id.tv_website)
    TextView tv_website;

    @BindView(R.id.tv_app_link)
    TextView tv_app_link;

    @BindView(R.id.tv_category_name)
    TextView tv_category_name;

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

    @BindView(R.id.tv_empty)
    TextView tv_empty;

    @BindView(R.id.tv_empty_logo)
    TextView tv_empty_logo;

    @BindView(R.id.tv_payment)
    TextView tv_payment;

    @BindView(R.id.tv_payment1)
    TextView tv_payment1;

    @BindView(R.id.tv_days_left)
    TextView tv_days_left;

    @BindView(R.id.layout_days_left)
    LinearLayout layout_days_left;


    @BindView(R.id.tv_next_subscription_date)
    TextView tv_next_subscription_date;

    @BindView(R.id.img_logo)
    ImageView img_logo;

    @BindView(R.id.rcv_images)
    RecyclerView rcv_images;

    DisplayMetrics metrics;
    int screenwidth, screenheight;

    @BindView(R.id.card)
    CardView card;

    @BindView(R.id.tvOrderHistory)
    TextView tvOrderHistory;
    @BindView(R.id.tvTill)
    TextView tvTill;
    @BindView(R.id.tvInventory)
    TextView tvInventory;


    CompanyImagesAdapter updateAdapter;
    String companyLogo = null;

    String currentDate = null;
    Users currentuser;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_company_details);

        metrics = getResources().getDisplayMetrics();
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;

        try {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            currentDate = df.format(c);
        } catch (Exception e) {

        }


        ButterKnife.bind(this);

        setParam();
        Bundle b = getIntent().getExtras();

        if (b != null) {
            company_id = b.getInt("company_id");
        }

        currentuser = new PrefrenceManager(SavedCompanyDetailsActivity.this).getCurrentuser();

        if (currentuser != null) {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null) {
                if (ApiClient.isNetworkAvailable(SavedCompanyDetailsActivity.this)) {
                    Log.e("company_id", "" + company_id);
                    new GetCompany(company_id).execute();
                } else {
                    Toast.makeText(SavedCompanyDetailsActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @OnClick(R.id.tvOrderHistory)
    public void onOrderHistoryClicked() {
        Intent intent = new Intent(SavedCompanyDetailsActivity.this, CompanyOrderHistoryActivity.class);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tvTill)
    public void onTillClicked() {
        Intent intent = new Intent(SavedCompanyDetailsActivity.this, TillActivity.class);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tvInventory)
    public void onInventoryClicked() {
        Intent intent = new Intent(SavedCompanyDetailsActivity.this, InventoryActivity.class);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
        finish();
    }

    public class GetCompany extends AsyncTask<Void, Void, Void> {
        int id = 0;

        public GetCompany(int company_id) {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd3 = new ProgressDialog(SavedCompanyDetailsActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getCompany(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void setParam() {
        CardView.LayoutParams p1 = (CardView.LayoutParams) img_logo.getLayoutParams();
        p1.width = screenwidth * 250 / 1080;
        p1.height = screenheight * 250 / 1920;
        img_logo.setLayoutParams(p1);

        RelativeLayout.LayoutParams pa1 = (RelativeLayout.LayoutParams) card.getLayoutParams();
        pa1.width = screenwidth * 250 / 1080;
        pa1.height = screenheight * 250 / 1920;
        pa1.setMargins(0, screenheight * 20 / 1920, screenwidth * 20 / 1080, 0);
        card.setLayoutParams(pa1);
    }

    @OnClick(R.id.img_logo)
    public void onClickImage()
    {
        if (companyLogo != null)
        {
            Intent i_image = new Intent(SavedCompanyDetailsActivity.this, View_Image.class);
            i_image.putExtra("path", companyLogo);
            startActivity(i_image);
        }
    }

    public void getDiffrenceBetweenDate(String currentDate, String expiryDate) {
        try {
//             currentDate= "2021-10-12";
//            String FinalDate= "09/26/2019";
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

            date1 = dates.parse(currentDate);
            date2 = dates.parse(expiryDate);

            if (date1.getTime() >= date2.getTime()) {
//                long difference = Math.abs(date1.getTime() - date2.getTime());
//                long differenceDates = difference / (24 * 60 * 60 * 1000);
//                String dayDifference = Long.toString(differenceDates);
                tv_days_left.setText("......");
            } else {
                long difference = Math.abs(date1.getTime() - date2.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);
                String dayDifference = Long.toString(differenceDates);
                tv_days_left.setText(dayDifference + " Days left to expire current subscription.");
            }
        } catch (Exception exception) {
            Toast.makeText(this, "Unable to find difference", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_email)
    public void openEmail() {
        if (tv_email.getText().toString().trim() != null) {
//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
//            intent.putExtra(Intent.EXTRA_EMAIL, tv_company_email.getText().toString().trim());
//            intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            }

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + tv_email.getText().toString().trim()));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "body");
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

            startActivity(Intent.createChooser(emailIntent, "Email"));
        }
    }

    public void getCompany(int id) {
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

                        company_id = currentCompany.getId();

                        company_number = currentCompany.getCompany_number();

                        tv_company_id.setText(company_number);

                        tv_company_name.setText(currentCompany.getCompanyName());
//                        tv_ethos.setText(currentCompany.getCompany_ethos());

                        tv_contact_number.setText(currentCompany.getTelephone());

                        tv_website.setText(currentCompany.getWebsite());

                        if (currentCompany.getPaypal_nonce() != null) {
                            if (currentCompany.getPaypal_nonce().equals("")) {
                                tv_payment.setText("Remaining Payment");
                                tv_payment1.setVisibility(View.VISIBLE);
                            } else {
                                tv_payment.setText("Payment done");
                                tv_payment1.setVisibility(View.GONE);
                            }
                        } else {
                            tv_payment.setText("Remaining Payment");
                            tv_payment1.setVisibility(View.VISIBLE);
                        }
                        if (currentCompany.getExpiry_date() == null) {
                            tv_next_subscription_date.setText("Not Available");
                            layout_days_left.setVisibility(View.GONE);
                        } else {
                            tv_next_subscription_date.setText(currentCompany.getExpiry_date());
                            layout_days_left.setVisibility(View.VISIBLE);
                        }

                        if (currentDate != null && currentCompany.getExpiry_date() != null) {
                            getDiffrenceBetweenDate(currentDate, currentCompany.getExpiry_date());
                        } else {

                        }


                        if (currentCompany.getApplink() != null) {
                            tv_app_link.setText(currentCompany.getApplink());
                        } else {
                            tv_app_link.setText("Not Available");
                        }
                        if (currentCompany.getCompany_ethos() != null) {
                            tv_ethos.setText(currentCompany.getCompany_ethos());
                        } else {
                            tv_ethos.setText("Not Available");
                        }
                        tv_email.setText(currentCompany.getEmail());

                        tv_building_number.setText(currentCompany.getBuilding_number());
                        tv_address1.setText(currentCompany.getAddress_line_1());
                        tv_city.setText(currentCompany.getCity());
                        tv_postcode.setText(currentCompany.getPostcode());

                        if (StoreData.categoryList != null && StoreData.categoryList.size() > 0) {
                            for (int i = 0; i < StoreData.categoryList.size(); i++) {
                                if (StoreData.categoryList.get(i).getId() == currentCompany.getCategory_id()) {
                                    String catname = StoreData.categoryList.get(i).getName();
                                    tv_category_name.setText(catname);
                                }
                            }
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

                        companyLogo = currentCompany.getCompany_image();

                        if (companyLogo != null) {
                            tv_empty_logo.setVisibility(View.GONE);
                            card.setVisibility(View.VISIBLE);

                            Glide.with(SavedCompanyDetailsActivity.this).load(companyLogo).into(img_logo);
                        } else {
                            tv_empty_logo.setVisibility(View.VISIBLE);
                            card.setVisibility(View.GONE);
                        }

                        imglist = currentCompany.getImages();

                        if (imglist != null && imglist.size() > 0) {
                            tv_empty.setVisibility(View.GONE);
                            rcv_images.setVisibility(View.VISIBLE);
                            fillUpdatedImage();
                        } else {
                            tv_empty.setVisibility(View.VISIBLE);
                            rcv_images.setVisibility(View.GONE);
                        }

                        if (currentCompany != null) {
                            if (pd3 != null & pd3.isShowing()) {
                                pd3.dismiss();
                            }
                        }
                    } else if (response.body().getResult() == 0) {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(SavedCompanyDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(SavedCompanyDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd3 != null & pd3.isShowing()) {
                        pd3.dismiss();
                    }
                    Toast.makeText(SavedCompanyDetailsActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd3 != null & pd3.isShowing()) {
                    pd3.dismiss();
                }
                Log.e(TAG, "onFailure call :- " + call);
                Log.e(TAG, "onFailure msg :- " + t.getMessage());
                Toast.makeText(SavedCompanyDetailsActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fillUpdatedImage() {
        if (imglist != null && imglist.size() > 0) {
            updateAdapter = new CompanyImagesAdapter(SavedCompanyDetailsActivity.this, imglist);
            rcv_images.setAdapter(updateAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(SavedCompanyDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rcv_images.setLayoutManager(manager);
            updateAdapter.notifyDataSetChanged();
        } else {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_images.setVisibility(View.GONE);
        }
    }

    public class GetOffers extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SavedCompanyDetailsActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getOffersFromId();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getOffersFromId() {
        Offers objOffers = new Offers();
        objOffers.setId(offerId);
        Call<Response> call = objInterface.GetOffers(objOffers);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body().getResult() == 1) {
                        Offers resultOffers = new Offers();
                        if (resultOffers != null) {
                            if (pd != null & pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    } else if (response.body().getResult() == 0) {
                        if (pd != null & pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(SavedCompanyDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd != null & pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(SavedCompanyDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd != null & pd.isShowing()) {
                        pd.dismiss();
                    }
                    Toast.makeText(SavedCompanyDetailsActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(SavedCompanyDetailsActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.TvProductList)
    public void onProductClicked() {
        Intent intent = new Intent(SavedCompanyDetailsActivity.this, CompanyProductListActivity.class);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tv_Edit)
    public void onEditClicked() {

        Intent intent = new Intent(SavedCompanyDetailsActivity.this, AddCompanyActivity.class);
        intent.putExtra("update", true);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.Ivback, R.id.offers, R.id.customers})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Ivback:

                Intent intent2s = new Intent(SavedCompanyDetailsActivity.this, MyCompaniesActivity.class);
                intent2s.putExtra("company_id", currentCompany.getId());
                startActivity(intent2s);
                finish();
//                onBackPressed();
                break;
            case R.id.offers:
                Intent intent2 = new Intent(SavedCompanyDetailsActivity.this, CompanyOffersListActivity.class);
                intent2.putExtra("company_id", currentCompany.getId());
                startActivity(intent2);
                finish();
                break;
            case R.id.customers:
                Intent intent = new Intent(SavedCompanyDetailsActivity.this, CustomerListActivity.class);
                intent.putExtra("company_id", currentCompany.getId());
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2 = new Intent(SavedCompanyDetailsActivity.this, MyCompaniesActivity.class);
        intent2.putExtra("company_id", currentCompany.getId());
        startActivity(intent2);
        finish();
    }
}
