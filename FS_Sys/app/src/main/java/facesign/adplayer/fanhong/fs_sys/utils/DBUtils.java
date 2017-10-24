package facesign.adplayer.fanhong.fs_sys.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import facesign.adplayer.fanhong.fs_sys.App;
import facesign.adplayer.fanhong.fs_sys.dbtables.ChildOfWorkersTable;
import facesign.adplayer.fanhong.fs_sys.dbtables.DateTable;
import facesign.adplayer.fanhong.fs_sys.dbtables.GetResultTable;
import facesign.adplayer.fanhong.fs_sys.models.BackupsModel;
import facesign.adplayer.fanhong.fs_sys.models.SignInfo;


/**
 * Created by Administrator on 2017/9/29.
 */

public class DBUtils {
    public static final int NORMAL = 1;
    public static final int BLACK = 2;
    public static final int ERROR = -1;

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
        if (!TextUtils.isEmpty(outTime)) {
            outHour = Integer.parseInt(outTime.split("_")[0]);
            outMinute = Integer.parseInt(outTime.split("_")[1]);
        }
        int id = -1;
        try {
            int year = HCTimeUtils.getDateTime(timeStamp, "year");
            int month = HCTimeUtils.getDateTime(timeStamp, "month");
            int day = HCTimeUtils.getDateTime(timeStamp, "day");
//            //添加打卡的日期，去重
//            String da = year+""+month+""+day;
//            App.daSet.add(da);

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
                        if (minute < outMinute) {
                            grt.setResult(App.RESULTS[2]);
                        } else {
                            grt.setResult(App.RESULTS[3]);
                        }
                    } else {
                        grt.setResult(App.RESULTS[3]);
                    }

                } else { //上下班记录都有
                    id = isSigned(ifHascard(idNumber, year, month, day, 2));
                    grt = App.db.selector(GetResultTable.class).where("id", "=", id).findFirst();
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
            grt.setTime(hour + ":" + minute + ":" + second);
            App.db.saveOrUpdate(grt);
            Log.i("xq", "触发打卡==>" + grt.toString());
            return new String[]{cowt.getDepartment(), cowt.getPosition()};
        } catch (DbException e) {
            e.printStackTrace();
        }
        return new String[]{"——", "——"};
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

    //传入证件号单独查询是否为黑名单
    public static int isBlack(String idNumber) {
        ChildOfWorkersTable cowt = null;
        try {
            cowt = App.db.selector(ChildOfWorkersTable.class).where("w_cardnumber", "=", idNumber).findFirst();
            if (cowt.getFlag() == 2) {
                return BLACK;
            } else if (cowt.getFlag() == 1) {
                return NORMAL;
            } else {
                return ERROR;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return ERROR;
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

    //将打卡的日期传入数据库
    public static void addDates() {
        Set<String> setA = new HashSet<>();
        Set<String> setB = new HashSet<>();
        Set<String> setC = new HashSet<>();
        try {
            List<GetResultTable> grts = App.db.selector(GetResultTable.class).findAll(); //总的打卡日期
            List<DateTable> dts = App.db.selector(DateTable.class).findAll();       //备份过的日期
            if (grts != null) {
                for (int i = 0; i < grts.size(); i++) {
                    setA.add(grts.get(i).getYear() + "/" + grts.get(i).getMonth() + "/" + grts.get(i).getDay());
                }
                for (int j = 0; j < dts.size(); j++) {
                    setB.add(dts.get(j).getSavedDate());
                }
                Iterator iterator = setA.iterator();
                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    if (!setB.contains(s)) {
                        setC.add(s);
                    }
                }
                postDatas(setC);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //上传未备份日期的打卡数据
    public static void postDatas(Set<String> set) {
        List<Serializable> list = new ArrayList<>();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            String[] strings = s.split("/");
            try {
                List<GetResultTable> grtList = App.db.selector(GetResultTable.class).where("m_year", "=", Integer.parseInt(strings[0]))
                        .and("m_month", "=", Integer.parseInt(strings[1])).and("m_day", "=", Integer.parseInt(strings[2])).findAll();
                for (int i = 0; i < grtList.size(); i++) {
                    ChildOfWorkersTable iwm = App.db.selector(ChildOfWorkersTable.class).
                            where("w_cardnumber", "=", grtList.get(i).getCardNumber()).findFirst();
                    BackupsModel model = new BackupsModel();
                    model.setDepartment(iwm.getDepartment());
                    model.setPosition(iwm.getPosition());
                    model.setName(iwm.getName());
                    model.setIdNumber(grtList.get(i).getCardNumber());
                    model.setDate(s);
                    model.setTime(grtList.get(i).getTime());
                    list.add(model);
                }
            } catch (DbException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        RequestParams params = new RequestParams(App.SAVEURL);
        params.addBodyParameter("content", JsonUtils.toJsonString(list));
        params.addBodyParameter("data", new SimpleDateFormat("yyyy年MM月dd日").format(System.currentTimeMillis()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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
    }
}
