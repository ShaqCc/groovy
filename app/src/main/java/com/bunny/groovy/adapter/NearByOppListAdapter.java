package com.bunny.groovy.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.OpportunityModel;
import com.bunny.groovy.presenter.ExplorerOpptnyPresenter;
import com.bunny.groovy.ui.fragment.apply.ApplyOppFragment;
import com.bunny.groovy.ui.fragment.releaseshow.OpportunityDetailFragment;
import com.bunny.groovy.utils.Utils;

import java.util.List;

/****************************************
 * 功能说明:  地图筛选 周边演出机会
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class NearByOppListAdapter extends RecyclerView.Adapter<NearByOppListAdapter.NearByHolder> implements View.OnClickListener {
    private List<OpportunityModel> mModelList;
    private Activity mContext;
    private ExplorerOpptnyPresenter mPresenter;

    public NearByOppListAdapter(List<OpportunityModel> modelList) {
        mModelList = modelList;
    }

    public void setPresenter(ExplorerOpptnyPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public NearByHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_nearby_opp_layout, null, false);
        return new NearByHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NearByHolder holder, int position) {
        OpportunityModel model = mModelList.get(position);
        if (!TextUtils.isEmpty(model.getHeadImg())) {
            Glide.with(mContext).load(model.getHeadImg())
                    .placeholder(R.drawable.venue_instead_pic)
                    .error(R.drawable.venue_instead_pic)
                    .into(holder.mIvHead);
        } else {
            holder.mIvHead.setImageResource(R.drawable.venue_instead_pic);
        }
        holder.mTvName.setText(model.getVenueName());
        holder.mTvStar.setText(model.getVenueScore());
        holder.mTvAddress.setText(model.getVenueAddress());
        holder.mTvDistance.setText(model.getDistance() + "mi");
        holder.mTvTime.setText(model.getPerformDate() + " " + model.getPerformTime());

        holder.itemView.setTag(position);//详情
        holder.mBtPhone.setTag(position);//打电话
        holder.mBtEmail.setTag(position);//发送邮件
        holder.mBtApply.setTag(position);//申请

        holder.itemView.setOnClickListener(this);
        holder.mBtPhone.setOnClickListener(this);
        holder.mBtEmail.setOnClickListener(this);
        holder.mBtApply.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null && mModelList.size() > 0) return mModelList.size();

        return 0;
    }

    public void refresh(List<OpportunityModel> list) {
        this.mModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        OpportunityModel model = mModelList.get(pos);
        switch (v.getId()) {
            case R.id.item_nearby_iv_phone:
                Utils.CallPhone(mContext, model.getPhoneNumber());
                break;
            case R.id.item_nearby_iv_email:
                Utils.sendEmail(mContext, model.getVenueEmail());
                break;
            case R.id.item_nearby_tv_apply:
                //申请
                Bundle arg = new Bundle();
                arg.putParcelable(ApplyOppFragment.KEY_OPP_BEAN, model);
                ApplyOppFragment.launch(mContext, arg);
                break;
            default:
                //详情
                Bundle bundle = new Bundle();
                bundle.putParcelable(OpportunityDetailFragment.KEY_OPPORTUNITY_BEAN, model);
                OpportunityDetailFragment.launch(mContext, bundle);
                break;
        }
    }

    static class NearByHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvHead;
        private final TextView mTvName;
        private final TextView mTvStar;
        private final ImageView mBtPhone, mBtEmail;
        private final TextView mTvAddress, mBtApply;
        private final TextView mTvTime;
        private final TextView mTvDistance;

        public NearByHolder(View itemView) {
            super(itemView);
            mIvHead = (ImageView) itemView.findViewById(R.id.item_nearby_iv_header);
            mTvName = (TextView) itemView.findViewById(R.id.item_nearby_tv_name);
            mTvStar = (TextView) itemView.findViewById(R.id.item_nearby_tv_score);
            mTvAddress = (TextView) itemView.findViewById(R.id.item_nearby_tv_address);
            mBtPhone = (ImageView) itemView.findViewById(R.id.item_nearby_iv_phone);
            mBtEmail = (ImageView) itemView.findViewById(R.id.item_nearby_iv_email);
            mBtApply = (TextView) itemView.findViewById(R.id.item_nearby_tv_apply);
            mTvTime = (TextView) itemView.findViewById(R.id.item_nearby_tv_perform_date);
            mTvDistance = (TextView) itemView.findViewById(R.id.item_nearby_tv_distance);
        }
    }
}
