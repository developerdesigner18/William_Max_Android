package com.afrobiz.afrobizfind.ui.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
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
import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity {

    ProgressDialog pd;

    @BindView(R.id.etFname)
    EditText etFname;
    @BindView(R.id.etsurname)
    EditText etsurname;
    @BindView(R.id.et_home_no_name)
    EditText et_home_no_name;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etaddress1)
    EditText etaddress1;
    @BindView(R.id.etcity)
    EditText etcity;
    @BindView(R.id.etpostcode)
    EditText etpostcode;
//    @BindView(R.id.etusername)
//    EditText etusername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.tvSignup)
    TextView tvSignup;
    @BindView(R.id.Ivback)
    ImageView Ivback;
    String fcmtoken;

    @BindView(R.id.tv_username)
    TextView tv_username;

    String userName, userEmail, userFirstName, userSurName, userPassword,  userMobile,
             userHomeNumberName , userAddress, userCity, userPostCode,
            userCustomerNumber;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Retrofit retrofit;
    ApiInterface objinterface;

//    @BindView(R.id.email_chip)
//    Chip email_chip;
//
//    @BindView(R.id.username_chip)
//    Chip username_chip;

//    @BindView(R.id.chipGroup)
//    ChipGroup chipGroup;

//    @BindView(R.id.textInputLayout)
//    TextInputLayout textInputLayout;

//    @BindView(R.id.textInputEditText)
//    TextInputEditText textInputEditText;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        fcmtoken = new PrefrenceManager(SignupActivity.this).get_FcmToken();

        objinterface = ApiClient.create_Istance();

        etEmail.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                tv_username.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

//        textInputEditText.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//                String value = s.toString().trim();
//                if(value.length() > 1 && value.endsWith(","))
//                {
//                    String chiptext = value.substring(0, value.length()-1);
//                    Chip newchip = getChip(chipGroup, chiptext);
//                    chipGroup.addView(newchip);
//                    s.clear();
//
//                    if(chipGroup.getChildCount() == 2)
//                    {
//                        textInputEditText.setVisibility(View.GONE);
//                    }
//                    else
//                    {
////                        textInputEditText.setFocusable(true);
////                        textInputEditText.requestFocus();
//                        textInputEditText.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//        });

    }

