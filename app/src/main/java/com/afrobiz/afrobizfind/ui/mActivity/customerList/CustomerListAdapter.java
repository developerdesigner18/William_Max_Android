package com.afrobiz.afrobizfind.ui.mActivity.customerList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Users;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.Holder>
{
    private Context context;
    private List<Users> list = new ArrayList<>();

    public CustomerListAdapter(Context context, List<Users> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_customers_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {

        holder.tv_number.setText(list.get(position).getMobileNumber());
        holder.tv_name.setText(list.get(position).getUsername());
        holder.tv_number.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + holder.tv_number.getText().toString()));
                context.startActivity(intent);
            }
        });
    }
    public void filterList(List<Users> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_number, tv_name;

        public Holder(@NonNull View itemView)
        {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
        }
    }
}
