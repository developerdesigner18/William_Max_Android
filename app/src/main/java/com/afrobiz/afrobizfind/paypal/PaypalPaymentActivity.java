package com.afrobiz.afrobizfind.paypal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.afrobiz.afrobizfind.DemoApplication;
import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.braintree.BaseActivity;
import com.afrobiz.afrobizfind.braintree.Settings;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.afrobiz.afrobizfind.ui.mActivity.addCompany.AddCompanyActivity;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.DataCollector;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreeResponseListener;
import com.braintreepayments.api.interfaces.ConfigurationListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.Configuration;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.google.gson.Gson;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
import java.util.Arrays;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PaypalPaymentActivity extends BaseActivity implements ConfigurationListener,
        PaymentMethodNonceCreatedListener, BraintreeErrorListener
{
    boolean isClick = false;
    private ImageView mNonceIcon;
    private TextView mNonceString;
    private TextView mNonceDetails;
    private TextView mDeviceData;
    String devicedata = null;
    RelativeLayout toolbar;

    ProgressDialog pd;

    private PaymentMethodNonce mNonce;

    String token = null;
    int com_id = 0;
    Users currentuser;
    ApiInterface objInterface;

    ProgressDialog pd_trans;
    Button btn_pay, btn_pay1, btn_trans;
    EditText et_amount;
    ImageView img_back;

    ProgressDialog pd1;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment);

        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        mNonceIcon = findViewById(R.id.nonce_icon);
        mNonceString = findViewById(R.id.nonce);
        mNonceDetails = findViewById(R.id.nonce_details);
        mDeviceData = findViewById(R.id.device_data);

        img_back = (ImageView) findViewById(R.id.img_back);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay1 = (Button) findViewById(R.id.btn_pay1);
        btn_trans = (Button) findViewById(R.id.btn_trans);
        et_amount = (EditText) findViewById(R.id.et_amount);

        currentuser = new PrefrenceManager(PaypalPaymentActivity.this).getCurrentuser();

        pd1 = new ProgressDialog(PaypalPaymentActivity.this);
        pd1.setCancelable(false);
        pd1.setMessage(getString(R.string.please_wait));

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);
        }

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            com_id = b.getInt("com_id");
        }

        enableButtons(false);

        btn_trans.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createTransaction();
            }
        });
        btn_pay1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Log.e("authorization", ""+mAuthorization);
                    launchSinglePayment(v);
                }
                catch (Exception e)
                {
                    onError(e);
                }
            }
        });
    }
    public void clickBack(View view)
    {
        onBackPressed();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(pd1 != null && pd1.isShowing())
        {
            pd1.dismiss();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(pd1 != null && pd1.isShowing())
        {
            pd1.dismiss();
        }

        isClick = false;
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce)
    {
        super.onPaymentMethodNonceCreated(paymentMethodNonce);

        Parcelable returnedData = null;
        String deviceData = null;

        returnedData = paymentMethodNonce;
        deviceData = devicedata;

        if (returnedData instanceof PaymentMethodNonce)
        {
            displayNonce((PaymentMethodNonce) returnedData, deviceData);
        }
        btn_trans.setEnabled(true);

        btn_trans.setVisibility(VISIBLE);
//        new SavePaymentNonceIndb().execute();
    }

    @Override
    protected void reset()
    {
        clearNonce();
    }

    @Override
    protected void onAuthorizationFetched()
    {
        try
        {
            if (mBraintreeFragment == null && mAuthorization != null)
            {
                mBraintreeFragment = BraintreeFragment.newInstance(PaypalPaymentActivity.this, mAuthorization);
                Log.e("authorization", mAuthorization);
                enableButtons(true);
            }
        }
        catch (InvalidArgumentException e)
        {
            onError(e);
        }
    }

    private void enableButtons(boolean enable)
    {
        btn_pay1.setEnabled(enable);
    }

    @Override
    public void onConfigurationFetched(Configuration configuration)
    {
        if (getIntent().getBooleanExtra(MainActivity.EXTRA_COLLECT_DEVICE_DATA, false)) {
            DataCollector.collectDeviceData(mBraintreeFragment, new BraintreeResponseListener<String>() {
                @Override
                public void onResponse(String deviceData)
                {
                    devicedata = deviceData;
                }
            });
        }
    }

//    public class SavePaymentNonceIndb extends AsyncTask<Void, Void, Void>
//    {
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//            pd = new ProgressDialog(PaypalPaymentActivity.this);
//            pd.setMessage(getString(R.string.please_wait));
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids)
//        {
//            saveInapi();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid)
//        {
//            super.onPostExecute(aVoid);
//        }
//
//        public void saveInapi()
//        {
//            String nonce = mNonce.getNonce().toString();
//
//            Company com = new Company();
//            com.setPaypal_nonce(nonce);
//            com.setId(com_id);
//            Log.e("com_id",""+com_id);
//            Log.e("nonce",""+nonce);
//            Call<Response> call = objInterface.SavePaymentNonce(com);
//            call.enqueue(new Callback<Response>() {
//                @Override
//                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
//                {
//                    Log.e("response555", ""+response.code());
//
//                    if(response != null && response.isSuccessful())
//                    {
//                        if(pd != null && pd.isShowing())
//                        {
//                            pd.dismiss();
//                        }
//                    }
//                    else
//                    {
//                        if(pd != null && pd.isShowing())
//                        {
//                            pd.dismiss();
//                        }
//                        Toast.makeText(PaypalPaymentActivity.this , getResources().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Response> call, Throwable t)
//                {
//                    if(pd != null && pd.isShowing())
//                    {
//                        pd.dismiss();
//                    }
//                    Log.e("messagefailure",""+t.getMessage());
//                    Toast.makeText(PaypalPaymentActivity.this , t.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }
    public void launchSinglePayment(View v)
    {
        if(isClick == false)
        {
            isClick = true;
            pd1.show();

            PayPal.requestOneTimePayment(mBraintreeFragment, getPayPalRequest("6.00"));
        }
        else
        {

        }

    }
    private PayPalRequest getPayPalRequest(@Nullable String amount)
    {
        PayPalRequest request = new PayPalRequest(amount);

        request.displayName(Settings.getPayPalDisplayName(this));
        request.currencyCode("GBP");
        request.landingPageType(PayPalRequest.LANDING_PAGE_TYPE_LOGIN);
        request.intent(PayPalRequest.INTENT_AUTHORIZE);

        if (Settings.isPayPalUseractionCommitEnabled(this)) {
            request.userAction(PayPalRequest.USER_ACTION_COMMIT);
        }

        if (Settings.isPayPalCreditOffered(this)) {
            request.offerCredit(true);
        }
        return request;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    private void displayNonce(PaymentMethodNonce paymentMethodNonce, String deviceData)
    {
        mNonce = paymentMethodNonce;
        Log.d("KANYE", mNonce.getNonce());

        mNonceIcon.setVisibility(VISIBLE);

        mNonceString.setText(getString(R.string.nonce_placeholder, mNonce.getNonce()));
        mNonceString.setVisibility(VISIBLE);

        String details = "";
        details = getDisplayString((PayPalAccountNonce) mNonce);

        mNonceDetails.setText(details);
        mNonceDetails.setVisibility(VISIBLE);

        mDeviceData.setText(getString(R.string.device_data_placeholder, deviceData));
        mDeviceData.setVisibility(VISIBLE);

        btn_trans.setEnabled(true);
    }
    public static String getDisplayString(PayPalAccountNonce nonce)
    {
        return "First name: " + nonce.getFirstName() + "\n" +
                "Last name: " + nonce.getLastName() + "\n" +
                "Email: " + nonce.getEmail() + "\n" +
                "Phone: " + nonce.getPhone() + "\n" +
                "Payer id: " + nonce.getPayerId() + "\n" +
                "Client metadata id: " + nonce.getClientMetadataId() + "\n" +
                "Billing address: " + formatAddress(nonce.getBillingAddress()) + "\n" +
                "Shipping address: " + formatAddress(nonce.getShippingAddress());
    }
    private static String formatAddress(PostalAddress address)
    {
        String addressString = "";
        List<String> addresses = Arrays.asList(
                address.getRecipientName(),
                address.getStreetAddress(),
                address.getExtendedAddress(),
                address.getLocality(),
                address.getRegion(),
                address.getPostalCode(),
                address.getCountryCodeAlpha2()
        );

        for (String line : addresses) {
            if (line == null) {
                addressString += "null";
            } else {
                addressString += line;
            }
            addressString += " ";
        }
        return addressString;
    }
    public class createTransaction extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd_trans = new ProgressDialog(PaypalPaymentActivity.this);
            pd_trans.setMessage(getString(R.string.please_wait));
            pd_trans.setCancelable(false);
            pd_trans.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            sendNonceToServer(mNonce);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void createTransaction()
    {
        if(ApiClient.isNetworkAvailable(PaypalPaymentActivity.this))
        {
            new createTransaction().execute();
        }
        else
        {
            Toast.makeText(PaypalPaymentActivity.this , getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }
    private void sendNonceToServer(PaymentMethodNonce nonce)
    {
        Users currentuser = new PrefrenceManager(PaypalPaymentActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            String token = currentuser.getToken();

            Call<com.afrobiz.afrobizfind.ui.modal.Response> call = DemoApplication.getClient_withAuth(token,
                    PaypalPaymentActivity.this).
                    CreateTransaction(nonce.getNonce(), com_id);

            Log.e("create Transaction", "" + call.request());

            call.enqueue(new Callback<com.afrobiz.afrobizfind.ui.modal.Response>() {
                @Override
                public void onResponse(Call<com.afrobiz.afrobizfind.ui.modal.Response> call, retrofit2.Response<Response> response)
                {
                    if (response != null && response.isSuccessful())
                    {
                        Log.e("response beauty", new Gson().toJson(response.body()));

                        Log.e("response", "" + response.body().getResult());

                        if (response.body().getResult() == 1)
                        {
                            btn_trans.setEnabled(false);

                            clearNonce();

                            Toast.makeText(PaypalPaymentActivity.this, "Transaction Completed",
                                    Toast.LENGTH_SHORT).show();

                            if(pd1 != null && pd1.isShowing())
                            {
                                pd1.dismiss();
                            }

                            Intent i_back = new Intent(PaypalPaymentActivity.this , AddCompanyActivity.class);
                            i_back.putExtra("update" , true);
                            i_back.putExtra("company_id", com_id);
                            startActivity(i_back);
                            finish();

                            isClick = false;

//                            onBackPressed();

                            if(pd_trans != null && pd_trans.isShowing())
                            {
                                pd_trans.dismiss();
                            }
                        }
                        else
                        {
                            if(pd_trans != null && pd_trans.isShowing())
                            {
                                pd_trans.dismiss();
                            }
                            Toast.makeText(PaypalPaymentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if(pd_trans != null && pd_trans.isShowing())
                        {
                            pd_trans.dismiss();
                        }
                        Toast.makeText(PaypalPaymentActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onFailure(Call<com.afrobiz.afrobizfind.ui.modal.Response> call, Throwable t)
                {
                    if(pd_trans != null && pd_trans.isShowing())
                    {
                        pd_trans.dismiss();
                    }
                    Toast.makeText(PaypalPaymentActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void clearNonce()
    {
        mNonceIcon.setVisibility(GONE);
        mNonceString.setVisibility(GONE);
        mNonceDetails.setVisibility(GONE);
        mDeviceData.setVisibility(GONE);
        btn_trans.setEnabled(false);
    }
}
