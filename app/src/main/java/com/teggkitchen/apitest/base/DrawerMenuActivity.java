package com.teggkitchen.apitest.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.adapter.MenuListAdapter;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.helper.MemberHelper;
import com.teggkitchen.apitest.model.Member;
import com.teggkitchen.apitest.page.member.MemberSignInActivity;
import com.teggkitchen.apitest.page.product.AddProductFragment;
import com.teggkitchen.apitest.page.product.OrdersFragment;
import com.teggkitchen.apitest.page.product.ShoppingProductsFragment;
import com.teggkitchen.apitest.page.product.UpdateProductsFragment;
import com.teggkitchen.apitest.util.Util;
import com.tingk.apibase.activity.TDrawerMenuActivity;

public class DrawerMenuActivity extends TDrawerMenuActivity {

    private MenuListAdapter adapter;
    private String[] defaultMenuItems;
    private String[] menuWeights;
    private Member member;


    //註冊廣播
    private void registerRC() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.SEND_MENU_STATUS_CHANGE);
        this.registerReceiver(broadcastReceiver, intentFilter);
    }


    //撤銷廣播
    public void unregisterReceiver() {
        try {
            this.unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerRC();
        initActionBar();
        initDrawerList();
        setMenuItems();
        initParam();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /* -----------------------------------------------------------
     * Init Process
     * --------------------------------------------------------- */

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        }
    }

    private void initDrawerList() {
        // 設定選單背景色
        drawerList.setBackgroundColor(Color.parseColor("#000000"));

        // 取得選單項目
        defaultMenuItems = this.getResources().getStringArray(R.array.drawer_menu);


        menuWeights = this.getResources().getStringArray(R.array.menu_weight);

        // 設定側選單項目Click事件
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                showPage(position);
            }
        });
    }

    private void initParam() {
    }

    /**
     * 設定選單項目
     */
    private void setMenuItems() {

        // 取得登入帳戶
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String memberJson = MemberHelper.getMember(this);
                member =gson.fromJson(memberJson, Member.class);

        // 設定選單Adapter
        adapter = new MenuListAdapter(this, member, defaultMenuItems,menuWeights);
        drawerList.setAdapter(adapter);
    }

    /**
     * 顯示畫面
     */
    public void showPage(int index) {

        if ((index == 0) && (Util.isEmpty(member))) {
            // 顯示登入頁
            goPage(MemberSignInActivity.class, false);
        }else if((index == adapter.getCount()-1) && (!Util.isEmpty(member))){
            // 監聽 登出Item
            //SP清除 - 清除member json
            MemberHelper.clearToken(this);
            MemberHelper.clearMember(this);
            setMenuItems();
            adapter.notifyDataSetChanged();
            Util.show_toast(this,"已登出");
            closeDrawer();

        } else {
            // 設定Fragment
            String fragmentName = null;
            switch (index) {
                case 2: {
                    // 顯示 增加商品
                    fragmentName = AddProductFragment.class.getName();
                    break;
                }
                case 3: {
                    // 顯示 編輯商品
                    fragmentName = UpdateProductsFragment.class.getName();
                    break;
                }
                case 4: {
                    // 顯示 購買商品
                    fragmentName = ShoppingProductsFragment.class.getName();
                    break;
                }
                case 6: {
                    // 顯示 查詢訂單
                    fragmentName = OrdersFragment.class.getName();
                    break;
                }


            }
            // 顯示頁面
            goContainerPage(DrawerMenuActivity.class, fragmentName, null, true, PAGE_DEFAULT_ANIMATION);
        }
    }


    //監聽廣播
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Config.SEND_MENU_STATUS_CHANGE)) {
                setMenuItems();
                adapter.notifyDataSetChanged();

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

}