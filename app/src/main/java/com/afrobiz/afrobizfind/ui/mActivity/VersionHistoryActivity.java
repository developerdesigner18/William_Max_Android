package com.afrobiz.afrobizfind.ui.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.adapter.VersionAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Version;
import com.google.gson.Gson;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class VersionHistoryActivity extends AppCompatActivity
{
    RecyclerView rcv_version;
    ImageView Ivback;
    VersionAdapter adapter;
    List<Version> versionlist;
    ApiInterface apiWithoutAuth;
    ProgressDialog pd;
    TextView tv_empty;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_version_history);

        rcv_version = (RecyclerView) findViewById(R.id.rcv_version);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        tv_empty = (TextView) findViewById(R.id.tv_empty);

        apiWithoutAuth = ApiClient.create_Istance();

        if(apiWithoutAuth != null)
        {
            if (ApiClient.isNetworkAvailable(VersionHistoryActivity.this))
            {
                new GetVersionHistory().execute();
            }
            else
            {
                Toast.makeText(VersionHistoryActivity.this , getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
            }
        }
        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(VersionHistoryActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public class  GetVersionHistory extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(VersionHistoryActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getVersionList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getVersionList()
    {
        Call<Response> call = apiWithoutAuth.GetVersionHistory();
        Log.e("call_version",""+call.request());

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    Log.e("response version", new Gson().toJson(response.body()));

                    if(response.body().getResult() == 1)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        versionlist = response.body().getVersions();
                        if(versionlist != null)
                        {
                            rcv_version.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);

                            adapter = new VersionAdapter(VersionHistoryActivity.this , versionlist);
                            rcv_version.setAdapter(adapter);
                            LinearLayoutManager manager = new LinearLayoutManager(VersionHistoryActivity.this);
                            rcv_version.setLayoutManager(manager);
                        }
                        else
                        {
                            rcv_version.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(VersionHistoryActivity.this , ""+response.body().getMessage() , Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(VersionHistoryActivity.this , getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(VersionHistoryActivity.this , ""+t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(VersionHistoryActivity.this, SettingActivity.class);
        startActivity(intent);
        finish();
    }
}
