package com.afrobiz.afrobizfind.ui.mActivity.Company_productlist;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;

import java.util.List;

public class CompanyProductListAdapter extends RecyclerView.Adapter<CompanyProductListAdapter.Holder> {

    private Context context;
    private List<Product> list;

    public CompanyProductListAdapter(Context context, List<Product> list1)
    {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override
    public CompanyProductListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_productl_ist, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyProductListAdapter.Holder holder, int position)
    {


        holder.tv_price.setText(""+list.get(position).getPrice());
        holder.tv_desc.setText(list.get(position).getDescription());

        holder.tv_product_name.setText(list.get(position).getProductName());

        List<Image> listimages = list.get(position).getImages();

        if(listimages != null && listimages.size() > 0)
        {
            Glide.with(this.context).load(listimages.get(0).getImagepath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.img_product);
        }

        holder.img_product.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(listimages != null && listimages.size() > 0)
                {
                    Intent i_view = new Intent(context, View_Image.class);
                    i_view.putExtra("path",listimages.get(0).getImagepath());
                    context.startActivity(i_view);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((CompanyProductListActivity)context).ShowProductsDetails(position);
//                Intent i_start = new Intent(context , CompanyProfileActivity.class);
//                i_start.putExtra("company_id", list.get(position).getCompanyId());
//                context.startActivity(i_start);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }
    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_price,tv_product_name,tv_desc;
        ImageView img_product;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
    public void filterList(List<Product> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
