package com.kcmap.frame.main;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.utils.AppManager;

/**
 * Created by lizhiwei on 2018/11/13.
 */
public class XcInfoZszlActivity extends ActivityGroup {
    private  String dqpc;
    TabHost tabHostZSZL=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcinfo_zszl2);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        dqpc =bundle.getString("dqpc");
        Toast.makeText(this, dqpc, Toast.LENGTH_SHORT).show();

        //AppManager.getAppManager().addActivity(this);

        tabHostZSZL = (TabHost)findViewById(R.id.mytabhost);
        //tabHostZSZL.setup();
        tabHostZSZL.setup(this.getLocalActivityManager());
        TabWidget tabWidget = tabHostZSZL.getTabWidget();


        TabHost.TabSpec spec;
        Intent intent2;
        Bundle bundleXCinfo;

        intent2 = new Intent(this,activityTypeA.class);

        bundleXCinfo = new Bundle();
        bundleXCinfo.putString("dqpc",dqpc);

        intent2.putExtras(bundleXCinfo);
        spec = tabHostZSZL.newTabSpec("A类问题").setIndicator("A类问题").setContent(intent2);
        tabHostZSZL.addTab(spec);


        intent2 = new Intent(this,activityTypeB.class);
        bundleXCinfo = new Bundle();
        bundleXCinfo.putString("dqpc",dqpc);
        intent2.putExtras(bundleXCinfo);
        spec = tabHostZSZL.newTabSpec("B类问题").setIndicator("B类问题").setContent(intent2);
        tabHostZSZL.addTab(spec);

        intent2 = new Intent(this,activityTypeC.class);
        bundleXCinfo = new Bundle();
        bundleXCinfo.putString("dqpc",dqpc);
        intent2.putExtras(bundleXCinfo);
        spec = tabHostZSZL.newTabSpec("C类问题").setIndicator("C类问题").setContent(intent2);
        tabHostZSZL.addTab(spec);

        intent2 = new Intent(this,activityTypeD.class);
        bundleXCinfo = new Bundle();
        bundleXCinfo.putString("dqpc",dqpc);
        intent2.putExtras(bundleXCinfo);
        spec = tabHostZSZL.newTabSpec("D类问题").setIndicator("D类问题").setContent(intent2);
        tabHostZSZL.addTab(spec);


        tabHostZSZL.setCurrentTab(0);//设置初始tab
        updateTab(tabHostZSZL);//初始化Tab的颜色，和字体的颜色

        //tab切换
        tabHostZSZL.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Toast.makeText(XcInfoZszlActivity.this, tabId, Toast.LENGTH_SHORT).show();
                tabHostZSZL.setCurrentTabByTag(tabId);
                updateTab(tabHostZSZL);
            }
        });











    }
    /**
     * 更新Tab标签的颜色，和字体的颜色
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            //标题居中
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0); //取消文字底边对齐
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE); //设置文字居中对齐
            tv.setTextSize(14);
            //tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
            if (tabHost.getCurrentTab() == i) {//选中
                view.setBackgroundDrawable(getResources().getDrawable(R.color.main_menu_title_color));
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.holo_orange_light));
            } else {//不选中
                view.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
