package facesign.adplayer.fanhong.fs_sys.dbtables;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23.
 */

@Table(name = "date_saved")
public class DateTable {
    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    int id;
    @Column(name = "d_saveddate") //备份过的日期
    String savedDate;   // "year/month/day"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", savedDate='" + savedDate + '\'' +
                '}';
    }
}
