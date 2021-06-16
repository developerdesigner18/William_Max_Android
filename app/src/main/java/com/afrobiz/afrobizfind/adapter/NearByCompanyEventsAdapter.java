package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.EventImage;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class NearByCompanyEventsAdapter extends RecyclerView.Adapter<NearByCompanyEventsAdapter.MyViewHolder1>
{
    Context context;
    List<Event> eventList;

    public NearByCompanyEventsAdapter(Context context1, List<Event> eventList1)
    {
        this.context = context1;
        this.eventList = eventList1;
    }

    @NonNull
    @Override
    public NearByCompanyEventsAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_products, parent, false);
        NearByCompanyEventsAdapter.MyViewHolder1 viewHolder = new NearByCompanyEventsAdapter.MyViewHolder1(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearByCompanyEventsAdapter.MyViewHolder1 holder, int position)
    {
        holder.tv_name.setText(eventList.get(position).getEventname());
        holder.tv_price.setText(""+eventList.get(position).getPrice());

        List<EventImage> objimages = eventList.get(position).getEventimages();

        if(eventList.get(position).getImagepath() != null )
        {
            Glide.with(context).load(eventList.get(position).getImagepath()).into(holder.img_logo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ((MainActivity) context).ClickNearByCompanyEvents(eventList.get(position).getCompanyId());
        }
    });
    }
    public void filterList1(List<Event> filterdNames)
    {
        this.eventList = filterdNames;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount()
    {
        return eventList.size();
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder
    {
        ImageView img_logo;
        TextView tv_price , tv_name;

        public MyViewHolder1(@NonNull View itemView)
        {
            super(itemView);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
