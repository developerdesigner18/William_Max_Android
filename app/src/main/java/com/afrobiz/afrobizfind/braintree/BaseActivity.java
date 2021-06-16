package com.afrobiz.afrobizfind.braintree;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;

import com.afrobiz.afrobizfind.BuildConfig;
import com.afrobiz.afrobizfind.DemoApplication;
import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.braintree.internal.SignatureVerificationOverrides;
import com.afrobiz.afrobizfind.braintree.models.ClientToken;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreePaymentResultListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.BinData;
import com.braintreepayments.api.models.BraintreePaymentResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.paypal.android.sdk.onetouch.core.PayPalOneTouchCore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@SuppressWarnings("deprecation")
public abstract class BaseActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback,
        PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener,
        BraintreePaymentResultListener, ActionBar.OnNavigationListener
{
    private static final String EXTRA_AUTHORIZATION = "com.braintreepayments.demo.EXTRA_AUTHORIZATION";
    private static final String EXTRA_CUSTOMER_ID = "com.braintreepayments.demo.EXTRA_CUSTOMER_ID";

    protected String mAuthorization;
    protected String mCustomerId;
    protected BraintreeFragment mBraintreeFragment;

    private boolean mActionBarSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            mAuthorization = savedInstanceState.getString(EXTRA_AUTHORIZATION);
            mCustomerId = savedInstanceState.getString(EXTRA_CUSTOMER_ID);

            Log.e("authoe=rization",""+mAuthorization);
            Log.e("mCustomerId=rization",""+mCustomerId);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!mActionBarSetup)
        {
            setupActionBar();
            mActionBarSetup = true;
        }

        SignatureVerificationOverrides.disableAppSwitchSignatureVerification(
                Settings.isPayPalSignatureVerificationDisabled(this));
        PayPalOneTouchCore.useHardcodedConfig(this, Settings.useHardcodedPayPalConfiguration(this));

        if (BuildConfig.DEBUG && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{ WRITE_EXTERNAL_STORAGE }, 1);
        }
        else
        {
            handleAuthorizationState();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleAuthorizationState();
    }

    private void handleAuthorizationState()
    {
        if (mAuthorization == null ||
                (Settings.useTokenizationKey(this) && !mAuthorization.equals(Settings.getTokenizationKey(this))) ||
                !TextUtils.equals(mCustomerId, Settings.getCustomerId(this)))
        {
            performReset();
        }
        else
        {
            onAuthorizationFetched();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (mAuthorization != null)
        {
            outState.putString(EXTRA_AUTHORIZATION, mAuthorization);
            outState.putString(EXTRA_CUSTOMER_ID, mCustomerId);
        }
    }

    @CallSuper
    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce)
    {
        setProgressBarIndeterminateVisibility(true);

        Log.d(getClass().getSimpleName(), "Payment Method Nonce received: " + paymentMethodNonce.getTypeLabel());
    }

    @CallSuper
    @Override
    public void onBraintreePaymentResult(BraintreePaymentResult result)
    {
        setProgressBarIndeterminateVisibility(true);

        Log.d(getClass().getSimpleName(), "Braintree Payment Result received: " + result.getClass().getSimpleName());
    }
    @CallSuper
    @Override
    public void onCancel(int requestCode)
    {
        setProgressBarIndeterminateVisibility(false);

        Log.d(getClass().getSimpleName(), "Cancel received: " + requestCode);
    }

    @CallSuper
    @Override
    public void onError(Exception error)
    {
        setProgressBarIndeterminateVisibility(false);

        Log.d(getClass().getSimpleName(), "Error received (" + error.getClass() + "): "  + error.getMessage());
        Log.d(getClass().getSimpleName(), error.toString());

        showDialog(BaseActivity.this,"An error occurred (" + error.getClass() + "): " + error.getMessage());
    }

    private void performReset()
    {
        setProgressBarIndeterminateVisibility(true);

        mAuthorization = null;
        mCustomerId = Settings.getCustomerId(this);

        if (mBraintreeFragment != null)
        {
            getSupportFragmentManager().beginTransaction().remove(mBraintreeFragment).commit();
            mBraintreeFragment = null;
        }

        reset();
        fetchAuthorization();
    }

    protected abstract void reset();

    protected abstract void onAuthorizationFetched();

    protected void fetchAuthorization()
    {
        if (mAuthorization != null)
        {
            setProgressBarIndeterminateVisibility(false);
            onAuthorizationFetched();
        }
        String authType = Settings.getAuthorizationType(this);

         if(authType.equals(getString(R.string.client_token)))
        {
            Users currentuser = new PrefrenceManager(BaseActivity.this).getCurrentuser();

            if(currentuser != null)
            {
               String  token = currentuser.getToken();
            Call<ClientToken> call = DemoApplication.getClient_withAuth(token, BaseActivity.this).getClientToken();

            Log.e("call_client_token",""+call.request());

            Log.e("mearchantid",""+   Settings.getMerchantAccountId(this));
            Log.e("customerid",""+   Settings.getCustomerId(this));

            call.enqueue(new Callback<ClientToken>()
            {
                @Override
                public void onResponse(Call<ClientToken> call, Response<ClientToken> response)
                {
                    setProgressBarIndeterminateVisibility(false);
                    if(response != null && response.isSuccessful())
                    {
                        if (TextUtils.isEmpty(response.body().getClientToken()))
                        {
                            showDialog(BaseActivity.this, "Client token was empty");
                        }
                        else
                        {
                            mAuthorization = response.body().getClientToken();
                            onAuthorizationFetched();
                            Log.e("clienttoken",""+mAuthorization);
                        }
                    }
                    else
                    {
                        showDialog(BaseActivity.this, response.message());
                    }
                }
                @Override
                public void onFailure(Call<ClientToken> call, Throwable t)
                {
                    setProgressBarIndeterminateVisibility(false);
                }
            });
        }
        }
    }

    public static void showDialog(Context con, String message)
    {
        new AlertDialog.Builder(con)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    protected void setUpAsBack()
    {
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupActionBar()
    {
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId)
    {
        String env = getResources().getStringArray(R.array.environments)[itemPosition];
        if (!Settings.getEnvironment(this).equals(env))
        {
            Settings.setEnvironment(this, env);
            performReset();
        }
        return true;
    }


    public static String getDisplayString(BinData binData)
    {
        return "Bin Data: \n"  +
                "         - Prepaid: " + binData.getHealthcare() + "\n" +
                "         - Healthcare: " + binData.getHealthcare() + "\n" +
                "         - Debit: " + binData.getDebit() + "\n" +
                "         - Durbin Regulated: " + binData.getDurbinRegulated() + "\n" +
                "         - Commercial: " + binData.getCommercial() + "\n" +
                "         - Payroll: " + binData.getPayroll() + "\n" +
                "         - Issuing Bank: " + binData.getIssuingBank() + "\n" +
                "         - Country of Issuance: " + binData.getCountryOfIssuance() + "\n" +
                "         - Product Id: " + binData.getProductId();
    }
}
