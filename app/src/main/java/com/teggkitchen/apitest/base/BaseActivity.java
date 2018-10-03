package com.teggkitchen.apitest.base;

import android.os.Bundle;
import android.widget.Toast;

import com.tingk.apibase.activity.TBaseActivity;

public abstract class BaseActivity extends TBaseActivity
{
    protected abstract void initView();
    protected abstract void initParam();
    protected abstract void initListener();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }



    /**
     * 顯示Toast
     */
    public void show_toast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void show_toast(String message, int duration)
    {
        Toast.makeText(this, message, duration).show();
    }
}
