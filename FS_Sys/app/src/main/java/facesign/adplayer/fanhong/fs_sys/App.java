package facesign.adplayer.fanhong.fs_sys;

import android.app.Application;
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
    private static final String SP_NAME = "f_s_setting";
    public static String superPwd = "2234";

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
