package com.afrobiz.afrobizfind.ui.inventory;

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
import com.afrobiz.afrobizfind.ui.modal.Product;

import java.util.List;

public class InventorySetupAdapter extends RecyclerView.Adapter<InventorySetupAdapter.MyViewHolder>
{
    Context context;
    List<Product> listProduct ;

    public InventorySetupAdapter(Context context, List<Product> listProduct)
    {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inventory_setup, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        if(position % 2 == 0)
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_even_bg);
        }
        else
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_odd_bg);
        }
        if(listProduct.get(position).getInventory().size() > 0 )
        {
                    holder.tv_last_updated.setText(""+listProduct.get(position).getInventory().get(0).getUpdatedAt());
            holder.tv_qty.setText(""+listProduct.get(position).getInventory().get(0).getQuantity());
        }

        if(listProduct.get(position).getWholesalePrice() != null)
        {
            holder.tv_wholesaleprice.setText(listProduct.get(position).getWholesalePrice());
        }
        else
        {
            holder.tv_wholesaleprice.setText("-");
        }

        holder.tv_retail_price.setText(listProduct.get(position).getPrice());
        holder.tv_number.setText(listProduct.get(position).getProduct_number());
        holder.tv_name.setText(listProduct.get(position).getProductName());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((InventorySetupActivity) context).startDetailctivity(listProduct.get(position));
            }
        });
    }
    
    @Override
    public int getItemCount()
    {
        return listProduct.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name, tv_number, tv_retail_price, tv_qty, tv_wholesaleprice, tv_last_updated;
        LinearLayout layout_main;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_retail_price = (TextView) itemView.findViewById(R.id.tv_retail_price);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
            tv_wholesaleprice = (TextView) itemView.findViewById(R.id.tv_wholesale_price);
            tv_last_updated = (TextView) itemView.findViewById(R.id.tv_last_updated);
            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);
        }
    }
    public void filterList(List<Product> filterdNames)
    {
        this.listProduct = filterdNames;
        notifyDataSetChanged();
    }
}

