package facesign.adplayer.fanhong.fs_sys.utils;

import java.util.Calendar;

import facesign.adplayer.fanhong.fs_sys.models.SignInfo;

/**
 * Created by Administrator on 2017/9/29.
 */

public class HCTimeUtils {
    // 500*************        1185094728
    public static String getTimeStr(int time) {
        String timeStr = "";
        int year = (((time) >> 26) + 2000);
        int month = (((time) >> 22) & 15);
        int day = (((time) >> 17) & 31);
        int hour = (((time) >> 12) & 31);
        int minute = (((time) >> 6) & 63);
        int second = (((time) >> 0) & 63);
        timeStr = year + "/" + month + "/" + day + "\u3000" + hour + ":" + minute + ":" + second;
        return timeStr;
    }

    public static int getDateTime(int time, String w) {
        if (w.equals("year"))
            return (((time) >> 26) + 2000);
        if (w.equals("month"))
            return (((time) >> 22) & 15);
        if (w.equals("day"))
            return (((time) >> 17) & 31);
        if (w.equals("hour"))
            return (((time) >> 12) & 31);
        if (w.equals("minute"))
            return (((time) >> 6) & 63);
        if (w.equals("second"))
            return (((time) >> 0) & 63);
        return -1;
    }
    public static String getWeek(int year,int month,int day) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三" ,"星期四", "星期五", "星期六"};
        Calendar c = Calendar.getInstance();
        c.set(year, month-1, day);
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
//    public static int getStatus(int time,int start){
//        int year=getDateTime(time,"year"),
//            month=getDateTime(time,"month"),
//            day=getDateTime(time,"day"),
//            hour=getDateTime(time,"hour"),
//            minute=getDateTime(time,"minute");
//        if (hour>=4)
//        return -1;
//    }
}
