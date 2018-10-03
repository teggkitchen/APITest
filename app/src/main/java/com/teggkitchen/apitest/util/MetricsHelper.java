package com.teggkitchen.apitest.util;

public class MetricsHelper {
    public static float density = 0;
    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static float screenScale = 0;

    public static float dpToPixel(float dp)
    {
        float px = dp * density;
        return px;
    }

    public static float pixelToDp(float pixel)
    {
        float dp = pixel / density;
        return dp;
    }
}
