package com.afrobiz.afrobizfind.ui.events;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.bumptech.glide.Glide;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class TicketPurchaseActivity extends AppCompatActivity
{
    ApiInterface apiWithoutAuth, objInterface;
    Users currentuser;
    String token = null;
    int CompanyId = 0, event_id = 0 ;
    ImageView img_event_logo, Ivback;
    Event currentEvent;
    EditText etQty;

    TextView etEventName, etDate, etProductPrice, etLocation, etOrg_Name, etOrg_Number, etTotalPrice, etCustNumb,
            etPaypal;
    TextView tv_proceed;
    ProgressDialog pd;
    int totalQty = 0, totalPrice;
    String price, realPrice, currency, totalPayableAmount;


    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket_purchase);

        etQty = (EditText) findViewById(R.id.etQty);

        tv_proceed = (TextView) findViewById(R.id.tv_proceed);

        etCustNumb = (TextView) findViewById(R.id.etCustNumb);
        etDate = (TextView) findViewById(R.id.etDate);
        etEventName = (TextView) findViewById(R.id.etEventName);
        etLocation = (TextView) findViewById(R.id.etLocation);
        etOrg_Name = (TextView) findViewById(R.id.etOrg_Name);
        etOrg_Number = (TextView) findViewById(R.id.etOrg_Number);
        etPaypal = (TextView) findViewById(R.id.etPaypal);
        etProductPrice = (TextView) findViewById(R.id.etProductPrice);

        etTotalPrice = (TextView) findViewById(R.id.etTotalPrice);

        img_event_logo = (ImageView) findViewById(R.id.img_event_logo);

        Ivback = (ImageView) findViewById(R.id.Ivback);

        currentuser = new PrefrenceManager(TicketPurchaseActivity.this).getCurrentuser();

        if (currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            Log.e("user id", "" + currentuser.getId());
            objInterface = ApiClient.create_InstanceAuth(token);
        }

        Bundle b = getIntent().getExtras();

        if (b != null)
        {
            CompanyId = b.getInt("company_id");
            currentEvent = (Event) b.getSerializable("event");

            CompanyId = currentEvent.getCompanyId();
            etOrg_Number.setText(currentEvent.getContactno());
            etOrg_Name.setText(currentEvent.getOrganizer());
            etEventName.setText(currentEvent.getEventname());
            etDate.setText(currentEvent.getDate());
            etLocation.setText(currentEvent.getLocation());
            etCustNumb.setText(""+currentuser.getUserNumber());
            etProductPrice.setText(""+currentEvent.getPrice());

            price = currentEvent.getPrice();

            if(price.contains(" "))
            {
                String[] priceArr = price.split(" ");
                currency = priceArr[0];
                realPrice = priceArr[1];
                Log.e("price",""+priceArr[0] +"        "+priceArr[1]);
            }
            else
            {
                realPrice = price;
                currency = "";
            }
            String flyerImaepath = currentEvent.getFlyerimage();
            if(flyerImaepath != null)
            {
                Glide.with(TicketPurchaseActivity.this).load(flyerImaepath).into(img_event_logo);
            }
        }
        etQty.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.toString() != null && s.length() > 0)
                {
                    String qtyStr = s.toString();
                    totalQty = Integer.parseInt(qtyStr);

                    totalPrice =  totalQty * Integer.parseInt(realPrice);
                    totalPayableAmount = currency + " "+totalPrice;
                    Log.e("totalPayableAmount",""+totalPayableAmount);
                    etTotalPrice.setText(totalPayableAmount);
                }
                else
                {
                    etTotalPrice.setText("");

                }
            }
        });
        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                onBackPressed();
                Intent i_back = new Intent(TicketPurchaseActivity.this, CompanyProfileActivity.class);
                i_back.putExtra("company_id", currentEvent.getCompanyId());
                startActivity(i_back);
                finish();
            }
        });

        tv_proceed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etTotalPrice.getText().toString().equals(""))
                {
                    etTotalPrice.setError("Enter Event Price");
                    return;
                }
                else
                {
                    etTotalPrice.setError(null);
                }
                if (etQty.getText().toString().equals(""))
                {
                    etQty.setError("Enter Event Quantity");
                    return;
                }
                else
                {
                    etQty.setError(null);
                }
                if (etTotalPrice.getText().toString().equals(""))
                {
                    etTotalPrice.setError("Enter Event Total Price");
                    return;
                }
                else
                {
                    etTotalPrice.setError(null);
                }
                if(ApiClient.isNetworkAvailable(TicketPurchaseActivity.this))
                {
                    new ProceedTicket().execute();
                }
                else
                {
                    Toast.makeText(TicketPurchaseActivity.this , getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public class ProceedTicket extends AsyncTask<Void, Void, Void >
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(TicketPurchaseActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            proceed();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void proceed()
    {
        if(currentuser.getId() != 0)
        {
            String qty = etQty.getText().toString().trim();
            Call<Response> call = objInterface.TicketPurchase(currentuser.getId(), currentEvent.getId(), Integer.parseInt(qty));

            Log.e("Purchase Request",""+call.request());
            call.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {
                    if(response != null && response.isSuccessful())
                    {
                        if(response.body().getResult() == 1)
                        {
                            Toast.makeText(TicketPurchaseActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }

                            Intent i_back = new Intent(TicketPurchaseActivity.this, CompanyProfileActivity.class);
                            i_back.putExtra("company_id", currentEvent.getCompanyId());
                            startActivity(i_back);
                            finish();
//                            onBackPressed();
//                            Intent i_back = new Intent(TicketPurchaseActivity.this, AddEventActivity.class);
//                            i_back.putExtra("update", true);
//                            i_back.putExtra("event", currentEvent);
//                            i_back.putExtra("company_id", currentEvent.getCompanyId());
//                            startActivity(i_back);
//                            finish();
                        }
                        else
                        {
                            if(pd != null && pd.isShowing())
                            {
                                pd.dismiss();
                            }
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
                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd != null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(TicketPurchaseActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(TicketPurchaseActivity.this, CompanyProfileActivity.class);
//                i_back.putExtra("update", true);
//                i_back.putExtra("event", currentEvent);
        i_back.putExtra("company_id", currentEvent.getCompanyId());
        startActivity(i_back);
//        Intent i_back = new Intent(TicketPurchaseActivity.this, AddEventActivity.class);
//        i_back.putExtra("update", true);
//        i_back.putExtra("event", currentEvent);
//        i_back.putExtra("company_id", currentEvent.getCompanyId());
//        startActivity(i_back);
        finish();
    }
}
