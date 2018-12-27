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

public class activityTypeD extends Activity {

    String WTDM;
    String WTMS;
    DBHelper dbHelper;
    RadioGroup radio_d;
    AppData appData;
    EditText eText_ZSZLD_CWMS;
    AnyResponse delegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_d);
        AppManager.getAppManager().addActivity(this);
        Stack<Activity> activityStack=AppManager.getAppManager().getActivityStack();
        for(Activity activity:activityStack){
            if(activity instanceof MainActivity){
                this.delegate=(AnyResponse)activity;
                break;
            }
        }
        appData=new AppData();
        WTDM="40D";
        WTMS="";
        String dbPath=UtilTool.getDBPath(this);
        dbHelper=new DBHelper(this,dbPath);

        eText_ZSZLD_CWMS=(EditText)findViewById(R.id.eText_ZSZLD_CWMS);
        //eText_ZSZLD_CWMS.setEnabled(false);
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

                String UID=appData.getAppData("UID",activityTypeD.this);

                if(appData.getAppData("TAG",activityTypeD.this).equalsIgnoreCase("POINT")){
                    String X=appData.getAppData("X",activityTypeD.this);
                    String Y=appData.getAppData("Y",activityTypeD.this);
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

                Toast.makeText(activityTypeD.this, WTMS+"|"+WTDM, Toast.LENGTH_SHORT).show();

                //----恢复控件
                WTMS="";
                eText_ZSZLD_CWMS.setText("");
                WTDM="40D";
                radio_d.check(-1);
                delegate.ResponseUID(UID);
                activityTypeD.this.getParent().getParent().finish();
            }
        });

    }
}
