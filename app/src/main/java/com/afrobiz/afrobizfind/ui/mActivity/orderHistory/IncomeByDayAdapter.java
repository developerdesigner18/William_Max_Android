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
import com.afrobiz.afrobizfind.ui.modal.IncomeByDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class IncomeByDayAdapter extends RecyclerView.Adapter<IncomeByDayAdapter.Holder> {

    private Context context;
    private List<IncomeByDay> list;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    private Calendar myCalendar = Calendar.getInstance();
    private int mHour, mMinute;

    public IncomeByDayAdapter(Context context, List<IncomeByDay> list1)
    {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_income_by_day, parent, false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        if(position % 2 == 0)
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_even_bg);
        }
        else
        {
            holder.layout_main.setBackgroundResource(R.drawable.table_odd_bg);
        }

        holder.tv_no_orders.setText(""+list.get(position).getTotalorder());
        holder.tv_no_products.setText(list.get(position).getTotalproduct());

        holder.tv_income_account.setText(list.get(position).getTotaldayincome());
        holder.tv_day_date.setText(list.get(position).getCreatedAt());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_no_products , tv_no_orders,tv_income_account, tv_day_date;
        LinearLayout layout_main;

        public Holder(@NonNull View itemView)
        {
            super(itemView);
            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);
            tv_no_products = (TextView) itemView.findViewById(R.id.tv_no_products);
            tv_no_orders = (TextView) itemView.findViewById(R.id.tv_no_orders);
            tv_income_account = (TextView) itemView.findViewById(R.id.tv_income_account);
            tv_day_date = (TextView) itemView.findViewById(R.id.tv_day_date);
        }
    }
    public void filterList(List<IncomeByDay> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
