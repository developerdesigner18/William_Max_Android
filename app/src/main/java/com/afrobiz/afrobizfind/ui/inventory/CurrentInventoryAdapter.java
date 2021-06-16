package com.afrobiz.afrobizfind.ui.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Inventory;
import com.afrobiz.afrobizfind.ui.modal.Product;
import java.util.List;

public class CurrentInventoryAdapter extends RecyclerView.Adapter<CurrentInventoryAdapter.MyViewHolder>
{
    Context context;
    List<Inventory> inventoryList ;

    public CurrentInventoryAdapter(Context context, List<Inventory> inventoryList1)
    {
        this.context = context;
        this.inventoryList = inventoryList1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_current_inventory, parent, false);
        MyViewHolder viewholder = new MyViewHolder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_qty.setText(""+inventoryList.get(position).getQuantity());
        holder.tv_product_name.setText(String.valueOf(inventoryList.get(position).getProductName()
                +" ("+inventoryList.get(position).getProductNumber()+") "));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }

    public void InventoryList(List<Inventory> filterdNames)
    {
        this.inventoryList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return inventoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_product_name, tv_qty;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_product_price);
        }
    }
}
