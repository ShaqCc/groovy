package com.bunny.groovy.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.OpportunityModel;
import com.bunny.groovy.ui.fragment.apply.ApplyOppFragment;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/26.
 ****************************************/

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.MyHolder> {

    private List<OpportunityModel.PerformerOpportunityBean> mList;
    private Activity mContext;

    public OpportunityAdapter(List<OpportunityModel.PerformerOpportunityBean> list) {
        mList = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_opp_detail_list_layout, null, false);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (mList.size()==1){
            holder.mTvTopLine.setVisibility(View.INVISIBLE);
            holder.mTvBottomLine.setVisibility(View.INVISIBLE);
        }else {
            if (position == 0){
                holder.mTvTopLine.setVisibility(View.INVISIBLE);
                holder.mTvBottomLine.setVisibility(View.VISIBLE);
            }else if (position==mList.size()-1){
                holder.mTvTopLine.setVisibility(View.VISIBLE);
                holder.mTvBottomLine.setVisibility(View.INVISIBLE);
            }else {
                holder.mTvTopLine.setVisibility(View.VISIBLE);
                holder.mTvBottomLine.setVisibility(View.VISIBLE);
            }
        }
        final OpportunityModel.PerformerOpportunityBean bean = mList.get(position);
        holder.mTvPerformTime.setText(bean.getPerformDate()+" "+bean.getPerformTime());
        holder.mTvMessage.setText(bean.getPerformDesc());

        holder.mTvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                venueID
//                performStartDate
//                performEndDate
//                opportunityID
                Bundle bundle = new Bundle();
                bundle.putString("venueID",bean.getVenueID());
                bundle.putString("performStartDate",bean.getStartDate());
                bundle.putString("performEndDate",bean.getEndDate());
                bundle.putString("opportunityID",bean.getOpportunityID());
                bundle.putString("performDate",bean.getPerformDate());
                bundle.putString("performTime",bean.getPerformTime());
                ApplyOppFragment.launch(mContext,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList!=null) return mList.size();
        return 0;
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        private final View mTvTopLine;
        private final View mTvDot;
        private final View mTvBottomLine;
        private final TextView mTvPerformTime;
        private final TextView mTvMessage;
        private final TextView mTvApply;

        public MyHolder(View itemView) {
            super(itemView);
            mTvTopLine = itemView.findViewById(R.id.tvTopLine);
            mTvDot = itemView.findViewById(R.id.tvDot);
            mTvBottomLine = itemView.findViewById(R.id.tvBottomLine);
            mTvPerformTime = itemView.findViewById(R.id.tvPerformTime);
            mTvMessage = itemView.findViewById(R.id.tvMessage);
            mTvApply = itemView.findViewById(R.id.tv_apply);
        }
    }
}
