package facesign.adplayer.fanhong.fs_sys.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.zip.Inflater;

import facesign.adplayer.fanhong.fs_sys.R;
import facesign.adplayer.fanhong.fs_sys.models.CameraInfo;

/**
 * Created by Administrator on 2017/10/18.
 */

public class CameraCardAdapter extends BaseAdapter {
    private List<CameraInfo> list;
    private LayoutInflater inflater;
    private Context context;
    private CameraHolder holder;

    public CameraCardAdapter(List<CameraInfo> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (null== convertView ) {
            convertView = inflater.inflate(R.layout.camera_card, null);
            holder = new CameraHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CameraHolder) convertView.getTag();
        }
        CameraInfo info = list.get(position);
        holder.tvNo_.setText("编号：" + info.getNo_());
        holder.tvAlias.setText("别名：" + info.getAlias());
        holder.tvState.setText("状态：" + (info.getAlarm() == -1 ? "未连接" : "已连接"));
        return convertView;
    }

    class CameraHolder {
        @ViewInject(R.id.tv_no_)
        TextView tvNo_;
        @ViewInject(R.id.tv_alias)
        TextView tvAlias;
        @ViewInject(R.id.tv_state)
        TextView tvState;

        public CameraHolder(View itemView) {
            x.view().inject(this, itemView);
            AutoUtils.autoSize(itemView);
        }
    }
}
