package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.bumptech.glide.Glide;

import java.util.List;

public class CatCompanyAdapter extends RecyclerView.Adapter<CatCompanyAdapter.Holder>
{
    private Context context;
    private List<Company> list;

    public CatCompanyAdapter(Context context, List<Company> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CatCompanyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.raw_featured_offer, parent, false);

        CatCompanyAdapter.Holder obj = new CatCompanyAdapter.Holder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull CatCompanyAdapter.Holder holder, int position)
    {
        holder.tv_company_name.setText(list.get(position).getCompanyName());
        Glide.with(context).load(list.get(position).getImagepath()).into(holder.img_company);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity) context).ClickNearCompany(list.get(position).getId());
            }
        });
    }

    public void filterList(List<Company> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        ImageView img_company;
        TextView tv_company_name;
        RelativeLayout relative_main;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            img_company = (ImageView) itemView.findViewById(R.id.img_company);
            relative_main = (RelativeLayout) itemView.findViewById(R.id.relative_main);
        }
    }
}
