package facesign.adplayer.fanhong.fs_sys.DbTables;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/18.
 */

@Table(name= "messages")
public class GetResultTable {
    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    int id;
    @Column(name = "m_cardnumber")
    String cardNumber;
    @Column(name = "m_year")
    int year;
    @Column(name = "m_month")
    int month;
    @Column(name = "m_day")
    int day;
    @Column(name = "m_time")
    String time;
    @Column(name = "m_week")    // "星期几"
    String week;
    @Column(name = "m_status")//打卡状态，上班为1，下班为2
    int status;
    @Column(name = "m_result")
    String result;
    public GetResultTable(){}

    public GetResultTable(String cardNumber, int year, int month, int day, String time, String week) {
        this.cardNumber = cardNumber;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.week = week;
        //status,result由判断得出
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "GetResultTable{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", time='" + time + '\'' +
                ", week=" + week +
                ", status=" + status +
                ", result='" + result + '\'' +
                '}';
    }
}
