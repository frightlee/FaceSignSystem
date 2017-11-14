package facesign.adplayer.fanhong.fs_sys.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import java.util.Calendar;

import facesign.adplayer.fanhong.fs_sys.utils.DBUtils;

/**
 * Created by Administrator on 2017/10/23.
 */

public class MyService extends Service {
    private Thread thread;

    @Override
    public void onCreate() {
        super.onCreate();
        doCheckBackUp(Calendar.getInstance());
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                long currenttime = System.currentTimeMillis();
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
//				  int minute = c.get(Calendar.MINUTE);
                if (hour == 12 /*&& minute == 0*/)
                    doCheckBackUp(c);
                try {
                    thread.sleep(60 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    private void doCheckBackUp(Calendar c) {
        /**
         * int year,month,day
         */
        DBUtils.addDates(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.stop();
    }

    /**
     * 必须实现的方法，在启动式方式下直接返回NULL
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
