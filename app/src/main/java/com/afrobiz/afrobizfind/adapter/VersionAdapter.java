package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Version;

import java.util.ArrayList;
import java.util.List;

public class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.MyViewHolder>
{
    Context context;
    List<Version> versionList= new ArrayList<>();

    public VersionAdapter(Context activity, List<Version> versionList)
    {
        this.context = activity;
        this.versionList = versionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_version , parent , false);
        MyViewHolder viewholder = new MyViewHolder(v);
        return viewholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        holder.tv_title.setText(versionList.get(position).getVerison());
        holder.tv_subtitle.setText(versionList.get(position).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return versionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout layout_main;
        TextView tv_title , tv_subtitle;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            layout_main = (LinearLayout) itemView.findViewById(R.id.layout_main);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
        }
    }
}
