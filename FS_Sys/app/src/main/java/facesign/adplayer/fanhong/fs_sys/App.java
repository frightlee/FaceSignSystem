package facesign.adplayer.fanhong.fs_sys;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2017/10/16.
 */

public class App extends Application {
    public static final String[] RESULTS = {"正常上班","迟到","早退","正常下班"};
    public static final String SP_NAME = "f_s_setting"; //sharedprefrences名字
    public static final String IN_ITME = "in_time";  //上班时间字段名
    public static final String OUT_TIME = "out_time"; //下班时间字段名
    public static String superPwd = "2234";
    //备份接口
    public static final String SAVEURL = "http://m.wuyebest.com/public/LTSXT/kq.php";
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }

    static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("fs_record.db")
            .setDbDir(new File(Environment.getExternalStorageDirectory().getPath()))
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                }
            })
            .setTableCreateListener(new DbManager.TableCreateListener() {
                @Override
                public void onTableCreated(DbManager db, TableEntity<?> table) {
                    Log.i("JAVA", "onTableCreated：" + table.getName());
                }
            })
            .setAllowTransaction(true);
    public static DbManager db = x.getDb(daoConfig);
}
