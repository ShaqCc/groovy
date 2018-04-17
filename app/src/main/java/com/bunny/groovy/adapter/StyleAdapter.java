package com.bunny.groovy.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.utils.UIUtils;
import com.socks.library.KLog;

import java.util.List;

/****************************************
 * 功能说明:  表演类型的适配器
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class StyleAdapter extends BaseAdapter {
    private List<StyleModel> dataList;
    private int mMax = 2;
    private int mCount = 0;

    public void setMax(int max) {
        this.mMax = max;
    }

    public StyleAdapter(List<StyleModel> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if (dataList != null) return dataList.size();
        return 0;
    }

    @Override
    public StyleModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.item_perform_style_layout, null);
        TextView tvName = convertView.findViewById(R.id.item_style_tv_name);
        final CheckBox mCheckBox = convertView.findViewById(R.id.item_style_checkbox);
        tvName.setText(dataList.get(position).getTypeName());
        mCheckBox.setChecked(dataList.get(position).isChecked());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StyleModel styleModel = dataList.get(position);
                if (styleModel.isChecked()) {
                    styleModel.setChecked(false);
                    mCount --;
                    if(mCount <0 ) mCount = 0;
                } else {
                    if (mCount >= mMax) {
                        UIUtils.showBaseToast("You can only choose " + mMax + " num.");
                        return;
                    }else {
                        styleModel.setChecked(true);
                        mCount++;
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public String getSelectStyle() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StyleModel model :
                dataList) {
            if (model.isChecked())
                stringBuilder.append(model.getTypeName()).append(",");
        }
        if (stringBuilder.length() == 0) return "";
        String substring = stringBuilder.substring(0, stringBuilder.length() - 1);
        return substring;
    }


    public void refresh(List<StyleModel> list, String selectStyle) {
//        this.dataList = list;
        mCount = 0;
        if (dataList != null && !TextUtils.isEmpty(selectStyle)) {
            String[] split = selectStyle.split(",");

            for (int i = 0; i < dataList.size(); i++) {
                for (String str :
                        split) {
                    if (str.equals(dataList.get(i).getTypeName())) {
                        mCount++;
                        dataList.get(i).setChecked(true);
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
