package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CustomerOrders;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.adapter.ProductDeatilsImagesAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.OrderHistory;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;
import com.afrobiz.afrobizfind.ui.mActivity.LoginActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class CustomerOrderHistoryActivity extends AppCompatActivity
{
    RecyclerView rcv_order;
    ApiInterface objInterface;
    String token = null;
    Users currentuser;
    List<OrderHistory> listOrderHistory = new ArrayList<>();
    ProgressDialog pd;
    TextView tv_total, tv_empty;
    ImageView Ivback;
    CustomerOrderHistoryAdapter adapter;
    EditText et_search;
    Product currentproduct;
    public Dialog d_login;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_order = (RecyclerView) findViewById(R.id.rcv_order);
        Ivback = (ImageView) findViewById(R.id.Ivback);
        et_search = (EditText) findViewById(R.id.et_search);

        currentuser = new PrefrenceManager(CustomerOrderHistoryActivity.this).getCurrentuser();

        if (currentuser != null)
        {
            token = currentuser.getToken();
            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if(ApiClient.isNetworkAvailable(CustomerOrderHistoryActivity.this))
            {
                new loadAllOrderHistoryOfUser().execute();
            }
            else
            {
                Toast.makeText(CustomerOrderHistoryActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }
        }
        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(CustomerOrderHistoryActivity.this, MainActivity.class);
                startActivity(i_back);
                finish();
            }
        });
        et_search.addTextChangedListener(new TextWatcher()
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
                if(s != null )
                {
                    FilterCustomerOrders(s.toString().toLowerCase());
                }
            }
        });
    }
    private void runLayoutAnimation(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom_scale);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public void FilterCustomerOrders(String text)
    {
        if(listOrderHistory != null && listOrderHistory.size() > 0)
        {
            List<OrderHistory> listTemp = new ArrayList<>();
            for (OrderHistory order : listOrderHistory)
            {
                if((order.getCompanyName() != null && order.getCompanyName().toLowerCase().contains(text))
                        || (order.getProductName() != null && order.getProductName().toLowerCase().contains(text)) ||
                        (order.getStatus() != null && order.getStatus().toLowerCase().contains(text)) ||
                        (order.getCreatedAt() != null && order.getCreatedAt().toLowerCase().contains(text)) ||
                        (order.getId() != 0 && String.valueOf(order.getId()).toLowerCase().contains(text)) ||
                        (order.getQuantity() != 0 && String.valueOf(order.getQuantity()).toLowerCase().contains(text)) ||
                        (order.getTotalprice() != 0 && String.valueOf(order.getTotalprice()).toLowerCase().contains(text)))
                {
                    listTemp.add(order);

                }
            }
            adapter.filterList(listTemp);
        }
    }

    public void ShowDetailsOfOrder(OrderHistory orderHistory)
    {
        Intent i_start = new Intent(CustomerOrderHistoryActivity.this, CustomerOrderDetails.class);
        i_start.putExtra("orderHistory", orderHistory);
        startActivity(i_start);
    }

    public void GetShowProductDetails(int productId)
    {
        new GetProduct(productId).execute();
    }

    public class loadAllOrderHistoryOfUser extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(CustomerOrderHistoryActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            AllOrderHistoriesOfUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public void AllOrderHistoriesOfUser()
    {
        Call<Response> call = objInterface.getAllOrdersOfUser();
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }

                if(response != null && response.isSuccessful())
                {
                    if(response.body() != null && response.body().getResult() == 1)
                    {
                        if(response.body().getOrderHistoryList() != null && response.body().getOrderHistoryList().size() > 0)
                        {
                            listOrderHistory = response.body().getOrderHistoryList();
                            if(listOrderHistory != null && listOrderHistory.size() > 0)
                            {
                                tv_total.setText(String.valueOf(listOrderHistory.size()));
                                rcv_order.setVisibility(View.VISIBLE);
                                tv_empty.setVisibility(View.GONE);

                                adapter = new CustomerOrderHistoryAdapter(CustomerOrderHistoryActivity.this, listOrderHistory);
                                LinearLayoutManager manager = new LinearLayoutManager(CustomerOrderHistoryActivity.this);
                                rcv_order.setLayoutManager(manager);
                                rcv_order.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                runLayoutAnimation(rcv_order);
                            }
                            else
                            {
                                tv_total.setText("0");
                                tv_empty.setVisibility(View.VISIBLE);
                                rcv_order.setVisibility(View.GONE);
                            }

                        }
                    }
                }
                else
                {
                    Toast.makeText(CustomerOrderHistoryActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(CustomerOrderHistoryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(CustomerOrderHistoryActivity.this, MainActivity.class);
        startActivity(i_back);
        finish();
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

            pd = new ProgressDialog(CustomerOrderHistoryActivity.this);
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

                        if(currentproduct != null)
                        {
                            ShowProductsDetails(currentproduct);
                        }

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
                        Toast.makeText(CustomerOrderHistoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd != null & pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(CustomerOrderHistoryActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null & pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(CustomerOrderHistoryActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(CustomerOrderHistoryActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void ShowProductsDetails(Product objProduct)
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

        tvOrderNow.setVisibility(View.GONE);

        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();

        Company objCompany = objProduct.getCompany();

        if(objCompany != null)
        {
            tv_company_name.setText(""+objCompany.getCompany_name());
        }

        tv_product_id.setText(""+objProduct.getProduct_number());
        List<Image> listimages = objProduct.getImages();
        if(listimages != null && listimages.size() > 0)
        {
            rcv_product_images.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);

            ProductDeatilsImagesAdapter imagesAdapter = new ProductDeatilsImagesAdapter(CustomerOrderHistoryActivity.this ,
                    listimages);
            rcv_product_images.setAdapter(imagesAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(CustomerOrderHistoryActivity.this , LinearLayoutManager.HORIZONTAL , false);
            rcv_product_images.setLayoutManager(manager);

        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_product_images.setVisibility(View.GONE);
        }
        tv_product_desc.setText(objProduct.getDescription());
        tv_product_name.setText(objProduct.getProductName());
        tv_product_price.setText(""+objProduct.getPrice());

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alert.cancel();
            }
        });
//        tvOrderNow.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(ApiClient.isNetworkAvailable(CustomerOrderHistoryActivity.this))
//                {
//                    BookOrder(objProduct, objCompany);
//                }
//                else
//                {
//                    Toast.makeText(CustomerOrderHistoryActivity.this, getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//        Product objOffers = product_list.get(position);


        alert.show();
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
                        Toast.makeText(CustomerOrderHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(CustomerOrderHistoryActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
        if(ApiClient.isNetworkAvailable(CustomerOrderHistoryActivity.this))
        {
            AddOrder(objProduct, objCompany);
        }
        else
        {
            Toast.makeText(CustomerOrderHistoryActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }
    public void ClickCompany(int id)
    {
        if (new PrefrenceManager(CustomerOrderHistoryActivity.this).getLogout() == true)
        {
            Intent intent = new Intent(CustomerOrderHistoryActivity.this, CompanyProfileActivity.class);
            intent.putExtra("company_id", id);
            startActivity(intent);
        }
        else
        {
            displayLoginDialog();
        }
    }
    public void displayLoginDialog()
    {
        Dialog d_login = new Dialog(CustomerOrderHistoryActivity.this);
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
                Intent i_start = new Intent(CustomerOrderHistoryActivity.this, LoginActivity.class);
//                i_start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i_start);
//                finish();
                d_login.dismiss();
            }
        });
        d_login.show();

    }
}
