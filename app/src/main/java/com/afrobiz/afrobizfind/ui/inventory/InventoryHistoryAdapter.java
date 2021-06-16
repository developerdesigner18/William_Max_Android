package com.afrobiz.afrobizfind.ui.inventory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.View_Image;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Inventory;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class InventoryHistoryAdapter extends RecyclerView.Adapter<InventoryHistoryAdapter.MyViewHolder>
{
    Context context;
    List<Inventory> inventoryList;

    public InventoryHistoryAdapter(Context context, List<Inventory> list)
    {
        this.context = context;
        this.inventoryList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inventory_history, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_qty.setText(""+inventoryList.get(position).getQuantity());
        holder.tv_cost.setText(""+inventoryList.get(position).getPrice() +" * "+ inventoryList.get(position).getQuantity()+ " = "+inventoryList.get(position).getTotalprice());
        holder.tv_date.setText(""+inventoryList.get(position).getCreatedAt());
        holder.tv_product_name.setText(String.valueOf(inventoryList.get(position).getProductName()));
        List<Image> listimages = inventoryList.get(position).getImages();

        if(listimages != null && listimages.size() > 0)
        {
            Glide.with(this.context).load(listimages.get(0).getImagepath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.img_product);
        }

        holder.img_product.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(listimages != null && listimages.size() > 0)
                {
                    Intent i_start = new Intent(context , View_Image.class);
                    i_start.putExtra("path", listimages.get(0).getImagepath());
                    context.startActivity(i_start);
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return inventoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_product_name, tv_qty, tv_cost, tv_date;
        ImageView img_close, img_product;
        LinearLayout layout_main;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            img_close = (ImageView) itemView.findViewById(R.id.img_close);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_cost = (TextView) itemView.findViewById(R.id.tv_cost);
            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);
        }
    }
}
