package com.kcmap.frame.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;
import com.kcmap.frame.work.DBHelper;

import java.io.File;

public class activityTypeA extends Activity {
    private  String dqpc;

    private Button button_ZSZLA_INSERT;
    private EditText eText_ZSZLA_CWMS;
    private  String workingDirectory;
    private  String dataBaseName = "AppTemplate.sqlite";
    AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_a);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        dqpc =bundle.getString("dqpc");
        //Toast.makeText(this, dqpc, Toast.LENGTH_SHORT).show();


        button_ZSZLA_INSERT= findViewById(R.id.button_ZSZLA_INSERT);
        eText_ZSZLA_CWMS = findViewById(R.id.eText_ZSZLA_CWMS);



        appData = new AppData();
        workingDirectory= appData.getAppData("workPath", this);
        workingDirectory+=File.separator+dqpc;



        DBHelper dbHelper=new DBHelper(this, workingDirectory + File.separator + dataBaseName);
        dbHelper.open();
        boolean a = dbHelper.isTableExist("N5_Record");
        if(a)
        {
            //eText_YSLB.setText("数据库中存在 N5_Record 这张表！");
            Toast.makeText(this, "数据库中存在 N5_Record 这张表！", Toast.LENGTH_LONG).show();
        }else{
            // eText_YSLB.setText("数据库中meiyou N5_Record 这张表！");
            Toast.makeText(this, "数据库中meiyou N5_Record 这张表！", Toast.LENGTH_LONG).show();
        }
        dbHelper.closeclose();


    }
}
