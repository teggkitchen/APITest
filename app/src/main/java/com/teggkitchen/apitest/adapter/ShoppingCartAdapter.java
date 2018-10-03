package com.teggkitchen.apitest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    ItemListClickListener listener;
    View view;
    ShoppingCartAdapter.ViewHolder viewHolder;
    List<Products> products;
    List<Integer> nums;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title, txt_price,txt_num;
        private Button btn_delete;
        private ImageView img_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_price= (TextView) itemView.findViewById(R.id.txt_price);
            txt_num= (TextView) itemView.findViewById(R.id.txt_num);
            btn_delete= (Button) itemView.findViewById(R.id.btn_delete);

            // 設定imageView比例(16:9)
            ViewGroup.LayoutParams params = img_item.getLayoutParams();
            params.width = (int) (params.height * Config.RECP_IMAGE_HEIGHT_RATIO_16_9_W);
            img_item.setLayoutParams(params);
        }
    }

    public ShoppingCartAdapter(List<Products> products,List<Integer> nums, ItemListClickListener listener) {
        this.products = products;
        this.nums = nums;
        this.listener = listener;
    }

    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_shopping_cart, parent, false);
        viewHolder = new ShoppingCartAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ShoppingCartAdapter.ViewHolder holder, final int position) {

        holder.txt_title.setText(products.get(position).getName());
        holder.txt_price.setText("$ "+products.get(position).getPrice());
        holder.txt_num.setText(+nums.get(position)+"");

        if (!Util.isEmpty(products.get(position).getImg())) {
            Picasso.get().load(Config.IMAGE_SHOW + products.get(position).getImg()).into(holder.img_item);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position, ItemListClickListener.ELEMENT_REMOVE);
            }
        });

    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}