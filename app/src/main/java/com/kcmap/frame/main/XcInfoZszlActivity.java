package com.kcmap.frame.main;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.kcmap.frame.R;

/**
 * Created by lizhiwei on 2018/11/13.
 */
public class XcInfoZszlActivity extends ActivityGroup {

    TabHost tabHostZSZL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcinfo_zszl);

        tabHostZSZL = (TabHost)findViewById(R.id.mytabhost);
        tabHostZSZL.setup(this.getLocalActivityManager());
        TabWidget tabWidget = tabHostZSZL.getTabWidget();


        TabHost.TabSpec spec;
        Intent intent;


        intent = new Intent(this,activityTypeA.class);
        spec = tabHostZSZL.newTabSpec("A类问题").setIndicator("A类问题").setContent(intent);
        tabHostZSZL.addTab(spec);


        intent = new Intent(this,activityTypeB.class);
        spec = tabHostZSZL.newTabSpec("B类问题").setIndicator("B类问题").setContent(intent);
        tabHostZSZL.addTab(spec);

        intent = new Intent(this,activityTypeC.class);
        spec = tabHostZSZL.newTabSpec("C类问题").setIndicator("C类问题").setContent(intent);
        tabHostZSZL.addTab(spec);

        intent = new Intent(this,activityTypeD.class);
        spec = tabHostZSZL.newTabSpec("D类问题").setIndicator("D类问题").setContent(intent);
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
