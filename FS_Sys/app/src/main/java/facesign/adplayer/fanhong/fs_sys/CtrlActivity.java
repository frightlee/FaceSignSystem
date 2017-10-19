package facesign.adplayer.fanhong.fs_sys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import facesign.adplayer.fanhong.fs_sys.DbTables.InputWorkers;

@ContentView(R.layout.activity_ctrl)
public class CtrlActivity extends AppCompatActivity {
    @ViewInject(R.id.load_in_workers)
    Button loadIn;
    @ViewInject(R.id.set_in_time)
    Button setIntime;
    @ViewInject(R.id.set_out_time)
    Button setOuttime;
    @ViewInject(R.id.remove_camera)
    Button removeCamera;
    @ViewInject(R.id.load_out_messages)
    Button loadOut;
    @ViewInject(R.id.change_password)
    Button changePassword;

    //设定时间弹窗里的控件
    TextView tv;
    EditText setHour, setMinute;
    Button submit;

    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    private void init() {
        mSharedPref = getApplicationContext().getSharedPreferences(App.SP_NAME, Context.MODE_PRIVATE);
    }
    @Event({R.id.load_in_workers,R.id.set_in_time,R.id.set_out_time,R.id.remove_camera,R.id.load_out_messages,R.id.change_password})
    private void onClick(View v){
        switch (v.getId()){
            case R.id.load_in_workers:
                loadInworkers(CtrlActivity.this);
                break;
            case R.id.set_in_time:
                setIntime(CtrlActivity.this);
                break;
            case R.id.set_out_time:
                break;
            case R.id.remove_camera:
                break;
            case R.id.load_out_messages:
                break;
            case R.id.change_password:
                break;
        }
    }

    public void loadInworkers(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("导入员工信息");
        builder.setMessage("请将命名为‘workers.xls’的Excel表放入主目录的‘inputFS’的文件夹下");
        builder.setPositiveButton("文件已确认，下一步", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputWorkers.readExcel();
            }
        });
    }
    public void setIntime(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_set_time, null);
        builder.setView(view);
        tv = view.findViewById(R.id.dialog_title);
        setHour = view.findViewById(R.id.input_hour);
        setMinute = view.findViewById(R.id.input_minute);
        submit = view.findViewById(R.id.submit);
        tv.setText("设置上班时间（24小时制）");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(setHour.getText()) || TextUtils.isEmpty(setMinute.getText())) {
                    Toast.makeText(context, "请输入上班时间", Toast.LENGTH_SHORT).show();
                } else {
                    int hour = Integer.parseInt(setHour.getText().toString());
                    int minute = Integer.parseInt(setMinute.getText().toString());
                    if (hour > 23 || hour < 0 || minute > 59 || minute < 0) {
                        Toast.makeText(context, "请设定正确的时间", Toast.LENGTH_SHORT).show();
                    } else {
                        mSharedPref.edit().putString(App.IN_ITME,hour+"_"+minute).commit();
                        Log.i("xq","commit intime==>success");
                    }
                }
            }
        });
    }
    public void setOuttime(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_set_time, null);
        builder.setView(view);
        tv = view.findViewById(R.id.dialog_title);
        setHour = view.findViewById(R.id.input_hour);
        setMinute = view.findViewById(R.id.input_minute);
        submit = view.findViewById(R.id.submit);
        tv.setText("设置下班时间（24小时制）");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(setHour.getText()) || TextUtils.isEmpty(setMinute.getText())) {
                    Toast.makeText(context, "请输入下班时间", Toast.LENGTH_SHORT).show();
                } else {
                    int hour = Integer.parseInt(setHour.getText().toString());
                    int minute = Integer.parseInt(setMinute.getText().toString());
                    if (hour > 23 || hour < 0 || minute > 59 || minute < 0) {
                        Toast.makeText(context, "请设定正确的时间", Toast.LENGTH_SHORT).show();
                    } else {
                        mSharedPref.edit().putString(App.OUT_TIME,hour+"_"+minute).commit();
                        Log.i("xq","commit intime==>success");
                    }
                }
            }
        });
    }
}
