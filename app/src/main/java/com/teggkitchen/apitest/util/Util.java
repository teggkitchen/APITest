package com.teggkitchen.apitest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.teggkitchen.apitest.config.Config;

import java.lang.reflect.Array;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class Util {
    private static SharedPreferences SP;
    private static SharedPreferences.Editor SP_edit;


    // 判斷物件空的方法
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }

    public static String getSpData(Context context, String key) {
        SP = context.getSharedPreferences(Config.SP_SOURCE, Context.MODE_PRIVATE);
        return SP.getString(key,"");
    }

    public static void setSpData(Context context,String key, String value) {
        SP = context.getSharedPreferences(Config.SP_SOURCE, Context.MODE_PRIVATE);
        SP_edit = SP.edit();
        SP_edit.putString(key, value);
        SP_edit.apply();
    }

    /**
     * 顯示Toast
     */
    public static void show_toast(Context context,String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void show_toast(Context context,String message, int duration)
    {
        Toast.makeText(context, message, duration).show();
    }

    /**
     * 輸入Date，取得自定義格式的日期時間，ex type => yyyy/MM/dd - HH:mm
     */
    public static String dateFormat(String type, Date date) {
        Format formatter = new SimpleDateFormat(type);
        return formatter.format(date);
    }
}
