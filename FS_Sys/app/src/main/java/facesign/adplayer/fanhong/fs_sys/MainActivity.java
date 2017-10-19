package facesign.adplayer.fanhong.fs_sys;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import facesign.adplayer.fanhong.fs_sys.models.CameraInfo;
import jna.HCNetSDKByJNA;
import jna.HCNetSDKJNAInstance;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLog";

    @ViewInject(R.id.rcv_sign)
    private RecyclerView recyclerView1;

    //ip,port,user,pwd
    private List<CameraInfo> cameras = new ArrayList<>();

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

//            cameras.add(new CameraInfo(11, "192.168.0.56", "8000", "admin", "fanhong2017"));
            cameras.add(new CameraInfo(12,"A1", "192.168.0.57", "8000", "admin", "fanhong2017"));

            if (cameras.size() > 0) {
                for (int i = 0; i < cameras.size(); i++) {
                    int loginId = hikLogin(cameras.get(i).getIP(), cameras.get(i).getPort(), cameras.get(i).getUser(), cameras.get(i).getPwd());
                    cameras.get(i).setLogin(loginId);
                    Log.e(TAG, "loginId:" + loginId);
                    if (loginId != -1) {
                        Pointer point = new Pointer(cameras.get(i).getNo_());
//                        point.setLong(11,cameras.get(i).getNo_());
                        int alarmId = hikMsgCallback(loginId, point);
                        cameras.get(i).setAlarm(alarmId);
                    }
                }
            } else {
                addCamera();
            }
        } else
            Log.e(TAG, "HCSDKinit Fail!");
    }

    private int hikLogin(String ip, String port, String user, String pwd) {
        int hcLogin = -1;
        try {
            // hikLogin on the device
            hcLogin = loginNormalDevice(ip, Integer.parseInt(port), user, pwd);
            if (hcLogin < 0) {
                Log.e(TAG, "This device logins failed!");
                return -1;
            } else {
                Log.e(TAG, "hcLogin=" + hcLogin);
            }
            // get instance of exception callback and set
            ExceptionCallBack oexceptionCbf = new ExceptionCallBack() {
                public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                    Log.e(TAG, "recv exception, type:" + iType);
                }
            };
            if (oexceptionCbf == null) {
                Log.e(TAG, "ExceptionCallBack object is failed!");
                return -1;
            }
            if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
                    oexceptionCbf)) {
                Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
                return -1;
            }
            Log.i(TAG,
                    "Login sucess ****************************1***************************");

        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
        return hcLogin;
    }

    private int loginNormalDevice(String ip, int port, String user, String pwd) {
        // get instance
        NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
        // call NET_DVR_Login_v30 to hikLogin on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ip, port,
                user, pwd, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "NET_DVR_Login is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");

        return iLogID;
    }

    private int hikMsgCallback(int loginId, Pointer user) {
        boolean r = HCNetSDKJNAInstance.getInstance().NET_DVR_SetDVRMessageCallBack_V30(msgCallback, user);
        Log.e(TAG, "报警回调注册结果：" + (r ? "成功" : "失败"));

        HCNetSDKByJNA.NET_DVR_SETUPALARM_PARAM arlarmParam = new HCNetSDKByJNA.NET_DVR_SETUPALARM_PARAM();
        arlarmParam.dwSize = arlarmParam.size();
        arlarmParam.byRetDevInfoVersion = 1;
        arlarmParam.write();
        int hcalarm = HCNetSDKJNAInstance.getInstance().NET_DVR_SetupAlarmChan_V41(loginId, arlarmParam.getPointer());
        Log.e(TAG, "布防结果：" + (hcalarm != -1 ? "成功" : "失败"));
        return hcalarm;
    }

    private HCNetSDKByJNA.FMSGCallBack msgCallback = new HCNetSDKByJNA.FMSGCallBack() {
        @Override
        public void invoke(int lCommand, HCNetSDKByJNA.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
            Log.e(TAG, "msg:lCommand=" + lCommand + "\npAlarmer:" + pAlarmer);
            if (lCommand == HCNetSDKByJNA.COMM_SNAP_MATCH_ALARM)//人脸黑名单比对结果信息pAlarmInfo
            {
                HCNetSDKByJNA.NET_VCA_FACESNAP_MATCH_ALARM alarmInfo = new HCNetSDKByJNA.NET_VCA_FACESNAP_MATCH_ALARM(pAlarmInfo);
                alarmInfo.read();
                final float fSimilarity = alarmInfo.fSimilarity;//对比相似度
                HCNetSDKByJNA.NET_VCA_FACESNAP_INFO_ALARM snapInfo = alarmInfo.struSnapInfo;//人脸抓拍上传信息
                HCNetSDKByJNA.NET_VCA_BLACKLIST_INFO_ALARM blackListInfo = alarmInfo.struBlackListInfo;//黑名单(预存)
                ByteByReference snapPicBuffer = alarmInfo.pSnapPicBuffer;//抓拍图片

                int absTime = snapInfo.dwAbsTime;

                ByteByReference blackPicBuffer = blackListInfo.pBuffer1;//黑名单图片
                byte[] blackPicBy = blackPicBuffer.getPointer().getByteArray(0L, blackListInfo.dwBlackListPicLen);//
                final Bitmap blackBitmap = BitmapFactory.decodeByteArray(blackPicBy, 0, blackPicBy.length);//黑名单图片

                HCNetSDKByJNA.NET_VCA_BLACKLIST_INFO blackInfo = blackListInfo.struBlackListInfo;//黑名单基本信息
                HCNetSDKByJNA.NET_VCA_HUMAN_ATTRIBUTE attribute = blackInfo.struAttribute;//人员信息
                String name = "", uid = "";
                try {
                    name = new String(attribute.byName, "GBK").trim();//姓名
                    uid = new String(attribute.byCertificateNumber, "GBK").trim();//证件号
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                long devNo_ = Pointer.nativeValue(pUser);

//                send(devNo_, uid, absTime);

            }
        }
    };

    private void addCamera() {
    }

    @Override
    protected void onResume() {
        Display d = getWindow().getWindowManager().getDefaultDisplay();
        if (d.getWidth()<d.getHeight()){
            //设为横屏
            if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (CameraInfo c : cameras) {
            if (c.getAlarm() != -1)
                if (HCNetSDKJNAInstance.getInstance().NET_DVR_CloseAlarmChan_V30(c.getAlarm())) {
                    Log.e(TAG, "login:" + c.getLogin() + "alarm:" + c.getAlarm() + "-->撤防成功");
                    c.setAlarm(-1);
                } else
                    Log.e(TAG, "撤防失败");
            if (c.getLogin() != -1)
                if (HCNetSDKJNAInstance.getInstance().NET_DVR_Logout(c.getLogin())) {
                    Log.e(TAG, "login:" + c.getLogin() + "-->注销登录成功");
                    c.setLogin(-1);
//                    cameras.remove(c);
                } else {
                    Log.e(TAG, "注销登录失败");
                }
        }
    }
}
