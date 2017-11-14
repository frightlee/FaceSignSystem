package facesign.adplayer.fanhong.fs_sys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import autolayout.AutoRelativeLayout;
import autolayout.config.AutoLayoutConifg;
import facesign.adplayer.fanhong.fs_sys.adapers.SignCardAdapter;
import facesign.adplayer.fanhong.fs_sys.models.CameraInfo;
import facesign.adplayer.fanhong.fs_sys.utils.CameraCardAdapter;
import facesign.adplayer.fanhong.fs_sys.utils.HCTimeUtils;
import facesign.adplayer.fanhong.fs_sys.utils.JsonUtils;
import jna.HCNetSDKByJNA;
import jna.HCNetSDKJNAInstance;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLog";
    public static final int SCREEN_HORIZONTAL = 0;
    public static final int SCREEN_VERITICAL = 1;

    private int screenOritation = SCREEN_HORIZONTAL;

    @ViewInject(R.id.rcv_sign)
    private RecyclerView recyclerView1;
    @ViewInject(R.id.layout_left)
    private AutoRelativeLayout layoutLeft;
    @ViewInject(R.id.lv_cameras)
    private AbsListView lvCameras;//ListView or GridView
    @ViewInject(R.id.btn_more)
    private Button btnMore;

    //ip,port,user,pwd
    private List<CameraInfo> cameras = new ArrayList<>();
    private List<SignCardAdapter.SignCard> signCards = new ArrayList<>();
    private CameraCardAdapter cameraAdapter;
    private SignCardAdapter signAdapter;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display d = getWindow().getWindowManager().getDefaultDisplay();
        if (d.getWidth() < d.getHeight()) {
            setContentView(R.layout.activity_main_vertical);
            screenOritation = SCREEN_VERITICAL;
            AutoLayoutConifg.getInstance().useDynamicDesignSize(1080, 1920);
        } else {
            setContentView(R.layout.activity_main);
            screenOritation = SCREEN_HORIZONTAL;
            AutoLayoutConifg.getInstance().useDynamicDesignSize(1920, 1080);
        }
        x.view().inject(this);
        String superPwd = getSharedPreferences(App.SP_NAME, Context.MODE_PRIVATE).getString("superPwd", App.superPwd);
        App.superPwd = superPwd;

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.load(this, R.raw.warnning, 1);
        soundPool.load(this, R.raw.ding, 1);

        initViews();
        SharedPreferences sp = getSharedPreferences(App.SP_NAME, Context.MODE_PRIVATE);
        String cameraStr = sp.getString("cameras", "-1");
        cameras = JsonUtils.getCameras(cameras, cameraStr);
        initHCNetSDK();
    }

    private void initViews() {
        cameraAdapter = new CameraCardAdapter(cameras, this);
        lvCameras.setAdapter(cameraAdapter);
        lvCameras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final CameraInfo info = cameras.get(position);
                new AlertDialog.Builder(MainActivity.this).setTitle(info.getNo_() + "-" + info.getAlias()).setNegativeButton("修改参数", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] defaults = {info.getNo_() + "", info.getAlias(), info.getIP(), info.getPort(), info.getUser(), info.getPwd()};
                        if (hikCloseAlarmLogout(position)) addCamera(position, defaults);
                    }
                }).setPositiveButton("删除摄像头", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hikCloseAlarmLogout(position))
                            if (cameras.remove(cameras.get(position))) {
                                saveCamera();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(MainActivity.this).setMessage("删除成功！").show();
                                        cameraAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                    }
                }).setNeutralButton("取消", null).show();
            }
        });
        if (screenOritation == SCREEN_VERITICAL) {
            ((GridView) lvCameras).setNumColumns(4);
        }

        signAdapter = new SignCardAdapter(this, signCards, screenOritation);
        recyclerView1.setAdapter(signAdapter);
