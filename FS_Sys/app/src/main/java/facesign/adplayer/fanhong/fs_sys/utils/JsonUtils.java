package facesign.adplayer.fanhong.fs_sys.utils;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class JsonUtils {
    public static String toJsonString(List<Serializable> list) {
        String jsonStr = "[";
        for (Serializable s : list) {
            Field[] fs = s.getClass().getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                if (i > 0)
                    jsonStr += ",";
                try {
                    jsonStr += "{\"" + fs[i].getName() + "\":\"" + fs[i].get(s) + "\"}";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return "]";
    }
}
