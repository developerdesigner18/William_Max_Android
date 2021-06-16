package com.afrobiz.afrobizfind.ui.mActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.favcompany.FavoriteCompanyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileDetailActivity extends AppCompatActivity {

    TextView tv_deleteAccount;
    @BindView(R.id.tvFavorite)
    TextView tvFavorite;

    @BindView(R.id.Ivback)
    ImageView Ivback;

    @BindView(R.id.tv_editprofile)
    TextView tv_editprofile;

    @BindView(R.id.et_firstname)
    EditText et_firstname;

    @BindView(R.id.et_cust_no)
    EditText et_cust_no;

    @BindView(R.id.et_surname)
    EditText et_surname;

    @BindView(R.id.et_city)
    EditText et_city;

    @BindView(R.id.et_first_line_add)
    EditText et_first_line_add;

    @BindView(R.id.et_home_no_name)
    EditText et_home_no_name;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_mobile_no)
    EditText et_mobile_no;

    @BindView(R.id.et_postcode)
    EditText et_postcode;

    @BindView(R.id.et_username)
    EditText et_username;

    Users currentuser;

    String token = null;

    ProgressDialog pd1, pd;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String userName, userEmail, userFirstName, userSurName, userPassword,  userMobile,
            userHouseNumber, userAddress, userCity, userPostCode,
            userCustomerNumber;
    ApiInterface objinterface;

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

        setContentView(R.layout.activity_profile_detail);
        ButterKnife.bind(this);

        et_email.setEnabled(false);
        et_cust_no.setEnabled(false);
        et_username.setEnabled(false);

        get_current_user();
        setEditable(false);

        tv_deleteAccount = findViewById(R.id.tv_deleteAccount);

        tv_deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });
    }
    public void get_current_user()
    {
        currentuser = new PrefrenceManager(ProfileDetailActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            if(token != null)
            {
                objinterface = ApiClient.create_InstanceAuth(token);
            }

            et_cust_no.setText(""+currentuser.getUserNumber());
            et_firstname.setText(currentuser.getFirstName());
            et_surname.setText(currentuser.getSurname());
            et_home_no_name.setText(currentuser.getHomeNumber());
            et_postcode.setText(currentuser.getPostcode());
            et_city.setText(currentuser.getCity());
            et_mobile_no.setText(currentuser.getMobileNumber());
            et_email.setText(currentuser.getEmail());
            et_first_line_add.setText(currentuser.getAddressLine1());
            et_username.setText(currentuser.getUsername());
        }
    }
    private void Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_delete_account, null);
        final EditText etpass = dialogLayout.findViewById(R.id.etPass);
        final TextView tvcancel = dialogLayout.findViewById(R.id.tv_cancel);
        final TextView tv_title = dialogLayout.findViewById(R.id.tv_title);
        final TextView tv_title1 = dialogLayout.findViewById(R.id.tv_title1);
        final TextView tvdeleteAccount = dialogLayout.findViewById(R.id.tv_deleteAccount);
        builder.setView(dialogLayout);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();

        if(alert.getWindow() != null)
        {
            alert.getWindow().setWindowAnimations(R.style.enter_exit_animate);
        }

        tvcancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                etpass.setText("");
                alert.cancel();
            }
        });
        tvdeleteAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String password = etpass.getText().toString().trim();
                if(!TextUtils.isEmpty(password))
                {
                    if(password.equals(currentuser.getPassword()))
                    {
                        tv_title.setText("Your Account Has been Deleted.");

                        etpass.setVisibility(View.GONE);

                        tvcancel.setVisibility(View.GONE);
                        tvdeleteAccount.setVisibility(View.GONE);

                        etpass.setText("");


                        if(ApiClient.isNetworkAvailable(ProfileDetailActivity.this))
                        {
                            if(objinterface != null)
                            {
                                new RemoveUser().execute();
                            }
                        }
                        else
                        {
                            Toast.makeText(ProfileDetailActivity.this, getString(R.string.no_connection) , Toast.LENGTH_LONG).show();
                        }

                        Handler h = new Handler();
                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                alert.cancel();

                                Intent i_start = new Intent(ProfileDetailActivity.this , MainActivity.class);
                                startActivity(i_start);
                                finish();
                            }
                        },2000);

                    }
                    else
                    {
                        tv_title.setText("Incorrect Password");
                        etpass.setText("");
                    }
                }
                else
                {
                    Toast.makeText(ProfileDetailActivity.this, "Please enter password first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.show();
    }
    public class RemoveUser extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd1 = new ProgressDialog(ProfileDetailActivity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getResources().getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            removeUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void removeUser()
    {
        Call<Response> callUser = objinterface.RemoveUser();

        callUser.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }

                        new PrefrenceManager(ProfileDetailActivity.this).logout(false);
                        new PrefrenceManager(ProfileDetailActivity.this).clearValue();

                        Toast.makeText(ProfileDetailActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i_go = new Intent(ProfileDetailActivity.this , LoginActivity.class);
                        startActivity(i_go);
                        finish();
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if (pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(ProfileDetailActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(ProfileDetailActivity.this , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd1 != null && pd1.isShowing())
                {
                    pd1.dismiss();
                }
                Toast.makeText(ProfileDetailActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.tvFavorite)
    public void onViewClicked() {

        Intent intent = new Intent(this, FavoriteCompanyActivity.class);
        intent.putExtra("from_profile",true);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tv_editprofile)
    public void onEditProfile()
    {
        if(tv_editprofile.getText().toString().equals("Edit Profile"))
        {
            setEditable(true);

            tv_editprofile.setText("Save");
        }
        else
        {
            validation();
        }
    }

    @OnClick(R.id.Ivback)
    public void onBack()
    {
        Intent i_back = new Intent(ProfileDetailActivity.this, MainActivity.class);
        startActivity(i_back);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(ProfileDetailActivity.this, MainActivity.class);
        startActivity(i_back);
        finish();
    }

    private void validation() {

        if (et_firstname.getText().toString().equals("")) {

            et_firstname.setError("Please Enter First Name");
            return;
        }
        else
        {
            et_firstname.setError(null);
        }
        if (et_surname.getText().toString().equals("")) {

            et_surname.setError("Please Enter Surname");
            return;
        }
        else
        {
            et_surname.setError(null);
        }

        if (et_mobile_no.getText().toString().equals("")) {

            et_mobile_no.setError("Please Enter Mobile Number");
            return;
        }
        else
        {
            et_mobile_no.setError(null);
        }
        if (et_home_no_name.getText().toString().equals("")) {

            et_home_no_name.setError("Please Enter Home Number Or Name");
            return;
        }
        else
        {
            et_home_no_name.setError(null);
        }
        if (et_first_line_add.getText().toString().equals(""))
        {
            et_first_line_add.setError("Please Enter First Line Address");
            return;
        }
        else
        {
            et_first_line_add.setError(null);
        }
        if (et_city.getText().toString().equals(""))
        {
            et_city.setError("Please Enter City");
            return;
        }
        else
        {
            et_city.setError(null);
        }
        if (et_postcode.getText().toString().equals("")) {

            et_postcode.setError("Please Enter Postcode");
            return;
        }
        else
        {
            et_postcode.setError(null);
        }
//        if (et_username.getText().toString().equals("")) {
//
//            et_username.setError("Please Enter User Name");
//            return;
//        }
//        else
//        {
//            et_username.setError(null);
//        }

        userAddress = et_first_line_add.getText().toString().trim();
        userPostCode = et_postcode.getText().toString().trim();
        userCity = et_city.getText().toString().trim();
        userEmail = et_email.getText().toString().trim();
        userName = et_username.getText().toString().trim();
        userHouseNumber = et_home_no_name.getText().toString().trim();
        userCustomerNumber = et_cust_no.getText().toString().trim();
        userSurName = et_surname.getText().toString().trim();
        userFirstName = et_firstname.getText().toString().trim();
        userMobile =et_mobile_no.getText().toString().trim();
        EdiProfile();
    }

    public void EdiProfile()
    {
        if(ApiClient.isNetworkAvailable(ProfileDetailActivity.this))
        {
            if(objinterface != null)
            {
                new UpdateProfile().execute();
            }
        }
        else
        {
            Toast.makeText(ProfileDetailActivity.this , getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }
    }
    public class UpdateProfile extends AsyncTask<Void ,Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileDetailActivity.this);
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

            objsaveUser.setMobileNumber(userMobile);

            objsaveUser.setHomeNumber(userHouseNumber);
            objsaveUser.setAddressLine1(userAddress);
            objsaveUser.setCity(userCity);
            objsaveUser.setPostcode(userPostCode);



            Call<Response> call = objinterface.SaveUser(objsaveUser);

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
                            Users userobj =  response.body().getUser();

                            if(userobj != null)
                            {
                                String usertname = userobj.getUsername();
                                String useremail = userobj.getEmail();

                                new PrefrenceManager(ProfileDetailActivity.this).save_CurrentUser(userobj);

                                new PrefrenceManager(ProfileDetailActivity.this).saveUserPassEmail(usertname , useremail, currentuser.getToken());
                                new PrefrenceManager(ProfileDetailActivity.this).logout(true);

                                setEditable(false);

                                tv_editprofile.setText("Edit Profile");
                                Log.e("sucess" , ""+response.body().getResult());

                                if(pd != null && pd.isShowing())
                                {
                                    pd.dismiss();
                                }
                                Toast.makeText(ProfileDetailActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
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

//                            if(response.body().getErrors().getEmail() != null)
//                            {
//                                Toast.makeText(ProfileDetailActivity.this , response.body().getErrors().getEmail()., Toast.LENGTH_LONG).show();
//                            }
//                            else
//                            {
                                Toast.makeText(ProfileDetailActivity.this , response.body().getMessage() , Toast.LENGTH_LONG).show();
//                            }

                        }
                    }
                    else
                    {
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(ProfileDetailActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(ProfileDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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

    public void setEditable(boolean edit)
    {
        et_city.setEnabled(edit);
//        et_email.setEnabled(edit);
//        et_password.setEnabled(edit);
        et_postcode.setEnabled(edit);
//        et_username.setEnabled(edit);
        et_first_line_add.setEnabled(edit);
        et_firstname.setEnabled(edit);
        et_home_no_name.setEnabled(edit);
//        et_cust_no.setEnabled(edit);
        et_surname.setEnabled(edit);
        et_mobile_no.setEnabled(edit);
    }
}
