package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.CompanyOrderHistory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.OrderHistory;

import java.util.List;

public class CompanyOrderHistoryAdapter extends RecyclerView.Adapter<CompanyOrderHistoryAdapter.Holder> {

    private Context context;
    private List<OrderHistory> list;

    public CompanyOrderHistoryAdapter(Context context, List<OrderHistory> list1)
    {
        this.context = context;
        this.list = list1;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_company_order_history, parent, false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {
        holder.tv_order_no.setText(""+list.get(position).getId());
        holder.tv_price.setText(""+list.get(position).getTotalprice());
        holder.tv_qty.setText(""+list.get(position).getQuantity());
        holder.tv_cust_no.setText(""+list.get(position).getUserid());
        holder.tv_email.setText(list.get(position).getUsername());
        holder.tv_paid_by.setText(list.get(position).getMethod());
        holder.tv_date_time.setText(list.get(position).getCreatedAt());
        Log.e("created at",""+list.get(position).getCreatedAt());

        holder.tv_order_status.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String statusID = list.get(position).getStatus();
                ((CompanyOrderHistoryActivity)context).ShowOrderStatus(statusID );
            }
        });
        holder.tv_order_details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ((CompanyOrderHistoryActivity)context).ShowProductOrderDetails(list.get(position));
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_order_details,tv_order_status,tv_price, tv_order_no, tv_cust_no, tv_email, tv_qty, tv_date_time, tv_paid_by;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            tv_order_details = (TextView) itemView.findViewById(R.id.tv_order_details);
            tv_order_status = (TextView) itemView.findViewById(R.id.tv_order_status);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);
            tv_cust_no = (TextView) itemView.findViewById(R.id.tv_cust_no);
            tv_date_time = (TextView) itemView.findViewById(R.id.tv_date_time);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
            tv_paid_by = (TextView) itemView.findViewById(R.id.tv_paid_by);
            tv_order_status.setSelected(true);
        }
    }
    public void filterList(List<OrderHistory> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
