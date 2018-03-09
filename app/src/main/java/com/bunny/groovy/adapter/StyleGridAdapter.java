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
    private List<StyleModel> dataList;
    private Context mContext;
    private String selectedStyle;
    private final RelativeLayout.LayoutParams params;
    private int mCount = 0;
    private int mMaxNum = 2;

    public StyleGridAdapter(List<StyleModel> dataList, String selected) {
        this.dataList = dataList;
        selectedStyle = selected;
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
        final StyleModel styleModel = dataList.get(position);
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
                    dataList.get(position).setChecked(false);
                } else {
                    mCount = 0;
                    for (StyleModel model :
                            dataList) {
                        if (model.isChecked()) mCount++;
                    }
                    if (mCount >= mMaxNum) {
                        UIUtils.showBaseToast("最多选两个");
                        return;
                    }
                    holder.mTvCheckBox.setBackgroundResource(R.drawable.btn_square_selected);
                    dataList.get(position).setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList != null) return dataList.size();
        return 0;
    }

    public String getSelectStyles() {
        selectedStyle = "";
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isChecked())
                selectedStyle = selectedStyle + dataList.get(i).getTypeName() + ",";
        }
        if (selectedStyle.length() > 0)
            return selectedStyle.substring(0, selectedStyle.length() - 1);
        return selectedStyle;
    }

    public void updateSelectedStyle(List<StyleModel> list, String selectedStyle) {
        this.dataList = list;
        if (dataList != null && !TextUtils.isEmpty(selectedStyle)) {
            String[] split = selectedStyle.split(",");

            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).setChecked(false);
                for (String str :
                        split) {
                    if (str.equals(dataList.get(i).getTypeName())) {
                        dataList.get(i).setChecked(true);
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    static class StyleHolder extends RecyclerView.ViewHolder {

        private final Button mTvCheckBox;
        private final TextView mTvName;
        private final ImageView mIvPic;

        public StyleHolder(View itemView) {
            super(itemView);
            mIvPic = (ImageView) itemView.findViewById(R.id.item_style_grid_iv_pic);
            mTvName = (TextView) itemView.findViewById(R.id.item_style_grid_tv_name);
            mTvCheckBox = (Button) itemView.findViewById(R.id.item_style_grid_cb);
        }
    }

}
