package facesign.adplayer.fanhong.fs_sys.utils;

import java.io.File;

/**
 * Created by Administrator on 2017/10/18.
 */

public class FileUtils {

    //判断文件是否存在或成功创建
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.mkdir()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
