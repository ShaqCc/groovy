package com.bunny.groovy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.PerformDetail;
import com.bunny.groovy.ui.fragment.releaseshow.UserShowDetailFragment;
import com.bunny.groovy.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/****************************************
 * 功能说明:  表演者详情页面，下面的列表适配器
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class PerformDetailListAdapter extends RecyclerView.Adapter<PerformDetailListAdapter.MusicianScheduleHolder> implements View.OnClickListener {

    private List<PerformDetail> mList;
    private Context mContext;

    public PerformDetailListAdapter(List<PerformDetail> list) {
        mList = list;
    }

    public void refresh(List<PerformDetail> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public MusicianScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_perfom_detail_next_show_layout, null, false);
        return new MusicianScheduleHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MusicianScheduleHolder holder, int position) {
        PerformDetail detail = mList.get(position);
        holder.tvTopLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        holder.tvBottomLine.setVisibility(position == mList.size() - 1 ? View.GONE : View.VISIBLE);
        holder.mTvUserName.setText(detail.getPerformerName());
        holder.mTvScore.setText(Utils.getStar(detail.getPerformerStarLevel()));
        holder.mTvData.setText(detail.getPerformType() + "  |  " + detail.getPerformDate() + detail.getPerformTime());
        Glide.with(mContext).load(detail.getPerformerImg()).placeholder(R.drawable.icon_default_photo).into(holder.mIvUserHead);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        PerformDetail detail = mList.get(pos);
        UserShowDetailFragment.launch(mContext, detail, false);
    }

    static class MusicianScheduleHolder extends RecyclerView.ViewHolder {

        private CircleImageView mIvUserHead;
        private TextView mTvUserName;
        private TextView mTvScore;
        private TextView mTvData;
        private View tvBottomLine, tvTopLine;


        public MusicianScheduleHolder(View itemView) {
            super(itemView);
            mTvUserName = itemView.findViewById(R.id.tv_name);
            mIvUserHead = itemView.findViewById(R.id.iv_head);
            mTvData = itemView.findViewById(R.id.tv_data);
            mTvScore = itemView.findViewById(R.id.tv_score);
            tvBottomLine = itemView.findViewById(R.id.tvBottomLine);
            tvTopLine = itemView.findViewById(R.id.tvTopLine);
        }
    }
}
