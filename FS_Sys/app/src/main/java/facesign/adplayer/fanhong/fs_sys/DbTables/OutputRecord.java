package facesign.adplayer.fanhong.fs_sys.dbtables;

import android.os.Environment;
import android.util.Log;

import org.xutils.ex.DbException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import facesign.adplayer.fanhong.fs_sys.App;
import facesign.adplayer.fanhong.fs_sys.models.OutputExcelModel;
import facesign.adplayer.fanhong.fs_sys.utils.FileUtils;
import facesign.adplayer.fanhong.fs_sys.utils.HCTimeUtils;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Administrator on 2017/10/18.
 */

public class OutputRecord {
    int whichYear;
    int whichMonth;

    public OutputRecord(int whichYear, int whichMonth) {
        this.whichYear = whichYear;
        this.whichMonth = whichMonth;
    }

    //得到导出model的集合List
    public List<OutputExcelModel> getListOfOutputExcel() {
        List<OutputExcelModel> list = new ArrayList<>();
        Log.i("xq","导出的年月==>"+whichYear+"丶"+whichMonth);
        try {
//            List<GetResultTable> grtList = App.db.selector(GetResultTable.class).findAll();
            List<GetResultTable> grtList = App.db.selector(GetResultTable.class)
                    .where("m_year", "=", whichYear)
                    .and("m_month", "=", whichMonth).findAll();
            Log.i("xq","查询的grtList.size==>"+grtList.size());
            for (int i = 0; i < grtList.size(); i++) {
                ChildOfWorkersTable iwm = App.db.selector(ChildOfWorkersTable.class).
                        where("w_cardnumber", "=", grtList.get(i).getCardNumber()).findFirst();
                Log.e("xq",iwm.toString());
                OutputExcelModel model = new OutputExcelModel();
                model.setDepartment(iwm.getDepartment());
                model.setPosition(iwm.getPosition());
                model.setName(iwm.getName());
                model.setCardNumber(grtList.get(i).getCardNumber());
                model.setDate(grtList.get(i).getYear() + "/" + grtList.get(i).getMonth() + "/" + grtList.get(i).getDay());
                model.setTime(grtList.get(i).getTime());
                model.setWeekday(HCTimeUtils.getWeek(grtList.get(i).getYear(), grtList.get(i).getMonth(), grtList.get(i).getDay()));
                model.setResult(grtList.get(i).getResult());
                list.add(model);
            }
            Log.i("xq","导出的list.size==>"+list.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    public int writeExcel(List<OutputExcelModel> list) {
        String tableName = whichYear + "年" + whichMonth + "月考勤记录表";
        int wid = 15;
        WritableWorkbook wwb = null;
        Label label = null;
        String[] titles = new String[]{"序号","部门", "职位", "名字", "证件号", "日期", "时间", "星期几", "打卡结果"};
        String filePath1 = Environment.getExternalStorageDirectory() + "//inputFS";
        if (FileUtils.isFileExists(filePath1)) {
            String filePath2 = filePath1 + "/" + tableName + ".xls";
            try {
                OutputStream os = new FileOutputStream(filePath2);
                wwb = Workbook.createWorkbook(os);
                WritableSheet ws = wwb.createSheet(tableName, 0);
                ws.mergeCells(0, 0, titles.length, 0);
                //设置表名
                label = new Label(0,0,tableName);
                ws.addCell(label);
                WritableCellFormat wc = new WritableCellFormat();
                wc.setAlignment(Alignment.CENTRE);
                for (int i = 0; i < titles.length; i++) {
                    label = new Label(i, 1, titles[i]);
                    ws.addCell(label);
                }
                ws.setColumnView(2,wid);
                ws.setColumnView(4,wid+3);
                ws.setColumnView(5,wid);
                ws.setColumnView(6,wid);
                for (int i = 0; i < list.size(); i++) {
                    label = new Label(0,i+2,(i+1)+"");
                    ws.addCell(label);
                    label = new Label(1, i + 2, list.get(i).getDepartment());
                    ws.addCell(label);
                    label = new Label(2, i + 2, list.get(i).getPosition());
                    ws.addCell(label);
                    label = new Label(3, i + 2, list.get(i).getName());
                    ws.addCell(label);
                    label = new Label(4, i + 2, list.get(i).getCardNumber());
                    ws.addCell(label);
                    label = new Label(5, i + 2, list.get(i).getDate());
                    ws.addCell(label);
                    label = new Label(6, i + 2, list.get(i).getTime());
                    ws.addCell(label);
                    label = new Label(7, i + 2, list.get(i).getWeekday());
                    ws.addCell(label);
                    label = new Label(8, i + 2, list.get(i).getResult());
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
            } finally {
                try {
                    wwb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
        return list.size();
    }

    //数据备份
    public static void sendRecord(){
        
    }
}
