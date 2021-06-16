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
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductProfileAdapter extends RecyclerView.Adapter<ProductProfileAdapter.MyViewHolder>
{
    Context context;
    List<Product> list;

    public ProductProfileAdapter(CompanyProfileActivity companyProfileActivity, List<Product> product_list)
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
        holder.tv_name.setText(list.get(position).getProductName());

        List<Image> listimages = list.get(position).getImages();
        if(listimages != null && listimages.size() >0)
        {
            Glide.with(context).load(listimages.get(0).getImagepath()).into(holder.img_logo);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((CompanyProfileActivity)context).ShowProductsDetails(position);
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
