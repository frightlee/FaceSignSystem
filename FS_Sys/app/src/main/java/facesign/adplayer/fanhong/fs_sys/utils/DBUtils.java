package facesign.adplayer.fanhong.fs_sys.utils;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import facesign.adplayer.fanhong.fs_sys.App;


/**
 * Created by Administrator on 2017/9/29.
 */

public class DBUtils {
    public static int isSigned(String finalName, String uid, int year, int month, int day, int status) {
        WhereBuilder wb = WhereBuilder.b();
        wb.and("s_name", "=", finalName);
        wb.and("s_uid", "=", uid);
        wb.and("s_year", "=", year);
        wb.and("s_month", "=", month);
        wb.and("s_day", "=", day);
        wb.and("s_status", "=", status);
        SignInfo info = null;
        try {
            info = App.db.selector(SignInfo.class).where(wb).findFirst();
            if (info != null)
                return info.getId();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean doSignIn(String finalName, String uid, int year, int month, int day, String sendTime) {
        SignInfo info = new SignInfo(finalName, uid, year, month, day, sendTime, 1);
        try {
            App.db.saveOrUpdate(info);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean doSignOut(int id, String finalName, String uid, int year, int month, int day, String sendTime) {
        SignInfo info = new SignInfo(finalName, uid, year, month, day, sendTime, 2);
        if (id != -1)
            info.setId(id);
        try {
            App.db.saveOrUpdate(info);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<SignInfo> findList(int year, int month) {
        List<SignInfo> list = null;
        WhereBuilder wb = WhereBuilder.b();
        wb.and("s_year", "=", year);
        wb.and("s_month", "=", month);
        try {
            list = App.db.selector(SignInfo.class).where(wb).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean delete() {
        try {
            App.db.dropTable(SignInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
