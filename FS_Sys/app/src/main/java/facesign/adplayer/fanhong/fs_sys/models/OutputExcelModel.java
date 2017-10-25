package facesign.adplayer.fanhong.fs_sys.models;

/**
 * Created by Administrator on 2017/10/17.
 */
//需要导出的Excel表
public class OutputExcelModel {
    String department;
    String position;
    String name;
    String cardNumber;
    String date;
    String time;
    String weekday;
    String result;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "{" +
                "department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", weekday='" + weekday + '\'' +
                ", result='" + result + '\'' +
                '}'+"\r\n";
    }
}
