package facesign.adplayer.fanhong.fs_sys.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import facesign.adplayer.fanhong.fs_sys.models.CameraInfo;

import static android.R.id.list;

/**
 * Created by Administrator on 2017/10/23.
 */

public class JsonUtils {
    public static String getJsonValue(String json, String key) {
        String result = "";
        try {
            JSONObject o = new JSONObject(json);
            result = o.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String toJsonString(List list) {
        String jsonStr = "[";
        for (Object s : list) {
            Field[] fs = s.getClass().getDeclaredFields();
            if (list.indexOf(s) == 0) {
                jsonStr += "{";
            } else {
                jsonStr += ",{";
            }
            for (int i = 0; i < fs.length; i++) {
                if (i > 0)
                    jsonStr += ",";
                try {
                    jsonStr += "\"" + fs[i].getName() + "\":\"" + fs[i].get(s) + "\"";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            jsonStr += "}";
        }
        return jsonStr + "]";
    }
    public static String getCamerasJson(List<CameraInfo> list) {
        String jsonStr = "[";
        for (CameraInfo s : list) {
            if (list.indexOf(s) == 0) {
                jsonStr += "{";
            } else {
                jsonStr += ",{";
            }
            jsonStr += "\"No_\":\"" + s.getNo_() + "\"";
            jsonStr += ",\"Alias\":\"" + s.getAlias() + "\"";
            jsonStr += ",\"IP\":\"" + s.getIP() + "\"";
            jsonStr += ",\"Port\":\"" + s.getPort() + "\"";
            jsonStr += ",\"User\":\"" + s.getUser() + "\"";
            jsonStr += ",\"Pwd\":\"" + s.getPwd() + "\"";

            jsonStr += "}";
        }
        return jsonStr + "]";
    }
    public static List<CameraInfo> getCameras(List<CameraInfo> cameras, String cameraStr) {
        try {
            JSONArray ja = new JSONArray(cameraStr);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject o = ja.getJSONObject(i);
                CameraInfo c = new CameraInfo(o.getLong("No_"), o.getString("Alias"), o.getString("IP"), o.getString("Port"), o.getString("User"), o.getString("Pwd"));
                c.setLogin(-1);
                c.setAlarm(-1);
                cameras.add(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cameras;
    }
}
