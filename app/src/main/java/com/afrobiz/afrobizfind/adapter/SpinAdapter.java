package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.afrobiz.afrobizfind.ui.modal.Category;

import java.util.List;

public class SpinAdapter extends ArrayAdapter<Category> {

    private Context context;
    private List<Category> values;

    public SpinAdapter(Context context, int textViewResourceId,
                       List<Category> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Category getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());
        label.setPadding(30,30,30,30);

        return label;
    }
}