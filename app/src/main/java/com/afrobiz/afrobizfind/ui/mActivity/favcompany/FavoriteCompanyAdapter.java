package com.afrobiz.afrobizfind.ui.mActivity.favcompany;

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
import com.afrobiz.afrobizfind.ui.modal.Category;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.mActivity.Company_profile.CompanyProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoriteCompanyAdapter extends RecyclerView.Adapter<FavoriteCompanyAdapter.Holder> {

    private Context context;
    private List<Company> list = new ArrayList<>();

    boolean isLike = false;

    public FavoriteCompanyAdapter(Context context, List<Company> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_favorite_companies, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {

        Category objcat = list.get(position).getCategory();
        if(objcat != null)
        {
            holder.tv_cat.setText(objcat.getName());
        }

        holder.tv_telephone.setText(list.get(position).getTelephone());
//        holder.tv_address.setText(list.get(position).getCompanyName());

       holder. tv_address.setText(list.get(position).getBuilding_number()+", " +list.get(position).getAddress_line_1()+", "
                +list.get(position).getCity()+", "+list.get(position).getPostcode());


        holder.tv_company_name.setText(list.get(position).getCompanyName());
        Glide.with(context).load(list.get(position).getImagepath()).into(holder.img_company);

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

//        if(holder.img_unlike.getVisibility() == View.VISIBLE)
//        {
//            holder.img_unlike.setVisibility(View.GONE);
//            holder.img_like.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            holder.img_like.setVisibility(View.GONE);
//            holder.img_unlike.setVisibility(View.VISIBLE);
//        }
//        if(isLike == false)
//        {
//            holder.img_unlike.setImageResource(R.drawable.ic_none_favorite);
//        }
//        else
//        {
//            holder.img_unlike.setImageResource(R.drawable.ic_favorite);
//        }
        holder.img_unlike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if(isLike == false)
//                {
//                    isLike = true;
//                    holder.img_unlike.setImageResource(R.drawable.ic_favorite);
//                }
//                else
//                {
//                    isLike = false;
//                    holder.img_unlike.setImageResource(R.drawable.ic_none_favorite);
//                }

//                if(isLike == true)
//                {
                int id = list.get(position).getId();
                    ((FavoriteCompanyActivity)context).removeFromFav(position);
                    notifyDataSetChanged();
//                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_fav = new Intent(context, CompanyProfileActivity.class);
                i_fav.putExtra("company_id", list.get(position).getId());
                context.startActivity(i_fav);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        ImageView img_like , img_unlike, img_company;
        TextView tv_address, tv_telephone, tv_company_name, tv_cat;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            img_unlike = (ImageView) itemView.findViewById(R.id.img_unlike);
            img_company = (ImageView) itemView.findViewById(R.id.img_company);

            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            tv_telephone = (TextView) itemView.findViewById(R.id.tv_telephone);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_cat = (TextView) itemView.findViewById(R.id.tv_cat);
        }
    }
}
