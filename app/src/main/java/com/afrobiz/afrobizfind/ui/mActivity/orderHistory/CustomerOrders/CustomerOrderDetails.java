package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CustomerOrders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.OrderHistory;
import com.afrobiz.afrobizfind.ui.modal.Users;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class CustomerOrderDetails extends AppCompatActivity
{
    ImageView Ivback;
    ProgressDialog pd;
    Users currentuser;
    ApiInterface objInterface;
    String token = null;
    List<OrderHistory> listOrderHistory = new ArrayList<>();
    RecyclerView rcv_orders;
    OrderHistory objOrderHistory;
    TextView tv_company_no, tv_company_name, tv_product_no, tv_product_name, tv_price, tv_qty, tv_order_no, tv_order_status,
            tv_order_date, tv_paid_by, tv_total_price;


    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tv_company_no = (TextView) findViewById(R.id.tv_company_no);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_product_no = (TextView) findViewById(R.id.tv_product_no);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_qty = (TextView) findViewById(R.id.tv_qty);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_order_status = (TextView) findViewById(R.id.tv_order_status);
        tv_order_date = (TextView) findViewById(R.id.tv_order_date);
        tv_paid_by = (TextView) findViewById(R.id.tv_paid_by);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);

        Ivback = (ImageView) findViewById(R.id.Ivback);

        Intent i_get = getIntent();

        objOrderHistory = (OrderHistory) i_get.getSerializableExtra("orderHistory");

        if(objOrderHistory != null)
        {
            tv_company_no.setText(objOrderHistory.getCompany_number());
            tv_company_name.setText(objOrderHistory.getCompanyName());
            tv_product_no.setText(String.valueOf(objOrderHistory.getProduct_number()));
            tv_product_name.setText(objOrderHistory.getProductName());
            tv_price.setText(String.valueOf(objOrderHistory.getPrice()));
            tv_qty.setText(String.valueOf(objOrderHistory.getQuantity()));
            tv_order_no.setText(String.valueOf(objOrderHistory.getId()));
            tv_order_status.setText(String.valueOf(objOrderHistory.getStatus()));
            tv_order_date.setText(objOrderHistory.getCreatedAt());
            tv_paid_by.setText(objOrderHistory.getMethod());
            tv_total_price.setText(String.valueOf(objOrderHistory.getTotalprice()));
        }

        currentuser = new PrefrenceManager(CustomerOrderDetails.this).getCurrentuser();

        if (currentuser != null)
        {
            token = currentuser.getToken();
            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);
            if(ApiClient.isNetworkAvailable(CustomerOrderDetails.this))
            {
//                new loadAllOrderHistoryOfUser().execute();
            }
            else
            {
                Toast.makeText(CustomerOrderDetails.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }
        }
        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_starrt = new Intent(CustomerOrderDetails.this, CustomerOrderHistoryActivity.class);
                startActivity(i_starrt);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_starrt = new Intent(CustomerOrderDetails.this, CustomerOrderHistoryActivity.class);
        startActivity(i_starrt);
        finish();
    }
}
