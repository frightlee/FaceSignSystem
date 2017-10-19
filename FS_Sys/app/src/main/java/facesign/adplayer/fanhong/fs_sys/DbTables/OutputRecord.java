package facesign.adplayer.fanhong.fs_sys.DbTables;

import android.os.Environment;

import org.xutils.ex.DbException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import facesign.adplayer.fanhong.fs_sys.App;
import facesign.adplayer.fanhong.fs_sys.models.InputWorkersModel;
import facesign.adplayer.fanhong.fs_sys.models.OutputExcelModel;
import facesign.adplayer.fanhong.fs_sys.utils.FileUtils;
import facesign.adplayer.fanhong.fs_sys.utils.HCTimeUtils;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Administrator on 2017/10/18.
 */

public class OutputRecord {

    //得到导出model的集合List
    public List<OutputExcelModel> getListOfOutputExcel(){
        List<OutputExcelModel> list = new ArrayList<>();
        try {
            List<GetResultTable> grtList = App.db.selector(GetResultTable.class).findAll();
            for(int i=0;i<grtList.size();i++){
                ChildOfWorkersTable iwm = App.db.selector(ChildOfWorkersTable.class).
                        where("w_cardnumber","=",grtList.get(i).getCardNumber()).findFirst();
                OutputExcelModel model = new OutputExcelModel();
                model.setDepartment(iwm.getDepartment());
                model.setPosition(iwm.getPosition());
                model.setName(iwm.getName());
                model.setCardNumber(grtList.get(i).getCardNumber());
                model.setDate(grtList.get(i).getYear()+"/"+grtList.get(i).getMonth()+"/"+grtList.get(i).getDay());
                model.setTime(grtList.get(i).getTime());
                model.setWeekday(HCTimeUtils.getWeek(grtList.get(i).getYear(),grtList.get(i).getMonth(),grtList.get(i).getDay()));
                model.setResult(grtList.get(i).getResult());
                list.add(model);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    String whichYear = "" ;
    String whichMonth = "";
    String tableName = whichYear+"年"+whichMonth+"月考勤记录表";
    public void writeExcel(List<OutputExcelModel> list){
        WritableWorkbook wwb = null;
        Label label = null;
        String[] titles = new String[]{"部门","职位","名字","身份证","日期","时间","星期几","打卡结果"};
        String filePath1 = Environment.getExternalStorageDirectory()+"//OutWorkers";
        if(FileUtils.isFileExists(filePath1)){
            String filePath2 = filePath1+"/"+tableName+".xls";
            try {
                OutputStream os = new FileOutputStream(filePath2);
                wwb = Workbook.createWorkbook(os);
                WritableSheet ws = wwb.createSheet(tableName,0);
                for(int i=0;i<titles.length;i++){
                    label = new Label(i, 1, titles[i]);
                    ws.addCell(label);
                }
                for (int i = 0; i < list.size(); i++) {
                    label = new Label(0, i + 2, list.get(i).getDepartment());
                    ws.addCell(label);
                    label = new Label(1, i + 2, list.get(i).getPosition());
                    ws.addCell(label);
                    label = new Label(2, i + 2, list.get(i).getName());
                    ws.addCell(label);
                    label = new Label(3, i + 2, list.get(i).getCardNumber());
                    ws.addCell(label);
                    label = new Label(4, i + 2, list.get(i).getDate());
                    ws.addCell(label);
                    label = new Label(5, i + 2, list.get(i).getTime());
                    ws.addCell(label);
                    label = new Label(6, i + 2, list.get(i).getWeekday());
                    ws.addCell(label);
                    label = new Label(7, i + 2, list.get(i).getResult());
                    ws.addCell(label);
                }
                wwb.write();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }finally {
                try {
                    wwb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
