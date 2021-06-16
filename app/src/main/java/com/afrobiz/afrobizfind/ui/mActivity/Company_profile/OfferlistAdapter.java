package com.afrobiz.afrobizfind.ui.mActivity.Company_profile;

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

public class OfferlistAdapter extends RecyclerView.Adapter<OfferlistAdapter.Holder>
{
    private Context context;
    private List<Offers> list;

    public OfferlistAdapter(Context context, List<Offers> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OfferlistAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_offers, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferlistAdapter.Holder holder, int position)
    {
        holder.tv_discount.setText(list.get(position).getDiscount());
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_date.setText(list.get(position).getStartDate()+" to "+list.get(position).getEndDate());


        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((CompanyProfileActivity)context).ShowOfferDetails(position);
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

        public Holder(@NonNull View itemView) {
            super(itemView);

            tv_discount = (TextView) itemView.findViewById(R.id.tv_discount);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

            tv_date = (TextView) itemView.findViewById(R.id.tv_date);

        }
    }
}
