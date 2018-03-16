package com.bunny.groovy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.utils.DateUtils;

import java.util.List;

/****************************************
 * 功能说明:  音乐厅详情页面，下面的列表适配器
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class VenueScheduleAdapter extends RecyclerView.Adapter<VenueScheduleAdapter.VenueScheduleHolder> {

    private List<VenueModel.ScheduleListBean> mList;

    public VenueScheduleAdapter(List<VenueModel.ScheduleListBean> list) {
        mList = list;
    }

    public void refresh(List<VenueModel.ScheduleListBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public VenueScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_venue_sechedule_layout, null, false);
        return new VenueScheduleHolder(inflate);
    }

    @Override
    public void onBindViewHolder(VenueScheduleHolder holder, int position) {
        VenueModel.ScheduleListBean scheduleListBean = mList.get(position);

        holder.mTvWeekDay.setText(DateUtils.CN_weeks[Integer.parseInt(scheduleListBean.getWeekNum())-1]);
        String isHaveCharges = scheduleListBean.getIsHaveCharges();
        if ("0".equals(isHaveCharges)) {
            holder.mTvMessage.setEnabled(false);
        } else if ("1".equals(isHaveCharges))
            holder.mTvMessage.setEnabled(true);
        holder.mTvPerformTime.setText(scheduleListBean.getStartDate() + "-" + scheduleListBean.getEndDate());
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    static class VenueScheduleHolder extends RecyclerView.ViewHolder {

        private final TextView mTvWeekDay;
        private final TextView mTvPerformTime;
        private final TextView mTvMessage;

        public VenueScheduleHolder(View itemView) {
            super(itemView);
            mTvWeekDay = itemView.findViewById(R.id.item_venue_schedule_tv_weekday);
            mTvPerformTime = itemView.findViewById(R.id.item_venue_schedule_tv_performtime);
            mTvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
