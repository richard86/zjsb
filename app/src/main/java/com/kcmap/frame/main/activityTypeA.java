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
import com.kcmap.frame.utils.AppManager;
import com.kcmap.frame.work.AnyResponse;
import com.kcmap.frame.work.DBHelper;
import com.kcmap.frame.work.UtilTool;

import java.util.Stack;

public class activityTypeA extends Activity {

    String WTDM;
    String WTMS;
    DBHelper dbHelper;
    RadioGroup radio_a;
    EditText eText_ZSZLA_CWMS;
    AppData appData;
    AnyResponse delegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_a);
        AppManager.getAppManager().addActivity(this);
        Stack<Activity> activityStack=AppManager.getAppManager().getActivityStack();
        for(Activity activity:activityStack){
            if(activity instanceof MainActivity){
                this.delegate=(AnyResponse)activity;
                break;
            }
        }
        appData=new AppData();
        WTDM="40A";
        WTMS="";
        String dbPath=UtilTool.getDBPath(this);
        dbHelper=new DBHelper(this,dbPath);
        eText_ZSZLA_CWMS=(EditText)findViewById(R.id.eText_ZSZLA_CWMS);
        //eText_ZSZLA_CWMS.setEnabled(false);
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
                eText_ZSZLA_CWMS.setText(WTMS);

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

                dbHelper.open();

                ContentValues contentValues = new ContentValues();
                contentValues.put("P_CLASS", "整饰质量");
                contentValues.put("S_CLASS", "A类问题");
                contentValues.put("XMH", UtilTool.getXMH(activityTypeA.this));
                contentValues.put("PCH", UtilTool.getPCH(activityTypeA.this));
                contentValues.put("YBH", UtilTool.getYBH(activityTypeA.this));
                contentValues.put("WTDM", WTDM);
                contentValues.put("WTMS", WTMS);

                String UID=appData.getAppData("UID",activityTypeA.this);

                if(appData.getAppData("TAG",activityTypeA.this).equalsIgnoreCase("POINT")){
                    String X=appData.getAppData("X",activityTypeA.this);
                    String Y=appData.getAppData("Y",activityTypeA.this);
                    contentValues.put("X",X);
                    contentValues.put("Y",Y);
                    double x=Double.valueOf(X);
                    double y=Double.valueOf(Y);
                    String geoString="POINT:"+x+","+y;
                    UID=java.util.UUID.randomUUID().toString();
                    String insertSql="insert into graphics (geometry,xmin,ymin,xmax,ymax,uid) values ('"+geoString+"',"+x+","+y+","+x+","+y+",'"+UID+"')";
                    dbHelper.execSQL(insertSql);
                }else{
                    dbHelper.execSQL("Update graphics set uid = '"+UID+"' Where uid is null");
                }
                contentValues.put("UID", UID);
                dbHelper.insert("N5_Record", contentValues);

                dbHelper.close();

                Toast.makeText(activityTypeA.this, WTMS+"|"+WTDM, Toast.LENGTH_SHORT).show();

                //----恢复控件
                WTMS="";
                eText_ZSZLA_CWMS.setText("");
                WTDM="40A";
                radio_a.check(-1);
                delegate.ResponseUID(UID);
                activityTypeA.this.getParent().getParent().finish();

            }
        });

    }
}
