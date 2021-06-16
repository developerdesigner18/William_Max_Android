package com.afrobiz.afrobizfind.ui.mActivity.orderHistory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.IncomeByProduct;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class IncomeByProductAdapter extends RecyclerView.Adapter<IncomeByProductAdapter.Holder> {

    private Context context;
    private List<IncomeByProduct> list;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    private Calendar myCalendar = Calendar.getInstance();
    private int mHour, mMinute;

    public IncomeByProductAdapter(Context context, List<IncomeByProduct> list1)
    {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_income_by_product, parent, false);
        return new Holder(view);
    }
    public void showdatepicker(Holder holder)
    {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                holder.tv_period.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.show();
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.tv_profit.setText(list.get(position).getTotalproductincome());
        holder.tv_product_name.setText(list.get(position).getProductName());
        holder.tv_qty.setText(String.valueOf(list.get(position).getQuantity()));

//        List<Image> listimages = list.get(position).getImages();
//
//        if(listimages != null && listimages.size() > 0)
//        {
//            Glide.with(this.context).load(listimages.get(0).getImagepath())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.img_product);
//        }


        holder.tv_period.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showdatepicker(holder);
//                changeDate(holder);
//                ((CustomerOrderHistoryActivity)context).ClickCompany(position);
            }
        });
    }
    public void changeDate(Holder holder)
    {
        date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                holder.tv_period.setText(DateFormat.format("yyyy-MM-dd", myCalendar));
            }
        };
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_period , tv_qty,tv_income, tv_profit, tv_product_name, tv_product_no;
        ImageView img_product;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            tv_period = (TextView) itemView.findViewById(R.id.tv_period);
            tv_income = (TextView) itemView.findViewById(R.id.tv_income);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_profit = (TextView) itemView.findViewById(R.id.tv_profit);
            tv_product_no = (TextView) itemView.findViewById(R.id.tv_product_no);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);

            tv_product_name.setSelected(true);
        }
    }
    public void filterList(List<IncomeByProduct> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
