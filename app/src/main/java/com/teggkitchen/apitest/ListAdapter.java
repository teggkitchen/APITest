package com.teggkitchen.apitest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teggkitchen.apitest.model.Products;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    ItemListClickListener listener;
    View view;
    ListAdapter.ViewHolder viewHolder;
    List<Products> list;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title, txt_price;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
        }
    }

    public ListAdapter(List<Products> list, ItemListClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        viewHolder = new ListAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position) {

        holder.txt_title.setText(list.get(position).getName());
        holder.txt_price.setText(list.get(position).getPrice()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position, "itemview");
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
