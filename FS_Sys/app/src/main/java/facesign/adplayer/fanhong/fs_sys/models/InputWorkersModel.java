package facesign.adplayer.fanhong.fs_sys.models;

/**
 * Created by Administrator on 2017/10/18.
 * 正经员工
 */

public class InputWorkersModel {
    String department;
    String position;
    String name;
    String cardNumber;
    int falg;

    public int getFalg() {
        return falg;
    }

    public void setFalg(int falg) {
        this.falg = falg;
    }

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

    @Override
    public String toString() {
        return "InputWorkersModel{" +
                "department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", falg=" + falg +
                '}';
    }
}
