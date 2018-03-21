package com.bunny.groovy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.ui.fragment.apply.MusicianDetailFragment;
import com.bunny.groovy.ui.fragment.releaseshow.InviteMusicianFragment;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.weidget.HeightLightTextView;

import java.util.List;

/****************************************
 * 功能说明:  收藏表演者列表的适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class CollectMusicianListAdapter extends RecyclerView.Adapter<CollectMusicianListAdapter.MusicianHolder> implements View.OnClickListener {
    private List<PerformerUserModel> mModelList;
    private Activity mContext;

    public CollectMusicianListAdapter(List<PerformerUserModel> modelList) {
        mModelList = modelList;
    }

    @Override
    public MusicianHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_collect_musician_adapter_layout, null, false);
        return new MusicianHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MusicianHolder holder, int position) {
        PerformerUserModel musicianModel = mModelList.get(position);
        if (!TextUtils.isEmpty(musicianModel.getHeadImg())) {
            Glide.with(mContext).load(musicianModel.getHeadImg())
                    .placeholder(R.drawable.icon_load_pic)
                    .error(R.drawable.icon_load_pic).dontAnimate()
                    .into(holder.mIvHead);
        } else {
            holder.mIvHead.setImageResource(R.drawable.icon_load_pic);
        }
        holder.mTvName.setText(musicianModel.getStageName());
        holder.mTvStar.setText(Utils.getStar(musicianModel.getStarLevel()));
        holder.mTvType.setText(musicianModel.getPerformTypeName());

        holder.mTvInvite.setTag(position);
        holder.mTvInvite.setOnClickListener(this);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null && mModelList.size() > 0) return mModelList.size();

        return 0;
    }

    public void refresh(List<PerformerUserModel> list) {
        this.mModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        PerformerUserModel model = mModelList.get(pos);
        if (v.getId() == R.id.musician_tv_invite) {
            InviteMusicianFragment.launch(mContext, model);
        } else {
            MusicianDetailFragment.launch(mContext, model.getUserID());
        }
    }

    static class MusicianHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvHead;
        private final HeightLightTextView mTvName;
        private final TextView mTvStar;
        private final TextView mTvType;
        private final TextView mTvInvite;

        public MusicianHolder(View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.iv_musician_head_pic);
            mTvName = itemView.findViewById(R.id.musician_tv_name);
            mTvStar = itemView.findViewById(R.id.musician_tv_performerStar);
            mTvType = itemView.findViewById(R.id.musician_tv_type);
            mTvInvite = itemView.findViewById(R.id.musician_tv_invite);
        }
    }
}
