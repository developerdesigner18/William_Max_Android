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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.PrefrenceManager;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.adapter.CompanyImagesAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyProductListActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.EventImage;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Ticket;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class EventDetailsActivity extends AppCompatActivity
{
    EventImagesAdapter updateAdapter;
    List<EventImage> imglist;
    Event objEvent;
    ImageView Ivback;
    RecyclerView rcv_event_images, rcv_tickets;
    TextView tv_tc, tv_empty_tickets , tv_empty,tv_max_tickets, tv_event_org_con, tv_event_org, tv_tc_available, tv_tc_sold, tv_ticket_amount,
            tv_price, tv_location,  tv_event_dates, tv_event_name, tv_edit;
    TicketInfoAdapter adapter;
    List<Ticket> listTickets = new ArrayList<>();

    ProgressDialog pd4;
    String token = null;
    ApiInterface objInterface;
    Users currentuser;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        tv_empty_tickets = (TextView) findViewById(R.id.tv_empty_tickets);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_max_tickets = (TextView) findViewById(R.id.tv_max_tickets);
        tv_event_org_con = (TextView) findViewById(R.id.tv_event_org_con);
        tv_event_org = (TextView) findViewById(R.id.tv_event_org);
        tv_tc_available = (TextView) findViewById(R.id.tv_tc_available);
        tv_tc_sold = (TextView) findViewById(R.id.tv_tc_sold);
        tv_tc = (TextView) findViewById(R.id.tv_tc);
        tv_ticket_amount = (TextView) findViewById(R.id.tv_ticket_amount);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_event_dates = (TextView) findViewById(R.id.tv_event_dates);
        tv_event_name = (TextView) findViewById(R.id.tv_event_name);
        tv_price = (TextView) findViewById(R.id.tv_price);
//        tv_edit = (TextView) findViewById(R.id.tv_edit);
        rcv_event_images = (RecyclerView) findViewById(R.id.rcv_event_images);
        rcv_tickets = (RecyclerView) findViewById(R.id.rcv_tickets);
        Ivback = (ImageView) findViewById(R.id.Ivback);

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            objEvent = (Event) b.getSerializable("event");
            Log.e("eventid",""+objEvent.getId());
        }
        if(objEvent != null)
        {
            tv_tc.setText(""+objEvent.getTermscondition());
            tv_max_tickets.setText(""+objEvent.getMaxNoTicket());
            tv_location.setText(objEvent.getLocation());
            tv_price.setText(""+objEvent.getPrice());
            tv_event_org_con.setText(objEvent.getContactno());
            tv_event_org.setText(objEvent.getOrganizer());
            tv_event_name.setText(objEvent.getEventname());
            tv_event_dates.setText(objEvent.getDate());

            imglist = objEvent.getEventimages();

            if(imglist != null && imglist.size() > 0)
            {
                rcv_event_images.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.GONE);
                fillUpdatedImage();
            }
            else
            {
                rcv_event_images.setVisibility(View.GONE);
                tv_empty.setVisibility(View.VISIBLE);
            }
        }
        currentuser = new PrefrenceManager(EventDetailsActivity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();

            Log.e("token", "" + token);
            objInterface = ApiClient.create_InstanceAuth(token);

            if (objInterface != null)
            {
                if (ApiClient.isNetworkAvailable(EventDetailsActivity.this))
                {
                    new GetBroughtList(objEvent.getId()).execute();
                }
                else
                {
                    Toast.makeText(EventDetailsActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_go = new Intent(EventDetailsActivity.this, CompanyProductListActivity.class);
                i_go.putExtra("company_id", objEvent.getCompanyId());
                startActivity(i_go);
                finish();
            }
        });

        adapter = new TicketInfoAdapter(EventDetailsActivity.this, listTickets);
        rcv_tickets.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(EventDetailsActivity.this);
        rcv_tickets.setLayoutManager(manager);
    }
    public class GetBroughtList extends AsyncTask<Void, Void, Void>
    {
        int event_id= 0 ;

        public GetBroughtList(int event_id1)
        {
            this.event_id = event_id1;
        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd4 =  new ProgressDialog(EventDetailsActivity.this);
            pd4.setCancelable(false);
            pd4.setMessage(getString(R.string.please_wait));
            pd4.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            getTicketList(event_id);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public void getTicketList(int id)
    {
//        Company objCompany = new Company();
//        objCompany.setId(id);
        Call<Response> call = objInterface.getTicketBroughtList(id);
        Log.e("callGet" , ""+call.request());
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Log.e("getcode" , ""+response.code());

                if(response != null && response.isSuccessful())
                {
                    Log.e("getresponse beauty", new Gson().toJson(response.body()));

                    Log.e("getresponse" , ""+response.body().getResult());

                    if(response.body().getResult() == 1)
                    {
                        listTickets = response.body().getTicketList();

                        if(listTickets !=null && listTickets.size() >0)
                        {
                            Log.e("listTickets",""+listTickets.size());
                            tv_empty.setVisibility(View.GONE);
                            rcv_tickets.setVisibility(View.VISIBLE);

                            adapter = new TicketInfoAdapter(EventDetailsActivity.this, listTickets);
                            rcv_tickets.setAdapter(adapter);
                            LinearLayoutManager manager = new LinearLayoutManager(EventDetailsActivity.this);
                            rcv_tickets.setLayoutManager(manager);
                            adapter.notifyDataSetChanged();

                        }
                        else
                        {
                            tv_empty.setVisibility(View.VISIBLE);
                            rcv_tickets.setVisibility(View.GONE);
                        }
                        if(pd4 != null & pd4.isShowing())
                        {
                            pd4.dismiss();
                        }
                    }
                    else if(response.body().getResult() == 0)
                    {
                        if(pd4 != null & pd4.isShowing())
                        {
                            pd4.dismiss();
                        }
                        Toast.makeText(EventDetailsActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pd4 != null & pd4.isShowing())
                        {
                            pd4.dismiss();
                        }
                        Toast.makeText(EventDetailsActivity.this , response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(pd4 != null & pd4.isShowing())
                    {
                        pd4.dismiss();
                    }
                    Toast.makeText(EventDetailsActivity.this , getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                if(pd4 != null & pd4.isShowing())
                {
                    pd4.dismiss();
                }
                Toast.makeText(EventDetailsActivity.this , t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void fillUpdatedImage()
    {
        if(imglist != null && imglist.size() > 0)
        {
            updateAdapter = new EventImagesAdapter(EventDetailsActivity.this , imglist);
            rcv_event_images.setAdapter(updateAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(EventDetailsActivity.this , LinearLayoutManager.HORIZONTAL , false);
            rcv_event_images.setLayoutManager(manager);
            updateAdapter.notifyDataSetChanged();
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
            rcv_event_images.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_go = new Intent(EventDetailsActivity.this, CompanyProductListActivity.class);
        i_go.putExtra("company_id", objEvent.getCompanyId());
        startActivity(i_go);
        finish();
    }

    public void showTicketInfo(int position)
    {
        Intent i_start = new Intent(EventDetailsActivity.this , TicketInfoActivity.class);
        i_start.putExtra("event", objEvent);
        i_start.putExtra("ticket_id", listTickets.get(position).getId());
        startActivity(i_start);
        finish();
    }
}