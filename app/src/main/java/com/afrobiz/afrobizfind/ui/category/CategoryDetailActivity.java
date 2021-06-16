package com.afrobiz.afrobizfind.ui.category;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.view.GridRecyclerView;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.adapter.CategoryDetailsAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Category;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;
import com.afrobiz.afrobizfind.ui.mActivity.LoginActivity;
import com.afrobiz.afrobizfind.ui.widgets.RevealAnimation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class CategoryDetailActivity extends AppCompatActivity
{
    LinearLayout linearLayout1;
    @BindView(R.id.rcv_category_details)
    GridRecyclerView rcv_category_details;
    ImageView Ivsearch;
    ImageView IvBack , img_sort;
    TextView tv_empty , tv_toollbar;
    String category;
    PopupWindow popupWindow;
    boolean islatest = false;
    boolean isSerach = false;
    EditText EtSearch;
    List<Company> companyList ;

    Category objCategory;
    List<Category> categoryList;
    int category_id;
    ProgressDialog pd3;
    String sortValue = "popular";
    ApiInterface objInterface;
    CategoryDetailsAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_detail);

        rcv_category_details = (GridRecyclerView) findViewById(R.id.rcv_category_details);

        IvBack = (ImageView) findViewById(R.id.IvBack);
        img_sort = (ImageView) findViewById(R.id.img_sort);
        Ivsearch = (ImageView) findViewById(R.id.Ivsearch);

        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_toollbar = (TextView) findViewById(R.id.tv_toollbar);

        EtSearch = (EditText) findViewById(R.id.EtSearch);
        objInterface  = ApiClient.create_Istance();
        sortValue = new PrefrenceManager(CategoryDetailActivity.this).getSortBy();

        if(sortValue == null)
        {
            sortValue = "popular";
        }
        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            categoryList = (List<Category>) b.getSerializable("list_category");
            objCategory = (Category) b.getSerializable("category");
            category_id = b.getInt("categoryid");

            if(objCategory != null )
            {
                category_id = objCategory.getId();
                category = objCategory.getName();
                tv_toollbar.setText(category);

                if (ApiClient.isNetworkAvailable(CategoryDetailActivity.this))
                {
                    if(objInterface != null)
                    {
                        new GetCompany(category_id, sortValue).execute();
                    }
                }
                else
                {
                    Toast.makeText(CategoryDetailActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if(category_id == 0)
                {
                    category = "newest 50 companies";
                    tv_toollbar.setText(category);

                    if (ApiClient.isNetworkAvailable(CategoryDetailActivity.this))
                    {
                        if(objInterface != null)
                        {
                            new GetCompany(category_id, sortValue).execute();
                        }
                    }
                    else
                    {
                        Toast.makeText(CategoryDetailActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        if(isSerach == false)
        {
            tv_toollbar.setVisibility(View.VISIBLE);
            EtSearch.setVisibility(View.GONE);
        }

        IvBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(EtSearch.getVisibility() == View.VISIBLE)
                {
                    tv_toollbar.setVisibility(View.VISIBLE);
                    EtSearch.setVisibility(View.GONE);
                }
                else
                {
                    if(category_id == 0)
                    {
                        onBackPressed();
                    }
                    else
                    {
                        Intent i_back = new Intent(CategoryDetailActivity.this, CategoryActivity.class);
                        i_back.putExtra("category" , (Serializable) category);
                        i_back.putExtra("list_category" , (Serializable) categoryList);
                        startActivity(i_back);
                        finish();
                    }
                }
            }
        });

        img_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ShowSortDialog(v);
            }
        });

        Ivsearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isSerach == false)
                {
                    isSerach = true;
                    tv_toollbar.setVisibility(View.GONE);
                    EtSearch.setVisibility(View.VISIBLE);

                }
                else
                {
                    isSerach = false;
                    tv_toollbar.setVisibility(View.VISIBLE);
                    EtSearch.setVisibility(View.GONE);
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
            }
        });
    }
    private void filterCompany(String text)
    {
        if(companyList  != null && companyList.size() >0)
        {
            List<Company> filterdNames = new ArrayList<>();

            for (Company s : companyList)
            {
                if (s.getCompanyName().toLowerCase().contains(text.toLowerCase()))
                {
                    filterdNames.add(s);
                }
            }
            if (filterdNames.size() > 0)
            {
                tv_empty.setVisibility(View.GONE);
                rcv_category_details.setVisibility(View.VISIBLE);

                adapter.filterList(filterdNames);

            }
            else
            {
                tv_empty.setVisibility(View.VISIBLE);
                rcv_category_details .setVisibility(View.GONE);
            }
        }
    }
    public class GetCompany extends AsyncTask<Void, Void, Void>
    {
        int id= 0 ;
        String sorting = null;
        public GetCompany(int company_id  , String sort)
        {
            this.id = company_id;
            this.sorting = sort;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd3 = new ProgressDialog(CategoryDetailActivity.this);
            pd3.setCancelable(false);
            pd3.setMessage(getString(R.string.please_wait));
            pd3.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getCompany(id, sorting);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public void getCompany(int id, String sorting)
    {
        Log.e("sorting",""+sorting);
        Category objCompany = new Category();
        objCompany.setId(id);
        objCompany.setSortby(sorting);
        Call<Response> call = objInterface.GetCartegoryProducts(objCompany);

        Log.e("callGet" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("getcode" , ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body().getCompanies()));

                    Log.e("getresponse" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {
                        companyList = response.body().getCompanies();

                        if(companyList != null && companyList.size() >0)
                        {
                            rcv_category_details.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);

                            adapter = new CategoryDetailsAdapter(CategoryDetailActivity.this, companyList);
                            rcv_category_details.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(CategoryDetailActivity.this , 2);
                            rcv_category_details.setLayoutManager(manager);

                            runLayoutAnimation(rcv_category_details);
                        }
                        else
                            {
                            tv_empty.setVisibility(View.VISIBLE);
                                rcv_category_details.setVisibility(View.GONE);
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
                        Toast.makeText(CategoryDetailActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd3 != null & pd3.isShowing())
                        {
                            pd3.dismiss();
                        }
                        Toast.makeText(CategoryDetailActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd3 != null & pd3.isShowing())
                    {
                        pd3.dismiss();
                    }
                    Toast.makeText(CategoryDetailActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd3 != null & pd3.isShowing())
                {
                    pd3.dismiss();
                }
                Toast.makeText(CategoryDetailActivity.this , t.toString(), Toast.LENGTH_LONG).show();
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
    public void ShowSortDialog(View v)
    {
        LayoutInflater layoutInflater = (LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_sort,null);

        LinearLayout layout_latest = (LinearLayout) customView.findViewById(R.id.layout_latest);
        LinearLayout layout_popular = (LinearLayout) customView.findViewById(R.id.layout_popular);

        ImageView img_latest = (ImageView) customView.findViewById(R.id.img_latest);
        ImageView img_popular = (ImageView) customView.findViewById(R.id.img_popular);

        sortValue = new PrefrenceManager(CategoryDetailActivity.this).getSortBy();

        if(sortValue.equals("popular"))
        {
            islatest  = false;
            img_latest.setImageResource(R.drawable.uncheck);
            img_popular.setImageResource(R.drawable.check);
        }
        else
        {
            islatest = true;
            img_popular.setImageResource(R.drawable.uncheck);
            img_latest.setImageResource(R.drawable.check);
        }

        img_latest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(islatest == false)
                {
                    islatest = true;
                    sortValue = "latest";
                    img_popular.setImageResource(R.drawable.uncheck);
                    img_latest.setImageResource(R.drawable.check);
                }
                else
                {
                    islatest = false;
                    sortValue = "popular";
                    img_latest.setImageResource(R.drawable.uncheck);
                    img_popular.setImageResource(R.drawable.check);
                }

                new PrefrenceManager(CategoryDetailActivity.this).setSortBy(sortValue);
//                new GetCompany(category_id, sortValue).execute();

                if (ApiClient.isNetworkAvailable(CategoryDetailActivity.this))
                {
                    if(objInterface != null)
                    {
                        new GetCompany(category_id, sortValue).execute();
                    }
                }
                else
                {
                    Toast.makeText(CategoryDetailActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }
        });

        img_popular.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(islatest == false)
                {
                    islatest = true;
                    sortValue = "latest";
                    img_popular.setImageResource(R.drawable.uncheck);
                    img_latest.setImageResource(R.drawable.check);
                }
                else
                {
                    islatest = false;
                    sortValue = "popular";
                    img_latest.setImageResource(R.drawable.uncheck);
                    img_popular.setImageResource(R.drawable.check);
                }
                new PrefrenceManager(CategoryDetailActivity.this).setSortBy(sortValue);
//                new GetCompany(category_id, sortValue).execute();
                if (ApiClient.isNetworkAvailable(CategoryDetailActivity.this))
                {
                    if(objInterface != null)
                    {
                        new GetCompany(category_id, sortValue).execute();
                    }
                }
                else
                {
                    Toast.makeText(CategoryDetailActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }
        });



        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(v, 0, 40);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0,0);
    }

    @Override
    public void onBackPressed()
    {
        if(EtSearch.getVisibility() == View.VISIBLE)
        {
            tv_toollbar.setVisibility(View.VISIBLE);
            EtSearch.setVisibility(View.GONE);
        }
        else
        {
            if(category_id == 0)
            {
                super.onBackPressed();
            }
            else
            {
                Intent i_back = new Intent(CategoryDetailActivity.this, CategoryActivity.class);
                i_back.putExtra("category" , (Serializable) category);
                i_back.putExtra("list_category" , (Serializable) categoryList);
                startActivity(i_back);
                finish();
            }



        }

    }
    private void startRevealActivity(View v)
    {
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, null);

        overridePendingTransition(0, 0);
    }

    public void ClickCompany(int id)
    {
        if (new PrefrenceManager(CategoryDetailActivity.this).getLogout() == true) {
            Intent intent = new Intent(CategoryDetailActivity.this, CompanyProfileActivity.class);
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
        Dialog d_login = new Dialog(CategoryDetailActivity.this);
        d_login.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_login.getWindow().getDecorView().setBackgroundColor(Color.WHITE);

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
                Intent i_start = new Intent(CategoryDetailActivity.this, LoginActivity.class);
                startActivity(i_start);
                d_login.dismiss();
            }
        });
        d_login.show();
    }
}
