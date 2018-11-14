package com.kcmap.frame.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.utils.AppManager;

/**
 * Created by lizhiwei on 2018/11/13.
 */
public class XcInfoMainActivity extends TabActivity {

    TabHost tabHost=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcinfo_main);

        AppManager.getAppManager().addActivity(this);

        ImageButton backBtn = (ImageButton)findViewById(R.id.xcbtnback);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                XcInfoMainActivity.this.finish();
            }
        });

//--------------------------------------------Tab内容，每个tab填充一个Activity
        tabHost = getTabHost();

        TabSpec spec;
        Intent intent;

        intent = new Intent(this,XcInfoDljdActivity.class);
        spec = tabHost.newTabSpec("地理精度").setIndicator("地理精度").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent(this,XcInfoZszlActivity.class);
        spec = tabHost.newTabSpec("整饰质量").setIndicator("整饰质量").setContent(intent);
        tabHost.addTab(spec);

        //tabHost.setBackgroundColor(Color.argb(100, 255, 255, 255));

        tabHost.setCurrentTab(0);//设置初始tab
        updateTab(tabHost);//初始化Tab的颜色，和字体的颜色

        //tab切换
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Toast.makeText(XcInfoMainActivity.this, tabId, Toast.LENGTH_SHORT).show();
                tabHost.setCurrentTabByTag(tabId);
                updateTab(tabHost);
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


}
