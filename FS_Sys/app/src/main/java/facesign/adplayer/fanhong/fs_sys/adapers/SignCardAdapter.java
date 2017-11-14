package facesign.adplayer.fanhong.fs_sys.adapers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import autolayout.utils.AutoUtils;
import facesign.adplayer.fanhong.fs_sys.MainActivity;
import facesign.adplayer.fanhong.fs_sys.R;

/**
 * Created by Administrator on 2017/9/22.
 */

public class SignCardAdapter extends RecyclerView.Adapter<SignCardAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<SignCard> list;
    private int screenOritation;

    public SignCardAdapter(Context context, List<SignCard> list, int screenOritation) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.screenOritation = screenOritation;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (screenOritation == MainActivity.SCREEN_VERITICAL)
            if (viewType == 0)
                view = inflater.inflate(R.layout.sign_card_last_vertical, parent, false);
            else
                view = inflater.inflate(R.layout.sign_card_vertical, parent, false);
        else {
            if (viewType == 0)
                view = inflater.inflate(R.layout.sign_card_last, parent, false);
            else
                view = inflater.inflate(R.layout.sign_card, parent, false);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SignCard info = list.get(position);
        holder.imgHead.setImageBitmap(info.blackPic);
        holder.tvName.setText("姓名：" + info.name);
        holder.tvDepartment.setText("部门：" + info.department);
        holder.tvPosition.setText("职位：" + info.position);
        holder.tvCamera.setText("摄像机：" + info.camera);
        String time = info.time;
        if (position != 0) {
            StringBuffer sbf = new StringBuffer(time);
            sbf.insert(sbf.indexOf("\u3000"), "\n\u3000\u3000\u3000\u3000");
            time = sbf.toString();
        }
        holder.tvTime.setText("签到时间：" + time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.img_head)
        ImageView imgHead;
        @ViewInject(R.id.tv_name)
        TextView tvName;
        @ViewInject(R.id.tv_department)
        TextView tvDepartment;
        @ViewInject(R.id.tv_position)
        TextView tvPosition;
        @ViewInject(R.id.tv_camera)
        TextView tvCamera;
        @ViewInject(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
            AutoUtils.autoSize(itemView);
        }
    }

    public static class SignCard {
        Bitmap blackPic;
        String name;
        String department;
        String position;
        String camera;
        String time;

        public SignCard(Bitmap blackPic, String name, String department, String position, String camera, String time) {
            this.blackPic = blackPic;
            this.name = name;
            this.department = department;
            this.position = position;
            this.camera = camera;
            this.time = time;
        }

        public SignCard() {
        }

        @Override
        public String toString() {
            return "SignCard{" +
                    "blackPic=" + blackPic +
                    ", name='" + name + '\'' +
                    ", department='" + department + '\'' +
                    ", position='" + position + '\'' +
                    ", camera='" + camera + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }
}
