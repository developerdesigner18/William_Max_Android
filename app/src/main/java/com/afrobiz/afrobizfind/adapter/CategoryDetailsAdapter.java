package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.category.CategoryDetailActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryDetailsAdapter extends RecyclerView.Adapter<CategoryDetailsAdapter.Holder>
{
    private Context context;
    private List<Company> list;

    public CategoryDetailsAdapter(Context context, List<Company> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryDetailsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_category_detail, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailsAdapter.Holder holder, int position)
    {
        holder.tv_company_name.setText(list.get(position).getCompanyName());
        Glide.with(context).load(list.get(position).getImagepath()).into(holder.img_company);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((CategoryDetailActivity) context).ClickCompany(list.get(position).getId());
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
        ImageView img_company ,img_left_top,img_left_bottom,img_right_top,img_right_bottom;
        TextView tv_company_name;
        RelativeLayout relative_main;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            img_company = (ImageView) itemView.findViewById(R.id.img_company);
//            img_left_bottom = (ImageView) itemView.findViewById(R.id.img_left_bottom);
//            img_left_top = (ImageView) itemView.findViewById(R.id.img_left_top);
//            img_right_bottom = (ImageView) itemView.findViewById(R.id.img_right_bottom);
//            img_right_top  = (ImageView) itemView.findViewById(R.id.img_right_top);
            relative_main = (RelativeLayout) itemView.findViewById(R.id.relative_main);

            LinearLayout.LayoutParams  p1 = (LinearLayout.LayoutParams) relative_main.getLayoutParams();
            p1.width =( (context.getResources().getDisplayMetrics().widthPixels / 2) - (5*10));
            p1.height = ( (context.getResources().getDisplayMetrics().widthPixels / 2) - (5*10));
            relative_main.setLayoutParams(p1);

            RelativeLayout.LayoutParams  p1q = (RelativeLayout.LayoutParams) img_company.getLayoutParams();
            p1q.width = ( (context.getResources().getDisplayMetrics().widthPixels / 2) - (5*10));
            p1q.height =( (context.getResources().getDisplayMetrics().widthPixels / 2) - (5*10));
            img_company.setLayoutParams(p1q);

            RelativeLayout.LayoutParams  p1ed = (RelativeLayout.LayoutParams) tv_company_name.getLayoutParams();
            p1ed.width = ( (context.getResources().getDisplayMetrics().widthPixels / 2) - (5*10));
            p1ed.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            tv_company_name.setLayoutParams(p1ed);

        }
    }
}
