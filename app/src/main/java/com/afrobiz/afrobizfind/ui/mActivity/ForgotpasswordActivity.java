package com.afrobiz.afrobizfind.ui.mActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobiz.afrobizfind.OTPVerificationActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class ForgotpasswordActivity extends AppCompatActivity
{
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.tv_send)
    TextView tv_send;

    @BindView(R.id.et_email)
    EditText et_email;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog pd;
    Users currentuser;
    ApiInterface objInterface;
    String token = null;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgotpassword);

        ButterKnife.bind(this);
//        currentuser = new PrefrenceManager(ForgotpasswordActivity.this).getCurrentuser();
//
//        if(currentuser != null)
//        {
//            token = currentuser.getToken();
//
//            Log.e("token" , ""+token);
//            objInterface  = ApiClient.create_InstanceAuth(token);
//        }

        objInterface = ApiClient.create_Istance();

    }

    @OnClick(R.id.tv_send)
    public void SendCode()
    {
        if (et_email.getText().toString().equals(""))
        {
            et_email.setError("Enter Email");
            return;
        }
        else
        {
            et_email.setError(null);
        }

        if (!et_email.getText().toString().matches(emailPattern))
        {
            et_email.setError("Enter Proper Email");
            return;
        }
        else
        {
            et_email.setError(null);
        }

        if(ApiClient.isNetworkAvailable(ForgotpasswordActivity.this))
        {
            new ForgotPassword(et_email.getText().toString().trim()).execute();
        }
        else
        {
            Toast.makeText(ForgotpasswordActivity.this, getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
        }

    }

    public class ForgotPassword extends AsyncTask<Void, Void, Void>
    {
        String email;

        public ForgotPassword(String email1)
        {
            this.email = email1;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(ForgotpasswordActivity.this);
            pd.setMessage(getResources().getString(R.string.please_wait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            forgetPassword(this.email);
            return null;
        }

        private void forgetPassword(String email)
        {
            Users objuser = new Users();
            objuser.setEmail(email);
            Call<Response> call = objInterface.ForgotPassword(objuser);

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

                            new PrefrenceManager(ForgotpasswordActivity.this).setEmailVerified(email);

                            Toast.makeText(ForgotpasswordActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i_back = new Intent(ForgotpasswordActivity.this , OTPVerificationActivity.class);
                            i_back.putExtra("email",email);
                            startActivity(i_back);
                            finish();
                        }
                        else
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            Toast.makeText(ForgotpasswordActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(ForgotpasswordActivity.this , getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(ForgotpasswordActivity.this, t.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    @OnClick(R.id.Ivback)
    public void onBack()
    {
        Intent i_back = new Intent(ForgotpasswordActivity.this , LoginActivity.class);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent i_back = new Intent(ForgotpasswordActivity.this , LoginActivity.class);
        startActivity(i_back);
        finish();
    }
}
