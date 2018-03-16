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
import com.bunny.groovy.model.MusicianModel;
import com.bunny.groovy.ui.fragment.apply.MusicianDetailFragment;

import java.util.List;

/****************************************
 * 功能说明:  普通用户-我收藏的所有表演者列表的适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class UserFavoriteListAdapter extends RecyclerView.Adapter<UserFavoriteListAdapter.VenueHolder> implements View.OnClickListener {
    private List<MusicianModel> mModelList;
    private Activity mContext;

    public UserFavoriteListAdapter(List<MusicianModel> modelList) {
        mModelList = modelList;
    }

    @Override
    public VenueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_user_favorite_layout, null, false);
        return new VenueHolder(inflate);
    }

    @Override
    public void onBindViewHolder(VenueHolder holder, int position) {
        MusicianModel model = mModelList.get(position);
        if (!TextUtils.isEmpty(model.headImg)) {
            Glide.with(mContext).load(model.headImg)
                    .placeholder(R.drawable.icon_default_photo)
                    .error(R.drawable.icon_default_photo)
                    .into(holder.mIvHead);
        } else {
            holder.mIvHead.setImageResource(R.drawable.icon_default_photo);
        }
        holder.mTvName.setText(model.stageName);
        holder.mTvStar.setText(model.starLevel);
        if (model.nextPerform != null) {
            holder.mTvTime.setText("NEXT SHOW, -" + model.nextPerform.getPerformDate());
        } else {
            holder.mTvTime.setText("NEXT SHOW, -");
        }


        holder.itemView.setTag(position);//详情
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null && mModelList.size() > 0) return mModelList.size();

        return 0;
    }

    public void refresh(List<MusicianModel> list) {
        this.mModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        MusicianModel musicianModel = mModelList.get(pos);
        MusicianDetailFragment.launch(mContext,musicianModel.userID);
    }

    static class VenueHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvHead;
        private final TextView mTvName;
        private final TextView mTvStar;
        private final TextView mTvTime;

        public VenueHolder(View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.iv_head);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvStar = itemView.findViewById(R.id.tv_score);
            mTvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
