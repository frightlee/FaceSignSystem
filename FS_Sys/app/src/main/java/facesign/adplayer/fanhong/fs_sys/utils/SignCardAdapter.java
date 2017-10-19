package facesign.adplayer.fanhong.fs_sys.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import facesign.adplayer.fanhong.fs_sys.R;

/**
 * Created by Administrator on 2017/9/22.
 */

public class SignCardAdapter extends RecyclerView.Adapter<SignCardAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<SignCard> list;

    public SignCardAdapter(Context context, List<SignCard> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.sign_card_last, parent, false);
        } else {
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
        holder.tvTime.setText("签到时间：" + info.time);
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
    }
}
