package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.afrobiz.afrobizfind.ImageSliderActivity;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.View_Image;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.List;

public class CompanyImagesAdapter extends RecyclerView.Adapter<CompanyImagesAdapter.MyViewHolder>
{
    Context context ;
    List<Image> imglist;
    DisplayMetrics metrics;
    int screenwidth , screenheight;

    public CompanyImagesAdapter(Context activity, List<Image> list)
    {
        this.context = activity;
        this.imglist = list;

        metrics = this.context.getResources().getDisplayMetrics();
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_images , parent , false);
        MyViewHolder viewholder = new MyViewHolder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {

        Glide.with(this.context).load(imglist.get(position).getImagepath())
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.img_path);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(imglist.get(position).getImagepath() != null)
                {
//                    Intent i_image = new Intent(context, View_Image.class);
//                    i_image.putExtra("path", imglist.get(position).getImagepath());
//                    context.startActivity(i_image);

//                    Intent i_image = new Intent(context, ImageSliderActivity.class);
//                    i_image.putExtra("position", position);
//                    i_image.putExtra("imgList", (Serializable) imglist);
//                    context.startActivity(i_image);

                    Intent i_image = new Intent(context, ImageSliderActivity.class);
                    i_image.putExtra("position", position);
                    i_image.putExtra("imgList", (Serializable) imglist);
                    i_image.putExtra("event", false);
                    context.startActivity(i_image);

                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return imglist.size();
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

            img_cancel.setVisibility(View.GONE);

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

