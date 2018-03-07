package com.bunny.groovy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.VenueOpportunityModel;

import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueNotifyItemAdapter extends RecyclerView.Adapter<VenueNotifyItemAdapter.NotifyHolder> implements View.OnClickListener {
    private List<VenueOpportunityModel.ApplyList> mList;
    private Activity mContext;

    public VenueNotifyItemAdapter(List<VenueOpportunityModel.ApplyList> list) {
        mList = list;
    }

    public void refresh(List<VenueOpportunityModel.ApplyList> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public NotifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_venue_notification_item_layout, null, false);

        return new NotifyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NotifyHolder holder, int position) {
        VenueOpportunityModel.ApplyList showModel = mList.get(position);
        String status = showModel.getApplyState();
        holder.tvImgHeadDark.setVisibility(TextUtils.equals("1", status) ? View.GONE : View.VISIBLE);
        Glide.with(mContext).load(showModel.getHeadImg()).error(R.mipmap.venue_instead_pic).into(holder.tvImgHead);
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }


    static class NotifyHolder extends RecyclerView.ViewHolder {

        private final ImageView tvImgHead;
        private final ImageView tvImgHeadDark;

        public NotifyHolder(View itemView) {
            super(itemView);
            tvImgHead = (ImageView) findViewById(R.id.item_notification_iv_header);
            tvImgHeadDark = (ImageView) findViewById(R.id.item_notification_iv_header_dark);
        }

        private View findViewById(int res) {
            return itemView.findViewById(res);
        }
    }
}
