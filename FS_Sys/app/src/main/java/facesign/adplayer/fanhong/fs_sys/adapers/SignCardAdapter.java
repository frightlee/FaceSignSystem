package facesign.adplayer.fanhong.fs_sys.adapers;

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sign_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imgHead.setImageBitmap(list.get(position).blackPic);
        holder.tvName.setText("姓名：" + list.get(position).name);
        holder.tvSimilar.setText("匹配率：" + list.get(position).similar);
        holder.tvTime.setText("签到时间：" + list.get(position).time);
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
        @ViewInject(R.id.tv_similar)
        TextView tvSimilar;
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
        String similar;
        String time;

        public SignCard(Bitmap blackPic, String name, String similar, String time) {
            this.blackPic = blackPic;
            this.name = name;
            this.similar = similar;
            this.time = time;
        }
    }
}
