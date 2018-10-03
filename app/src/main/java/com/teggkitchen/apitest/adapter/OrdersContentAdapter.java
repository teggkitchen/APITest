package com.teggkitchen.apitest.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.teggkitchen.apitest.ItemListClickListener;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.model.Orders;
import com.teggkitchen.apitest.model.OrdersContent;
import com.teggkitchen.apitest.util.Util;

import java.util.List;

public class OrdersContentAdapter extends RecyclerView.Adapter<OrdersContentAdapter.ViewHolder> {
    View view;
    OrdersContentAdapter.ViewHolder viewHolder;
    List<OrdersContent> datas;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_img,txt_title, txt_price, txt_num, txt_count;
        private ImageView img_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            txt_img= (TextView) itemView.findViewById(R.id.txt_img);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_num = (TextView) itemView.findViewById(R.id.txt_num);
            txt_count = (TextView) itemView.findViewById(R.id.txt_count);

            // 設定imageView比例(16:9)
            ViewGroup.LayoutParams params = img_item.getLayoutParams();
            params.width = (int) (params.height * Config.RECP_IMAGE_HEIGHT_RATIO_16_9_W);
            img_item.setLayoutParams(params);
        }
    }

    public OrdersContentAdapter(List<OrdersContent> datas) {
        this.datas = datas;

        //第零項
        OrdersContent ordersContent = new OrdersContent();
        datas.add(0, ordersContent);
    }

    @Override
    public OrdersContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_orders_content, parent, false);
        viewHolder = new OrdersContentAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(OrdersContentAdapter.ViewHolder holder, final int position) {
        if (position == 0) {

            holder.txt_img.setText("圖片");
            holder.txt_title.setText("商品");
            holder.txt_price.setText("價錢");
            holder.txt_num.setText("數量");
            holder.txt_count.setText("總金額");

            holder.txt_img.setVisibility(View.VISIBLE);
            holder.img_item.setVisibility(View.GONE);

            holder.txt_img.setTypeface(null, Typeface.BOLD);
            holder.txt_title.setTypeface(null, Typeface.BOLD);
            holder.txt_price.setTypeface(null, Typeface.BOLD);
            holder.txt_num.setTypeface(null, Typeface.BOLD);
            holder.txt_count.setTypeface(null, Typeface.BOLD);

            holder.txt_img.setTextSize(14);
            holder.txt_title.setTextSize(14);
            holder.txt_price.setTextSize(14);
            holder.txt_num.setTextSize(14);
            holder.txt_count.setTextSize(14);

            holder.txt_img.setTextColor(view.getResources().getColor(R.color.colorBlack));
            holder.txt_title.setTextColor(view.getResources().getColor(R.color.colorBlack));
            holder.txt_price.setTextColor(view.getResources().getColor(R.color.colorBlack));
            holder.txt_num.setTextColor(view.getResources().getColor(R.color.colorBlack));
            holder.txt_count.setTextColor(view.getResources().getColor(R.color.colorBlack));
        } else {

            holder.txt_title.setText(datas.get(position).getName());
            holder.txt_price.setText("$ " + datas.get(position).getPrice());
            holder.txt_num.setText(datas.get(position).getNum() + "");
            holder.txt_count.setText("$ " + (datas.get(position).getPrice() * datas.get(position).getNum()));

            if (!Util.isEmpty(datas.get(position).getImg())) {
                Picasso.get().load(Config.IMAGE_SHOW + datas.get(position).getImg()).into(holder.img_item);
            }

        }
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }
}