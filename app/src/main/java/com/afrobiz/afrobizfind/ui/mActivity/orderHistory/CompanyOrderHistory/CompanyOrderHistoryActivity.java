package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CompanyOrderHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.adapter.OrderStatusAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.IncomeByDayActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.IncomeByProductActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.ProductOrderAdapter;
import com.afrobiz.afrobizfind.ui.modal.OrderHistory;
import com.afrobiz.afrobizfind.ui.modal.OrderStatus;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;


public class CompanyOrderHistoryActivity extends AppCompatActivity {
    EditText et_search;
    ImageView Ivback;
    TextView tvIncomeday, tvIncomeProduct, tvOrderHistory, tv_empty;
    RecyclerView rcv_orders;
    List<Product> list = new ArrayList<>();
    List<OrderStatus> listOrderStatus = new ArrayList<>();
    List<OrderHistory> listOrderHistory = new ArrayList<>();
    ApiInterface objInterface;
    String token = null;
    Users currentuser;
    ProgressDialog pd;
    CompanyOrderHistoryAdapter orderHistoryAdapter;
    int orderStatusID = 0, company_id = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_order_history);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            company_id = b.getInt("company_id");
            Log.e("com", "" + company_id);
        }


        currentuser = new PrefrenceManager(CompanyOrderHistoryActivity.this).getCurrentuser();

        if (currentuser != null) {
            token = currentuser.getToken();
            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);
            LoadAllStatus();
            if (ApiClient.isNetworkAvailable(CompanyOrderHistoryActivity.this)) {
                new loadAllOrderHistory().execute();
            } else {
                Toast.makeText(CompanyOrderHistoryActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }

        }

        Ivback = (ImageView) findViewById(R.id.Ivback);

        et_search = (EditText) findViewById(R.id.et_search);

        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_orders = (RecyclerView) findViewById(R.id.rcv_orders);

        tvIncomeday = (TextView) findViewById(R.id.tvIncomeday);
        tvIncomeProduct = (TextView) findViewById(R.id.tvIncomeProduct);
        tvOrderHistory = (TextView) findViewById(R.id.tvOrderHistory);

        orderHistoryAdapter = new CompanyOrderHistoryAdapter(CompanyOrderHistoryActivity.this, listOrderHistory);
        LinearLayoutManager manager = new LinearLayoutManager(CompanyOrderHistoryActivity.this);
        rcv_orders.setLayoutManager(manager);
        rcv_orders.setAdapter(orderHistoryAdapter);
        runLayoutAnimation(rcv_orders);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(String.valueOf(s).toLowerCase());
            }
        });

        tvOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i_back = new Intent(CompanyOrderHistoryActivity.this, IncomeByProductActivity.class);
//                startActivity(i_back);
//                finish();
            }
        });
        tvIncomeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_back = new Intent(CompanyOrderHistoryActivity.this, IncomeByProductActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
//                finish();
            }
        });
        tvIncomeday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_back = new Intent(CompanyOrderHistoryActivity.this, IncomeByDayActivity.class);
                i_back.putExtra("company_id", company_id);
                startActivity(i_back);
