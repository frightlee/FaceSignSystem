package facesign.adplayer.fanhong.fs_sys.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import facesign.adplayer.fanhong.fs_sys.App;
import facesign.adplayer.fanhong.fs_sys.DbTables.ChildOfWorkersTable;
import facesign.adplayer.fanhong.fs_sys.DbTables.GetResultTable;
import facesign.adplayer.fanhong.fs_sys.models.SignInfo;


/**
 * Created by Administrator on 2017/9/29.
 */

public class DBUtils {

    //触发打卡
    public static String[] triggerCard(Context context, String idNumber, int timeStamp) {
        GetResultTable grt = null;
        //默认上下班时间，早9晚6
        int inHour = 9;
        int inMinute = 0;
        int outHour = 18;
        int outMinute = 0;
        String inTime = context.getSharedPreferences(App.SP_NAME, Context.MODE_PRIVATE).getString(App.IN_ITME, "");
        String outTime = context.getSharedPreferences(App.SP_NAME, Context.MODE_PRIVATE).getString(App.OUT_TIME, "");
        if (!TextUtils.isEmpty(inTime)) {
            inHour = Integer.parseInt(inTime.split("_")[0]);
            inMinute = Integer.parseInt(inTime.split("_")[1]);
        }
        if(!TextUtils.isEmpty(outTime)){
            outHour = Integer.parseInt(outTime.split("_")[0]);
            outMinute = Integer.parseInt(outTime.split("_")[1]);
        }
        int id = -1;
        try {
            int year = HCTimeUtils.getDateTime(timeStamp, "year");
            int month = HCTimeUtils.getDateTime(timeStamp, "month");
            int day = HCTimeUtils.getDateTime(timeStamp, "day");
            String time = HCTimeUtils.getTimeStr(timeStamp);
//            String week = HCTimeUtils.getWeek(year, month, day);
            int hour = HCTimeUtils.getDateTime(timeStamp, "hour");
            int minute = HCTimeUtils.getDateTime(timeStamp, "minute");
            int second = HCTimeUtils.getDateTime(timeStamp, "second");
            ChildOfWorkersTable cowt = App.db.selector(ChildOfWorkersTable.class).
                    where("w_cardnumber", "=", idNumber).findFirst();

            grt = new GetResultTable(idNumber, year, month, day);
            if (isSigned(ifHascard(idNumber, year, month, day, 1)) == -1) { //没有上班记录
                grt.setStatus(1);//此时为上班
                if (hour < inHour) {
                    grt.setResult(App.RESULTS[0]);
                } else if (hour == inHour) {
                    if (minute <= inMinute) {
                        grt.setResult(App.RESULTS[0]);
                    } else {
                        grt.setResult(App.RESULTS[1]);
                    }
                } else {
                    grt.setResult(App.RESULTS[1]);
                }

            } else {
                if (isSigned(ifHascard(idNumber, year, month, day, 2)) == -1) { //有上班记录无下班
//                    grt = new GetResultTable(idNumber, year, month, day);
                    grt.setStatus(2);//此时为下班
                    if (hour < outHour) {
                        grt.setResult(App.RESULTS[2]);
                    } else if (hour == outHour) {
                        if (minute <= outMinute) {
                            grt.setResult(App.RESULTS[2]);
                        } else {
                            grt.setResult(App.RESULTS[3]);
                        }
                    } else {
                        grt.setResult(App.RESULTS[3]);
                    }

                } else { //上下班记录都有
                    id = isSigned(ifHascard(idNumber, year, month, day ,2));
                    grt = App.db.selector(GetResultTable.class).where("id","=",id).findFirst();
                    if (hour < outHour) {
                        grt.setResult(App.RESULTS[2]);
                    } else if (hour == outHour) {
                        if (minute <= outMinute) {
                            grt.setResult(App.RESULTS[2]);
                        } else {
                            grt.setResult(App.RESULTS[3]);
                        }
                    } else {
                        grt.setResult(App.RESULTS[3]);
                    }

                }
            }
            grt.setTime(hour+":"+minute+":"+second);
            App.db.saveOrUpdate(grt);
            Log.i("xq","触发打卡==>"+grt.toString());
            return new String[]{cowt.getDepartment(),cowt.getPosition()};
        } catch (DbException e) {
            e.printStackTrace();
        }
        return new String[]{"——","——"};
    }

    //查询是否已打上/下班卡的条件   status=1上班，status=2下班
    public static WhereBuilder ifHascard(String cardNumber, int year, int month, int day, int status) {
        WhereBuilder wb = WhereBuilder.b();
        wb.and("m_cardnumber", "=", cardNumber);
        wb.and("m_year", "=", year);
        wb.and("m_month", "=", month);
        wb.and("m_day", "=", day);
        wb.and("m_status", "=", status);
        return wb;
    }

    //查询是否已打卡（table = messages）
    public static int isSigned(WhereBuilder wb) {
        GetResultTable grt = null;
        try {
            grt = App.db.selector(GetResultTable.class).where(wb).findFirst();
            if (grt != null)
                return grt.getId();
            else
                return -1;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //是否为上班
    public static boolean doSignIn(String bumen, String zhiwei, String name, String uid, int year, int month, int day, String time) {
        SignInfo info = new SignInfo();
        try {
            App.db.saveOrUpdate(info);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //是否为下班
    public static boolean doSignOut(int id, String bumen, String zhiwei, String name, String uid, String date, String time) {
        SignInfo info = new SignInfo();
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
