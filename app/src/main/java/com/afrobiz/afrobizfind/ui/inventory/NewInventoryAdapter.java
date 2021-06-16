package com.afrobiz.afrobizfind.ui.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Inventory;
import com.afrobiz.afrobizfind.ui.modal.Product;

import java.util.List;

public class NewInventoryAdapter extends RecyclerView.Adapter<NewInventoryAdapter.MyViewHolder>
{
    Context context;
    List<Inventory> list;

    public NewInventoryAdapter(Context context, List<Inventory> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_new_inventory, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_product_name.setText(list.get(position).getProductName());

        holder.tv_qty.setText(""+list.get(position).getQuantity());

        if(position % 2 == 0)
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_even_bg);
        }
        else
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_odd_bg);
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_product_name, tv_qty;
        ImageView img_close;
        LinearLayout layout_main;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            img_close = (ImageView) itemView.findViewById(R.id.img_close);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);
        }
    }
}
