package facesign.adplayer.fanhong.fs_sys.DbTables;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/18.
 */

@Table(name = "workers")
public class ChildOfWorkersTable {
    @Column(name = "id" ,isId = true,autoGen = true,property = "NOT NULL")
    int id;
    @Column(name = "w_department") //部门
    String department;
    @Column(name = "w_position")  //职位
    String position;
    @Column(name = "w_name")  //名字
    String name;
    @Column(name = "w_cardnumber") //身份证号
    String cardNumber;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "ChildOfWorkersTable{" +
                "id=" + id +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
