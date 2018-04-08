package com.bunny.groovy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunny.groovy.R;
import com.bunny.groovy.model.LocationModel;
import com.bunny.groovy.weidget.HeightLightTextView;

import java.util.List;

/****************************************
 * 功能说明:  地址搜索列表adapter
 *
 * Author: Created by wjy on 2018/4/8.
 ****************************************/

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.LocationHolder> implements View.OnClickListener {
    private List<LocationModel.LocationDetail> mModelList;
    private Activity mContext;
    private String mKeyword;

    public void setKeyword(String keyword) {
        this.mKeyword = keyword;
    }

    public SearchListAdapter(List<LocationModel.LocationDetail> modelList) {
        mModelList = modelList;
    }

    @Override
    public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_search_location_layout, null, false);
        return new LocationHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LocationHolder holder, int position) {
        LocationModel.LocationDetail model = mModelList.get(position);
        //地址
        holder.mTvAddress.setTextHeighLight(model.name, mKeyword);
        //点击效果
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null) return mModelList.size();
        return 0;
    }

    public void refresh(List<LocationModel.LocationDetail> list) {
        this.mModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        try {
            int pos = (int) v.getTag();
            LocationModel.LocationDetail locationModel = mModelList.get(pos);
        } catch (Exception e) {
        }
    }

    static class LocationHolder extends RecyclerView.ViewHolder {

        private final HeightLightTextView mTvAddress;

        public LocationHolder(View itemView) {
            super(itemView);
            mTvAddress = itemView.findViewById(R.id.tv_address);
        }
    }
}
