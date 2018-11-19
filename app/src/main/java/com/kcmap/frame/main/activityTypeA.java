package com.kcmap.frame.main;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;
import com.kcmap.frame.work.DBHelper;
import com.kcmap.frame.work.UtilTool;

import java.io.File;

public class activityTypeA extends Activity {

    String WTDM;
    String pre_WTMS;
    String WTMS;
    DBHelper dbHelper;
    RadioGroup radio_a;
    AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_a);
        appData=new AppData();
        WTDM="40A";
        WTMS="";
        String dbPath=UtilTool.getDBPath(this);
        dbHelper=new DBHelper(this,dbPath);

        radio_a=(RadioGroup)findViewById(R.id.radio_a);
        radio_a.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                if(checkID==R.id.rButton_ZSZLA_TMTH){
                    WTMS="图名图号同时错误";
                }else if(checkID==R.id.rButton_ZSZLA_TKZS){
                    WTMS="图廓没有整饰";
                }else if(checkID==R.id.rButton_ZSZLA_FH){
                    WTMS="符号规格严重不符要求";
                }else if(checkID==R.id.rButton_ZSZLA_XH){
                    WTMS="线划规格严重不符要求";
                }else if(checkID==R.id.rButton_ZSZLA_ZJ){
                    WTMS="注记规格严重不符要求";
                }

            }
        });

        Button button_ZSZLA_INSERT=(Button)findViewById(R.id.button_ZSZLA_INSERT);
        button_ZSZLA_INSERT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(WTMS.length()==0){
                    Toast.makeText(activityTypeA.this, "请选择【问题类型】！", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText eText_ZSZLA_CWMS=(EditText)findViewById(R.id.eText_ZSZLA_CWMS);
                pre_WTMS=eText_ZSZLA_CWMS.getText().toString();
                if(pre_WTMS.length()==0){
                    Toast.makeText(activityTypeA.this, "请填写【问题描述】！", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    WTMS=pre_WTMS+WTMS;
                }

                dbHelper.open();

                ContentValues contentValues = new ContentValues();
                contentValues.put("P_CLASS", "整饰质量");
                contentValues.put("S_CLASS", "A类问题");
                contentValues.put("XMH", UtilTool.getXMH(activityTypeA.this));
                contentValues.put("PCH", UtilTool.getPCH(activityTypeA.this));
                contentValues.put("YBH", UtilTool.getYBH(activityTypeA.this));
                contentValues.put("WTDM", WTDM);
                contentValues.put("WTMS", WTMS);
                contentValues.put("X",appData.getAppData("X",activityTypeA.this));
                contentValues.put("Y",appData.getAppData("Y",activityTypeA.this));

                dbHelper.insert("N5_Record", contentValues);

                dbHelper.closeclose();

                Toast.makeText(activityTypeA.this, WTMS+"|"+WTDM, Toast.LENGTH_SHORT).show();

                //----恢复控件
                eText_ZSZLA_CWMS.setText(pre_WTMS=WTMS="");
                WTDM="40A";
                radio_a.check(-1);

            }
        });

    }
}
