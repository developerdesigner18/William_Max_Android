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
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class NearByCompanyProductsAdapter extends RecyclerView.Adapter<NearByCompanyProductsAdapter.MyViewHolder1>
{
    Context context;
    List<Product> productList;

    public NearByCompanyProductsAdapter(Context context1, List<Product> productList1)
    {
        this.context = context1;
        this.productList = productList1;
    }

    @NonNull
    @Override
    public NearByCompanyProductsAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_products, parent, false);
        NearByCompanyProductsAdapter.MyViewHolder1 viewHolder = new NearByCompanyProductsAdapter.MyViewHolder1(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearByCompanyProductsAdapter.MyViewHolder1 holder, int position)
    {
        holder.tv_name.setText(productList.get(position).getProductName());
        holder.tv_price.setText(""+productList.get(position).getPrice());

        List<Image> objimages = productList.get(position).getImages();

        if(objimages != null && objimages.size() >0)
        {
            Glide.with(context).load(objimages.get(0).getImagepath()).into(holder.img_logo);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity) context).ClickNearByCompanyProducts(productList.get(position).getCompanyId());
            }
        });
    }
    public void filterList1(List<Product> filterdNames)
    {
        this.productList = filterdNames;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount()
    {
        return productList.size();
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