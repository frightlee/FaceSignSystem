package facesign.adplayer.fanhong.fs_sys.DbTables;

import android.os.Environment;
import android.util.Log;

import org.xutils.ex.DbException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import facesign.adplayer.fanhong.fs_sys.App;
import facesign.adplayer.fanhong.fs_sys.models.InputWorkersModel;
import facesign.adplayer.fanhong.fs_sys.utils.FileUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by Administrator on 2017/10/18.
 */

public class InputWorkers {
    //录入表格中的数据
    public static int readExcel() {
        List<InputWorkersModel> list = new ArrayList<>();
        Workbook book = null;
        Sheet sheet = null;
        Cell[] cells = new Cell[4];
        String stringPath = Environment.getExternalStorageDirectory() + "//inputFS";
        if(FileUtils.isFileExists(stringPath)){
            String stringPath1 = stringPath + "/" + "workers.xls";
            File file = new File(stringPath1);
            try {
                book = Workbook.getWorkbook(file);
                // 获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
                sheet = book.getSheet(0);
                int i = 2;
                while (true) {
                    // 获取每一行的单元格
                    cells[0] = sheet.getCell(1, i);// （列，行）
                    cells[1] = sheet.getCell(2, i);
                    cells[2] = sheet.getCell(3, i);
                    cells[3] = sheet.getCell(4, i);
                    InputWorkersModel model = new InputWorkersModel();
                    model.setDepartment(cells[0].getContents());
                    model.setPosition(cells[1].getContents());
                    model.setName(cells[2].getContents());
                    model.setCardNumber(cells[3].getContents());
                    list.add(model);
                    if (cells[0].getContents().equals("")) {
                        break;
                    }
                    i++;
                }
                book.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        return setChildOfWorkers(list);
    }

    public static int setChildOfWorkers(List<InputWorkersModel> lists){
        for(int i=0;i<lists.size();i++){
            ChildOfWorkersTable cow = new ChildOfWorkersTable();
            cow.setDepartment(lists.get(i).getDepartment());
            cow.setPosition(lists.get(i).getPosition());
            cow.setName(lists.get(i).getName());
            cow.setCardNumber(lists.get(i).getCardNumber());
            try {
                App.db.save(cow);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        try {
//            List<ChildOfWorkersTable> workersList = App.db.selector(ChildOfWorkersTable.class).where("w_name","=","湛洋").findAll();
            List<ChildOfWorkersTable> workersList = App.db.selector(ChildOfWorkersTable.class).findAll();
            Log.i("xq","找到==>"+workersList.toString());
            if(workersList.size() > 0){
                return workersList.size();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
