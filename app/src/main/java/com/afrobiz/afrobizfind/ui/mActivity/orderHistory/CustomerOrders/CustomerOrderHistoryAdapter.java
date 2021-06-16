package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CustomerOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.OrderHistory;

import java.util.List;

public class CustomerOrderHistoryAdapter extends RecyclerView.Adapter<CustomerOrderHistoryAdapter.Holder> {

    private Context context;
    private List<OrderHistory> list;

    public CustomerOrderHistoryAdapter(Context context, List<OrderHistory> list1)
    {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override

    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_order_history, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.tv_total_price.setText(String.valueOf(list.get(position).getTotalprice()));
        holder.tv_product_name.setText(list.get(position).getProductName());
        holder.tv_qty.setText(String.valueOf(list.get(position).getQuantity()));
        holder.tv_date_time.setText(list.get(position).getCreatedAt());
        holder.tv_order_number.setText(String.valueOf(list.get(position).getId()));
        holder.tv_order_status.setText(list.get(position).getStatus());
        holder.tv_company_name.setText(list.get(position).getCompanyName());

        holder.tv_company_name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((CustomerOrderHistoryActivity)context).ClickCompany(position);
            }
        });

        holder.tv_product_name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int ProductId = list.get(position).getProductid();
                ((CustomerOrderHistoryActivity)context).GetShowProductDetails(ProductId );
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                Product proo = null;

                OrderHistory objOrderHistory = list.get(position);

                if(objOrderHistory != null)
                {
                    ((CustomerOrderHistoryActivity)context).ShowDetailsOfOrder(list.get(position));
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_total_price,tv_product_name,tv_order_status, tv_order_number, tv_company_name, tv_qty, tv_date_time;
        ImageView img_product;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            tv_order_status = (TextView) itemView.findViewById(R.id.tv_order_status);
            tv_order_number = (TextView) itemView.findViewById(R.id.tv_order_number);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_company_name = (TextView) itemView.findViewById(R.id.tv_company_name);
            tv_total_price = (TextView) itemView.findViewById(R.id.tv_total_price);
            tv_date_time = (TextView) itemView.findViewById(R.id.tv_date_time);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
        }
    }
    public void filterList(List<OrderHistory> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
