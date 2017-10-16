package facesign.adplayer.fanhong.fs_sys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.hikvision.netsdk.HCNetSDK;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import facesign.adplayer.fanhong.fs_sys.utils.CameraInfo;
import jna.HCNetSDKJNAInstance;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLog";
    private static final String SP_NAME = "f_s_setting";

    @ViewInject(R.id.recyclerView1)
    private RecyclerView recyclerView1;

    //ip,port,user,pwd
    private List<CameraInfo> cameras = new ArrayList<>();
    private int hcLogin = -1;//登录注册设备ID
    private int hcalarm = -1;//报警布防号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        x.view().inject(this);
        initHCNetSDK();
    }

    private void initHCNetSDK() {

        if (HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(TAG, "HCSDKinit Success!");
            if (cameras.size() > 0) {
//                if (hcLogin == -1) {
//                    hikLogin(settingsHC[0], settingsHC[1], settingsHC[2], settingsHC[3]);
//                }
//                if (hcLogin != -1)
//                    hikMsgCallback();
                for (int i=0;i<cameras.size();i++) {
                    int loginId=hikLogin(cameras.get(i).getIP(),cameras.get(i).getPort(),cameras.get(i).getUser(),cameras.get(i).getPwd());
                    cameras.get(i).setLogin(loginId);
                }
            } else {
                addCamera();
            }
        } else
            Log.e(TAG, "HCSDKinit Fail!");
    }

    private int hikLogin(String ip, String port, String user, String pwd) {
        return -1;
    }

    private void addCamera() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hcalarm != -1)
            if (HCNetSDKJNAInstance.getInstance().NET_DVR_CloseAlarmChan_V30(hcalarm)) {
                Log.e(TAG, "撤防成功");
                hcalarm = -1;
            } else
                Log.e(TAG, "撤防失败");
    }
}
