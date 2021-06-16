package com.afrobiz.afrobizfind.ui.mActivity.favcompany;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.ProfileDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class FavoriteCompanyActivity extends AppCompatActivity
{
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.RvFavoriteCompany)
    RecyclerView RvFavoriteCompany;


    private List<Company> list = new ArrayList<>();
    FavoriteCompanyAdapter adapter;
    LinearLayoutManager layoutManager;

    ApiInterface objInterface;
    ProgressDialog pd;
    Users currentuser;
    int company_id = 0;
    String token = null;
    TextView tv_empty;

    ProgressDialog pd1;
    boolean from_profile = false;

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
        setContentView(R.layout.activity_favourite_company);
        ButterKnife.bind(this);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.VISIBLE);
        RvFavoriteCompany.setVisibility(View.GONE);
        Init();
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            company_id = b.getInt("company_id");
            from_profile = b.getBoolean("from_profile");
        }

        currentuser = new PrefrenceManager(FavoriteCompanyActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token" , ""+token);
            objInterface  = ApiClient.create_InstanceAuth(token);

            if(objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(FavoriteCompanyActivity.this))
                {
                    Log.e("company_id",""+company_id);
                    new GetFavouriteCompany().execute();
                }
                else
                {
                    Toast.makeText(FavoriteCompanyActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
                }
            }
        }
//        objInterface = ApiClient.create_Istance();

//        if(ApiClient.isNetworkAvailable(FavoriteCompanyActivity.this))
//        {
//
//        }
    }
    public void addFavourites()
    {
//        String company_id = null;
        Company objCompany = new Company();
        objCompany.setId(company_id);

        Call<Response>  call = objInterface.AddFavouriteCompany(objCompany);

//        Call<Response> call = objInterface.AddFavouriteCompany(com);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if (response.body().getResult() == 1)
                    {
                        Company resultCompany = response.body().getCompany();

                        if(resultCompany != null)
                        {
                            if (pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                        }
                        else
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(FavoriteCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(FavoriteCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(FavoriteCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getFavourites()
    {
//        String company_id = null;
//        Company com = new Company();
//        com.setCompany_id(company_id);

        Call<Response> call = objInterface.GetFavouriteCompany();

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse" , ""+response.body().getResult());

                    if (response.body().getResult() == 1)
                    {
                        list = response.body().getCompanies();

                        if(list != null && list.size() >0)
                        {
                            Collections.reverse(list);

                            Init();


                            RvFavoriteCompany.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);

                            if (pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                        }
                        else
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(FavoriteCompanyActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(FavoriteCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(FavoriteCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addToFav(int position)
    {
        if(ApiClient.isNetworkAvailable(FavoriteCompanyActivity.this))
        {
            if(objInterface != null)
            {
                new AddFavouriteCompany().execute();
            }
        }
        else
        {
            Toast.makeText(FavoriteCompanyActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
        }
    }

    public void removeFromFav(int position)
    {
        if(ApiClient.isNetworkAvailable(FavoriteCompanyActivity.this))
        {
            if(objInterface != null)
            {
                new RemoveFromFav(position).execute();
            }
        }
        else
        {
            Toast.makeText(FavoriteCompanyActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
        }

    }

    public class  GetFavouriteCompany extends AsyncTask<Void, Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(FavoriteCompanyActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getFavourites();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public class  AddFavouriteCompany extends AsyncTask<Void, Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd = new ProgressDialog(FavoriteCompanyActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            addFavourites();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    private void Init()
    {
        layoutManager = new LinearLayoutManager(this);
        adapter = new FavoriteCompanyAdapter(this, list);
        RvFavoriteCompany.setAdapter(adapter);
        RvFavoriteCompany.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();

        runLayoutAnimation(RvFavoriteCompany);
    }

    @OnClick(R.id.Ivback)
    public void onViewClicked()
    {
        if(from_profile == true)
        {
            Intent i_back = new Intent(FavoriteCompanyActivity.this , ProfileDetailActivity.class);
            startActivity(i_back);
            finish();
        }
        else
        {
            Intent i_back = new Intent(FavoriteCompanyActivity.this , MainActivity.class);
            startActivity(i_back);
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if(from_profile == true)
        {
            Intent i_back = new Intent(FavoriteCompanyActivity.this , ProfileDetailActivity.class);
            startActivity(i_back);
            finish();
        }
        else
        {
            Intent i_back = new Intent(FavoriteCompanyActivity.this , MainActivity.class);
            startActivity(i_back);
            finish();
        }
    }

    public class RemoveFromFav extends AsyncTask<Void, Void, Void>
    {
        int c_id= 0 ;
        int pos =0 ;

        public RemoveFromFav(int positon)
        {
            this.pos = positon;

            this.c_id = list.get(positon).getId();

            Log.e("companyid",""+c_id);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd1 = new ProgressDialog(FavoriteCompanyActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            removeFav(c_id, pos);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    public void removeFav(int id, int pos)
    {
        Company objCompany = new Company();
        objCompany.setId(id);
        Call<Response>  call = objInterface.RemoveFavouriteCompany(objCompany);

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
                        if(pd1 != null & pd1.isShowing())
                        {
                            pd1.dismiss();
                        }

                        list.remove(pos);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        Toast.makeText(FavoriteCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd1 != null & pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(FavoriteCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd1 != null & pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(FavoriteCompanyActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd1 != null & pd1.isShowing())
                    {
                        pd1.dismiss();
                    }
                    Toast.makeText(FavoriteCompanyActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd1 != null & pd1.isShowing())
                {
                    pd1.dismiss();
                }
                Toast.makeText(FavoriteCompanyActivity.this , t.toString(), Toast.LENGTH_LONG).show();
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
}
