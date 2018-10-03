package com.teggkitchen.apitest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teggkitchen.apitest.model.Member;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuListAdapter extends BaseAdapter {
    private Context context;
    private List<String> items;
    private List<String> weights;
    private Member member;
    private int lastIndex;

    public MenuListAdapter(Context context, Member member, String[] items, String[] weights) {
        this.context = context;
        this.items = new ArrayList(Arrays.asList(items));
        this.weights = new ArrayList(Arrays.asList(weights));
        this.member = member;

        if (!Util.isEmpty(member)) {
            lastIndex = items.length;
            this.items.add(lastIndex, "登出");
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        // 設定Account項目
        if (position == 0) {
            // 判斷帳號是否存在
            if (member != null) {
                MenuAccountHolder holder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_menu_account, null);
                    holder = new MenuAccountHolder();
//                    holder.imageView = (CircleImageView) convertView.findViewById(R.id.image_mb_photo);
                    holder.title = (TextView) convertView.findViewById(R.id.text_title);
                } else {
                    holder = (MenuAccountHolder) convertView.getTag();
                }


                // 設定資料
                Member mb = member;
                holder.title.setText(mb.getEmail());

//                if (!Util.isEmpty(mb.getImg())) {
//                    Picasso.get().load(Config.URL + "/" + mb.getImg()).centerCrop().into(holder.imageView);
//                }


                convertView.setTag(holder);
            }  else {
                return setMenuItem(convertView, inflater,
                        context.getResources().getString(R.string.menu_login_title),position);
            }
        }

        // 設定一般項目
        else {
            return setMenuItem(convertView, inflater, items.get(position),position);
        }

        return convertView;
    }


    /**
     * 設定一般項目
     */
    private View setMenuItem(View convertView, LayoutInflater inflater, String title,int position) {
        MenuItemHolder holder;
        if (convertView == null) {
            if (weights.get(position).equals("subtitle")){
                convertView = inflater.inflate(R.layout.item_menu_subtitle, null);
            }else {
                convertView = inflater.inflate(R.layout.item_menu_title, null);
            }

            holder = new MenuItemHolder();
            holder.title = (TextView) convertView.findViewById(R.id.text_title);
            holder.line = (View) convertView.findViewById(R.id.line);
            if (!Util.isEmpty(member)&&position == (items.size()-1)||position == 0)holder.line.setVisibility(View.GONE);
        } else {
            holder = (MenuItemHolder) convertView.getTag();
        }

        // 設定資料
        holder.title.setText(title);
        convertView.setTag(holder);
        return convertView;
    }

    /* -----------------------------------------------------------
     * Holder Classes
     * --------------------------------------------------------- */

    private class MenuHeaderHolder {
        TextView title;
    }

    private class MenuAccountHolder {
//        CircleImageView imageView;
        TextView title;
    }

    private class MenuItemHolder {
        TextView title;
        View line;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // 登入後不可點擊
        if (position==0&&!Util.isEmpty(member)){
            return false;
        }

        // Item為title不可點擊
        if (weights.get(position).equals("title")){
            return false;
        }
        return super.isEnabled(position);
    }
}
