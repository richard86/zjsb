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

public class activityTypeC extends Activity {

    String WTDM;
    String WTMS;
    DBHelper dbHelper;
    EditText eText_ZSZLC_CWMS;
    RadioGroup radio_c;
    AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_c);
        appData=new AppData();
        WTDM="40C";
        WTMS="";
        String dbPath=UtilTool.getDBPath(this);
        dbHelper=new DBHelper(this,dbPath);
        eText_ZSZLC_CWMS=(EditText)findViewById(R.id.eText_ZSZLC_CWMS);
        eText_ZSZLC_CWMS.setEnabled(false);
        radio_c=(RadioGroup)findViewById(R.id.radio_c);
        radio_c.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                if(checkID==R.id.rButton_ZSZLC_TKZS){
                    WTMS="图廓整饰存在错误";
                }else if(checkID==R.id.rButton_ZSZLC_FH){
                    WTMS="符号规格不符要求";
                }else if(checkID==R.id.rButton_ZSZLC_XH){
                    WTMS="线划规格不符要求";
                }else if(checkID==R.id.rButton_ZSZLC_ZJ){
                    WTMS="注记规格不符要求";
                }
                eText_ZSZLC_CWMS.setText(WTMS);
            }
        });

        Button button_ZSZLC_INSERT=(Button)findViewById(R.id.button_ZSZLC_INSERT);
        button_ZSZLC_INSERT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(WTMS.length()==0){
                    Toast.makeText(activityTypeC.this, "请选择【问题类型】！", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.open();

                ContentValues contentValues = new ContentValues();
                contentValues.put("P_CLASS", "整饰质量");
                contentValues.put("S_CLASS", "C类问题");
                contentValues.put("XMH", UtilTool.getXMH(activityTypeC.this));
                contentValues.put("PCH", UtilTool.getPCH(activityTypeC.this));
                contentValues.put("YBH", UtilTool.getYBH(activityTypeC.this));
                contentValues.put("WTDM", WTDM);
                contentValues.put("WTMS", WTMS);
                contentValues.put("X",appData.getAppData("X",activityTypeC.this));
                contentValues.put("Y",appData.getAppData("Y",activityTypeC.this));

                dbHelper.insert("N5_Record", contentValues);

                dbHelper.closeclose();

                Toast.makeText(activityTypeC.this, WTMS+"|"+WTDM, Toast.LENGTH_SHORT).show();

                //----恢复控件
                WTMS="";
                eText_ZSZLC_CWMS.setText("");
                WTDM="40C";
                radio_c.check(-1);

            }
        });

    }
}
