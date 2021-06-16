package com.afrobiz.afrobizfind.ui.events;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.View_Image;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyProductListActivity;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class CompanyEventListAdapter extends RecyclerView.Adapter<CompanyEventListAdapter.Holder> {

    private Context context;
    private List<Event> list;

    public CompanyEventListAdapter(Context context, List<Event> list1)
    {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override
    public CompanyEventListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_event_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyEventListAdapter.Holder holder, int position)
    {
        holder.tv_price.setText(""+list.get(position).getPrice());
        holder.tv_date.setText(list.get(position).getDate());
        holder.tv_event_name.setText(""+list.get(position).getEventname());
        holder.tv_location.setText(list.get(position).getLocation());
//
//        holder.tv_product_name.setText(list.get(position).getProductName());
//
//        List<Image> listimages = list.get(position).getImages();
//
        String flyerimage = list.get(position).getFlyerimage();
        if(flyerimage != null )
        {
            Glide.with(this.context).load(flyerimage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.img_event);
        }

        holder.img_event.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String flyerimage = list.get(position).getFlyerimage();
                if(flyerimage != null )
                {
                    Intent i_start = new Intent(context , View_Image.class);
                    i_start.putExtra("path", flyerimage);
                    context.startActivity(i_start);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                ((CompanyProductListActivity)context).ShowProductsDetails(position);
                Intent i_start = new Intent(context , EventDetailsActivity.class);
                i_start.putExtra("event", list.get(position));
                context.startActivity(i_start);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_price,tv_event_name, tv_location , tv_date;
        ImageView img_event;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            img_event = (ImageView) itemView.findViewById(R.id.img_event);
            tv_event_name = (TextView) itemView.findViewById(R.id.tv_event_name);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
    public void filterList(List<Event> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
