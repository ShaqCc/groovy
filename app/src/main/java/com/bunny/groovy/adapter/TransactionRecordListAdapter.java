package com.bunny.groovy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.MusicianDetailModel;

import java.util.List;

/****************************************
 * 功能说明:  打赏记录适配器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class TransactionRecordListAdapter extends RecyclerView.Adapter<TransactionRecordListAdapter.MusicianHolder> {
    private List<MusicianDetailModel.TransactionRecord> mModelList;
    private Activity mContext;

    public TransactionRecordListAdapter(List<MusicianDetailModel.TransactionRecord> modelList) {
        mModelList = modelList;
    }

    @Override
    public MusicianHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_transaction_record_layout, null, false);
        return new MusicianHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MusicianHolder holder, int position) {
        MusicianDetailModel.TransactionRecord record = mModelList.get(position);
        holder.mTvAmount.setText(record.cost + "$");
        holder.mTvTime.setText(record.dealDate);
        holder.tvTopLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        holder.tvBottomLine.setVisibility(position == mModelList.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if (mModelList != null && mModelList.size() > 0) return mModelList.size();

        return 0;
    }

    public void refresh(List<MusicianDetailModel.TransactionRecord> list) {
        this.mModelList = list;
        notifyDataSetChanged();
    }

    static class MusicianHolder extends RecyclerView.ViewHolder {

        private final TextView mTvAmount;
        private final TextView mTvTime;
        private final View tvBottomLine, tvTopLine;

        public MusicianHolder(View itemView) {
            super(itemView);
            mTvAmount = itemView.findViewById(R.id.tv_amount);
            mTvTime = itemView.findViewById(R.id.tv_time);
            tvBottomLine = itemView.findViewById(R.id.tvBottomLine);
            tvTopLine = itemView.findViewById(R.id.tvTopLine);
        }
    }
}
