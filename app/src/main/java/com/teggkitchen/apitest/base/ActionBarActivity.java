package com.teggkitchen.apitest.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.teggkitchen.apitest.R;
import com.tingk.apibase.activity.TActionBarActivity;

public class ActionBarActivity extends TActionBarActivity
{
    public Fragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initActionBar();
        initParam();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    /* -----------------------------------------------------------
     * Init Process
     * --------------------------------------------------------- */

    private void initActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initParam() {}

    /* -----------------------------------------------------------
     * Custom Process
     * --------------------------------------------------------- */

}
