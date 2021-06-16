package com.afrobiz.afrobizfind.ui.mActivity.orderHistory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.OrderHistory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.Holder> {

    private Context context;
    private List<OrderHistory> list;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    private Calendar myCalendar = Calendar.getInstance();
    private int mHour, mMinute;

    public ProductOrderAdapter(Context context, List<OrderHistory> list1)
    {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_product_order, parent, false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {
        if(position % 2 == 0)
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_even_bg);
        }
        else
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_odd_bg);
        }

        holder.tv_product_name.setText(list.get(position).getProductName());
        holder.tv_product_price.setText(list.get(position).getPrice() +" * "+list.get(position).getQuantity() +" = "+list.get(position).getTotalprice());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_product_name , tv_product_price;
        LinearLayout layout_main;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_product_price = (TextView) itemView.findViewById(R.id.tv_product_price);
            tv_product_name.setSelected(true);
        }
    }
    public void filterList(List<OrderHistory> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
