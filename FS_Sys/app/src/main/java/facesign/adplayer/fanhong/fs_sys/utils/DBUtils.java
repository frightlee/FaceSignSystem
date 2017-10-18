package facesign.adplayer.fanhong.fs_sys.utils;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

import facesign.adplayer.fanhong.fs_sys.App;
import facesign.adplayer.fanhong.fs_sys.DbTables.ChildOfWorkersTable;
import facesign.adplayer.fanhong.fs_sys.DbTables.GetResultTable;
import facesign.adplayer.fanhong.fs_sys.models.SignInfo;
import facesign.adplayer.fanhong.fs_sys.models.TraiggerModel;


/**
 * Created by Administrator on 2017/9/29.
 */

public class DBUtils {
    //触发打卡
    public static TraiggerModel triggerCard(String idNumber, int timeStamp)  {
        TraiggerModel tm = null;
        int id = -1;
        try {
            int year = HCTimeUtils.getDateTime(timeStamp,"year");
            int month = HCTimeUtils.getDateTime(timeStamp,"month");
            int day = HCTimeUtils.getDateTime(timeStamp,"day");
            String time = HCTimeUtils.getTimeStr(timeStamp);
            String week = HCTimeUtils.getWeek(year,month,day);
            ChildOfWorkersTable cowt = App.db.selector(ChildOfWorkersTable.class).
                    where("w_cardnumber","=",idNumber).findFirst();

            tm = new TraiggerModel();
            tm.setName(cowt.getName());
            tm.setDepartment(cowt.getDepartment());
            tm.setPosition(cowt.getPosition());
            tm.setTime(time);
            if(isSigned(idNumber,year,month,day,time,week) == -1){
                GetResultTable grt = new GetResultTable(idNumber,year,month,day,time,week);
                grt.setStatus(1);//此时为上班

            }else {
                id = isSigned(idNumber,year,month,day,time,week);

            }
//            tm.setResult();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return tm;
    }
    //查询是否已打卡（table = messages）
    public static int isSigned(String cardNumber, int year, int month, int day, String time, String week) {
        WhereBuilder wb = WhereBuilder.b();
        wb.and("m_cardnumber", "=", cardNumber);
        wb.and("m_year", "=", year);
        wb.and("m_month", "=", month);
        wb.and("m_day", "=", day);
//        wb.and("m_time", "=", time);
        wb.and("m_week", "=", week);
//        wb.and("m_status", "=", status);
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
