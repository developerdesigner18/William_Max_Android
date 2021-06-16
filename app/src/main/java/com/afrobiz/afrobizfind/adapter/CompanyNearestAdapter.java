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
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.bumptech.glide.Glide;

import java.util.List;

public class CompanyNearestAdapter extends RecyclerView.Adapter<CompanyNearestAdapter.MyViewHolder>
{
    Context context;
    List<Company> companyList;

    public CompanyNearestAdapter(Context context, List<Company> companyname)
    {
        this.context = context;
        this.companyList = companyname;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_company_nearest, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tv_name.setText(companyList.get(position).getCompanyName());
        Glide.with(context).load(companyList.get(position).getImagepath()).into(holder.img_logo);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity) context).ClickNearCompany(companyList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return companyList.size() ;
    }

    public void filterList(List<Company> filterdNames)
    {
        this.companyList = filterdNames;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_logo;
        TextView tv_name;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
