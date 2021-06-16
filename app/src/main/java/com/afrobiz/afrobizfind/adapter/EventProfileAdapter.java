package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.EventImage;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class EventProfileAdapter extends RecyclerView.Adapter<EventProfileAdapter.MyViewHolder>
{
    Context context;
    List<Event> list;

    public EventProfileAdapter(CompanyProfileActivity companyProfileActivity, List<Event> product_list)
    {
        this.context = companyProfileActivity;
        list = product_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_products, parent, false);
        MyViewHolder viewholder = new MyViewHolder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_price.setText(""+list.get(position).getPrice());
        holder.tv_name.setText(list.get(position).getEventname());

        List<EventImage> listimages = list.get(position).getEventimages();
        if(list.get(position).getImagepath() != null )
        {
            Glide.with(context).load(list.get(position).getImagepath()).into(holder.img_logo);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((CompanyProfileActivity)context).ShowEventDeatils(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name, tv_price;
        ImageView img_logo;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price =(TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
