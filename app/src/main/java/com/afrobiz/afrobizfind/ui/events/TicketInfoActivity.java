package com.afrobiz.afrobizfind.ui.events;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Ticket;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class TicketInfoActivity extends AppCompatActivity
{
    String token;
    ImageView Ivback;
    TextView tv_cust_no, tv_email,tv_price,tv_total_price,tv_qty,tv_payment_method,tv_refund,tv_cust_name, tv_date_purchase,
            tv_status,tv_last_updated;
    Event objEvent;
    int ticket_id = 0 ;
    ProgressDialog pd;
    ApiInterface  objInterface;
    Users currentuser;
    Ticket currentTicket;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket_info);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        tv_cust_no = (TextView) findViewById(R.id.tv_cust_no);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_qty = (TextView) findViewById(R.id.tv_qty);
        tv_payment_method = (TextView) findViewById(R.id.tv_payment_method);
        tv_refund = (TextView) findViewById(R.id.tv_refund);
        tv_cust_name = (TextView) findViewById(R.id.tv_cust_name);
        tv_date_purchase = (TextView) findViewById(R.id.tv_date_purchase);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_last_updated = (TextView) findViewById(R.id.tv_last_updated);

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            objEvent = (Event) b.getSerializable("event");
            ticket_id =  b.getInt("ticket_id");
        }
        currentuser = new PrefrenceManager(TicketInfoActivity.this).getCurrentuser();

        if (currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            Log.e("cust num", "" + currentuser.getCustomer_number());
            objInterface = ApiClient.create_InstanceAuth(token);

            new TicketInfoClass().execute();


        }
        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_back = new Intent(TicketInfoActivity.this, EventDetailsActivity.class);
                i_back.putExtra("event", objEvent);
                startActivity(i_back);
                finish();
            }
        });
        
    }
    public class TicketInfoClass extends AsyncTask<Void, Void, Void >
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(TicketInfoActivity.this);
            pd.setCancelable(false);
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            ticketInfo();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void ticketInfo()
    {
        Call<Response> call = objInterface.getTicketInfo(ticket_id);
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                if(response != null && response.isSuccessful())
                {
                    if(response.body().getResult() == 1)
                    {
                        Log.e("Ticket response", "" + new Gson().toJson(response.body()));
                        if(pd != null && pd.isShowing())
                        {
                            pd.dismiss();
                        }

                        currentTicket = response.body().getTicketInfo();

                        tv_cust_name.setText(currentTicket.getFirstName() + " "+currentTicket.getSurname());
                        tv_cust_no.setText(""+currentTicket.getCustomerNumber());

                        tv_email.setText(currentTicket.getEmail());
                        tv_price.setText(""+currentTicket.getPrice());
                        tv_total_price.setText(""+currentTicket.getTotalPrice());
                        tv_qty.setText(""+currentTicket.getQuantity());
                        tv_date_purchase.setText(currentTicket.getPurchasedDate());
                        tv_last_updated.setText(currentTicket.getLastUpdated());
                        tv_status.setText(currentTicket.getStatus());
                        tv_payment_method.setText(currentTicket.getPaymentMethod());
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
                Toast.makeText(TicketInfoActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_back = new Intent(TicketInfoActivity.this, EventDetailsActivity.class);
        i_back.putExtra("event", objEvent);
        startActivity(i_back);
        finish();
    }
}
