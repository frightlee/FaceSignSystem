package facesign.adplayer.fanhong.fs_sys.models;

/**
 * Created by Administrator on 2017/10/18.
 */

//触发打卡返回的model
public class TraiggerModel {
    private String name;
    private String position;
    private String department;
    private String result; //打卡结果
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TraigerModel{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", result='" + result + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
