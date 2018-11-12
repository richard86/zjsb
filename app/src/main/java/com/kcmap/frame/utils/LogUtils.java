package com.kcmap.frame.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LogUtils {
    
    public static void showToast(Context mContext, String showContent) {
        
        Toast.makeText(mContext, showContent, Toast.LENGTH_SHORT).show();
    }
    
    public static void showDebug(String debugContent) {
        Log.e("kcmap", debugContent);
    }
    
    public static void debug(String msg) {
        Log.d("kcmap", msg);
    }
    
    public static void info(String msg) {
        Log.i("kcmap", msg);
    }
    
    public static void warn(String msg) {
        Log.w("kcmap", msg);
    }
    
    public static void error(String msg) {
        Log.e("kcmap", msg);
    }
    
    public static void error(String msg, Throwable e) {
        Log.e("kcmap", msg, e);
    }
    
    public static void error(Exception e) {
        Log.e("kcmap", e.getMessage(), e);
    }
    
}
