package com.kcmap.frame.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.kcmap.frame.R;

/**
 * Created by lizhiwei on 2018/11/13.
 */
public class XcInfoZszlActivity extends Activity {
    private  String dqpc;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcinfo_zszl);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        dqpc =bundle.getString("dqpc");
        Toast.makeText(this, dqpc, Toast.LENGTH_SHORT).show();
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
