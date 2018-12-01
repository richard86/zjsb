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

public class activityTypeD extends Activity {

    String WTDM;
    String WTMS;
    DBHelper dbHelper;
    RadioGroup radio_d;
    AppData appData;
    EditText eText_ZSZLD_CWMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_d);
        appData=new AppData();
        WTDM="40D";
        WTMS="";
        String dbPath=UtilTool.getDBPath(this);
        dbHelper=new DBHelper(this,dbPath);

        eText_ZSZLD_CWMS=(EditText)findViewById(R.id.eText_ZSZLD_CWMS);
        eText_ZSZLD_CWMS.setEnabled(false);
        radio_d=(RadioGroup)findViewById(R.id.radio_d);
        radio_d.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                if(checkID==R.id.rButton_ZSZLD_ZJYG){
                    WTMS="注记压盖";
                }else if(checkID==R.id.rButton_ZSZLD_FHYG){
                    WTMS="符号压盖";
                }else if(checkID==R.id.rButton_ZSZLD_FHCW){
                    WTMS="符号运用错误";
                }else if(checkID==R.id.rButton_ZSZLD_ZJWZ){
                    WTMS="注记位置不合理";
                }
                eText_ZSZLD_CWMS.setText(WTMS);
            }
        });

        Button button_ZSZLD_INSERT=(Button)findViewById(R.id.button_ZSZLD_INSERT);
        button_ZSZLD_INSERT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(WTMS.length()==0){
                    Toast.makeText(activityTypeD.this, "请选择【问题类型】！", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.open();

                ContentValues contentValues = new ContentValues();
                contentValues.put("P_CLASS", "整饰质量");
                contentValues.put("S_CLASS", "D类问题");
                contentValues.put("XMH", UtilTool.getXMH(activityTypeD.this));
                contentValues.put("PCH", UtilTool.getPCH(activityTypeD.this));
                contentValues.put("YBH", UtilTool.getYBH(activityTypeD.this));
                contentValues.put("WTDM", WTDM);
                contentValues.put("WTMS", WTMS);
                contentValues.put("X",appData.getAppData("X",activityTypeD.this));
                contentValues.put("Y",appData.getAppData("Y",activityTypeD.this));

                dbHelper.insert("N5_Record", contentValues);

                dbHelper.close();

                Toast.makeText(activityTypeD.this, WTMS+"|"+WTDM, Toast.LENGTH_SHORT).show();

                //----恢复控件
                WTMS="";
                eText_ZSZLD_CWMS.setText("");
                WTDM="40D";
                radio_d.check(-1);

            }
        });

    }
}
