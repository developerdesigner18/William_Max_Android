package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.mActivity.addCompany.AddCompanyActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class CompanyImageAdapter extends RecyclerView.Adapter<CompanyImageAdapter.MyViewHolder>
{
    Context context;
    List<String> pathList;
    DisplayMetrics metrics;
    int screenwidth , screenheight;

    public CompanyImageAdapter(Context context1, List<String> pathList1)
    {
        this.context = context1;
        this.pathList = pathList1;

        metrics = this.context.getResources().getDisplayMetrics();
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_images , parent , false);
        MyViewHolder view_holder = new MyViewHolder(v);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        Glide.with(this.context).load(pathList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.img_path);

        holder.img_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((AddCompanyActivity)context).removeImage(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder
    {
        RelativeLayout layout_image , relative_image;
        ImageView img_cancel , img_path , img_path1;
        CardView card;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            layout_image = (RelativeLayout) itemView.findViewById(R.id.layout_image);
            relative_image = (RelativeLayout) itemView.findViewById(R.id.relative_image);
            img_cancel = (ImageView) itemView.findViewById(R.id.img_cancel);
            card = (CardView) itemView.findViewById(R.id.card);
            img_path = (ImageView) itemView.findViewById(R.id.img_path);

            CardView.LayoutParams p1  = (CardView.LayoutParams) img_path.getLayoutParams();
            p1.width = screenwidth * 250/1080;
            p1.height = screenheight * 250 / 1920;
            img_path.setLayoutParams(p1);

            RelativeLayout.LayoutParams pa1  = (RelativeLayout.LayoutParams) card.getLayoutParams();
            pa1.width = screenwidth * 250/1080;
            pa1.height = screenheight * 250 / 1920;
            pa1.setMargins(0, screenheight*20/1920 , screenwidth * 20 /1080 , 0);
            card.setLayoutParams(pa1);

            RelativeLayout.LayoutParams p1s  = (RelativeLayout.LayoutParams) img_cancel.getLayoutParams();
            p1s.width = screenwidth * 70/1080;
            p1s.height = screenheight * 70 / 1920;
            img_cancel.setLayoutParams(p1s);

            ConstraintLayout.LayoutParams p1zs  = (ConstraintLayout.LayoutParams) relative_image.getLayoutParams();
            p1zs.width = screenwidth * 250/1080;
            p1zs.height = screenheight * 250 / 1920;
            relative_image.setLayoutParams(p1zs);

        }
    }
}