//        RecyclerView.LayoutManager lmanager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        GridLayoutManager lmanager;
        if (screenOritation == SCREEN_HORIZONTAL)
            lmanager = new GridLayoutManager(this, 5);
        else
            lmanager = new GridLayoutManager(this, 2);
        lmanager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0)
                    return screenOritation == SCREEN_HORIZONTAL ? 5 : 2;
                return 1;
            }
        });
        recyclerView1.setLayoutManager(lmanager);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 0.5f);
        anim.setDuration(2000);
        anim.setRepeatMode(AlphaAnimation.REVERSE);
        anim.setRepeatCount(AlphaAnimation.INFINITE);
        btnMore.startAnimation(anim);
    }

    private void initHCNetSDK() {
        if (HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(TAG, "HCSDKinit Success!");
            if (cameras.size() > 0) {
                for (CameraInfo c : cameras) {
                    //登录HIK
                    int loginId = hikLogin(c.getIP(), c.getPort(), c.getUser(), c.getPwd());
                    c.setLogin(loginId);
                    Log.e(TAG, "loginId:" + loginId);
                    if (loginId != -1) {
                        Pointer point = new Pointer(c.getNo_());
                        int alarmId = hikMsgCallback(loginId, point);
                        c.setAlarm(alarmId);
                        saveCamera();
                    } else {
                        new AlertDialog.Builder(MainActivity.this).setMessage("可能由于网络原因连接未成功，请手动确认！").setPositiveButton("确认", null).show();
                        return;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cameraAdapter.notifyDataSetChanged();
                    }
                });
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

    //    private List<Long> vt = new ArrayList<>();
    private HCNetSDKByJNA.FMSGCallBack msgCallback = new HCNetSDKByJNA.FMSGCallBack() {

        @Override
        public void invoke(int lCommand, HCNetSDKByJNA.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
            Log.e(TAG, "msg:lCommand=" + lCommand + "\npAlarmer:" + pAlarmer);
            if (lCommand == HCNetSDKByJNA.COMM_SNAP_MATCH_ALARM)//人脸黑名单比对结果信息pAlarmInfo
            {
                HCNetSDKByJNA.NET_VCA_FACESNAP_MATCH_ALARM alarmInfo = new HCNetSDKByJNA.NET_VCA_FACESNAP_MATCH_ALARM(pAlarmInfo);
                alarmInfo.read();
//                final float fSimilarity = alarmInfo.fSimilarity;//对比相似度
                HCNetSDKByJNA.NET_VCA_FACESNAP_INFO_ALARM snapInfo = alarmInfo.struSnapInfo;//人脸抓拍上传信息
                HCNetSDKByJNA.NET_VCA_BLACKLIST_INFO_ALARM blackListInfo = alarmInfo.struBlackListInfo;//黑名单(预存)
//                ByteByReference snapPicBuffer = alarmInfo.pSnapPicBuffer;//抓拍图片

                final int absTime = snapInfo.dwAbsTime;

                ByteByReference blackPicBuffer = blackListInfo.pBuffer1;//黑名单图片
                byte[] blackPicBy = blackPicBuffer.getPointer().getByteArray(0L, blackListInfo.dwBlackListPicLen);//
                Bitmap blackBitmap = BitmapFactory.decodeByteArray(blackPicBy, 0, blackPicBy.length);//黑名单图片

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
                final showCardsInfo card = new showCardsInfo(blackBitmap, name, uid, CameraInfo.findNameByNo_(cameras, devNo_), HCTimeUtils.getTimeStr(absTime));

                if (uid.equals(App.lastUid))
                    if (HCTimeUtils.inXSeconds(absTime, App.lastTime, 5))
                        return;

//                vt.add(System.currentTimeMillis());
                RequestParams params = new RequestParams(App.SAVEURL);
                params.addParameter("uid", uid);//证件号
                params.addParameter("_No", devNo_);//摄像机编号
                params.addParameter("alias", CameraInfo.findNameByNo_(cameras, devNo_));//摄像机别名
                params.addParameter("date", HCTimeUtils.getDateTime(absTime, 1));//年-月-日
                params.addParameter("time", HCTimeUtils.getDateTime(absTime, 2));//时:分:秒
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
//                        vt.add(System.currentTimeMillis());
                        Log.e(TAG, "json=" + result);
                        int r = Integer.parseInt(JsonUtils.getJsonValue(result, "result"));
                        switch (r) {
                            case 1:
                                String data = JsonUtils.getJsonValue(result, "data");
                                String department = JsonUtils.getJsonValue(data, "department");
                                String position = JsonUtils.getJsonValue(data, "position");
                                SignCardAdapter.SignCard signCard = new SignCardAdapter.SignCard(card.bitmap, card.name, department, position, card.alias, card.time);
                                signCards.add(0, signCard);
                                soundPool.play(2, 1, 1, 1, 0, 1);
                                if (signCards.size() > 6) {
                                    for (int i = 6; i < signCards.size(); i++) {
                                        signCards.remove(i);
                                    }
                                }
                                Log.e(TAG, signCards.get(0).toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        showTimeSpend("white", absTime);
                                        signAdapter.notifyDataSetChanged();
                                    }
                                });
                                break;
                            case 2:
                                if (App.blackAlarm) {
//                                    showTimeSpend("black", absTime);
                                    alarmBlack(card.bitmap, card.name, card.uid, card.alias, card.time);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
                App.lastUid = uid;
                App.lastTime = absTime;
            }
        }
    };

//    private void showTimeSpend(String log, int absTime) {
//        Log.i(TAG, "shotime(" + log + "):" + vt.toString());
//        String str = "抓拍时间：";
//        str += HCTimeUtils.getTimeStr(absTime);
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(vt.get(0));
//        str += "\n获取到信号：" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);
//        str += "\n数据建模：+" + (vt.get(1) - vt.get(0));
//        str += "\n数据分析：+" + (vt.get(2) - vt.get(1));
//        str += "\n后台回复：+" + (vt.get(3) - vt.get(2));
//        new AlertDialog.Builder(this).setMessage(str).show();
//    }

    private class showCardsInfo {
        Bitmap bitmap;
        String name, uid, alias, time;

        public showCardsInfo(Bitmap bitmap, String name, String uid, String alias, String time) {
            this.bitmap = bitmap;
            this.name = name;
            this.uid = uid;
            this.alias = alias;
            this.time = time;
        }
    }

    private void alarmBlack(final Bitmap blackBitmap, final String name, final String uid, final String alias, final String timeStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View layout = View.inflate(MainActivity.this, R.layout.dialog_black, null);
                ImageView imageView = layout.findViewById(R.id.img_head);
                TextView tvName = layout.findViewById(R.id.tv_name);
                TextView tvNo = layout.findViewById(R.id.tv_no);
                TextView tvCamera = layout.findViewById(R.id.tv_camera);
                TextView tvTime = layout.findViewById(R.id.tv_time);
                imageView.setImageBitmap(blackBitmap);
                tvName.setText("姓名：" + name);
                tvNo.setText("编号：" + uid);
                tvCamera.setText("摄像机：" + alias);
                StringBuffer sbf = new StringBuffer(timeStr);
                sbf.insert(sbf.indexOf("\u3000"), "\n\u3000\u3000\u3000\u3000");
                tvTime.setText("时间：" + sbf.toString());
                soundPool.play(1, 1, 1, 1, 0, 1);
                final Dialog alertBlack = new AlertDialog.Builder(MainActivity.this).setView(layout).create();
                alertBlack.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertBlack.dismiss();
                    }
                }, 10 * 1000);
            }
        });
    }

    private void addCamera(final int position, String[] defaultStr) {
        LinearLayout layout_add_dialog = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);
        final EditText[] edts = new EditText[6];
        String[] hints = {"编号", "别名", "IP", "port", "user", "password"};
        for (int i = 0; i < 6; i++) {
            edts[i] = new EditText(this);
//            edts[i].setHintTextColor(0xededed);
            edts[i].setHint(hints[i]);
//            edts[i].setEms(10);
            if (defaultStr != null)
                edts[i].setText(defaultStr[i]);
            layout_add_dialog.addView(edts[i]);
        }
        edts[0].setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        layout_add_dialog.setOrientation(LinearLayout.VERTICAL);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.addView(layout_add_dialog);
        new AlertDialog.Builder(this).setTitle("添加摄像头").setView(scrollView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long no = Long.parseLong(edts[0].getText().toString().trim());
                String alias = edts[1].getText().toString();
                String ip = edts[2].getText().toString().trim();
                String port = edts[3].getText().toString().trim();
                String user = edts[4].getText().toString().trim();
                String pwd = edts[5].getText().toString().trim();
                if (position == -1) {
                    cameras.add(0, new CameraInfo(no, alias, ip, port, user, pwd));//添加到第0个
                } else {
                    cameras.set(position, new CameraInfo(no, alias, ip, port, user, pwd));//更改
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cameraAdapter.notifyDataSetChanged();
                        Log.e(TAG, "notify success");
                    }
                });
                //登录HIK
                int loginId = hikLogin(ip, port, user, pwd);
                if (position == -1)
                    cameras.get(0).setLogin(loginId);
                else
                    cameras.get(position).setLogin(loginId);
                Log.e(TAG, "loginId:" + loginId);
                if (loginId != -1) {
                    Pointer point = new Pointer(no);
                    int alarmId = hikMsgCallback(loginId, point);
                    if (position == -1)
                        cameras.get(0).setAlarm(alarmId);
                    else
                        cameras.get(position).setAlarm(alarmId);
                    if (alarmId != -1)
                        new AlertDialog.Builder(MainActivity.this).setMessage("添加成功!").setPositiveButton("确定", null).show();
                    saveCamera();
                } else
                    new AlertDialog.Builder(MainActivity.this).setMessage("登陆失败，请检查后重试!").setPositiveButton("确定", null).show();
            }
        }).setNegativeButton("取消", null).show();
    }

    private void saveCamera() {
        SharedPreferences sp = getSharedPreferences(App.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String cameraList = JsonUtils.toJsonString(cameras);
        editor.putString("cameras", cameraList);
        editor.apply();
    }

    @Event({R.id.btn_add, R.id.btn_more, R.id.btn_set})
    private void onClicks(View v) {
        switch (v.getId()) {
            case R.id.btn_more:
                if (layoutLeft.getVisibility() == View.GONE) {
                    final EditText edtSuperPwd = new EditText(this);
                    new AlertDialog.Builder(this).setTitle("验证").setMessage("请输入管理员密码").setView(edtSuperPwd).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pwd = edtSuperPwd.getText().toString().trim();
                            if (pwd.equals(App.superPwd) || pwd.equals("-fhtxinovation")) {
                                layoutLeft.setVisibility(View.VISIBLE);
                                if (screenOritation == SCREEN_VERITICAL) {
                                    findViewById(R.id.tv_bottom).setVisibility(View.GONE);
                                }
                            }
                        }
                    }).setNegativeButton("取消", null).show();
                } else {
                    layoutLeft.setVisibility(View.GONE);
                    if (screenOritation == SCREEN_VERITICAL) {
                        findViewById(R.id.tv_bottom).setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.btn_add:
                addCamera(-1, null/*new String[]{"57", "A2", "192.168.0.57", "8000", "admin", "fanhong2017"}*/);
                break;
            case R.id.btn_set:
                startActivity(new Intent(this, CtrlActivity.class));
                break;
        }
    }

    private boolean hikCloseAlarmLogout(int position) {
        CameraInfo c = cameras.get(position);
        if (c.getAlarm() != -1)
            if (HCNetSDKJNAInstance.getInstance().NET_DVR_CloseAlarmChan_V30(c.getAlarm())) {
                Log.e(TAG, "login:" + c.getLogin() + "alarm:" + c.getAlarm() + "-->撤防成功");
                c.setAlarm(-1);
            } else
                return false;
        if (c.getLogin() != -1)
            if (HCNetSDKJNAInstance.getInstance().NET_DVR_Logout(c.getLogin())) {
                Log.e(TAG, "login:" + c.getLogin() + "-->注销登录成功");
                c.setLogin(-1);
            } else
                return false;
        return true;
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
                } else {
                    Log.e(TAG, "注销登录失败");
                }
        }
    }
}
