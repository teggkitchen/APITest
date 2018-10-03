package com.teggkitchen.apitest.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.tingk.apibase.activity.TBaseActivity;

import java.io.Serializable;

public abstract class BaseFragment  extends Fragment implements Serializable
{
    protected abstract void initView();
    protected abstract void initParam();
    protected abstract void initListener();

    protected TBaseActivity baseActivity;

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        baseActivity = (TBaseActivity) getActivity();
    }

    /**
     * 顯示Toast
     */
    public void show_toast(String message)
    {
        Toast.makeText(baseActivity, message, Toast.LENGTH_LONG).show();
    }
    public void show_toast(String message, int duration)
    {
        Toast.makeText(baseActivity, message, duration).show();
    }
}