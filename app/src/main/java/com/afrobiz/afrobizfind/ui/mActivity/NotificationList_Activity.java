package com.afrobiz.afrobizfind.ui.mActivity;

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
import com.afrobiz.afrobizfind.adapter.NotificationAdapter;
import com.afrobiz.afrobizfind.api.ApiClient;
import com.afrobiz.afrobizfind.api.ApiInterface;
import com.afrobiz.afrobizfind.ui.modal.Notification;
import com.afrobiz.afrobizfind.ui.modal.Response;
import com.afrobiz.afrobizfind.ui.modal.Users;
import com.google.gson.Gson;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class NotificationList_Activity extends AppCompatActivity
{
    RecyclerView rcv_notification;
    ImageView Ivback;
    public ApiInterface objInterface;
    Users currentuser;
    String token = null;
    ProgressDialog pd1;
    List<Notification> notificationList;
    NotificationAdapter adapter ;
    TextView tv_empty;
    TextView tv_prev , tv_more, tv_notification_count, tv_page;

    int total_notification =0 , total_pages =0;

    int pagenum = 0, current_count = 1, next_count=10 ;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        Ivback = (ImageView) findViewById(R.id.Ivback);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_more = (TextView) findViewById(R.id.tv_more);
        tv_prev = (TextView) findViewById(R.id.tv_prev);
        tv_notification_count = (TextView) findViewById(R.id.tv_notification_count);
        tv_page = (TextView) findViewById(R.id.tv_page);
        rcv_notification = (RecyclerView) findViewById(R.id.rcv_notification);

        currentuser = new PrefrenceManager(NotificationList_Activity.this).getCurrentuser();

        if(currentuser != null)
        {
            token = currentuser.getToken();
            objInterface  = ApiClient.create_InstanceAuth(token);

            if(objInterface != null)
            {
                if(ApiClient.isNetworkAvailable(NotificationList_Activity.this))
                {
                    pagenum = pagenum+1;

                    tv_page.setText("Page :- "+pagenum);

                    tv_notification_count.setText(""+current_count +" - "+next_count);
                    new GetAll_Notifications_List(pagenum).execute();
                }
                else
                {
                    Toast.makeText(NotificationList_Activity.this , getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                }
            }
        }

        tv_more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(pagenum < total_pages)
                    {
                    if(objInterface != null)
                    {
                        if(ApiClient.isNetworkAvailable(NotificationList_Activity.this))
                        {
                            pagenum = pagenum+1;

                            tv_page.setText("Page :- "+pagenum);

                            if(pagenum == total_pages)
                            {
                                next_count = next_count + 10;
                                current_count = next_count -9;

                                tv_notification_count.setText(""+current_count +" - "+total_notification);
                            }
                            else
                            {
                                tv_page.setText("Page :- "+pagenum);

                                next_count = next_count + 10;
                                current_count = next_count -9;

                                tv_notification_count.setText(""+current_count +" - "+next_count);
                            }
                            new GetAll_Notifications_List(pagenum).execute();
                        }
                        else
                        {
                            Toast.makeText(NotificationList_Activity.this , getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        tv_prev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(pagenum > 1)
                {
                    if(objInterface != null)
                    {
                        if(ApiClient.isNetworkAvailable(NotificationList_Activity.this))
                        {
                            pagenum = pagenum - 1;

                            tv_page.setText("Page :- "+pagenum);

                            next_count = next_count -10;
                            current_count = next_count -9;

                            tv_notification_count.setText(""+current_count +" - "+next_count);
                            new GetAll_Notifications_List(pagenum).execute();
                        }
                        else
                        {
                            Toast.makeText(NotificationList_Activity.this , getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_go = new Intent(NotificationList_Activity.this , SettingActivity.class);
                startActivity(i_go);
                finish();
            }
        });
    }
    public class GetAll_Notifications_List extends AsyncTask<Void , Void , Void>
    {
        int pagenum = 0 ;

        public GetAll_Notifications_List(int pagenum)
        {
            this.pagenum = pagenum;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd1 = new ProgressDialog(NotificationList_Activity.this);
            pd1.setCancelable(false);
            pd1.setMessage(getString(R.string.please_wait));
            pd1.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            get_all_Notifications(this.pagenum);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    private void get_all_Notifications(int pagenum)
    {
        Notification objnoti = new Notification();
        objnoti.setPage(pagenum);

        try
        {
            Call<Response> callNotification = objInterface.GetNotificationList(objnoti);
            callNotification.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {
                    if(response != null && response.isSuccessful())
                    {
                        if (response.body().getResult() == 1)
                        {
                            Log.e("all_notification", "" + new Gson().toJson(response.body()));

                            notificationList = response.body().getNotifications();

                            total_notification = response.body().getTotalnotifications();
                            total_pages = response.body().getTotalPages();

                            if(notificationList != null && notificationList.size() > 0)
                            {
                                Log.e("cnxnvkj" , "");
                                tv_empty.setVisibility(View.GONE);
                                rcv_notification.setVisibility(View.VISIBLE);

                                adapter = new NotificationAdapter(NotificationList_Activity.this , notificationList);
                                rcv_notification.setAdapter(adapter);
                                LinearLayoutManager manager = new LinearLayoutManager(NotificationList_Activity.this);
                                rcv_notification.setLayoutManager(manager);
                            }
                            else
                            {
                                Log.e("null" , "");
                                tv_empty.setVisibility(View.VISIBLE);
                                rcv_notification.setVisibility(View.GONE);
                            }
                            if (pd1 != null && pd1.isShowing())
                            {
                                pd1.dismiss();
                            }
                        }
                        else
                        {
                            if(pd1 != null && pd1.isShowing())
                            {
                                pd1.dismiss();
                            }
                            Log.e("all_notification" , ""+new Gson().toJson(response.body()));
                            Toast.makeText(NotificationList_Activity.this ,response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if(pd1 != null && pd1.isShowing())
                        {
                            pd1.dismiss();
                        }
                        Toast.makeText(NotificationList_Activity.this ,getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    if(pd1 != null && pd1.isShowing())
                    {
                        pd1.dismiss();
                    }
                    Log.e("notifications" , ""+t.getMessage());
                    Toast.makeText(NotificationList_Activity.this , t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e)
        {
            Log.e("exception" , ""+e.toString());
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_go = new Intent(NotificationList_Activity.this , SettingActivity.class);
        startActivity(i_go);
        finish();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }
}
