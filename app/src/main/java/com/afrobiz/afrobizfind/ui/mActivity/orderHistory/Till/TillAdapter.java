package com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Till;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;

import java.util.HashMap;
import java.util.List;

public class TillAdapter extends RecyclerView.Adapter<TillAdapter.MyViewHolder> {
    private static final String TAG = "TillAdapter";
    Context context;
    List<Product> listProduct;
    TillInterface _interface;
    int discount = 0;
    int qty = 0;
    HashMap<Integer, Integer> map_GrandTotal = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> map_qty = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> map_discount = new HashMap<Integer, Integer>();

    public TillAdapter(Context context, List<Product> listProduct, TillInterface _interface)
    {
        this.context = context;
        this.listProduct = listProduct;
        this._interface = _interface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bill, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.setIsRecyclable(false);

        Log.e(TAG, "onBindViewHolder product pricd :--- " + listProduct.get(position).getPrice());

        holder.img_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _interface.OnProductClose(listProduct.get(position), position);
            }
        });

//        total = 0;

        holder.tv_price.setText("Price :- " + listProduct.get(position).getPrice());

        holder.tv_product_name.setText(listProduct.get(position).getProductName());

        if (map_GrandTotal.get(position) == null)
        {
            Log.e(TAG, "onBindViewHolder if : " + map_GrandTotal.get(position));
            holder.tv_total.setText("Total :- " + listProduct.get(position).getPrice());
        }
        else
        {
            Log.e(TAG, "onBindViewHolder else : " + map_GrandTotal.get(position));
            holder.tv_total.setText("Total :- " + map_GrandTotal.get(position));
        }

        if (map_qty.get(position) == null)
        {
            Log.e(TAG, "onBindViewHolder map_qty if : " + map_qty.get(position));
            holder.et_qty.setText("" + 1);
        }
        else
        {
            Log.e(TAG, "onBindViewHolder map_qty else : " + map_qty.get(position));
            holder.et_qty.setText("" + map_qty.get(position));
        }

        if (map_discount.get(position) == null)
        {
            Log.e(TAG, "onBindViewHolder if map_discount : " + map_discount.get(position));
            holder.tv_discount.setText("" + 0);
        }
        else
        {
            Log.e(TAG, "onBindViewHolder else : " + map_discount.get(position));
            holder.tv_discount.setText("" + map_discount.get(position));
        }

//        holder.tv_total.setText("Total :- " + map_GrandTotal.get(position) == null ? listProduct.get(position).getPrice() : map_GrandTotal.get(position));

        String filterd_price = listProduct.get(position).getPrice().replace(",", "");
        int price = Integer.parseInt(filterd_price.substring(2));

        holder.et_qty.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
//                if(charSequence.toString().equals("0"))
//                {
//                    holder.et_qty.setText("1");
//                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.length() > 0)
                {
                    if (editable.length() > 7)
                    {
                        Toast.makeText(context, "Quntity is too large", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
//                        int num=Integer.parseInt(editable.toString());

                        TotalCalucation(position, price, Integer.parseInt(editable.toString()),
                                (holder.tv_discount.getText().length() > 0) ? Integer.parseInt(holder.tv_discount.getText().toString()) : 0);
                        holder.tv_total.setText("Total :-  " + map_GrandTotal.get(position));
                    }
                }
                else
                {
                    TotalCalucation(position, price, 1, (holder.tv_discount.getText().length() > 0) ?
                            Integer.parseInt(holder.tv_discount.getText().toString()) : 0);

                    holder.tv_total.setText("Total :-  " + map_GrandTotal.get(position));
                }
            }
        });
        holder.tv_discount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.length() > 0)
                {
                    Log.e(TAG, "afterTextChanged -----: if ----- " + editable.length());
                    if (editable.length() > 7)
                    {
                        Toast.makeText(context, "Discount is too large", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                        {
                        TotalCalucation(position, price, (holder.et_qty.getText().length() > 0) ? Integer.parseInt(holder.et_qty.getText().toString()) : 1,
                                Integer.parseInt(editable.toString()));
                        holder.tv_total.setText("Total :-  " + map_GrandTotal.get(position));
                    }
                }
                else
                {
                    Log.e(TAG, "afterTextChanged -----: else ----- ");
//                    total = ((price + 0) * ((holder.et_qty.getText().length() > 0) ? Integer.parseInt(holder.et_qty.getText().toString()) : 1));
                    TotalCalucation(position, price, (holder.et_qty.getText().length() > 0) ? Integer.parseInt(holder.et_qty.getText().toString()) : 1, 0);
                    holder.tv_total.setText("Total :-  " + map_GrandTotal.get(position));
                }
            }
        });
    }
    private int TotalCalucation(int position, int price, int qty, int dis)
    {
//        int total = 0;
        if (price == 0)
            return 0;

        if (qty > 0)
        {
            map_qty.put(position, qty);
        }
        else
        {
            map_qty.put(position, 1);
        }

        if (dis > 0)
        {
            map_discount.put(position, dis);
        }
        else
        {
            map_discount.put(position, 0);
        }

        map_discount.put(position, dis);
//        map_GrandTotal.put(position, ((price * qty) - dis));
////        _interface.OnDatachangeListner(position, (price * qty) - dis, qty);

        if(dis != 0 && qty != 0)
        {
            map_GrandTotal.put(position, (((price * qty) * dis) /100));
            _interface.OnDatachangeListner(position, ((price * qty) * dis) / 100 , qty);
        }
        else
        {
            if(dis == 0)
            {
                map_GrandTotal.put(position, (((price * qty) )));
                _interface.OnDatachangeListner(position, ((price * qty) ) , qty);
            }
            else if(qty == 0)
            {
                map_GrandTotal.put(position, (((price * dis) /100)));
                _interface.OnDatachangeListner(position, ((price * dis) /100) , qty);
            }
        }
        return (((price * qty) * dis) / 100);
    }
    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_close;
        TextView tv_product_name, tv_price, et_qty, tv_discount, tv_total;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_close = itemView.findViewById(R.id.img_close);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            et_qty = itemView.findViewById(R.id.et_qty);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_total = itemView.findViewById(R.id.tv_total);
        }
    }
}
