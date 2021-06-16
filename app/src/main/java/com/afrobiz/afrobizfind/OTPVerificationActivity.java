package com.afrobiz.afrobizfind;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.afrobiz.afrobizfind.ui.mActivity.ForgotpasswordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class OTPVerificationActivity extends AppCompatActivity
{
    String email;

    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.et_1)
    EditText et_1;

    @BindView(R.id.et_2)
    EditText et_2;

    @BindView(R.id.et_3)
    EditText et_3;

    @BindView(R.id.et_4)
    EditText et_4;

    @BindView(R.id.et_5)
    EditText et_5;

    @BindView(R.id.et_6)
    EditText et_6;

    @BindView(R.id.tv_verify)
    TextView tv_verify;

    @BindView(R.id.tv_send_again)
    TextView tv_send_again;

    @BindView(R.id.tv_email)
    TextView tv_email;

    boolean isvisible = false;
    private EditText[] editTexts;

    ApiInterface objInterface;
    ProgressDialog pd;

    String fcmtoken = null;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        ButterKnife.bind(this);

        fcmtoken = new PrefrenceManager(OTPVerificationActivity.this).get_FcmToken();

        objInterface = ApiClient.create_Istance();

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            email = b.getString("email");
            if(email != null)
            {
                tv_email.setText(email);
            }
        }
        editTexts = new EditText[]{et_1, et_2, et_3, et_4, et_5,et_6};

        et_1.addTextChangedListener(new PinTextWatcher(0));
        et_2.addTextChangedListener(new PinTextWatcher(1));
        et_3.addTextChangedListener(new PinTextWatcher(2));
        et_4.addTextChangedListener(new PinTextWatcher(3));
        et_5.addTextChangedListener(new PinTextWatcher(4));
        et_6.addTextChangedListener(new PinTextWatcher(5));

        et_1.setOnKeyListener(new PinOnKeyListener(0));
        et_2.setOnKeyListener(new PinOnKeyListener(1));
        et_3.setOnKeyListener(new PinOnKeyListener(2));
        et_4.setOnKeyListener(new PinOnKeyListener(3));
        et_5.setOnKeyListener(new PinOnKeyListener(4));
        et_6.setOnKeyListener(new PinOnKeyListener(5));


    }
    @OnClick(R.id.tv_send_again)
    public void SendAgain()
    {
        if(ApiClient.isNetworkAvailable(OTPVerificationActivity.this))
        {
            new ForgotPassword(email).execute();
        }
        else
        {
            Toast.makeText(OTPVerificationActivity.this, getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
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
            pd = new ProgressDialog(OTPVerificationActivity.this);
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

                            Toast.makeText(OTPVerificationActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            Intent i_back = new Intent(OTPVerificationActivity.this , OTPVerificationActivity.class);
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
                            Toast.makeText(OTPVerificationActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(OTPVerificationActivity.this , getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(OTPVerificationActivity.this, t.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }



    @OnClick(R.id.tv_verify)
    public void onVerify()
    {
        String et1 = et_1.getText().toString().trim();
        String et2 = et_2.getText().toString().trim();
        String et3 = et_3.getText().toString().trim();
        String et4 = et_4.getText().toString().trim();
        String et5 = et_5.getText().toString().trim();
        String et6 = et_6.getText().toString().trim();

        String otp = et1+et2+et3+et4+et5+et6;
        Log.e("otp",""+otp);

        if(otp != null && otp.length() == 6)
        {
            if(ApiClient.isNetworkAvailable(OTPVerificationActivity.this))
            {
                new VerifyOTP(email, otp,fcmtoken).execute();
            }
            else
            {
                Toast.makeText(OTPVerificationActivity.this, getResources().getString(R.string.no_connection),Toast.LENGTH_SHORT).show();
            }
        }


    }
    public class VerifyOTP extends AsyncTask<Void, Void, Void>
    {
        String email, otp,fcmtoken;

        public VerifyOTP(String email1, String otp1, String fcmtoken1)
        {
            this.email = email1;
            this.otp = otp1;
            this.fcmtoken = fcmtoken1;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(OTPVerificationActivity.this);
            pd.setMessage(getResources().getString(R.string.please_wait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Verify(this.email,this.otp,this.fcmtoken);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void Verify(String email , String OTP, String fcmToken)
    {
        Users objuser = new Users();
        objuser.setEmail(email);
        objuser.setOtp(OTP);
        objuser.setFcmtoken(fcmToken);

        Call<Response> call = objInterface.VerifyOTP(objuser);
        Log.e("call",""+call.request());
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
                        StoreData.isVerified = true;
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Users objusers = response.body().getUser();
                        String token = null;
                        if(objusers != null)
                        {
                            token = objusers.getToken();
                        }

                         new PrefrenceManager(OTPVerificationActivity.this).setOTP_Verified(true);

                        Toast.makeText(OTPVerificationActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i_back = new Intent(OTPVerificationActivity.this , NewPasswordActivity.class);
                        i_back.putExtra("token",token);
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
                        Toast.makeText(OTPVerificationActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(OTPVerificationActivity.this , getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Toast.makeText(OTPVerificationActivity.this, t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @OnClick(R.id.Ivback)
    public void onBack()
    {
        Intent i_back = new Intent(OTPVerificationActivity.this , ForgotpasswordActivity.class);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent i_back = new Intent(OTPVerificationActivity.this , ForgotpasswordActivity.class);
        startActivity(i_back);
        finish();
    }
    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) {
                // isLast is optional
//                Log.e("otp",""+newTypedString);
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP ) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }

}
