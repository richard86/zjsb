package com.kcmap.frame.utils;

import android.annotation.SuppressLint;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
    public static Date getSysTime(){
        return new Date(System.currentTimeMillis());// 获取当前时间
    }

    @SuppressLint("SimpleDateFormat")
    public static String forMatTime(Date date,String forMatStr){
        SimpleDateFormat formatter = new SimpleDateFormat(forMatStr);//"yyyy年MM月dd日 HH:mm"
        return formatter.format(date);
    }

    public static String forMatTime(String date,String forMatStr) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2=formatter.parse(date);
        return forMatTime(date2,forMatStr);
    }

    public static String getOtherDay(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        String dateOk = df.format(newDate2);
        return dateOk;
    }
}
