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

public class activityTypeB extends Activity {

    String WTDM;
    String WTMS;
    DBHelper dbHelper;
    RadioGroup radio_b;
    EditText eText_ZSZLB_CWMS;
    AppData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_b);
        appData=new AppData();
        WTDM="40B";
        WTMS="";
        String dbPath=UtilTool.getDBPath(this);
        dbHelper=new DBHelper(this,dbPath);

        eText_ZSZLB_CWMS=(EditText)findViewById(R.id.eText_ZSZLB_CWMS);
        eText_ZSZLB_CWMS.setEnabled(false);
        radio_b=(RadioGroup)findViewById(R.id.radio_b);
        radio_b.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                if(checkID==R.id.rButton_ZSZLB_TM){
                    WTMS="图名错误";
                }else if(checkID==R.id.rButton_ZSZLB_TH){
                    WTMS="图号错误";
                }else if(checkID==R.id.rButton_ZSZLB_TKZS){
                    WTMS="图廓整饰存在重要错误";
                }else if(checkID==R.id.rButton_ZSZLB_FH){
                    WTMS="符号规格明显严重不符要求";
                }else if(checkID==R.id.rButton_ZSZLB_XH){
                    WTMS="线划规格明显严重不符要求";
                }else if(checkID==R.id.rButton_ZSZLB_ZJ){
                    WTMS="注记规格明显严重不符要求";
                }
                eText_ZSZLB_CWMS.setText(WTMS);
            }
        });

        Button button_ZSZLB_INSERT=(Button)findViewById(R.id.button_ZSZLB_INSERT);
        button_ZSZLB_INSERT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(WTMS.length()==0){
                    Toast.makeText(activityTypeB.this, "请选择【问题类型】！", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.open();

                ContentValues contentValues = new ContentValues();
                contentValues.put("P_CLASS", "整饰质量");
                contentValues.put("S_CLASS", "B类问题");
                contentValues.put("XMH", UtilTool.getXMH(activityTypeB.this));
                contentValues.put("PCH", UtilTool.getPCH(activityTypeB.this));
                contentValues.put("YBH", UtilTool.getYBH(activityTypeB.this));
                contentValues.put("WTDM", WTDM);
                contentValues.put("WTMS", WTMS);
                contentValues.put("X",appData.getAppData("X",activityTypeB.this));
                contentValues.put("Y",appData.getAppData("Y",activityTypeB.this));

                dbHelper.insert("N5_Record", contentValues);

                dbHelper.close();

                Toast.makeText(activityTypeB.this, WTMS+"|"+WTDM, Toast.LENGTH_SHORT).show();

                //----恢复控件
                WTMS="";
                eText_ZSZLB_CWMS.setText("");
                WTDM="40B";
                radio_b.check(-1);

            }
        });

    }
}
