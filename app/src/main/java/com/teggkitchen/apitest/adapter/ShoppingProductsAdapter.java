package com.teggkitchen.apitest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teggkitchen.apitest.ItemListClickListener;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.util.MetricsHelper;
import com.teggkitchen.apitest.util.Util;

import java.util.List;

public class ShoppingProductsAdapter extends RecyclerView.Adapter<ShoppingProductsAdapter.ViewHolder> {
    ItemListClickListener listener;
    View view;
    ShoppingProductsAdapter.ViewHolder viewHolder;
    List<Products> datas;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title, txt_price;
        private ImageView img_item;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_price= (TextView) itemView.findViewById(R.id.txt_price);
            img_item= (ImageView) itemView.findViewById(R.id.img_item);


            // 設定imageView比例(16:9)
            ViewGroup.LayoutParams params = img_item.getLayoutParams();
            params.width = (MetricsHelper.screenWidth/2);
            params.height = (int) (params.width * Config.RECP_IMAGE_HEIGHT_RATIO_16_9_H);

            img_item.setLayoutParams(params);
        }
    }

    public ShoppingProductsAdapter(List<Products> datas, ItemListClickListener listener) {
        this.datas = datas;
        this.listener = listener;
    }

    @Override
    public ShoppingProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_shopping_products, parent, false);
        viewHolder = new ShoppingProductsAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ShoppingProductsAdapter.ViewHolder holder, final int position) {

        holder.txt_title.setText(datas.get(position).getName());
        holder.txt_price.setText("$ "+datas.get(position).getPrice()+"");
        if (!Util.isEmpty(datas.get(position).getImg())) {
            Picasso.get().load(Config.IMAGE_SHOW + datas.get(position).getImg()).into(holder.img_item);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position, ItemListClickListener.ELEMENT_ITEM);
            }
        });

    }


    @Override
    public int getItemCount() {
        return datas.size();
    }
}