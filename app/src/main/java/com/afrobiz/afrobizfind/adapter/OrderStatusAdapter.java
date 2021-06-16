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
import com.afrobiz.afrobizfind.ui.modal.OrderStatus;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder>
{
    int orderStatusID = 0;
    Context context;
    List<OrderStatus> listOrderStatus;

    public OrderStatusAdapter(Context context, int orderStatusID1, List<OrderStatus> listOrderStatus)
    {
        this.context = context;
        this.orderStatusID = orderStatusID1;
        this.listOrderStatus = listOrderStatus;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_status, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_status.setText(listOrderStatus.get(position).getStatus());
        if(listOrderStatus.get(position).getId() == orderStatusID)
        {
            holder.img_status.setImageResource(R.drawable.check);
        }
        else
        {
            holder.img_status.setImageResource(R.drawable.uncheck);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listOrderStatus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_status;
        ImageView img_status;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            img_status = (ImageView) itemView.findViewById(R.id.img_status);
        }
    }
}
