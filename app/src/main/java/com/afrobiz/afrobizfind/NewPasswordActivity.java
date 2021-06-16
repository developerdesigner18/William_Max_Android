package com.afrobiz.afrobizfind;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.afrobiz.afrobizfind.ui.mActivity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class NewPasswordActivity extends AppCompatActivity
{
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.et_new_pass)
    EditText et_new_pass;

    @BindView(R.id.et_confirm_pass)
    EditText et_confirm_pass;

    @BindView(R.id.tv_reset)
    TextView tv_reset;
    ApiInterface objInterface;
    ProgressDialog pd;
    String token = null, email = null;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_password);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            token = b.getString("token");
            email = b.getString("email");
            if(token != null)
            {
                objInterface = ApiClient.create_InstanceAuth(token);
            }
        }
    }

    @OnClick(R.id.tv_reset)
    public void Reset_new_pass()
    {
        if (et_new_pass.getText().toString().equals("")) {
            et_new_pass.setError("Enter password");
            return;
        } else {
            et_new_pass.setError(null);
        }
        if (et_new_pass.getText().toString().length() > 12 || et_new_pass.getText().toString().length() < 8) {
            et_new_pass.setError("Enter password between 8 to 12 charactors");
            return;
        } else {
            et_new_pass.setError(null);
        }
        if (et_confirm_pass.getText().toString().equals("")) {
            et_confirm_pass.setError("Enter password");
            return;
        } else {
            et_confirm_pass.setError(null);
        }

        if (!et_confirm_pass.getText().toString().equals(et_new_pass.getText().toString())) {
            et_confirm_pass.setError("Confirm password is not match with password");
            return;
        } else {
            et_confirm_pass.setError(null);
        }

            if(ApiClient.isNetworkAvailable(NewPasswordActivity.this))
            {
                new ResetPasssword(et_new_pass.getText().toString().trim()).execute();
            }
            else
            {
                Toast.makeText(NewPasswordActivity.this, getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
            }
    }
    public class ResetPasssword extends AsyncTask<Void, Void, Void>
    {
        String password;

        public ResetPasssword(String password1)
        {
            this.password = password1;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(NewPasswordActivity.this);
            pd.setMessage(getResources().getString(R.string.please_wait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            NewPassword(this.password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void NewPassword(String password)
    {
        Users objuser = new Users();
        objuser.setPassword(password);

        Call<Response> call = objInterface.ChangePassword(objuser);
//        Log.e("call",""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
//                Log.e("response555", ""+response.code());

                if(response != null && response.isSuccessful())
                {
//                    Log.e("response beauty", new Gson().toJson(response.body()));

//                    Log.e("response" , ""+response.body().getResult());
                    if(response.body().getResult() == 1)
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }

                        Toast.makeText(NewPasswordActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i_back = new Intent(NewPasswordActivity.this , LoginActivity.class);
//                        i_back.putExtra("email",email);
                        startActivity(i_back);
                        finish();
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(NewPasswordActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(NewPasswordActivity.this , getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(NewPasswordActivity.this, t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @OnClick(R.id.Ivback)
    public void onBack()
    {
        Intent i_back = new Intent(NewPasswordActivity.this , OTPVerificationActivity.class);
        i_back.putExtra("email",email);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent i_back = new Intent(NewPasswordActivity.this , OTPVerificationActivity.class);
        i_back.putExtra("email",email);
        startActivity(i_back);
        finish();

    }
}
