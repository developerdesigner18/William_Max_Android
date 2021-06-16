package com.afrobiz.afrobizfind.ui.mActivity.Company_productlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Offers;

import java.util.List;

public class CompanyOffersListAdapter extends RecyclerView.Adapter<CompanyOffersListAdapter.Holder>
{
    private Context context;
    private List<Offers> list;

    public CompanyOffersListAdapter(Context context, List<Offers> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override

    public CompanyOffersListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_offer_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyOffersListAdapter.Holder holder, int position)
    {
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_discount.setText(""+list.get(position).getDiscount());
        holder.tv_date.setText(list.get(position).getStartDate()+" to "+list.get(position).getEndDate());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((CompanyOffersListActivity)context).ShowOfferDetails(position);

//                Intent i_start = new Intent(context , CompanyProfileActivity.class);
//                context.startActivity(i_start);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tv_name,tv_discount,tv_date;
        public Holder(@NonNull View itemView)
        {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_discount = (TextView) itemView.findViewById(R.id.tv_discount);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
    public void filterList(List<Offers> filterdNames)
    {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
