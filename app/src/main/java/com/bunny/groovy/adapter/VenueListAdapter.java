package com.bunny.groovy.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.weidget.HeightLightTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/****************************************
 * 功能说明:  演出厅列表的适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class VenueListAdapter extends RecyclerView.Adapter<VenueListAdapter.VenueHolder> implements View.OnClickListener {
    private List<VenueModel> mModelList;
    private String keyword;
    private Activity mContext;

    public VenueListAdapter(List<VenueModel> modelList, String key) {
        mModelList = modelList;
        this.keyword = key;
    }

    @Override
    public VenueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.include_recent_show_layout, null, false);
        return new VenueHolder(inflate);
    }

    @Override
    public void onBindViewHolder(VenueHolder holder, int position) {
        VenueModel venueModel = mModelList.get(position);
        if (!TextUtils.isEmpty(venueModel.getHeadImg())) {
            Glide.with(mContext).load(venueModel.getHeadImg())
                    .placeholder(R.drawable.icon_load_pic)
                    .error(R.drawable.icon_load_pic)
                    .into(holder.mIvHead);
        } else {
            holder.mIvHead.setImageResource(R.drawable.icon_load_pic);
        }
        holder.mTvName.setTextHeighLight(venueModel.getVenueName(), keyword);
        holder.mTvStar.setText(Utils.getStar(venueModel.getVenueScore()));
        holder.mTvPhone.setText(venueModel.getPhoneNumber());
        holder.mTvAddress.setText(venueModel.getVenueAddress());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null && mModelList.size() > 0) return mModelList.size();

        return 0;
    }

    public void refresh(List<VenueModel> list, String keyword) {
        this.mModelList = list;
        this.keyword = keyword;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        EventBus.getDefault().post(mModelList.get(pos));
        mContext.finish();
    }

    static class VenueHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvHead;
        private final HeightLightTextView mTvName;
        private final TextView mTvStar;
        private final TextView mTvPhone;
        private final TextView mTvAddress;

        public VenueHolder(View itemView) {
            super(itemView);
            mIvHead = (ImageView) itemView.findViewById(R.id.nextshow_iv_head);
            mTvName = (HeightLightTextView) itemView.findViewById(R.id.nextshow_tv_performerName);
            mTvStar = (TextView) itemView.findViewById(R.id.nextshow_tv_performerStar);
            mTvPhone = (TextView) itemView.findViewById(R.id.nextshow_tv_address);
            mTvAddress = (TextView) itemView.findViewById(R.id.nextshow_tv_time);
        }
    }
}
