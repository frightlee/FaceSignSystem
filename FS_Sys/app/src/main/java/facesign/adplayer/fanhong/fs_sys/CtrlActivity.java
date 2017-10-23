package facesign.adplayer.fanhong.fs_sys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import facesign.adplayer.fanhong.fs_sys.dbtables.ChildOfWorkersTable;
import facesign.adplayer.fanhong.fs_sys.dbtables.InputWorkers;
import facesign.adplayer.fanhong.fs_sys.dbtables.OutputRecord;


@ContentView(R.layout.activity_ctrl)
public class CtrlActivity extends AppCompatActivity {
//    @ViewInject(R.id.load_in_workers)
//    Button loadIn;
//    @ViewInject(R.id.set_in_time)
//    Button setIntime;
//    @ViewInject(R.id.set_out_time)
//    Button setOuttime;
//    @ViewInject(R.id.remove_camera)
//    Button removeCamera;
//    @ViewInject(R.id.load_out_messages)
//    Button loadOut;
//    @ViewInject(R.id.change_password)
//    Button changePassword;

    //设定时间弹窗里的控件
    TextView tv, hy, mm;
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
    @Event(value = R.id.open_call,type = CompoundButton.OnCheckedChangeListener.class)
    private void checkClick(CompoundButton buttonView, boolean isChecked){
        if(isChecked){
            App.blackAlarm = true;
        }else {
            App.blackAlarm = false;
        }
    }

    @Event({R.id.load_in_workers, R.id.set_in_time, R.id.set_out_time, R.id.load_out_messages, R.id.change_password, R.id.btn_back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_in_workers:
                try {
                    App.db.dropTable(ChildOfWorkersTable.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                loadInworkers(CtrlActivity.this);
                break;
            case R.id.set_in_time:
                setIntime(CtrlActivity.this);
                break;
            case R.id.set_out_time:
                setOuttime(CtrlActivity.this);
                break;
            case R.id.load_out_messages:
//                OutputRecord or = new OutputRecord(2017, 10);
//                or.writeExcel(or.getListOfOutputExcel());
                outExcel(CtrlActivity.this);
                break;
            case R.id.change_password:
                changeSuperPwd();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void changeSuperPwd() {
        final EditText edt = new EditText(this);
        new AlertDialog.Builder(this).setTitle("更改管理员密码").setView(edt).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPwd = edt.getText().toString().trim();
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("superPwd", newPwd);
                if (editor.commit()) {
                    App.superPwd = newPwd;
                    new AlertDialog.Builder(CtrlActivity.this).setMessage("设置成功！").show();
                } else
                    new AlertDialog.Builder(CtrlActivity.this).setMessage("设置失败！").show();
            }
        }).setNegativeButton("取消", null).show();
    }

    public void loadInworkers(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("导入员工信息");
        builder.setMessage("请将命名为‘workers.xls’的Excel表放入主目录的‘inputFS’的文件夹下");
        builder.setPositiveButton("文件已确认，下一步", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int length = InputWorkers.readExcel();
                if (length > 0) {
                    Toast.makeText(context, "导入成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "导入失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setIntime(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_set_time, null);
        builder.setView(view);
        tv = view.findViewById(R.id.dialog_title);
        setHour = view.findViewById(R.id.input_hour);
        setMinute = view.findViewById(R.id.input_minute);
        submit = view.findViewById(R.id.submit);
        tv.setText("设置上班时间（24小时制）");
        final AlertDialog dialog = builder.create();
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
                        mSharedPref.edit().putString(App.IN_ITME, hour + "_" + minute).commit();
                        Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
//                        Log.i("xq", "commit intime==>success");
                    }
                }
            }
        });
        dialog.show();
    }

    public void setOuttime(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_set_time, null);
        builder.setView(view);
        tv = view.findViewById(R.id.dialog_title);
        setHour = view.findViewById(R.id.input_hour);
        setMinute = view.findViewById(R.id.input_minute);
        submit = view.findViewById(R.id.submit);
        tv.setText("设置下班时间（24小时制）");
        final AlertDialog dialog = builder.create();
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
                        mSharedPref.edit().putString(App.OUT_TIME, hour + "_" + minute).commit();
                        Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
//                        Log.i("xq", "commit intime==>success");
                    }
                }
            }
        });
        dialog.show();
    }

    public void outExcel(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_set_time, null);
        builder.setView(view);
        tv = view.findViewById(R.id.dialog_title);
        hy = view.findViewById(R.id.hour_year);
        mm = view.findViewById(R.id.minute_month);
        setHour = view.findViewById(R.id.input_hour);
        setMinute = view.findViewById(R.id.input_minute);
        submit = view.findViewById(R.id.submit);
        tv.setText("输入要导出记录的年和月");
        hy.setText("年");
        mm.setText("月");
        final AlertDialog dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(setHour.getText()) && !TextUtils.isEmpty(setMinute.getText())) {
                    int year = Integer.parseInt(setHour.getText().toString());
                    int month = Integer.parseInt(setMinute.getText().toString());
                    OutputRecord or = new OutputRecord(year, month);
                    if ((or.getListOfOutputExcel() != null && or.writeExcel(or.getListOfOutputExcel()) > 0)) {
                        Toast.makeText(context, "导出成功！", Toast.LENGTH_SHORT).show();
                        outSuccess(context);
                    } else {
                        Toast.makeText(context, "导出失败！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "导出失败！", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void outSuccess(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("Excel表已保存于 inputFS文件夹目录下");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Display d = getWindow().getWindowManager().getDefaultDisplay();
        if (d.getWidth() < d.getHeight()) {
            //设为横屏
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }
}
