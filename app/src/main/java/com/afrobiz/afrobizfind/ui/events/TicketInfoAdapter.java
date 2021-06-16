package com.afrobiz.afrobizfind.ui.events;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Ticket;

import org.w3c.dom.Text;

import java.util.List;

public class TicketInfoAdapter extends RecyclerView.Adapter<TicketInfoAdapter.MyViewHolder>
{
    Context context;
    List<Ticket> listTicket;

    public TicketInfoAdapter(Context context, List<Ticket> listTicket) {
        this.context = context;
        this.listTicket = listTicket;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ticket_info, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_status.setText(listTicket.get(position).getStatus());
        holder.tv_tickets_qty.setText(""+listTicket.get(position).getQuantity());
        holder.tv_used_dates.setText(""+listTicket.get(position).getTicketUsedDate());
        holder.tv_cust_number.setText(""+listTicket.get(position).getCustomerNumber());
            if(position % 2 == 0)
            {
                holder.layout_main.setBackgroundResource(R.drawable.table_even_bg);
            }
            else
            {
                holder.layout_main.setBackgroundResource(R.drawable.table_odd_bg);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ((EventDetailsActivity) context).showTicketInfo(position);

                }
            });
    }

    @Override
    public int getItemCount()
    {
        return listTicket.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_cust_number, tv_status,tv_action,tv_tickets_qty,tv_used_dates;
        LinearLayout layout_main;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_action = (TextView) itemView.findViewById(R.id.tv_action);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_tickets_qty = (TextView) itemView.findViewById(R.id.tv_tickets_qty);
            tv_cust_number = (TextView) itemView.findViewById(R.id.tv_cust_number);
            tv_used_dates = (TextView) itemView.findViewById(R.id.tv_used_dates);
            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);

        }
    }
}
