package com.teggkitchen.apitest.base;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.teggkitchen.apitest.R;
import com.teggkitchen.apitest.page.product.ShoppingProductsFragment;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends BaseActivity {

    private static final int DELAY_TIME = 2000; // Launch延遲時間：1秒

//    private ImageView imgLogo;
//    private RelativeLayout layoutBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initView();
        initApp();
    }

    /**
     * 取得介面元件
     */
    @Override
    protected void initView() {
//        layoutBG = (RelativeLayout) findViewById(R.id.layout_bg);
//        imgLogo = (ImageView) findViewById(R.id.img_logo);
    }


    /**
     * 初始化APP
     */
    private void initApp()
    {
        // 顯示啟動畫面
        displayLaunchScreen();
    }


    /**
     * 顯示啟動畫面
     */
    private void displayLaunchScreen()
    {


        // 設定背景圖
//        layoutBG.setBackgroundResource(R.drawable.img_main_bg);
//        imgLogo.setBackgroundResource(R.drawable.img_launch_bg);


    }


    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            goContainerPage(DrawerMenuActivity.class, ShoppingProductsFragment.class.getName(), null, true, PAGE_NO_ANIMATION);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Timer timer = new Timer();
        timer.schedule(task, DELAY_TIME);
    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initListener() {

    }
}
