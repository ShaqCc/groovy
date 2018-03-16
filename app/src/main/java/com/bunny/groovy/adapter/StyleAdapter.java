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
import com.socks.library.KLog;

import java.util.List;

/****************************************
 * 功能说明:  表演类型的适配器
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class StyleAdapter extends BaseAdapter {
    private List<StyleModel> dataList;
    private StringBuilder mStringBuffer;

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
        CheckBox mCheckBox = convertView.findViewById(R.id.item_style_checkbox);
        tvName.setText(dataList.get(position).getTypeName());
        mCheckBox.setChecked(dataList.get(position).isChecked());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataList.get(position).setChecked(isChecked);
            }
        });
        return convertView;
    }

    public String getSelectStyle() {
        mStringBuffer = new StringBuilder();
        for (StyleModel model :
                dataList) {
            if (model.isChecked())
                mStringBuffer.append(model.getTypeName()).append(",");
        }
        if (mStringBuffer.length() == 0) return "";
        String substring = mStringBuffer.substring(0, mStringBuffer.length() - 1);
        KLog.a(substring);
        return substring;
    }


    public void refresh(List<StyleModel> list, String selectStyle) {
//        this.dataList = list;
        if (dataList != null && !TextUtils.isEmpty(selectStyle)) {
            String[] split = selectStyle.split(",");

            for (int i = 0; i < dataList.size(); i++) {
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
}
