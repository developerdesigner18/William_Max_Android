package com.afrobiz.afrobizfind.ui.mActivity.Company_productlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.AddOffersActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Till.MySpinner;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Offers;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.widgets.SwipeHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class CompanyOffersListActivity extends AppCompatActivity {
    private static final String TAG = "CompanyOffersListActivi";
    @BindView(R.id.Ivback)
    ImageView Ivback;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    @BindView(R.id.img_search)
    ImageView img_search;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.Rv_OffersList)
    RecyclerView Rv_OffersList;

    @BindView(R.id.TvAddOffers)
    TextView TvAddOffers;

    @BindView(R.id.tv_total)
    TextView tv_total;

    private MySpinner<String> PartSpinner;
    List<String> PartName = new ArrayList<>();
    List<Offers> offerList = new ArrayList<>();
    List<Offers> Offers = new ArrayList<>();
    ImageView img_scan;
    //    CompanyOfferList model;
    Users currentuser;

    ProgressDialog pd3;

    int company_id = 0;


    CompanyOffersListAdapter adapter;

    LinearLayoutManager layoutManager;
    ConstraintLayout constraintLayout;
    String token = null;
    ApiInterface objInterface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offers_list);

        constraintLayout = findViewById(R.id.clayout);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            company_id = b.getInt("company_id");
        }
        PartSpinner = findViewById(R.id.spinnerPart);
        currentuser = new PrefrenceManager(CompanyOffersListActivity.this).getCurrentuser();

        if (currentuser != null) {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null) {
                if (ApiClient.isNetworkAvailable(CompanyOffersListActivity.this)) {
                    Log.e("company_id", "" + company_id);
                    new GetCompanyOffersList(company_id).execute();
                } else {
                    Toast.makeText(CompanyOffersListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void filter(String text) {
        if (offerList != null && offerList.size() > 0) {
            List<Offers> filterdNames = new ArrayList<>();

            for (Offers s : offerList) {
                Log.e(TAG, "filter s.getName() :- " + s.getName().toLowerCase() + " s.getOffer_number():- " + s.getOffer_number().toLowerCase());
                if (s.getName().toLowerCase().contains(text.toLowerCase()) ||
                        s.getOffer_number().toLowerCase().contains(text.toLowerCase())) {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0) {
                tv_empty.setVisibility(View.GONE);
                Rv_OffersList.setVisibility(View.VISIBLE);
                tv_total.setText("" + filterdNames.size());
                adapter.filterList(filterdNames);
            } else {
                tv_total.setText("0");
                tv_empty.setVisibility(View.VISIBLE);
                Rv_OffersList.setVisibility(View.GONE);
            }
        }
    }
    private void runLayoutAnimation(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public class GetCompanyOffersList extends AsyncTask<Void, Void, Void> {
        int id = 0;

        public GetCompanyOffersList(int company_id) {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd3 = new ProgressDialog(CompanyOffersListActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getOffers(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getOffers(int id) {
        Log.e("company_id", "" + company_id);
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response> call = objInterface.GetAllOffers(objCompany);

        Log.e("callGet", "" + call.request());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {
                        offerList = response.body().getOffers();

                        if (offerList != null && offerList.size() > 0) {
                            Log.e("offerslist", "" + offerList.size());
                            tv_empty.setVisibility(View.GONE);
                            Rv_OffersList.setVisibility(View.VISIBLE);

                            tv_total.setText("" + offerList.size());

                            layoutManager = new LinearLayoutManager(CompanyOffersListActivity.this);
                            adapter = new CompanyOffersListAdapter(CompanyOffersListActivity.this, offerList);
                            Rv_OffersList.setLayoutManager(layoutManager);
                            Rv_OffersList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            runLayoutAnimation(Rv_OffersList);

                            Drawable delete = getResources().getDrawable(android.R.drawable.ic_menu_delete, null);

                            SwipeHelper swipeHelper = new SwipeHelper(CompanyOffersListActivity.this) {
                                @Override
                                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder,
                                                                      List<UnderlayButton> underlayButtons) {
                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                                            "Delete",
                                            0,
                                            Color.parseColor("#FF3C30"),

                                            new SwipeHelper.UnderlayButtonClickListener() {
                                                @Override
                                                public void onClick(final int pos) {
                                                    if (ApiClient.isNetworkAvailable(CompanyOffersListActivity.this)) {
                                                        if (objInterface != null) {
                                                            int offerId = offerList.get(pos).getId();
                                                            new DeleteOffers(offerId, pos).execute();
                                                        }
                                                    } else {
                                                        Toast.makeText(CompanyOffersListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    ));
                                }
                            };
                            swipeHelper.attachToRecyclerView(Rv_OffersList);
                        } else {
                            tv_total.setText("0");
                            tv_empty.setVisibility(View.VISIBLE);
                            Rv_OffersList.setVisibility(View.GONE);
                        }
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                    } else if (response.body().getResult() == 0) {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(CompanyOffersListActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (pd3 != null & pd3.isShowing()) {
                            pd3.dismiss();
                        }
                        Toast.makeText(CompanyOffersListActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd3 != null & pd3.isShowing()) {
                        pd3.dismiss();
                    }
                    Toast.makeText(CompanyOffersListActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd3 != null & pd3.isShowing()) {
                    pd3.dismiss();
                }
                Toast.makeText(CompanyOffersListActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class DeleteOffers extends AsyncTask<Void, Void, Void> {
        int id = 0, posi = 0;

        public DeleteOffers(int offerId, int pos) {
            this.id = offerId;
            this.posi = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd3 = new ProgressDialog(CompanyOffersListActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DeleteOffers(id, posi);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void DeleteOffers(int id, int pos) {
        Offers objOfferrs = new Offers();
        objOfferrs.setId(id);

        Call<Response> call = objInterface.DeleteOffers(objOfferrs);

        Log.e("callGet", "" + call.request());

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.e("getcode", "" + response.code());

                if (response != null && response.isSuccessful()) {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse", "" + response.body().getResult());

                    if (response.body().getResult() == 1) {

                        if (pd3 != null && pd3.isShowing()) {
                            pd3.dismiss();
                        }

                        offerList.remove(pos);
                        Toast.makeText(CompanyOffersListActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } else if (response.body().getResult() == 0) {
                        if (pd3 != null && pd3.isShowing()) {
                            pd3.dismiss();
                        }

                        Toast.makeText(CompanyOffersListActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (pd3 != null && pd3.isShowing()) {
                        pd3.dismiss();
                    }

                    Toast.makeText(CompanyOffersListActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd3 != null && pd3.isShowing()) {
                    pd3.dismiss();
                }
                Toast.makeText(CompanyOffersListActivity.this, t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @OnClick(R.id.Ivback)
    public void onViewClicked() {
        Intent i_back = new Intent(CompanyOffersListActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i_back = new Intent(CompanyOffersListActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }

    @OnClick(R.id.TvAddOffers)
    public void onView1Clicked() {
        Intent intent = new Intent(CompanyOffersListActivity.this, AddOffersActivity.class);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
        finish();
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
        if(alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.Dialog_Offers_Animation);
        }

        Ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

        Offers objOffers = offerList.get(position);
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
}

//public class GetCompanyOffersList extends AsyncTask<Void, Void, Void> {
//    int id = 0;
//
//    public GetCompanyOffersList(int company_id) {
//        this.id = company_id;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//        pd3 = new ProgressDialog(CompanyOffersListActivity.this);
//        pd3.setCancelable(false);
//        pd3.setMessage(getString(R.string.please_wait));
//        pd3.show();
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//        getOffers(id);
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//    }
//}
//
//    public void getOffers(int id) {
//        Log.e("company_id", "" + company_id);
//        Company objCompany = new Company();
//        objCompany.setId(id);
//        Call<Response> call = objInterface.GetAllOffers(objCompany);
//
//        Log.e("callGet", "" + call.request());
//        call.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                Log.e("getcode", "" + response.code());
//
//                if (response != null && response.isSuccessful()) {
//                    Log.e("getresponse beauty", new Gson().toJson(response.body()));
//
//                    Log.e("getresponse", "" + response.body().getResult());
//
//                    if (response.body().getResult() == 1) {
//                        offerList = response.body().getOffers();
//
//                        if (offerList != null && offerList.size() > 0) {
//                            Log.e("offerslist", "" + offerList.size());
//                            tv_empty.setVisibility(View.GONE);
//                            Rv_OffersList.setVisibility(View.VISIBLE);
//
//                            tv_total.setText("" + offerList.size());
//
//                            layoutManager = new LinearLayoutManager(CompanyOffersListActivity.this);
//                            adapter = new CompanyOffersListAdapter(CompanyOffersListActivity.this, offerList);
//                            Rv_OffersList.setLayoutManager(layoutManager);
//                            Rv_OffersList.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//                            Drawable delete = getResources().getDrawable(android.R.drawable.ic_menu_delete, null);
//
//                            SwipeHelper swipeHelper = new SwipeHelper(CompanyOffersListActivity.this) {
//                                @Override
//                                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder,
//                                                                      List<UnderlayButton> underlayButtons) {
//                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                            "Delete",
//                                            0,
//                                            Color.parseColor("#FF3C30"),
//
//                                            new SwipeHelper.UnderlayButtonClickListener() {
//                                                @Override
//                                                public void onClick(final int pos) {
//                                                    if (ApiClient.isNetworkAvailable(CompanyOffersListActivity.this)) {
//                                                        if (objInterface != null) {
//                                                            int offerId = offerList.get(pos).getId();
//                                                            new DeleteOffers(offerId, pos).execute();
//                                                        }
//                                                    } else {
//                                                        Toast.makeText(CompanyOffersListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            }
//                                    ));
//                                }
//                            };
//                            swipeHelper.attachToRecyclerView(Rv_OffersList);
//                        } else {
//                            tv_total.setText("0");
//                            tv_empty.setVisibility(View.VISIBLE);
//                            Rv_OffersList.setVisibility(View.GONE);
//                        }
//                        if (pd3 != null & pd3.isShowing()) {
//                            pd3.dismiss();
//                        }
//                    } else if (response.body().getResult() == 0) {
//                        if (pd3 != null & pd3.isShowing()) {
//                            pd3.dismiss();
//                        }
//                        Toast.makeText(CompanyOffersListActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        if (pd3 != null & pd3.isShowing()) {
//                            pd3.dismiss();
//                        }
//                        Toast.makeText(CompanyOffersListActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    if (pd3 != null & pd3.isShowing()) {
//                        pd3.dismiss();
//                    }
//                    Toast.makeText(CompanyOffersListActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//                if (pd3 != null & pd3.isShowing()) {
//                    pd3.dismiss();
//                }
//                Toast.makeText(CompanyOffersListActivity.this, t.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