//    private Chip getChip(final ChipGroup entryChipGroup, String text) {
//        final Chip chip = new Chip(this);
//        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.my_chip));
//        int paddingDp = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, 10,
//                getResources().getDisplayMetrics()
//        );
//        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
//        chip.setText(text);
////        chip.setOnEditorActionListener(new TextView.OnEditorActionListener()
////        {
////            @Override
////            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
////            {
////                v.setText();
////                return false;
////            }
////        });
//        chip.setOnCloseIconClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                entryChipGroup.removeView(chip);
//
//                if(chipGroup.getChildCount() == 2)
//                {
//                    textInputEditText.setVisibility(View.GONE);
//                }
//                else
//                {
////                    textInputEditText.requestFocus();
////                    textInputEditText.setFocusable(true);
//                    textInputEditText.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        return chip;
//    }
    @OnClick(R.id.Ivback)
    public void OnBack()
    {
        onBackPressed();
//        Intent i_main = new Intent(SignupActivity.this , MainActivity.class);
//        startActivity(i_main);
//        finish();
    }

    @OnClick(R.id.tvSignup)
    public void onViewClicked() {
        validation();
    }

    private void validation()
    {
        if (etFname.getText().toString().equals(""))
        {
            etFname.setError("Enter First Name");
            return;
        }
        else
        {
            etFname.setError(null);
        }

        if (etsurname.getText().toString().equals(""))
        {
            etsurname.setError("Enter Surname");
            return;
        }
        else
        {
            etsurname.setError(null);
        }

        if (etEmail.getText().toString().equals(""))
        {
            etEmail.setError("Enter Email");
            return;
        }
        else
        {
            etEmail.setError(null);
        }
        if (!etEmail.getText().toString().matches(emailPattern))
        {
            etEmail.setError("Enter Proper Email");
            return;
        }
        else
        {
            etEmail.setError(null);
        }
        if (etMobile.getText().toString().equals(""))
        {
            etMobile.setError("Enter Mobile Number");
            return;
        }
        else
        {
            etMobile.setError(null);
        }

        if (et_home_no_name.getText().toString().equals(""))
        {
            et_home_no_name.setError("Enter Home Number/Name");
            return;
        }
        else
        {
            et_home_no_name.setError(null);
        }
        if (etaddress1.getText().toString().equals(""))
        {
            etaddress1.setError("Enter Address");
            return;
        }
        else
        {
            etaddress1.setError(null);
        }

        if (etcity.getText().toString().equals(""))
        {
            etcity.setError("Enter City");
            return;
        }
        else
        {
            etcity.setError(null);
        }

        if (etpostcode.getText().toString().equals(""))
        {
            etpostcode.setError("Enter Postcode");
            return;
        }
        else
        {
            etpostcode.setError(null);
        }

//        if (etusername.getText().toString().equals(""))
//        {
//            etusername.setError("Enter username");
//            return;
//        }
//        else
//        {
//            etusername.setError(null);
//        }

        if (etPassword.getText().toString().equals(""))
        {
            etPassword.setError("Enter password");
            return;
        }
        else
        {
            etPassword.setError(null);
        }

        if (etPassword.getText().toString().length() > 12 || etPassword.getText().toString().length() < 8)
        {
            etPassword.setError("Enter password between 8 to 12 charactors");
            return;
        }
        else
        {
            etPassword.setError(null);
        }

        if (etConfirmPassword.getText().toString().equals(""))
        {
            etConfirmPassword.setError("Enter password");
            return;
        }
        else
        {
            etConfirmPassword.setError(null);
        }

        if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString()))
        {
            etConfirmPassword.setError("Confirm password is not match with password");
            return;
        }
        else
        {
            etConfirmPassword.setError(null);
        }

        userHomeNumberName = et_home_no_name.getText().toString().trim();
        userAddress = etaddress1.getText().toString().trim();
        userPostCode = etpostcode.getText().toString().trim();
        userCity = etcity.getText().toString().trim();
        userEmail = etEmail.getText().toString().trim();
        userPassword= etPassword.getText().toString().trim();
        userName = etEmail.getText().toString().trim();
        userCustomerNumber = null;
        userSurName = etsurname.getText().toString().trim();
        userFirstName = etFname.getText().toString().trim();
        userMobile =etMobile.getText().toString().trim();

        signup();
    }

    private void signup()
    {
        if(ApiClient.isNetworkAvailable(SignupActivity.this))
        {
            if(objinterface != null)
            {
                new Register().execute();
            }
        }
        else
        {
            Toast.makeText(SignupActivity.this , getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }
    }
    public class Register extends AsyncTask<Void ,Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(SignupActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Users objsaveUser = new Users();
            objsaveUser.setFirstName(userFirstName);
            objsaveUser.setSurname(userSurName);
            objsaveUser.setUsername(userName);

            objsaveUser.setEmail(userEmail);
            objsaveUser.setPassword(userPassword);

            objsaveUser.setMobileNumber(userMobile);

            objsaveUser.setHomeNumber(userHomeNumberName);
            objsaveUser.setAddressLine1(userAddress);
            objsaveUser.setCity(userCity);
            objsaveUser.setPostcode(userPostCode);
            objsaveUser.setFcmtoken(fcmtoken);

            Call<Response> call = objinterface.Do_SignIn(objsaveUser);

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
                            String token = response.body().getToken();
                            Users userobj =  response.body().getUser();
                            if(userobj != null)
                            {
                                new PrefrenceManager(SignupActivity.this).save_CurrentUser(userobj);
                                Toast.makeText(SignupActivity.this, "Successfull signup", Toast.LENGTH_SHORT).show();

                                Intent i_start = new Intent(SignupActivity.this , LoginActivity.class);
                                i_start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i_start);
                                finish();

                                Log.e("sucess" , ""+response.body().getResult());

                                if(pd != null && pd.isShowing())
                                {
                                    pd.dismiss();
                                }

//                                if(userobj.getEmailVerifiedAt() == null)
//                                {
//                                    Toast.makeText(SignupActivity.this , "Bitte bestätige deine E-Mailadresse. Wir haben dir einen Aktivierungslink zugemailt." , Toast.LENGTH_LONG).show();
//                                }

//                                showEditProfileDialog();

//                                Intent i_start = new Intent(SignupActivity.this , LoginActivity.class);
//                                startActivity(i_start);
//                                finish();

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

                            if(response.body().getErrors().getEmail() != null)
                            {
                                Toast.makeText(SignupActivity.this , response.body().getErrors().getEmail().get(0), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(SignupActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        OnBack();
    }
}
