package facesign.adplayer.fanhong.fs_sys.utils;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class JsonUtils {
    public static String toJsonString(List<Serializable> list) {
        String jsonStr="[";
        for (Serializable s : list) {
        Field[] keys = list.get(0).getClass().getDeclaredFields();
            for (int i = 0; i < keys.length; i++) {
                
            }
        }

        return "]";
    }
}
