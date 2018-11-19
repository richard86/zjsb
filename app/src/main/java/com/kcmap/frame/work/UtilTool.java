package com.kcmap.frame.work;

import android.app.Activity;

import com.kcmap.frame.appData.AppData;

import java.io.File;

/**
 * Created by lizhiwei on 2018/11/17.
 */
public class UtilTool {

    public static String getProjectPath(Activity activity){
        AppData data=new AppData();
        return data.getAppData("workPath",activity);
    }

    public static String getXMH(Activity activity){
        AppData data=new AppData();
        return data.getAppData("XMH",activity);
    }

    public static String getPCH(Activity activity){
        AppData data=new AppData();
        return data.getAppData("PCH",activity);
    }

    public static String getYBH(Activity activity){
        AppData data=new AppData();
        return data.getAppData("YBH",activity);
    }

    public static String getDBPath(Activity activity){
        return getProjectPath(activity) + File.separator +getPCH(activity)+ File.separator+ "AppTemplate.sqlite";
    }
}
