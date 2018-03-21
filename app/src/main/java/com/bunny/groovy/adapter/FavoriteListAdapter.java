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
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.ui.fragment.apply.ApplyVenueFragment;
import com.bunny.groovy.ui.fragment.apply.VenueDetailFragment;
import com.bunny.groovy.utils.Utils;

import java.util.List;

/****************************************
 * 功能说明:  我收藏的演出厅列表的适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.VenueHolder> implements View.OnClickListener {
    private List<VenueModel> mModelList;
    private Activity mContext;

    public FavoriteListAdapter(List<VenueModel> modelList) {
        mModelList = modelList;
    }

    @Override
    public VenueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_layout, null, false);
        return new VenueHolder(inflate);
    }

    @Override
    public void onBindViewHolder(VenueHolder holder, int position) {
        VenueModel model = mModelList.get(position);
        if (!TextUtils.isEmpty(model.getHeadImg())) {
            Glide.with(mContext).load(model.getHeadImg())
                    .placeholder(R.drawable.icon_load_pic)
                    .error(R.drawable.icon_load_pic)
                    .into(holder.mIvHead);
        } else {
            holder.mIvHead.setImageResource(R.drawable.icon_load_pic);
        }
        holder.mTvName.setText(model.getVenueName());
        holder.mTvStar.setText(model.getVenueScore());
        holder.mTvAddress.setText(model.getVenueAddress());

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

    public void refresh(List<VenueModel> list) {
        this.mModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        switch (v.getId()){
            case R.id.item_favorite_iv_phone:
                Utils.CallPhone(mContext,mModelList.get(pos).getPhoneNumber());
                break;
            case R.id.item_favorite_iv_email:
                Utils.sendEmail(mContext,mModelList.get(pos).getVenueEmail());
                break;
            case R.id.item_favorite_tv_apply:
                Bundle arg = new Bundle();
                arg.putParcelable(ApplyVenueFragment.KEY_VENUE_BEAN,mModelList.get(pos));
                ApplyVenueFragment.launch(mContext,arg);
                break;
            default:
                Bundle bundle = new Bundle();
                bundle.putString(VenueDetailFragment.KEY_VENUE_ID,mModelList.get(pos).getVenueID());
                VenueDetailFragment.launch(mContext,bundle);
                break;
        }
    }

    static class VenueHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvHead;
        private final TextView mTvName;
        private final TextView mTvStar;
        private final ImageView mBtPhone, mBtEmail;
        private final TextView mTvAddress, mBtApply;

        public VenueHolder(View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.item_favorite_iv_header);
            mTvName = itemView.findViewById(R.id.item_favorite_tv_name);
            mTvStar = itemView.findViewById(R.id.item_favorite_tv_score);
            mTvAddress = itemView.findViewById(R.id.item_favorite_tv_address);
            mBtPhone = itemView.findViewById(R.id.item_favorite_iv_phone);
            mBtEmail = itemView.findViewById(R.id.item_favorite_iv_email);
            mBtApply = itemView.findViewById(R.id.item_favorite_tv_apply);
        }
    }
}
