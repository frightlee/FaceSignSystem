package facesign.adplayer.fanhong.fs_sys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
}