//                finish();
            }
        });
        Ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_back = new Intent(CompanyOrderHistoryActivity.this, SavedCompanyDetailsActivity.class);
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
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_left);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public void filter(String text) {
        List<OrderHistory> temp = new ArrayList();
        for (OrderHistory d : listOrderHistory) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if ((d.getId() != 0 && String.valueOf(d.getId()).contains(text)) ||
                    (d.getCreatedAt() != null && d.getCreatedAt().toLowerCase().contains(text)) ||
                    (d.getQuantity() != 0 && String.valueOf(d.getQuantity()).contains(text)) ||
                    (d.getUserid() != 0 && String.valueOf(d.getUserid()).contains(text)) ||
                    (d.getTotalprice() != 0 && String.valueOf(d.getTotalprice()).toLowerCase().contains(text)) ||
                    (d.getProductName() != null && d.getProductName().toLowerCase().contains(text)) ||
                    (d.getUsername() != null && d.getUsername().toLowerCase().contains(text)) ||
                    (d.getStatus() != null && d.getStatus().toLowerCase().contains(text)) ||
                    (d.getPrice() != null && String.valueOf(d.getPrice()).contains(text)) ||
                    (d.getQuantity() != 0 && String.valueOf(d.getQuantity()).contains(text))
            ) {
                Log.e("filter", "" + new Gson().toJson(d));
                temp.add(d);
            }
        }
        //update recyclerview
        orderHistoryAdapter.filterList(temp);
    }

    public class loadAllOrderHistory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CompanyOrderHistoryActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AllOrderHistories();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void AllOrderHistories() {
        Call<Response> call = objInterface.getAllOrdersOfCompany(company_id);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response != null && response.isSuccessful()) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }

                    if (response.body() != null && response.body().getResult() == 1) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        listOrderHistory = response.body().getCompanyorderList();
                        if (listOrderHistory != null && listOrderHistory.size() > 0) {
                            rcv_orders.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);

                            orderHistoryAdapter = new CompanyOrderHistoryAdapter(CompanyOrderHistoryActivity.this, listOrderHistory);
                            LinearLayoutManager manager = new LinearLayoutManager(CompanyOrderHistoryActivity.this);
                            rcv_orders.setLayoutManager(manager);
                            rcv_orders.setAdapter(orderHistoryAdapter);
                            orderHistoryAdapter.notifyDataSetChanged();

                            runLayoutAnimation(rcv_orders);
                        } else {
                            tv_empty.setVisibility(View.VISIBLE);
                            rcv_orders.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                    Toast.makeText(CompanyOrderHistoryActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(CompanyOrderHistoryActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoadAllStatus() {
        if (ApiClient.isNetworkAvailable(CompanyOrderHistoryActivity.this)) {
            getAllOrderStatus();
        } else {
            Toast.makeText(CompanyOrderHistoryActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllOrderStatus() {
        Call<Response> call = objInterface.GetOrderStatusList();
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 1) {
                            if (response.body().getOrderStatusList() != null && response.body().getOrderStatusList().size() > 0) {
                                listOrderStatus = response.body().getOrderStatusList();

                            }
                        }
                    }
                } else {
                    Toast.makeText(CompanyOrderHistoryActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(CompanyOrderHistoryActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowOrderStatus(String objOrder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_order_status, null);
        RecyclerView rcv_order_status = (RecyclerView) dialogLayout.findViewById(R.id.rcv_order_status);

        if (listOrderStatus != null && listOrderStatus.size() > 0) {
            for (int i = 0; i < listOrderStatus.size(); i++) {
                if (listOrderStatus.get(i).getStatus().equals(objOrder)) {
                    orderStatusID = listOrderStatus.get(i).getId();
                    Log.e("orderSID", "" + orderStatusID);
////                    return;
                }
            }
            OrderStatusAdapter statusAdapter = new OrderStatusAdapter(CompanyOrderHistoryActivity.this, orderStatusID, listOrderStatus);
            rcv_order_status.setAdapter(statusAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(CompanyOrderHistoryActivity.this);
            rcv_order_status.setLayoutManager(manager);

            runLayoutAnimation(rcv_order_status);
        }
        final ImageView img_cancel = dialogLayout.findViewById(R.id.img_cancel);
//        final LinearLayout layout_ordered = dialogLayout.findViewById(R.id.layout_ordered);
//        final LinearLayout layout_ready = dialogLayout.findViewById(R.id.layout_ready);
//        final LinearLayout layout_collected = dialogLayout.findViewById(R.id.layout_collected);
//        final LinearLayout layout_refunded = dialogLayout.findViewById(R.id.layout_refunded);
//        final ImageView img_ordered = dialogLayout.findViewById(R.id.img_ordered);
//        final ImageView img_ready = dialogLayout.findViewById(R.id.img_ready);
//        final ImageView img_collected = dialogLayout.findViewById(R.id.img_collected);
//        final ImageView img_refunded = dialogLayout.findViewById(R.id.img_refunded);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        if(alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.SlidingDialogAnimation);
        }
//        img_ordered.setImageResource(R.drawable.check);
//
//        layout_ordered.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                img_ordered.setImageResource(R.drawable.check);
//                img_ready.setImageResource(R.drawable.uncheck);
//                img_collected.setImageResource(R.drawable.uncheck);
//                img_refunded.setImageResource(R.drawable.uncheck);
//                alert.cancel();
//            }
//        });
//        layout_collected.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                img_ordered.setImageResource(R.drawable.uncheck);
//                img_ready.setImageResource(R.drawable.uncheck);
//                img_collected.setImageResource(R.drawable.check);
//                img_refunded.setImageResource(R.drawable.uncheck);
//                alert.cancel();
//            }
//        });
//        layout_ready.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                img_ordered.setImageResource(R.drawable.uncheck);
//                img_ready.setImageResource(R.drawable.check);
//                img_collected.setImageResource(R.drawable.uncheck);
//                img_refunded.setImageResource(R.drawable.uncheck);
//                alert.cancel();
//            }
//        });
//        layout_refunded.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                img_ordered.setImageResource(R.drawable.uncheck);
//                img_ready.setImageResource(R.drawable.uncheck);
//                img_collected.setImageResource(R.drawable.uncheck);
//                img_refunded.setImageResource(R.drawable.check);
//                alert.cancel();
//            }
//        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

//        Product objOffers = product_list.get(position);
//        Company objCompany = objOffers.getCompany();

//        if(objCompany != null)
//        {
//            tv_company_name.setText(""+objCompany.getCompany_name());
//        }
//        tv_product_id.setText(""+objOffers.getProduct_number());
//        List<Image> listimages = objOffers.getImages();
//        if(listimages != null && listimages.size() > 0)
//        {
//            rcv_product_images.setVisibility(View.VISIBLE);
//            tv_empty.setVisibility(View.GONE);

//            ProductDeatilsImagesAdapter imagesAdapter = new ProductDeatilsImagesAdapter(CustomerOrderHistoryActivity.this ,
//                    listimages);
//            rcv_product_images.setAdapter(imagesAdapter);
//            LinearLayoutManager manager = new LinearLayoutManager(CustomerOrderHistoryActivity.this , LinearLayoutManager.HORIZONTAL , false);
//            rcv_product_images.setLayoutManager(manager);

//        }
//        else
//        {
//            tv_empty.setVisibility(View.VISIBLE);
//            rcv_product_images.setVisibility(View.GONE);
//        }
//        tv_product_desc.setText(objOffers.getDescription());
//        tv_product_name.setText(objOffers.getProductName());
//        tv_product_price.setText(""+objOffers.getPrice());

        alert.show();
    }

    public void ShowProductOrderDetails(OrderHistory orderHistory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_product_order_history, null);
        final ImageView Ivback = dialogLayout.findViewById(R.id.Ivback);
        final TextView tv_product_name = dialogLayout.findViewById(R.id.tv_product_name);
        final TextView tv_product_price = dialogLayout.findViewById(R.id.tv_product_price);
        final RecyclerView rcv_products = dialogLayout.findViewById(R.id.rcv_products);
        final TextView tv_empty = dialogLayout.findViewById(R.id.tv_empty);

        List<OrderHistory> listOrders = new ArrayList<>();
        listOrders.add(orderHistory);

        ProductOrderAdapter adapter = new ProductOrderAdapter(CompanyOrderHistoryActivity.this, listOrders);
        LinearLayoutManager manager = new LinearLayoutManager(CompanyOrderHistoryActivity.this);
        rcv_products.setLayoutManager(manager);
        rcv_products.setAdapter(adapter);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();

        if(alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.enter_exit_animate);
        }

//        tv_product_name.setText(orderHistory.getProductName());
//        tv_product_price.setText(orderHistory.getPrice() +"*"+orderHistory.getQuantity() +"="+orderHistory.getTotalprice());

        Ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

//        Product objOffers = product_list.get(position);
//        Company objCompany = objOffers.getCompany();

//        if(objCompany != null)
//        {
//            tv_company_name.setText(""+objCompany.getCompany_name());
//        }
//        tv_product_id.setText(""+objOffers.getProduct_number());
//        List<Image> listimages = objOffers.getImages();
//        if(listimages != null && listimages.size() > 0)
//        {
//            rcv_product_images.setVisibility(View.VISIBLE);
//            tv_empty.setVisibility(View.GONE);

//            ProductDeatilsImagesAdapter imagesAdapter = new ProductDeatilsImagesAdapter(CustomerOrderHistoryActivity.this ,
//                    listimages);
//            rcv_product_images.setAdapter(imagesAdapter);
//            LinearLayoutManager manager = new LinearLayoutManager(CustomerOrderHistoryActivity.this , LinearLayoutManager.HORIZONTAL , false);
//            rcv_product_images.setLayoutManager(manager);

//        }
//        else
//        {
//            tv_empty.setVisibility(View.VISIBLE);
//            rcv_product_images.setVisibility(View.GONE);
//        }
//        tv_product_desc.setText(objOffers.getDescription());
//        tv_product_name.setText(objOffers.getProductName());
//        tv_product_price.setText(""+objOffers.getPrice());

        alert.show();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent i_back = new Intent(CompanyOrderHistoryActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id", company_id);
        startActivity(i_back);
        finish();
    }
}

