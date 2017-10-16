package facesign.adplayer.fanhong.fs_sys.utils;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/9/27.
 */
@Table(name = "sign_record")
public class SignInfo {
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;
    @Column(name = "s_name")
    private String name;
    @Column(name = "s_uid")
    private String uid;
    @Column(name = "s_year")
    private int year;
    @Column(name = "s_month")
    private int month;
    @Column(name = "s_day")
    private int day;
    @Column(name = "s_time")
    private String time;
    @Column(name = "s_status")
    private int status;

    public SignInfo() {
    }

    public SignInfo(String name, String uid, int year, int month, int day, String time, int status) {
        this.name = name;
        this.uid = uid;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SignInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", time='" + time + '\'' +
                ", status=" + status +
                '}';
    }
}
