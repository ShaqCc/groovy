package com.bunny.groovy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.VenueOpportunityModel;
import com.bunny.groovy.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueOpportunityDetailAdapter extends RecyclerView.Adapter<VenueOpportunityDetailAdapter.NotifyHolder> implements View.OnClickListener {
    private List<VenueOpportunityModel.ApplyList> mList;
    private static Activity mContext;
    private int mPosition;
    private boolean mIsChoose;


    public VenueOpportunityDetailAdapter(List<VenueOpportunityModel.ApplyList> list, boolean isChoose) {
        mList = list;
        mIsChoose = isChoose;
    }

    public void refresh(List<VenueOpportunityModel.ApplyList> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public NotifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_notification_item_adapter_layout, null, false);

        return new NotifyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NotifyHolder holder, int position) {
        VenueOpportunityModel.ApplyList showModel = mList.get(position);
        holder.viewTopLine.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        holder.viewBottomLine.setVisibility((mList.size() > 0 && mList.size() == position + 1) ? View.INVISIBLE : View.VISIBLE);
        holder.tvName.setText(showModel.getUserName());
        holder.tvStar.setText(Utils.getStar(showModel.getStarLevel()));
        holder.tvType.setText(showModel.getPerformType());
        holder.tvDes.setText(showModel.getPerformDesc());
        holder.imgSelect.setImageResource(mIsChoose ? R.drawable.selector_perform_choosed_light : R.drawable.selector_perform_light);
        String chooseStatus = showModel.getApplyState();
        if (mIsChoose) {
            holder.imgSelect.setSelected(TextUtils.equals("1", chooseStatus));
        } else {
            holder.imgSelect.setSelected(mPosition == position);
        }
        Glide.with(mContext).load(showModel.getHeadImg()).placeholder(R.drawable.head)
                .error(R.drawable.head).dontAnimate().into(holder.imgHead);

        holder.layout.setTag(position);
        holder.layout.setOnClickListener(this);
        holder.imgSelect.setTag(position);
        holder.imgSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!mIsChoose) {
            int pos = (int) v.getTag();
            switch (v.getId()) {
                case R.id.select_img:
                case R.id.detail_layout:
                    if (mOnSelectListener != null) {
                        mOnSelectListener.select(pos);
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }


    static class NotifyHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout layout;
        private final ImageView imgHead;
        private final ImageView imgSelect;
        private final View viewTopLine;
        private final View viewBottomLine;
        private final TextView tvName;
        private final TextView tvStar;
        private final TextView tvType;
        private final TextView tvDes;

        public NotifyHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) findViewById(R.id.detail_layout);
            imgHead = (ImageView) findViewById(R.id.item_notification_iv_header);
            imgSelect = (ImageView) findViewById(R.id.select_img);
            viewTopLine = findViewById(R.id.select_top_line);
            viewBottomLine = findViewById(R.id.select_bottom_line);
            tvName = (TextView) findViewById(R.id.item_notification_tv_name);
            tvStar = (TextView) findViewById(R.id.item_notification_tv_venueStar);
            tvType = (TextView) findViewById(R.id.item_notification_tv_type);
            tvDes = (TextView) findViewById(R.id.item_notification_tv_des);
        }

        private View findViewById(int res) {
            return itemView.findViewById(res);
        }
    }

    public void selectPosition(int position) {
        mPosition = position;
        notifyDataSetChanged();
    }

    public interface OnSelectListener {
        void select(int position);
    }

    private OnSelectListener mOnSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        mOnSelectListener = listener;
    }
}
