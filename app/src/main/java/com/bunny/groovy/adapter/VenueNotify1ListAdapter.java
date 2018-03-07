package com.bunny.groovy.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.VenueOpportunityModel;
import com.bunny.groovy.ui.fragment.notify.VenueOpportunityDetailFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueNotify1ListAdapter extends RecyclerView.Adapter<VenueNotify1ListAdapter.NotifyHolder> implements View.OnClickListener {
    private List<VenueOpportunityModel> mList;
    private static Activity mContext;

    public VenueNotify1ListAdapter(List<VenueOpportunityModel> list) {
        mList = list;
    }

    public void refresh(List<VenueOpportunityModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public NotifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_venue_notification1_layout, null, false);

        return new NotifyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NotifyHolder holder, int position) {
        VenueOpportunityModel showModel = mList.get(position);
        StringBuilder stringBuilder = new StringBuilder("Show Opportunity on ");
        if (!TextUtils.isEmpty(showModel.getPerformDate())) {
            holder.tvDate.setText(stringBuilder.append(showModel.getPerformDate()));
        }
        if (!TextUtils.isEmpty(showModel.getPerformTime())) {
            holder.tvDate.setText(" ");
            holder.tvDate.setText(stringBuilder.append(showModel.getPerformTime()));
        }
        holder.tvCreateTime.setText(showModel.getCreateDate());
        List<VenueOpportunityModel.ApplyList> list = showModel.getApplyList();
        String status = showModel.getOpportunityState();
        StringBuilder stringBuilder2 = new StringBuilder();
        if (TextUtils.equals("0", status)) {
            holder.tvDetail.setText("REVIEW");
            stringBuilder2.append(list.size()).append(" Applicants for your review:");
        } else {
            holder.tvDetail.setText("DETAILS");
            stringBuilder2.append(list.size()).append(" Applicants: ");
            stringBuilder2.append(list.size()).append(" Selected");
        }
        holder.tvNumber.setText(stringBuilder2.toString());
        if (list.size() > 0) {
            VenueNotifyItemAdapter mAdapter = new VenueNotifyItemAdapter(list);
            holder.recycleView.setAdapter(mAdapter);
        }

        //onclick
        holder.tvDetail.setTag(position);
        holder.tvDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        VenueOpportunityModel showModel = mList.get(pos);
        switch (v.getId()) {
            case R.id.item_notification_tv_details://详情
                Bundle bundle = new Bundle();
                bundle.putParcelable("key_show_bean", showModel);
                VenueOpportunityDetailFragment.launch(mContext, bundle);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }


    static class NotifyHolder extends RecyclerView.ViewHolder {

        private final TextView tvDate;
        private final TextView tvNumber;
        private final TextView tvCreateTime;
        private final RecyclerView recycleView;
        private final TextView tvDetail;

        public NotifyHolder(View itemView) {
            super(itemView);
            tvCreateTime = (TextView) findViewById(R.id.item_notification_tv_createTime);
            tvNumber = (TextView) findViewById(R.id.item_notification_tv_number);
            tvDate = (TextView) findViewById(R.id.item_notification_tv_date);
            recycleView = (RecyclerView) findViewById(R.id.item_notification_recyclerView);
            tvDetail = (TextView) findViewById(R.id.item_notification_tv_details);
            recycleView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        }

        private View findViewById(int res) {
            return itemView.findViewById(res);
        }
    }
}
