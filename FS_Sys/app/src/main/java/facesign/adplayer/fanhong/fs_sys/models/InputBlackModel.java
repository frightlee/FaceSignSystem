package facesign.adplayer.fanhong.fs_sys.models;

/**
 * Created by Administrator on 2017/10/23.
 * 黑名单
 */

public class InputBlackModel {
    String name;  //名字
    String number; //编号

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "InputBlackModel{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
