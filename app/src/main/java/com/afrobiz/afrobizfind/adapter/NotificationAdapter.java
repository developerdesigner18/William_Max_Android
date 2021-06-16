package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Notification;
import com.afrobiz.afrobizfind.ui.category.CategoryDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>
{
    Context context;
    List<Notification> notificationlist = new ArrayList<>();

    public NotificationAdapter(Context activity, List<Notification> notificationlist1)
    {
        this.context = activity;
        this.notificationlist = notificationlist1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification , parent , false);
        MyViewHolder viewholder = new MyViewHolder(v);
        return viewholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        holder.tv_title.setText(notificationlist.get(position).getTitle());
        holder.tv_time.setText(notificationlist.get(position).getCreatedAt());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context , CategoryDetailActivity.class);
                intent.putExtra("categoryid" , 0);
                context.startActivity(intent);

//                Intent intent = new Intent(context, CompanyProfileActivity.class);
//                intent.putExtra("company_id", notificationlist.get(position).getCompanyId());
//                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return notificationlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout layout_main;
        TextView tv_title , tv_subtitle, tv_time;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
