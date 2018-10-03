package com.teggkitchen.apitest.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.teggkitchen.apitest.ItemListClickListener;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.model.Orders;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.util.Util;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    ItemListClickListener listener;
    View view;
    OrdersAdapter.ViewHolder viewHolder;
    List<Orders> datas;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_num, txt_order_id, txt_time, txt_count;
        private Button btn_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_num = (TextView) itemView.findViewById(R.id.txt_num);
            txt_order_id = (TextView) itemView.findViewById(R.id.txt_order_id);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }

    public OrdersAdapter(List<Orders> datas, ItemListClickListener listener) {
        this.datas = datas;
        this.listener = listener;

        //第零項
        Orders orders =new Orders();
        datas.add(0,orders);
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_orders, parent, false);
        viewHolder = new OrdersAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, final int position) {
        Log.e("position",position+"");
        Log.e("getOrder_id",datas.get(position).getOrder_id() + "");
        if (position==0){
            holder.txt_num.setText("順序");
            holder.txt_order_id.setText("編號");
            holder.txt_time.setText("日期");
            holder.txt_count.setText("總金額");
            holder.btn_delete.setVisibility(View.GONE);

            // Title樣式
            holder.txt_num.setTypeface(null, Typeface.BOLD);
            holder.txt_order_id.setTypeface(null, Typeface.BOLD);
            holder.txt_time.setTypeface(null, Typeface.BOLD);
            holder.txt_count.setTypeface(null, Typeface.BOLD);

            holder.txt_num.setTextSize(14);
            holder.txt_order_id.setTextSize(14);
            holder.txt_time.setTextSize(14);
            holder.txt_count.setTextSize(14);

            holder.txt_num.setTextColor(view.getResources().getColor(R.color.colorBlack));
            holder.txt_order_id.setTextColor(view.getResources().getColor(R.color.colorBlack));
            holder.txt_time.setTextColor(view.getResources().getColor(R.color.colorBlack));
            holder.txt_count.setTextColor(view.getResources().getColor(R.color.colorBlack));
        }else {
//            setContentStyle(viewHolder);
            holder.txt_num.setText((position) + "");
            holder.txt_order_id.setText(datas.get(position).getOrder_id() + "");
            String time = Util.dateFormat("yyyy-MM-dd", datas.get(position).getCreatedAt());
            holder.txt_time.setText(time);
            holder.txt_count.setText("$ "+datas.get(position).getCount());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(position, ItemListClickListener.ELEMENT_ITEM);
                }
            });

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(position, ItemListClickListener.ELEMENT_REMOVE);
                }
            });


        }


    }


    @Override
    public int getItemCount() {
        return datas.size();
    }



}