package com.kcmap.frame.main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;
import com.kcmap.frame.work.DBHelper;

import java.io.File;

/**
 * Created by lizhiwei on 2018/11/13.
 */
public class XcInfoDljdActivity extends Activity {
    String XMH;//项目号
    String PCH;//批次号
    String YBH;//样本号
    String workingDirectory;
    String dataBaseName;
    DBHelper dbHelper;
    AppData appData;

    String WTDM="30";
    RadioGroup errorclasstype;
    RadioGroup ycerrortype;
    RadioGroup mcerrortype;
    EditText eText_YSLB;
    EditText eText_CWMC;
    Button button_YSCW_INSERT;
    Button button_MCCW_INSERT;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xcinfo_dljd);

        appData = new AppData();
        XMH=appData.getAppData("XMH",XcInfoDljdActivity.this);
        PCH=appData.getAppData("PCH",XcInfoDljdActivity.this);
        YBH=appData.getAppData("YBH",XcInfoDljdActivity.this);

        workingDirectory=appData.getAppData("workPath", this);
        workingDirectory+=File.separator+PCH;

        dataBaseName = "AppTemplate.sqlite";

        dbHelper=new DBHelper(this, workingDirectory + File.separator + dataBaseName);

        errorclasstype=(RadioGroup)findViewById(R.id.errorclasstype);
        errorclasstype.setOnCheckedChangeListener(errorclasstype_OnCheckedChangeListener);

        ycerrortype=(RadioGroup)findViewById(R.id.yserrortype);
        mcerrortype=(RadioGroup)findViewById(R.id.mcerrortype);

        eText_YSLB = (EditText)findViewById(R.id.eText_YSLB);
        eText_CWMC = (EditText)findViewById(R.id.eText_CWMC);


        button_YSCW_INSERT= (Button) findViewById(R.id.button_YSCW_INSERT);
        button_YSCW_INSERT.setOnClickListener(button_YSCW_INSERT_Click);

        button_MCCW_INSERT= (Button) findViewById(R.id.button_MCCW_INSERT);
        button_MCCW_INSERT.setOnClickListener(button_MCCW_INSERT_Click);



    }

    //-------------------------错误分类码change事件
    private RadioGroup.OnCheckedChangeListener errorclasstype_OnCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
            //----------错误分类码
            WTDM="30";
            if(checkID==R.id.rButton_CWFLM_A){
                WTDM+="A";

            }else if(checkID==R.id.rButton_CWFLM_B){
                WTDM+="B";

            }else if(checkID==R.id.rButton_CWFLM_C){
                WTDM+="C";

            }else if(checkID==R.id.rButton_CWFLM_D){
                WTDM+="D";
            }else {
                //未选中
                WTDM="30";
            }
        }
    };

    //-------------------------要素类别错误录入按钮事件
    View.OnClickListener button_YSCW_INSERT_Click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String WTMS="";
            String YSCWLB=eText_YSLB.getText().toString();

            if(WTDM=="30"){
                Toast.makeText(XcInfoDljdActivity.this, "请选择一个【错误分类代码】！", Toast.LENGTH_SHORT).show();
                return;
            }

            if(YSCWLB.isEmpty() || YSCWLB.length()==0){
                Toast.makeText(XcInfoDljdActivity.this, "请填写【要素类别】！", Toast.LENGTH_SHORT).show();
                return;
            }else{
                WTMS=YSCWLB;
            }

            //--------问题描述
            int checkID=ycerrortype.getCheckedRadioButtonId();
            if(checkID>0){
                WTMS+=((RadioButton)findViewById(checkID)).getText();
            }else {
                Toast.makeText(XcInfoDljdActivity.this, "请选择一个【错误类型】！", Toast.LENGTH_SHORT).show();
                return;
            }

            //----------插入数据库

            dbHelper.open();

            ContentValues contentValues = new ContentValues();
            contentValues.put("P_CLASS", "地理精度");
            contentValues.put("S_CLASS", "要素错误");
            contentValues.put("XMH", XMH);
            contentValues.put("PCH", PCH);
            contentValues.put("YBH", YBH);
            contentValues.put("WTDM", WTDM);
            contentValues.put("WTMS", WTMS);
            contentValues.put("X",appData.getAppData("X",XcInfoDljdActivity.this));
            contentValues.put("Y",appData.getAppData("Y",XcInfoDljdActivity.this));

            dbHelper.insert("N5_Record", contentValues);

            dbHelper.closeclose();
            Toast.makeText(XcInfoDljdActivity.this, WTMS+"|"+WTDM, Toast.LENGTH_SHORT).show();
            //----------清空控件值
            WTDM="30";
            errorclasstype.check(-1);
            eText_YSLB.setText("");
            ycerrortype.check(-1);

        }
    };

    View.OnClickListener button_MCCW_INSERT_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String WTMS="";
            String CWMC=eText_CWMC.getText().toString();

            if(WTDM=="30"){
                Toast.makeText(XcInfoDljdActivity.this, "请选择一个【错误分类代码】！", Toast.LENGTH_SHORT).show();
                return;
            }

            if(CWMC.isEmpty() || CWMC.length()==0){
                Toast.makeText(XcInfoDljdActivity.this, "请填写【错误名称】！", Toast.LENGTH_SHORT).show();
                return;
            }else {
                WTMS=CWMC;
            }

            int checkID=mcerrortype.getCheckedRadioButtonId();
            if(checkID>0){
                WTMS+=((RadioButton)findViewById(checkID)).getText();
            }else {
                Toast.makeText(XcInfoDljdActivity.this, "请选择一个【错误类型】！", Toast.LENGTH_SHORT).show();
                return;
            }



            //----------插入数据库

            dbHelper.open();

            ContentValues contentValues = new ContentValues();
            contentValues.put("P_CLASS", "地理精度");
            contentValues.put("S_CLASS", "名称错误");
            contentValues.put("XMH", XMH);
            contentValues.put("PCH", PCH);
            contentValues.put("YBH", YBH);
            contentValues.put("WTDM", WTDM);
            contentValues.put("WTMS", WTMS);
            contentValues.put("X",appData.getAppData("X",XcInfoDljdActivity.this));
            contentValues.put("Y",appData.getAppData("Y",XcInfoDljdActivity.this));

            dbHelper.insert("N5_Record", contentValues);

            dbHelper.closeclose();

            //----------清空控件值
            WTDM="30";
            errorclasstype.check(-1);
            eText_CWMC.setText("");
            mcerrortype.check(-1);
        }
    };


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
