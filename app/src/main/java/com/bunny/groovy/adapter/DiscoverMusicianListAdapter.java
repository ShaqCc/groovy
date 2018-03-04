package com.bunny.groovy.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.ui.fragment.releaseshow.InviteMusicianFragment;
import com.bunny.groovy.ui.fragment.releaseshow.SearchMusicianFragment;
import com.bunny.groovy.weidget.HeightLightTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/****************************************
 * 功能说明:  发现表演者列表的适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class DiscoverMusicianListAdapter extends RecyclerView.Adapter<DiscoverMusicianListAdapter.MusicianHolder> implements View.OnClickListener {
    private List<PerformerUserModel> mModelList;
    private String keyword;
    private Activity mContext;

    public DiscoverMusicianListAdapter(List<PerformerUserModel> modelList, String key) {
        mModelList = modelList;
        this.keyword = key;
    }

    @Override
    public MusicianHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.include_discover_musician_adapter_layout, null, false);
        return new MusicianHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MusicianHolder holder, int position) {
        PerformerUserModel performerModel = mModelList.get(position);
        if (!TextUtils.isEmpty(performerModel.getHeadImg())) {
            Glide.with(mContext).load(performerModel.getHeadImg())
                    .placeholder(R.mipmap.icon_load_pic)
                    .error(R.mipmap.icon_load_pic).dontAnimate()
                    .into(holder.mIvHead);
        } else {
            holder.mIvHead.setImageResource(R.mipmap.icon_load_pic);
        }
        holder.mTvName.setTextHeighLight(performerModel.getUserName(), keyword);
        holder.mTvStar.setText(performerModel.getStarLevel());
        holder.mTvDistance.setText(performerModel.getDistance() + "m");
        holder.mTvType.setText(performerModel.getPerformTypeName());

        holder.mTvInvite.setTag(position);
        holder.mTvInvite.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null && mModelList.size() > 0) return mModelList.size();

        return 0;
    }

    public void refresh(List<PerformerUserModel> list, String keyword) {
        this.mModelList = list;
        this.keyword = keyword;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        PerformerUserModel model = mModelList.get(pos);
        InviteMusicianFragment.launch(mContext, model);
    }

    static class MusicianHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvHead;
        private final HeightLightTextView mTvName;
        private final TextView mTvStar;
        private final TextView mTvType;
        private final TextView mTvDistance;
        private final TextView mTvInvite;

        public MusicianHolder(View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.iv_musician_head_pic);
            mTvName = itemView.findViewById(R.id.musician_tv_name);
            mTvStar = itemView.findViewById(R.id.musician_tv_performerStar);
            mTvType = itemView.findViewById(R.id.musician_tv_type);
            mTvDistance = itemView.findViewById(R.id.musician_tv_distance);
            mTvInvite = itemView.findViewById(R.id.musician_tv_invite);
        }
    }
}
