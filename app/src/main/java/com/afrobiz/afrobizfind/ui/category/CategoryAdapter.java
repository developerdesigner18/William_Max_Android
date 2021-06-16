package com.afrobiz.afrobizfind.ui.category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Category;

import java.io.Serializable;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>
{
    Context context;
    List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category , parent , false);
        MyViewHolder viewholder = new MyViewHolder(v);
        return  viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_name.setText(categoryList.get(position).getName());

        Glide.with(context).load(categoryList.get(position).getImagepath()).into(holder.img_icon);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_cat = new Intent(context , CategoryDetailActivity.class);
                i_cat.putExtra("category" , categoryList.get(position));
                i_cat.putExtra("list_category" , (Serializable) categoryList);
                context.startActivity(i_cat);
                ((CategoryActivity)context).finish();
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return categoryList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_icon;
        TextView tv_name;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name  = (TextView) itemView.findViewById(R.id.tv_name);
            img_icon = (ImageView) itemView.findViewById(R.id.img_icon);

        }
    }
}
