package com.afrobiz.afrobizfind.ui.mActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afrobiz.afrobizfind.BuildConfig;
import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.ManageStorageActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class SettingActivity extends AppCompatActivity
{
    LinearLayout layout_share_app, layout_rate_app, layout_tc, layout_logout;
    LinearLayout layout_version, layout_units, layout_manage_storage, layout_notification;

    Users currentuser;
    TextView tv_username;

    boolean iskm = true;
    String units = null;

    ApiInterface objInterface;

    ProgressDialog pd;
    ImageView Ivback;
    String token = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        layout_logout = (LinearLayout) findViewById(R.id.layout_logout);
        layout_rate_app = (LinearLayout) findViewById(R.id.layout_rate_app);
        layout_share_app = (LinearLayout) findViewById(R.id.layout_share_app);
        layout_tc = (LinearLayout) findViewById(R.id.layout_tc);

        layout_version = (LinearLayout) findViewById(R.id.layout_version);
        layout_units = (LinearLayout) findViewById(R.id.layout_units);
        layout_manage_storage = (LinearLayout) findViewById(R.id.layout_manage_storage);
        layout_notification = (LinearLayout) findViewById(R.id.layout_notification);

        tv_username = (TextView) findViewById(R.id.tv_username);

        Ivback = (ImageView) findViewById(R.id.Ivback);

        currentuser = new PrefrenceManager(SettingActivity.this).getCurrentuser();

        units = new PrefrenceManager(SettingActivity.this).getUnit();

        if(units.equals("km"))
        {
            iskm = true;
        }
        else
        {
            iskm = false;
        }

        if(currentuser != null)
        {
            tv_username.setText(currentuser.getUsername());

            token = currentuser.getToken();

            Log.e("token" , ""+token);
            objInterface  = ApiClient.create_InstanceAuth(token);
        }

        layout_units.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Dialog d_units = new Dialog(SettingActivity.this);
                d_units.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d_units.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                d_units.setContentView(R.layout.dialog_units);
                ImageView img_ml = (ImageView) d_units.findViewById(R.id.img_ml);
                ImageView img_km = (ImageView) d_units.findViewById(R.id.img_km);

                if(iskm == false)
                {
                    img_km.setImageResource(R.drawable.uncheck);
                    img_ml.setImageResource(R.drawable.check);
                }
                else
                {
                    img_ml.setImageResource(R.drawable.uncheck);
                    img_km.setImageResource(R.drawable.check);
                }
                img_ml.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(iskm == false)
                        {
                            iskm = true;
                            img_ml.setImageResource(R.drawable.uncheck);
                            img_km.setImageResource(R.drawable.check);
                        }
                        else
                        {
                            iskm = false;
                            img_km.setImageResource(R.drawable.uncheck);
                            img_ml.setImageResource(R.drawable.check);
                        }
                        d_units.dismiss();
                        if(iskm == true)
                        {
                            units = "km";
                        }
                        else
                        {
                            units= "miles";
                        }
                        new PrefrenceManager(SettingActivity.this).setUnit(units);
                    }
                });

                img_km.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(iskm == false)
                        {
                            iskm = true;
                            img_ml.setImageResource(R.drawable.uncheck);
                            img_km.setImageResource(R.drawable.check);
                        }
                        else
                        {
                            iskm = false;
                            img_km.setImageResource(R.drawable.uncheck);
                            img_ml.setImageResource(R.drawable.check);
                        }
                        d_units.dismiss();

                        if(iskm == true)
                        {
                            units = "km";
                        }
                        else
                        {
                            units= "miles";
                        }
                        new PrefrenceManager(SettingActivity.this).setUnit(units);
                    }
                });

                d_units.show();
            }
        });

        layout_notification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_edit = new Intent(SettingActivity.this , NotificationList_Activity.class);
                startActivity(i_edit);
                finish();
            }
        });

        layout_manage_storage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_edit = new Intent(SettingActivity.this , ManageStorageActivity.class);
                startActivity(i_edit);
                finish();
            }
        });

        layout_version.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_edit = new Intent(SettingActivity.this , VersionHistoryActivity.class);
                startActivity(i_edit);
                finish();
            }
        });

        layout_share_app.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" +
                            BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                }
                catch(Exception e)
                {
                    //e.toString();
                }
            }
        });

        layout_rate_app.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try
                {
                    startActivity(myAppLinkToMarket);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(SettingActivity.this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        layout_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(objInterface != null)
                {
                    if(ApiClient.isNetworkAvailable(SettingActivity.this))
                    {
                        new Logout().execute();
                    }
                    else
                    {
                        Toast.makeText(SettingActivity.this, getString(R.string.no_connection) , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        layout_tc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_edit = new Intent(SettingActivity.this , TermsConditionActivity.class);
                startActivity(i_edit);
                finish();
            }
        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
//        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class Logout extends AsyncTask<Void , Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(SettingActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            doLogout();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void  doLogout()
    {
        Call<Response> call = objInterface.LogOut();

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }

                        Toast.makeText(SettingActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();

                        new PrefrenceManager(SettingActivity.this).logout(false);
                        new PrefrenceManager(SettingActivity.this).clearValue();

                        Intent i_edit = new Intent(SettingActivity.this , LoginActivity.class);
                        startActivity(i_edit);
                        finish();
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(SettingActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(SettingActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(SettingActivity.this  , t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
