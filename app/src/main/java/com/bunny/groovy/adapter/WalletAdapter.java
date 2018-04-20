package com.bunny.groovy.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.WalletBean;

import java.util.List;

/**
 * Created by bayin on 2018/1/15.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletHolder>{

    List<WalletBean> listData;

    public WalletAdapter(List<WalletBean> list) {
        this.listData = list;
    }

    @Override
    public WalletHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_layout, null, false);
        return new WalletHolder(inflate);
    }

    @Override
    public void onBindViewHolder(WalletHolder holder, int position) {
        WalletBean walletBean = listData.get(position);
        String dealType = walletBean.getDealType();
        switch (dealType){
            // 交易类型（0-提现 1-充值 3-打赏支出 4-打赏收入 5-推广包购买支出）
            case "0":
                holder.tvType.setText("WITHDRAW");
                holder.tvAmount.setText("-"+walletBean.getCost());
                break;
            case "1":
                holder.tvType.setText("RECHARGE");
                holder.tvAmount.setText("+"+walletBean.getCost());
                break;
            case "3":
                holder.tvType.setText("REWARD@"+walletBean.getUserName());
                holder.tvAmount.setText("-"+walletBean.getCost());
                break;
            case "4":
                holder.tvType.setText("RECEIVE REWARD");
                holder.tvAmount.setText("+"+walletBean.getCost());
                break;
            case "5":
                holder.tvType.setText("SPOTLIGHT");
                holder.tvAmount.setText("-"+walletBean.getCost());
                break;
        }

        holder.tvTime.setText(!TextUtils.isEmpty(walletBean.getDealDate()) ? walletBean.getDealDate() : walletBean.getCreateDate());
    }

    public void refresh(List<WalletBean> list){
        this.listData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listData!=null) return listData.size();
        return 0;
    }

    static class WalletHolder extends RecyclerView.ViewHolder{

        private final TextView tvType;
        private final TextView tvTime;
        private final TextView tvAmount;

        public WalletHolder(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.item_wallet_tv_amount);
            tvTime = itemView.findViewById(R.id.item_wallet_tv_time);
            tvType = itemView.findViewById(R.id.item_wallet_tv_type);
        }
    }
}
