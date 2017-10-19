package facesign.adplayer.fanhong.fs_sys.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import facesign.adplayer.fanhong.fs_sys.models.SignInfo;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Administrator on 2017/10/9.
 */

public class MyExcelUtils {
    private static WritableWorkbook createExcel(String filePath, String tableTitle) {
        try {
            WritableWorkbook book = Workbook.createWorkbook(new File(filePath + "/" + tableTitle + ".xls"));
            WritableSheet sheet1 = book.createSheet("表一", 0);
            sheet1.mergeCells(0, 0, 5, 0);
            WritableCellFormat wc = new WritableCellFormat();
            wc.setAlignment(Alignment.CENTRE);
            Label label_t = new Label(0, 0, tableTitle, wc);
            Label label1 = new Label(0, 1, "序号");
            Label label2 = new Label(1, 1, "姓名");
            Label label3 = new Label(2, 1, "证件号");
            Label label4 = new Label(3, 1, "打卡时间");
            Label label5 = new Label(4, 1, "星期几");
            Label label6 = new Label(5, 1, "打卡结果");
            sheet1.addCell(label_t);
            sheet1.addCell(label1);
            sheet1.addCell(label2);
            sheet1.addCell(label3);
            sheet1.addCell(label4);
            sheet1.addCell(label5);
            sheet1.addCell(label6);
            return book;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createFile(String path, int year, int month, int[] settingsTime, List<SignInfo> listInfo) {
        String fileName = year + "年" + month + "月考勤统计表";
        WritableWorkbook book = createExcel(path, fileName);
        if (book != null) {
            WritableSheet sheet = book.getSheet(0);
            sheet.setColumnView(3, 20);
            try {
                for (int i = 0; i < listInfo.size(); i++) {
                    SignInfo info = listInfo.get(i);
                    jxl.write.Number label1 = new jxl.write.Number(0, i + 2, i + 1);
                    Label label2 = new Label(1, i + 2, info.getName());
                    Label label3 = new Label(2, i + 2, info.getUid());
                    Label label4 = new Label(3, i + 2, info.getTime());
                    Label label5 = new Label(4, i + 2, getWeek(info));//星期几
                    Label label6 = new Label(5, i + 2, getResult(info, settingsTime));
                    sheet.addCell(label1);
                    sheet.addCell(label2);
                    sheet.addCell(label3);
                    sheet.addCell(label4);
                    sheet.addCell(label5);
                    sheet.addCell(label6);
                }
                book.write();
                book.close();
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String getWeek(SignInfo info) {
        String[] weekDays = {"星期四", "星期五", "星期六","星期日", "星期一", "星期二", "星期三" };
        int year = info.getYear(), month = info.getMonth(), day = info.getDay();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    private static String getResult(SignInfo info, int[] settingsTime) {
        if (info.getStatus() == 1) {//上班卡
            String time = info.getTime();
            int hour = Integer.parseInt(time.substring(time.indexOf("\u3000") + 1, time.indexOf(":")));
            if (hour < settingsTime[0])
                return "正常上班";
            else if (hour > settingsTime[0])
                return "迟到";
            else {
                int minute = Integer.parseInt(time.substring(time.indexOf(":") + 1,
                        time.indexOf(":", time.indexOf(":") + 1)));
                if (minute <= settingsTime[1])
                    return "正常上班";
                else
                    return "迟到";
            }
        } else if (info.getStatus() == 2) {//下班卡
            String time = info.getTime();
            int hour = Integer.parseInt(time.substring(time.indexOf("\u3000") + 1, time.indexOf(":")));
            if (hour > settingsTime[2])
                return "正常下班";
            else if (hour < settingsTime[2])
                return "早退";
            else {
                int minute = Integer.parseInt(time.substring(time.indexOf(":") + 1,
                        time.indexOf(":", time.indexOf(":") + 1)));
                if (minute >= settingsTime[3])
                    return "正常下班";
                else
                    return "早退";
            }
        }
        return "";
    }
}
