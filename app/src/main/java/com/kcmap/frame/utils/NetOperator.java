package com.kcmap.frame.utils;

//模拟网络环境
public class NetOperator {

    public void operator(){
        try {
            //休眠1秒
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

