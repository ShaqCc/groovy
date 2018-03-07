package com.bunny.groovy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.MusicianDetailModel;

import java.util.List;

/****************************************
 * 功能说明:  表演者详情页面，下面的列表适配器
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class MusicianScheduleAdapter extends RecyclerView.Adapter<MusicianScheduleAdapter.MusicianScheduleHolder> {

    private List<MusicianDetailModel.PerformViewer> mList;

    public MusicianScheduleAdapter(List<MusicianDetailModel.PerformViewer> list) {
        mList = list;
    }

    public void refresh(List<MusicianDetailModel.PerformViewer> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public MusicianScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_musician_sechedule_layout, null, false);
        return new MusicianScheduleHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MusicianScheduleHolder holder, int position) {
        MusicianDetailModel.PerformViewer performViewer = mList.get(position);
        holder.tvTopLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        holder.tvBottomLine.setVisibility(position == mList.size() - 1 ? View.GONE : View.VISIBLE);
        holder.mTvUserName.setText(performViewer.userName);
        holder.mTvReplyData.setText(performViewer.performerStarLevel + "  |  " + performViewer.evaluateContent);
        if (!TextUtils.isEmpty(performViewer.evaluateDate)) {
            holder.mTvReplyTime.setText(performViewer.evaluateDate);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    static class MusicianScheduleHolder extends RecyclerView.ViewHolder {

        private TextView mTvUserName;
        private TextView mTvReplyTime;
        private TextView mTvReplyData;
        private View tvBottomLine, tvTopLine;


        public MusicianScheduleHolder(View itemView) {
            super(itemView);
            mTvUserName = (TextView) itemView.findViewById(R.id.item_musician_reply_user_name);
            mTvReplyTime = (TextView) itemView.findViewById(R.id.item_musician_reply_time);
            mTvReplyData = (TextView) itemView.findViewById(R.id.item_musician_reply_data);
            tvBottomLine = itemView.findViewById(R.id.tvBottomLine);
            tvTopLine = itemView.findViewById(R.id.tvTopLine);
        }
    }
}
