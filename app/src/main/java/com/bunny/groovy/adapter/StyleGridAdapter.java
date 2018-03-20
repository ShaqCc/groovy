package com.bunny.groovy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.utils.UIUtils;
import com.socks.library.KLog;

import java.util.List;

/****************************************
 * 功能说明: 表演类型适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class StyleGridAdapter extends RecyclerView.Adapter<StyleGridAdapter.StyleHolder> {
    private List<StyleModel> mDataList;
    private Context mContext;
    private String mSelectStyle;
    private final RelativeLayout.LayoutParams params;
    private int mCount = 0;
    private int mMaxNum = 2;

    public StyleGridAdapter(List<StyleModel> dataList, String selected) {
        this.mDataList = dataList;
        mSelectStyle = selected;
        setCheckData();
        int unitWidth = (UIUtils.getScreenWidth() - UIUtils.dip2Px(32)) / 3;
        params = new RelativeLayout.LayoutParams(unitWidth, unitWidth);
    }

    @Override
    public StyleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_style_grid_layout, null, false);
        return new StyleHolder(inflate);
    }

    public void setSelectNum(int num) {
        this.mMaxNum = num;
    }

    @Override
    public void onBindViewHolder(final StyleHolder holder, final int position) {
        final StyleModel styleModel = mDataList.get(position);
        if (!TextUtils.isEmpty(styleModel.getTypeImg())) {
            Glide.with(mContext).load(styleModel.getTypeImg()).into(holder.mIvPic);
        } else holder.mIvPic.setImageResource(R.drawable.icon_load_pic);
        holder.itemView.setLayoutParams(params);
        KLog.d(position + "--" + styleModel.isChecked());
        if (styleModel.isChecked()) {
            holder.mTvCheckBox.setBackgroundResource(R.drawable.btn_square_selected);
        } else {
            holder.mTvCheckBox.setBackgroundResource(R.drawable.btn_square);
        }
        holder.mTvName.setText(styleModel.getTypeName());
        holder.mTvCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (styleModel.isChecked()) {
                    holder.mTvCheckBox.setBackgroundResource(R.drawable.btn_square);
                    mDataList.get(position).setChecked(false);
                } else {
                    mCount = 0;
                    for (StyleModel model :
                            mDataList) {
                        if (model.isChecked()) mCount++;
                    }
                    if (mCount >= mMaxNum) {
                        UIUtils.showBaseToast("You can only choose " + mMaxNum + " num.");
                        return;
                    }
                    holder.mTvCheckBox.setBackgroundResource(R.drawable.btn_square_selected);
                    mDataList.get(position).setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) return mDataList.size();
        return 0;
    }

    public String getSelectStyles() {
        mSelectStyle = "";
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).isChecked())
                mSelectStyle = mSelectStyle + mDataList.get(i).getTypeName() + ",";
        }
        if (mSelectStyle.length() > 0)
            return mSelectStyle.substring(0, mSelectStyle.length() - 1);
        return mSelectStyle;
    }

    public void updateSelectedStyle(List<StyleModel> list, String selectedStyle) {
        this.mDataList = list;
        setCheckData();
        notifyDataSetChanged();
    }

    public void setCheckData() {
        if (mDataList != null && !TextUtils.isEmpty(mSelectStyle)) {
            String[] split = mSelectStyle.split(",");
            for (StyleModel model : mDataList) {
                model.setChecked(false);
                for (String str : split) {
                    if (TextUtils.equals(str, model.getTypeName())) {
                        model.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    static class StyleHolder extends RecyclerView.ViewHolder {

        private final Button mTvCheckBox;
        private final TextView mTvName;
        private final ImageView mIvPic;

        public StyleHolder(View itemView) {
            super(itemView);
            mIvPic = itemView.findViewById(R.id.item_style_grid_iv_pic);
            mTvName = itemView.findViewById(R.id.item_style_grid_tv_name);
            mTvCheckBox = itemView.findViewById(R.id.item_style_grid_cb);
        }
    }

}
