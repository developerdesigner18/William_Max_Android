package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Till;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afrobiz.afrobizfind.ui.modal.Product;

import java.util.List;

public class TillSearchAdapter extends ArrayAdapter<Product>
{
    private Context context;
    // Your custom values for the spinner (User)
    private List<Product> values;

    public TillSearchAdapter(@NonNull Context context, int resource , List<Product> listProduct)
    {
        super(context, resource);
        this.context = context;
        this.values = listProduct;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getProductName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public int getCount()
    {
        return values.size();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getProductName());

        return label;
    }

    @Nullable
    @Override
    public Product getItem(int position)
    {
        return values.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getPosition(@Nullable Product item)
    {
        return item.getId();
    }
}
