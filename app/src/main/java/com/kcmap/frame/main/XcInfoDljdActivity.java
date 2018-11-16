package com.kcmap.frame.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;
import com.kcmap.frame.work.DBHelper;

import java.io.File;

/**
 * Created by lizhiwei on 2018/11/13.
 */
public class XcInfoDljdActivity extends Activity {
    private  String dqpc;

    private Button button_YSCW_INSERT;
    private EditText eText_YSLB;
    private  String workingDirectory;
    private  String dataBaseName = "AppTemplate.sqlite";
    AppData appData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcinfo_dljd);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        dqpc =bundle.getString("dqpc");
        Toast.makeText(this, dqpc, Toast.LENGTH_SHORT).show();



        button_YSCW_INSERT= findViewById(R.id.button_YSCW_INSERT);
        eText_YSLB = findViewById(R.id.eText_YSLB);
        appData = new AppData();
        workingDirectory= appData.getAppData("workPath", this);
        workingDirectory+=File.separator+dqpc;
        DBHelper dbHelper=new DBHelper(this, workingDirectory + File.separator + dataBaseName);
        dbHelper.open();
        boolean a = dbHelper.isTableExist("XMInfo2");
        if(a)
        {
            //eText_YSLB.setText("数据库中存在 N5_Record 这张表！");
            Toast.makeText(this, "数据库中存在 XMInfo2 这张表！", Toast.LENGTH_SHORT).show();
        }else{
           // eText_YSLB.setText("数据库中meiyou N5_Record 这张表！");
            Toast.makeText(this, "数据库中meiyou XMInfo2 这张表！", Toast.LENGTH_SHORT).show();
        }
        dbHelper.closeclose();










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
