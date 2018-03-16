package com.bunny.groovy.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.ui.fragment.releaseshow.ShowDetailFragment;
import com.bunny.groovy.weidget.HeightLightTextView;

import java.util.List;

/****************************************
 * 功能说明:  个人中心历史记录适配器
 *
 * Author: Created by bayin on 2017/12/25.
 ****************************************/

public class ShowHistoryAdapter extends RecyclerView.Adapter<ShowHistoryAdapter.HisHolder> {
    private List<ShowModel> mList;
    private Activity mContext;

    public ShowHistoryAdapter(List<ShowModel> list) {
        mList = list;
    }

    public void refresh(List<ShowModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public HisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.include_recent_show_layout, null, false);
        return new HisHolder(inflate);
    }

    @Override
    public void onBindViewHolder(HisHolder holder, int position) {
        final ShowModel bean = mList.get(position);
        Glide.with(mContext).load(bean.getHeadImg())
                .placeholder(R.drawable.venue_instead_pic)
                .error(R.drawable.venue_instead_pic)
                .into(holder.mIvHead);
        holder.mTvStar.setText(bean.getVenueScore());
        holder.mTvName.setText(bean.getVenueName());
        holder.mTvStyle.setText(bean.getPerformType());
        holder.mTvShowTime.setText(bean.getPerformDate() + " " + bean.getPerformTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ShowDetailFragment.KEY_SHOW_BEAN,bean);
                ShowDetailFragment.launch(mContext, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    static class HisHolder extends RecyclerView.ViewHolder {
        private final ImageView mIvHead;
        private final HeightLightTextView mTvName;
        private final TextView mTvStar;
        private final TextView mTvStyle;
        private final TextView mTvShowTime;

        public HisHolder(View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.nextshow_iv_head);
            mTvName = itemView.findViewById(R.id.nextshow_tv_performerName);
            mTvStar = itemView.findViewById(R.id.nextshow_tv_performerStar);
            mTvStyle = itemView.findViewById(R.id.nextshow_tv_address);
            mTvShowTime = itemView.findViewById(R.id.nextshow_tv_time);
        }
    }
}
