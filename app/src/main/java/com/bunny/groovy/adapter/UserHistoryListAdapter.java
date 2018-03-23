package com.bunny.groovy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.PerformDetail;
import com.bunny.groovy.ui.fragment.releaseshow.UserShowDetailFragment;
import com.bunny.groovy.ui.fragment.user.RewardFragment;
import com.bunny.groovy.ui.fragment.user.UserReviewFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/****************************************
 * 功能说明:  普通用户-我收藏的所有表演者列表的适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class UserHistoryListAdapter extends RecyclerView.Adapter<UserHistoryListAdapter.VenueHolder> implements View.OnClickListener {
    private List<PerformDetail> mModelList;
    private Activity mContext;

    public UserHistoryListAdapter(List<PerformDetail> modelList) {
        mModelList = modelList;
    }

    @Override
    public VenueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_user_history_layout, null, false);
        return new VenueHolder(inflate);
    }

    @Override
    public void onBindViewHolder(VenueHolder holder, int position) {
        PerformDetail model = mModelList.get(position);
        //表演者头像
        String headImg = model.getPerformerImg();
        if (TextUtils.isEmpty(headImg)) {
            holder.mIvHead.setImageResource(R.drawable.icon_default_photo);
        } else {
            Glide.with(mContext).load(headImg).placeholder(R.drawable.icon_default_photo).into(holder.mIvHead);
        }
        //表演者姓名
        holder.mTvName.setText(model.getPerformerName());
        //评分
        holder.mTvStar.setText(TextUtils.isEmpty(model.getPerformerStarLevel()) ? "0":model.getPerformerStarLevel());
        //演出厅名字
        holder.mTvVenueName.setText("@" + model.getVenueName());
        //演出厅地址
        holder.mTvAddress.setText(model.getVenueAddress());
        //演出时间
        holder.mTvTime.setText(model.getPerformDate() + " " + model.getPerformTime());
        //评论
        holder.mTvReview.setVisibility(TextUtils.isEmpty(model.getIsEvaluate()) ? View.VISIBLE : View.INVISIBLE);

        //点击效果
        holder.mTvReview.setTag(position);
        holder.mTvReward.setTag(position);
        holder.itemView.setTag(position);
        holder.mTvReview.setOnClickListener(this);
        holder.mTvReward.setOnClickListener(this);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null && mModelList.size() > 0) return mModelList.size();

        return 0;
    }

    public void refresh(List<PerformDetail> list) {
        this.mModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        try {
            int pos = (int) v.getTag();
            PerformDetail performDetail = mModelList.get(pos);
            switch (v.getId()) {
                case R.id.tv_reward:
                    RewardFragment.launch(mContext, performDetail.getPerformerID(), true);
                    break;
                case R.id.tv_review:
                    UserReviewFragment.launch(mContext, performDetail.getPerformID());
                    break;
                default:
                    UserShowDetailFragment.launch(mContext, performDetail, true);
                    break;
            }
        } catch (Exception e) {
        }

    }

    static class VenueHolder extends RecyclerView.ViewHolder {

        private final CircleImageView mIvHead;
        private final TextView mTvName;
        private final TextView mTvStar;
        private final TextView mTvTime;
        private final TextView mTvVenueName;
        private final TextView mTvAddress;
        private final TextView mTvReward;
        private final TextView mTvReview;

        public VenueHolder(View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.iv_head);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvStar = itemView.findViewById(R.id.tv_score);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvVenueName = itemView.findViewById(R.id.tv_venue_name);
            mTvAddress = itemView.findViewById(R.id.tv_venue_address);
            mTvReward = itemView.findViewById(R.id.tv_reward);
            mTvReview = itemView.findViewById(R.id.tv_review);
        }
    }
}
