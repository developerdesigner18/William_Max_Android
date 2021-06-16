package com.afrobiz.afrobizfind.ui.mActivity.MyCompanies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.View_Image;
import com.bumptech.glide.Glide;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;
import com.afrobiz.afrobizfind.ui.mActivity.SavedCompanyDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class MyCompaniesAdapter extends RecyclerView.Adapter<MyCompaniesAdapter.Holder> {

    boolean isLike = false;
    private final Context context;
    private List<Company> list = new ArrayList<Company>();

    public MyCompaniesAdapter(Context context, List<Company> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_my_company, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvCompanyName.setText(list.get(position).getCompanyName());
        Glide.with(context).load(list.get(position).getImagepath()).into(holder.img_company);

        if (list.get(position).getStatus() == 0) {
            holder.tv_paid_free.setText("Free");
        } else {
            holder.tv_paid_free.setText("Paid");
        }

        holder.tvCompanyDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, SavedCompanyDetailsActivity.class);
                intent.putExtra("company_id", list.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CompanyProfileActivity.class);
                intent.putExtra("company_id", list.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.img_company.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i_view = new Intent(context , View_Image.class);
                i_view.putExtra("path",list.get(position).getImagepath());
                context.startActivity(i_view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        ImageView img_company;

        TextView tv_paid_free, tvCompanyDetails, tvProfile, tvCompanyName;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tvCompanyDetails = (TextView) itemView.findViewById(R.id.tvCompanyDetails);
            tvProfile = (TextView) itemView.findViewById(R.id.tvProfile);
            tvCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
            img_company = (ImageView) itemView.findViewById(R.id.img_company);
            tv_paid_free = (TextView) itemView.findViewById(R.id.tv_paid_free);
        }
    }
}
