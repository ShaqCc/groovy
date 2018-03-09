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
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.ui.fragment.releaseshow.VenueShowDetailFragment;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.weidget.HeightLightTextView;

import java.util.List;

/****************************************
 * 功能说明:  个人中心历史记录适配器
 *
 * Author: Created by bayin on 2017/12/25.
 ****************************************/

public class ShowMusicianHistoryAdapter extends RecyclerView.Adapter<ShowMusicianHistoryAdapter.HisHolder> {
    private List<VenueShowModel> mList;
    private Activity mContext;

    public ShowMusicianHistoryAdapter(List<VenueShowModel> list) {
        mList = list;
    }

    public void refresh(List<VenueShowModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public HisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_show_musician_history_adapter_layout, null, false);
        return new HisHolder(inflate);
    }

    @Override
    public void onBindViewHolder(HisHolder holder, int position) {
        final VenueShowModel bean = mList.get(position);
        Glide.with(mContext).load(bean.getPerformerImg())
                .placeholder(R.drawable.venue_instead_pic)
                .error(R.drawable.venue_instead_pic)
                .into(holder.mIvHead);
        holder.mTvStar.setText(Utils.getStar(bean.getPerformerScore()));
        holder.mTvName.setText(bean.getPerformerName());
        holder.mTvStyle.setText(bean.getPerformType());
        holder.mTvShowTime.setText(bean.getPerformDate() + " " + bean.getPerformTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(VenueShowDetailFragment.KEY_SHOW_BEAN,bean);
                VenueShowDetailFragment.launch(mContext, bundle);
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
            mIvHead = (ImageView) itemView.findViewById(R.id.iv_musician_head_pic);
            mTvName = (HeightLightTextView) itemView.findViewById(R.id.musician_tv_name);
            mTvStar = (TextView) itemView.findViewById(R.id.musician_tv_performerStar);
            mTvStyle = (TextView) itemView.findViewById(R.id.musician_tv_type);
            mTvShowTime = (TextView) itemView.findViewById(R.id.musician_tv_time);
        }
    }
}
