package com.afrobiz.afrobizfind.ui.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.afrobiz.afrobizfind.OTPVerificationActivity;
import com.afrobiz.afrobizfind.ui.events.TicketPurchaseActivity;
import com.google.gson.Gson;
import com.afrobiz.afrobizfind.MainActivity;
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

public class LoginActivity extends AppCompatActivity {


    ProgressDialog pd;

    @BindView(R.id.tvSignin)
    TextView tvSignin;
    @BindView(R.id.tvForgotpass)
    TextView tvForgotpass;

    @BindView(R.id.tvVerified)
    TextView tvVerified;

    @BindView(R.id.tvSignup)
    TextView tvSignup;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.IvBack)
    ImageView IvBack;

    ApiInterface objInterface;

    String  email,  password;

//    public static boolean isLogin = false;

    private static final String TAG = "LoginActivity";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Users currentUser;
    String fcmToken;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        currentUser =  new PrefrenceManager(LoginActivity.this).getCurrentuser();
//        String fcmtoken = currentUser.getFcmtoken();

        fcmToken = new PrefrenceManager(LoginActivity.this) .get_FcmToken();

        Log.e("fcm_token" , ""+fcmToken);

        objInterface = ApiClient.create_Istance();

        boolean isOTPVerified = new PrefrenceManager(LoginActivity.this).getOTP_Verified();
        String verifiedEmail = new PrefrenceManager(LoginActivity.this).getEmailVerified();
        if(verifiedEmail != null && isOTPVerified == false)
        {
            tvVerified.setVisibility(View.VISIBLE);
        }
        else
        {
            tvVerified.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.IvBack)
    public void onBack()
    {
        onBackPressed();
    }

    @OnClick(R.id.tvVerified)
    public void onVerified()
    {
        String verifiedemail = new PrefrenceManager(LoginActivity.this).getEmailVerified();
        Intent i_back = new Intent(LoginActivity.this , OTPVerificationActivity.class);
        i_back.putExtra("email",verifiedemail);
        startActivity(i_back);
        finish();
    }

    @OnClick({R.id.tvSignin, R.id.tvForgotpass, R.id.tvSignup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSignin:
                validation();
                break;
            case R.id.tvForgotpass:
                Intent intent = new Intent(LoginActivity.this, ForgotpasswordActivity.class);
                startActivity(intent);

                break;
            case R.id.tvSignup:
                Intent intent1 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent1);
                break;
        }
    }


    private void validation() {

        if (etEmail.getText().toString().equals("")) {

            etEmail.setError("Please Enter Email");
            return;
        } else {
            etEmail.setError(null);
        }
        if (!etEmail.getText().toString().matches(emailPattern)) {
            etEmail.setError("Enter Proper Email");
            return;
        } else {
            etEmail.setError(null);
        }
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError("Please Enter Password");
            return;
        } else {
            etPassword.setError(null);
        }
        if (etPassword.getText().toString().length() > 12 || etPassword.getText().toString().length() < 8) {
            etPassword.setError("Enter password between 8 to 12 charactors");
            return;
        } else {
            etPassword.setError(null);
        }
//        username = etEmail.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        login(email , password, fcmToken);
    }

    private void login(String email, String password, String fcmtoken1)
    {
        if(ApiClient.isNetworkAvailable(LoginActivity.this))
        {
            if(objInterface != null)
            {
                new LoginAsnc(email, password,fcmtoken1).execute();
            }
        }
        else
        {
            Toast.makeText(LoginActivity.this , getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }
    }
    public class LoginAsnc extends AsyncTask<Void ,Void , Void>
    {
        String email, password, fcmToken ;

        public LoginAsnc(String email, String password, String fcmToken1)
        {
//            this.username = username;
            this.email = email;
            this.password = password;
            this.fcmToken = fcmToken1;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Users objsaveUser = new Users();

            objsaveUser.setEmail(this.email);
            objsaveUser.setPassword(this.password);


//            String fctoken = new PrefrenceManager(LoginActivity.this).get_FcmToken();

            objsaveUser.setFcmtoken(this.fcmToken);

            Call<Response> call =   objInterface.Do_Login(objsaveUser);

            Log.e("call" , ""+call.request());

            Log.e("details" ,  new Gson().toJson(objsaveUser));

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

                        if(response.body().getResult()  == 1)
                        {
//                            String token = response.body().getToken();
                            Users userobj =  response.body().getData().getUser();

                            if(userobj != null)
                            {
                                String token = userobj.getToken();

                                userobj.setToken(token);

                                Toast.makeText(LoginActivity.this, "Successfull Login", Toast.LENGTH_SHORT).show();

                                new PrefrenceManager(LoginActivity.this).save_CurrentUser(userobj);

                                new PrefrenceManager(LoginActivity.this).saveUserPassEmail(email , password, token);
                                new PrefrenceManager(LoginActivity.this).logout(true);

                                Users currentuser = new PrefrenceManager(LoginActivity.this).getCurrentuser();
                                Log.e("userid",""+currentuser.getId());

                                Intent i_main = new Intent(LoginActivity.this , MainActivity.class);
                                i_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i_main);
                                finish();
                                if(pd != null && pd.isShowing())
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
                                Toast.makeText(LoginActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(LoginActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<com.afrobiz.afrobizfind.ui.modal.Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
}
