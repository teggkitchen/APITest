package com.teggkitchen.apitest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.teggkitchen.apitest.ItemListClickListener;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.model.Products;

import java.util.List;

public class UpdateProductsAdapter extends RecyclerView.Adapter<UpdateProductsAdapter.ViewHolder> {
    ItemListClickListener listener;
    View view;
    UpdateProductsAdapter.ViewHolder viewHolder;
    List<Products> datas;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title, txt_price;
        private Button btn_update,btn_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_price= (TextView) itemView.findViewById(R.id.txt_price);
            btn_update= (Button) itemView.findViewById(R.id.btn_update);
            btn_delete= (Button) itemView.findViewById(R.id.btn_delete);
        }
    }

    public UpdateProductsAdapter(List<Products> datas, ItemListClickListener listener) {
        this.datas = datas;
        this.listener = listener;
    }

    @Override
    public UpdateProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_update_products, parent, false);
        viewHolder = new UpdateProductsAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(UpdateProductsAdapter.ViewHolder holder, final int position) {

        holder.txt_title.setText(datas.get(position).getName());
        holder.txt_price.setText("$ "+datas.get(position).getPrice()+"");

        holder.btn_update.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public int getItemCount() {
        return datas.size();
    }
}