package com.afrobiz.afrobizfind;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class HelpActivity extends AppCompatActivity
{
    EditText etEmail, etname, et_msg;
    TextView tvSend , tv_help1_link, tv_help2_link, tv_help21_link, tv_help3_link;
    Users currentuser;
    String name, email,message;

    ApiInterface objInterface;
    ProgressDialog pd;

    ImageView Ivback;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etname = (EditText) findViewById(R.id.etname);
        et_msg = (EditText) findViewById(R.id.et_msg);

        tvSend = (TextView) findViewById(R.id.tvSend);

        tv_help1_link = (TextView) findViewById(R.id.tv_help1_link);
        tv_help2_link = (TextView) findViewById(R.id.tv_help2_link);
        tv_help21_link = (TextView) findViewById(R.id.tv_help21_link);
        tv_help3_link = (TextView) findViewById(R.id.tv_help3_link);

        tv_help1_link.setMovementMethod(LinkMovementMethod.getInstance());
        tv_help1_link.setLinkTextColor(Color.BLUE);

        tv_help2_link.setMovementMethod(LinkMovementMethod.getInstance());
        tv_help2_link.setLinkTextColor(Color.BLUE);

        tv_help21_link.setMovementMethod(LinkMovementMethod.getInstance());
        tv_help21_link.setLinkTextColor(Color.BLUE);

        tv_help3_link.setMovementMethod(LinkMovementMethod.getInstance());
        tv_help3_link.setLinkTextColor(Color.BLUE);

        get_current_user();

        objInterface = ApiClient.create_Istance();

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etEmail.getText().toString().equals(""))
                {
                    etEmail.setError("Please enter email id.");
                    return;
                }
                else
                {
                    etEmail.setError(null);
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                {
                    etEmail.setError("Please enter valid email");
                    return;
                }
                else
                {
                    etEmail.setError(null);
                }
                if (et_msg.getText().toString().equals(""))
                {
                    et_msg.setError("Please enter message");
                    return;
                }
                else
                {
                    et_msg.setError(null);
                }

                if (etname.getText().toString().equals(""))
                {
                    etname.setError("Please enter name.");
                    return;
                }
                else
                {
                    etname.setError(null);
                }

                email = etEmail.getText().toString().trim();
                name = etname.getText().toString().trim();
                message = et_msg.getText().toString().trim();

                if(objInterface != null)
                {
                    if(ApiClient.isNetworkAvailable(HelpActivity.this))
                    {
                        new ContactUs().execute();
                    }
                    else
                    {
                        Toast.makeText(HelpActivity.this , getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public class ContactUs extends AsyncTask<Void , Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(HelpActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            callContactUs();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void callContactUs()
    {
        Users objuser = new Users();
        objuser.setFirstName(name);
        objuser.setEmail(email);
        objuser.setMessage(message);

        Call<Response> call = objInterface.ContactUs(objuser);

        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult()  == 1)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(HelpActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();

                        Intent i_go = new Intent(HelpActivity.this , MainActivity.class);
                        startActivity(i_go);
                        finish();
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(HelpActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(HelpActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(HelpActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void get_current_user()
    {
        currentuser = new PrefrenceManager(HelpActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            etEmail.setText(currentuser.getEmail());
            etname.setText(currentuser.getUsername());
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(HelpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
