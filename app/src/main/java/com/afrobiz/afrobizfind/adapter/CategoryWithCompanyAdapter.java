package com.afrobiz.afrobizfind.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.MainActivity;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Category;
import com.afrobiz.afrobizfind.ui.modal.Company;
import com.afrobiz.afrobizfind.ui.modal.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryWithCompanyAdapter extends RecyclerView.Adapter<CategoryWithCompanyAdapter.MyViewHolder>
{
    MainActivity context;

    List<Category> categoryList ;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public CategoryWithCompanyAdapter(MainActivity context, List<Category> categoryList)
    {
        this.context = context;
        this.categoryList = categoryList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public CategoryWithCompanyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category_companies, parent, false);
        CategoryWithCompanyAdapter.MyViewHolder viewHolder = new CategoryWithCompanyAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryWithCompanyAdapter.MyViewHolder holder, int position)
    {
        holder.tv_category_name.setText(categoryList.get(position).getName());
        List<Company> companyList = categoryList.get(position).getCompanies();

        if(companyList != null && companyList.size() > 0)
        {
            List<Product> productList = new ArrayList<>();

            if(productList != null)
            {
                for(int i = 0 ; i < companyList.size() ; i ++)
                {
                    if(companyList.get(i).getProducts() != null)
                    {
                        productList.addAll(companyList.get(i).getProducts());
                    }
                }
            }

            if(productList != null && productList.size() > 0)
            {
                holder.tv_empty_product.setVisibility(View.GONE);
                holder.rcv_products.setVisibility(View.VISIBLE);
                CompanyProductsAdapter adapter = new CompanyProductsAdapter(context, productList);
                holder.rcv_products.setAdapter(adapter);
            }
            else
            {
                holder.rcv_products.setVisibility(View.GONE);
                holder.tv_empty_product.setVisibility(View.VISIBLE);
            }
        }

        if(companyList != null && companyList.size() > 0)
        {
            holder.tv_empty.setVisibility(View.GONE);
            holder.rcv_companies.setVisibility(View.VISIBLE);
            CatCompanyAdapter adapter = new CatCompanyAdapter(context, companyList);
            holder.rcv_companies.setAdapter(adapter);
        }
        else
        {
            holder.rcv_companies.setVisibility(View.GONE);
            holder.tv_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return categoryList.size();
    }

    public void filterList(List<Category> filterdNames)
    {
        this.categoryList = filterdNames;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_category_name, tv_empty, tv_empty_product;
        RecyclerView rcv_companies, rcv_products;

        private LinearLayoutManager horizontalManager = new
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        private LinearLayoutManager horizontalManager1 = new
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tv_category_name = (TextView) itemView.findViewById(R.id.tv_category_name);
            tv_empty = (TextView) itemView.findViewById(R.id.tv_empty);
            tv_empty_product = (TextView) itemView.findViewById(R.id.tv_empty_product);
            rcv_companies = (RecyclerView) itemView.findViewById(R.id.rcv_companies);
            rcv_products = (RecyclerView) itemView.findViewById(R.id.rcv_products);

            rcv_companies.setHasFixedSize(true);
            rcv_companies.setNestedScrollingEnabled(false);
            rcv_companies.setLayoutManager(horizontalManager);
            rcv_companies.setItemAnimator(new DefaultItemAnimator());

            rcv_products.setHasFixedSize(true);
            rcv_products.setNestedScrollingEnabled(false);
            rcv_products.setLayoutManager(horizontalManager1);
            rcv_products.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
