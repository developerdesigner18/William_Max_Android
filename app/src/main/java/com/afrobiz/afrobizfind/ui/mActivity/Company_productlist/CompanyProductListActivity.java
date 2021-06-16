package com.afrobiz.afrobizfind.ui.mActivity.Company_productlist;

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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.ui.events.AddEventActivity;
import com.afrobiz.afrobizfind.ui.events.CompanyEventListAdapter;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.adapter.ProductDeatilsImagesAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.AddProductActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;
import com.afrobiz.afrobizfind.ui.widgets.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class CompanyProductListActivity extends AppCompatActivity
{
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.img_search)
    ImageView img_search;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.Rv_productList)
    RecyclerView RvProductList;

    @BindView(R.id.TvAddProduct)
    TextView TvAddProduct;

    @BindView(R.id.tv_total)
    TextView tv_total;


    @BindView(R.id.TvAddEvent)
    TextView TvAddEvent;

    TextView tv_products, tv_all, tv_events, tv_empty_product, tv_empty_all, tv_empty_event;

    private List<String> list = new ArrayList<>();

    CompanyProductListAdapter adapter, adapterAllProduct;

    LinearLayoutManager layoutManager;
    ConstraintLayout constraintLayout;
    RelativeLayout relative_all, relative_product, relative_event;
    RecyclerView Rv_allProductList, Rv_allEventList , Rv_eventList;
    String deleteimageid = null;
    CompanyEventListAdapter event_adapter , event_adapter_ALL;
    String token = null;
    ApiInterface objInterface;
    ProgressDialog pd2, pd , pd3, pd4;
    List<Product> product_list = new ArrayList<>();
    List<Event> event_list = new ArrayList<>();
    Users currentuser;

    int deleteId = 0 , company_id = 0;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.getSupportActionBar().hide();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_product_list);

        constraintLayout = findViewById(R.id.clayout);

        relative_all = findViewById(R.id.relative_all);
        relative_product = findViewById(R.id.relative_product);
        relative_event = findViewById(R.id.relative_event);

        Rv_eventList = findViewById(R.id.Rv_eventList);
        Rv_allEventList = findViewById(R.id.Rv_allEventList);
        Rv_allProductList = findViewById(R.id.Rv_allProductList);

        tv_empty_all = findViewById(R.id.tv_empty_all);
        tv_empty_event = findViewById(R.id.tv_empty_event);
        tv_empty_product = findViewById(R.id.tv_empty_product);

        tv_all = findViewById(R.id.tv_all);
        tv_products = findViewById(R.id.tv_products);
        tv_events = findViewById(R.id.tv_events);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com",""+company_id);
        }

        currentuser = new PrefrenceManager(CompanyProductListActivity.this).getCurrentuser();

            if(currentuser != null)
            {
                token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(CompanyProductListActivity.this))
                {
                    Log.e("company_id", "" + company_id);
                    new GetCompanyProductList(company_id).execute();
                    new GetCompanyEventList(company_id).execute();


                }
                else
                {
                    Toast.makeText(CompanyProductListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }

        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
        tv_all.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int ans = product_list.size() + event_list.size();
                tv_total.setText(""+ans);
                relative_all.setVisibility(View.VISIBLE);
                relative_product.setVisibility(View.GONE);
                relative_event.setVisibility(View.GONE);

                runLayoutAnimation(Rv_allEventList);
                runLayoutAnimation(Rv_allProductList);
            }
        });

        tv_products.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tv_total.setText(""+product_list.size());
                relative_all.setVisibility(View.GONE);
                relative_product.setVisibility(View.VISIBLE);
                relative_event.setVisibility(View.GONE);

                runLayoutAnimationFromleft(RvProductList);
            }
        });

        tv_events.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tv_total.setText(""+event_list.size());
                relative_all.setVisibility(View.GONE);
                relative_product.setVisibility(View.GONE);
                relative_event.setVisibility(View.VISIBLE);

                runLayoutAnimationFromRight(Rv_eventList);

            }
        });

    }

    public class GetCompanyEventList extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;

        public GetCompanyEventList(int company_id)
        {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd4 =  new ProgressDialog(CompanyProductListActivity.this);
            pd4.setCancelable(false);
            pd4.setMessage(getString(R.string.please_wait));
            pd4.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getEvents(id);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getEvents(int id)
    {
        Call<Response>  call = objInterface.GetAllCompanyEvents(id);

        Log.e("callGet" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("getcode" , ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {

                        Log.e("com_id",""+id);
                        event_list = response.body().getEventList();

                        if(event_list !=null && event_list.size() >0)
                        {
                            Log.e("productlist",""+event_list.size());
                            tv_empty_event.setVisibility(View.GONE);
                            Rv_eventList.setVisibility(View.VISIBLE);

                            tv_empty_all.setVisibility(View.GONE);
                            Rv_allEventList.setVisibility(View.VISIBLE);

                            tv_total.setText(""+event_list.size());

                            int ans = product_list.size() + event_list.size();
                            tv_total.setText(""+ans);

                            event_adapter = new CompanyEventListAdapter(CompanyProductListActivity.this, event_list);
                            LinearLayoutManager productlayoutManager = new LinearLayoutManager(CompanyProductListActivity.this);
                            Rv_eventList.setLayoutManager(productlayoutManager);
                            Rv_eventList.setAdapter(event_adapter);
                            event_adapter.notifyDataSetChanged();

                            runLayoutAnimation(Rv_eventList);

                            event_adapter_ALL = new CompanyEventListAdapter(CompanyProductListActivity.this, event_list);
                            LinearLayoutManager productlayoutManager11 = new LinearLayoutManager(CompanyProductListActivity.this);
                            Rv_allEventList.setLayoutManager(productlayoutManager11);
                            Rv_allEventList.setAdapter(event_adapter_ALL);
                            event_adapter_ALL.notifyDataSetChanged();

                            runLayoutAnimation(Rv_allEventList);

                            SwipeHelper swipeHelper = new SwipeHelper(CompanyProductListActivity.this)
                            {
                                @Override
                                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                                            "Delete",
                                            0,
                                            Color.parseColor("#FF3C30"),
                                            new SwipeHelper.UnderlayButtonClickListener()
                                            {
                                                @Override
                                                public void onClick(final int pos)
                                                {
                                                    if(ApiClient.isNetworkAvailable(CompanyProductListActivity.this))
                                                    {
                                                        if(objInterface != null)
                                                        {
                                                            int productid = event_list.get(pos).getId();
                                                            Log.e("deleteid" , ""+productid);
                                                            new DeleteEvent(productid , pos).execute();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(CompanyProductListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    ));
                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                                            "Edit",
                                            0,
                                            Color.parseColor("#FF9502"),
                                            new SwipeHelper.UnderlayButtonClickListener()
                                            {
                                                @Override
                                                public void onClick(int pos)
                                                {
                                                    Intent i_update = new Intent(CompanyProductListActivity.this , AddEventActivity.class);
                                                    i_update.putExtra("update",true);
                                                    i_update.putExtra("company_id",company_id);
                                                    i_update.putExtra("event", event_list.get(pos));
                                                    startActivity(i_update);
                                                    finish();
                                                }
                                            }
                                    ));
                                }
                            };
                            swipeHelper.attachToRecyclerView(Rv_eventList);

                            SwipeHelper swipeHelperAllEvent = new SwipeHelper(CompanyProductListActivity.this)
                            {
                                @Override
                                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                                            "Delete",
                                            0,
                                            Color.parseColor("#FF3C30"),
                                            new SwipeHelper.UnderlayButtonClickListener()
                                            {
                                                @Override
                                                public void onClick(final int pos)
                                                {
                                                    if(ApiClient.isNetworkAvailable(CompanyProductListActivity.this))
                                                    {
                                                        if(objInterface != null)
                                                        {
                                                            int productid = event_list.get(pos).getId();
                                                            Log.e("deleteid" , ""+productid);
                                                            new DeleteEvent(productid , pos).execute();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(CompanyProductListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    ));
                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                                            "Edit",
                                            0,
                                            Color.parseColor("#FF9502"),
                                            new SwipeHelper.UnderlayButtonClickListener()
                                            {
                                                @Override
                                                public void onClick(int pos)
                                                {
                                                    Intent i_update = new Intent(CompanyProductListActivity.this , AddEventActivity.class);
                                                    i_update.putExtra("update",true);
                                                    i_update.putExtra("company_id",company_id);
                                                    i_update.putExtra("event", event_list.get(pos));
                                                    startActivity(i_update);
                                                    finish();
                                                }
                                            }
                                    ));
                                }
                            };
                            swipeHelperAllEvent.attachToRecyclerView(Rv_allEventList);


                        }
                        else
                        {
                            tv_total.setText("0");
                            tv_empty_event.setVisibility(View.VISIBLE);
                            Rv_eventList.setVisibility(View.GONE);
                        }
                        if(pd4 != null & pd4.isShowing())
                        {
                            pd4.dismiss();
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd4 != null & pd4.isShowing())
                        {
                            pd4.dismiss();
                        }
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd4 != null & pd4.isShowing())
                        {
                            pd4.dismiss();
                        }
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd4 != null & pd4.isShowing())
                    {
                        pd4.dismiss();
                    }
                    Toast.makeText(CompanyProductListActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd4 != null & pd4.isShowing())
                {
                    pd4.dismiss();
                }
                Log.e("exception Get All Events",""+t.toString());
                Toast.makeText(CompanyProductListActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public class GetCompanyProductList extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;

        public GetCompanyProductList(int company_id)
        {
            this.id = company_id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(CompanyProductListActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getProduct(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getProduct(int id)
    {
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response>  call = objInterface.GetAllCompanyProducts(objCompany);

        Log.e("callGet" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("getcode" , ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {
                        product_list = response.body().getProducts();

                        if(product_list !=null && product_list.size() >0)
                        {
                            Log.e("productlist",""+product_list.size());
                            tv_empty_product.setVisibility(View.GONE);
                            RvProductList.setVisibility(View.VISIBLE);

                            tv_empty_all.setVisibility(View.GONE);
                            Rv_allProductList.setVisibility(View.VISIBLE);

                            tv_total.setText(""+product_list.size());

                            adapter = new CompanyProductListAdapter(CompanyProductListActivity.this, product_list);
                            LinearLayoutManager productlayoutManager = new LinearLayoutManager(CompanyProductListActivity.this);
                            RvProductList.setLayoutManager(productlayoutManager);
                            RvProductList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

//                            int resId = R.anim.layout_animation_fall_down;
//                            LayoutAnimationController animation =
//                                    AnimationUtils.loadLayoutAnimation(CompanyProductListActivity.this, resId);
//                            RvProductList.setLayoutAnimation(animation);

                            runLayoutAnimationFromleft(RvProductList);

                            adapterAllProduct = new CompanyProductListAdapter(CompanyProductListActivity.this, product_list);
                            LinearLayoutManager productlayoutManager1 = new LinearLayoutManager(CompanyProductListActivity.this);
                            Rv_allProductList.setLayoutManager(productlayoutManager1);
                            Rv_allProductList.setAdapter(adapterAllProduct);
                            adapterAllProduct.notifyDataSetChanged();

                            runLayoutAnimation(Rv_allProductList);

                            SwipeHelper swipeHelper = new SwipeHelper(CompanyProductListActivity.this)
                            {
                                @Override
                                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                                            "Delete",
                                            0,
                                            Color.parseColor("#FF3C30"),
                                            new SwipeHelper.UnderlayButtonClickListener()
                                            {
                                                @Override
                                                public void onClick(final int pos)
                                                {
                                                    if(ApiClient.isNetworkAvailable(CompanyProductListActivity.this))
                                                    {
                                                        if(objInterface != null)
                                                        {
                                                            int productid = product_list.get(pos).getId();
                                                            Log.e("deleteid" , ""+productid);
                                                            new DeleteProduct(productid , pos).execute();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(CompanyProductListActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    ));
                                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                                            "Edit",
                                            0,
                                            Color.parseColor("#FF9502"),
                                            new SwipeHelper.UnderlayButtonClickListener()
                                            {
                                                @Override
                                                public void onClick(int pos)
                                                {
                                                    Intent i_update = new Intent(CompanyProductListActivity.this , AddProductActivity.class);
                                                    i_update.putExtra("update",true);
                                                    i_update.putExtra("company_id",company_id);
                                                    i_update.putExtra("product", product_list.get(pos));
                                                    startActivity(i_update);
                                                    finish();
                                                }
                                            }
                                    ));
                                }
                            };
                            swipeHelper.attachToRecyclerView(RvProductList);
                            swipeHelper.attachToRecyclerView(Rv_allProductList);
                        }
                        else
                        {
                            tv_total.setText("0");
                            tv_empty_product.setVisibility(View.VISIBLE);
                            RvProductList.setVisibility(View.GONE);
                        }
                            if(pd3 != null & pd3.isShowing())
                            {
                                pd3.dismiss();
                            }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(CompanyProductListActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Log.e("failure" , ""+t.toString());
                Toast.makeText(CompanyProductListActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public class DeleteProduct extends AsyncTask<Void, Void, Void>
    {
        int id = 0 , posi = 0;
        public DeleteProduct(int offerId , int pos)
        {
            this.id = offerId;
            this.posi = pos;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(CompanyProductListActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            deleteFromId(id, posi);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public class DeleteEvent extends AsyncTask<Void, Void, Void>
    {
        int id = 0 , posi = 0;
        public DeleteEvent(int offerId , int pos)
        {
            this.id = offerId;
            this.posi = pos;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd2 = new ProgressDialog(CompanyProductListActivity.this);
            pd2.setCancelable(false);
            pd2.setMessage(getString(R.string.please_wait));
            pd2.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            deleteFromIdEvent(id, posi);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
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
    private void runLayoutAnimationFromleft(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_left);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    private void runLayoutAnimationFromRight(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public void deleteFromIdEvent(int deleteid, int pos)
    {
//        Event objOffers = new Event();
//        objOffers.setId(deleteid);
        Call<Response>  call = objInterface.DeleteEvent(deleteid);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {

                        event_list.remove(pos);
                        Toast.makeText(CompanyProductListActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                event_adapter.notifyDataSetChanged();
                                event_adapter_ALL.notifyDataSetChanged();
                            }
                        });
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
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd2 != null & pd2.isShowing())
                        {
                            pd2.dismiss();
                        }
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd2 != null & pd2.isShowing())
                    {
                        pd2.dismiss();
                    }
                    Toast.makeText(CompanyProductListActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd2 != null && pd2.isShowing())
                {
                    pd2.dismiss();
                }
                Toast.makeText(CompanyProductListActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void deleteFromId(int deleteid, int pos)
    {
        Product objOffers = new Product();
        objOffers.setId(deleteid);
        Call<Response>  call = objInterface.DeleteProduct(objOffers);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {
                        product_list.remove(pos);
                        Toast.makeText(CompanyProductListActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                adapter.notifyDataSetChanged();
                                adapterAllProduct.notifyDataSetChanged();
                            }
                        });
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
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd != null & pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(CompanyProductListActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null & pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(CompanyProductListActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(CompanyProductListActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void filter(String text)
    {
        if(product_list != null && product_list.size() >0)
        {
            List<Product> filterdNames = new ArrayList<>();

            for (Product s : product_list)
            {
                if (s.getProductName().toLowerCase().contains(text.toLowerCase())||
                        s.getProduct_number().toLowerCase().contains(text.toLowerCase()))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                tv_empty_product.setVisibility(View.GONE);
                RvProductList.setVisibility(View.VISIBLE);
                tv_total.setText(""+filterdNames.size());
                adapter.filterList(filterdNames);
            }
            else
            {
                tv_total.setText("0");
                tv_empty_product.setVisibility(View.VISIBLE);
                RvProductList.setVisibility(View.GONE);
            }
        }
    }
    @OnClick(R.id.TvAddEvent)
    public void onAddEventClicked()
    {
        Intent intent = new Intent(CompanyProductListActivity.this, AddEventActivity.class);
        intent.putExtra("company_id",company_id);
        startActivity(intent);
    }

    @OnClick(R.id.TvAddProduct)
    public void onAddProductClicked()
    {
        Intent intent = new Intent(CompanyProductListActivity.this, AddProductActivity.class);
        intent.putExtra("company_id",company_id);
        startActivity(intent);
    }

    private void Dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        builder.setMessage("Write your message here.");
        View dialogLayout = getLayoutInflater().inflate(R.layout.raw_productl_ist, null);
        final EditText etpass = dialogLayout.findViewById(R.id.etPass);
        final TextView tvcancel = dialogLayout.findViewById(R.id.tv_cancel);
        final TextView tvdeleteAccount = dialogLayout.findViewById(R.id.tv_deleteAccount);
        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        if(alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.FadDialogAnimation1);
        }
        tvcancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                etpass.setText("");
                alert.cancel();
            }
        });
        tvdeleteAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alert.cancel();
                etpass.setText("");
            }
        });

        alert.show();
    }

    @OnClick(R.id.Ivback)
    public void onViewClicked()
    {
        Intent i_back= new Intent(CompanyProductListActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id",company_id);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back= new Intent(CompanyProductListActivity.this, SavedCompanyDetailsActivity.class);
        i_back.putExtra("company_id",company_id);
        startActivity(i_back);
        finish();
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
        Log.e("price",""+priceArr[0] +"        "+priceArr[1]);

        Call<Response> call = objInterface.AddOrder(productID, currentuser.getUserNumber(),
                Double.parseDouble(value),quantity,  orderStatus, paymentmethod);

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
                            Toast.makeText(CompanyProductListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(CompanyProductListActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
        if(ApiClient.isNetworkAvailable(CompanyProductListActivity.this))
        {
            AddOrder(objProduct, objCompany);
        }
        else
        {
            Toast.makeText(CompanyProductListActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
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
        final TextView tv_empty = dialogLayout.findViewById(R.id.tv_empty);
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

            ProductDeatilsImagesAdapter imagesAdapter = new ProductDeatilsImagesAdapter(CompanyProductListActivity.this ,
                    listimages);
            rcv_product_images.setAdapter(imagesAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(CompanyProductListActivity.this , LinearLayoutManager.HORIZONTAL , false);
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

        tvOrderNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(ApiClient.isNetworkAvailable(CompanyProductListActivity.this))
                {
                    BookOrder(objProduct, objCompany);
                }
                else
                {
                    Toast.makeText(CompanyProductListActivity.this, getString(R.string.no_connection),Toast.LENGTH_SHORT).show();

                }
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
}
