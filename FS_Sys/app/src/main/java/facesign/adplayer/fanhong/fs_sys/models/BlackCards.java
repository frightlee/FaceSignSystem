package facesign.adplayer.fanhong.fs_sys.models;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/10/23.
 */

public class BlackCards {
    public Bitmap bitmap;
    public String name,uid,camera,time;

    public BlackCards(Bitmap bitmap, String name, String uid, String camera, String time) {
        this.bitmap = bitmap;
        this.name = name;
        this.uid = uid;
        this.camera = camera;
        this.time = time;
    }
}
