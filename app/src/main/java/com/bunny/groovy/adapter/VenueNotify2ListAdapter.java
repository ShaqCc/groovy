package com.bunny.groovy.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.api.ApiService;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueApplyModel;
import com.bunny.groovy.ui.fragment.notify.VenueApplyDetailsFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueNotify2ListAdapter extends RecyclerView.Adapter<VenueNotify2ListAdapter.NotifyHolder> implements View.OnClickListener {
    private List<VenueApplyModel> mList;
    private Activity mContext;
    private String Status_OK = "CONFIRMED";
    private String Status_NO = "REJECTED";

    private String Opp_confirm_perform = "has confirmed your application";
    private String Opp_reject_perform = "has rejected your application";

    public VenueNotify2ListAdapter(List<VenueApplyModel> list) {
        mList = list;
    }

    public void refresh(List<VenueApplyModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public NotifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_venue_notification_layout, null, false);

        return new NotifyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NotifyHolder holder, int position) {
        VenueApplyModel showModel = mList.get(position);
        holder.llActionLayout.setVisibility(View.GONE);
        String PerformState = showModel.getPerformState();//接口上说-演出状态（0-待验证1-已发布 2-已取消）
        if (!TextUtils.isEmpty(PerformState)) {
            holder.tvMsg.setVisibility(View.VISIBLE);
            holder.tvMsg.setText(R.string.invite_you_for_a_show);
            switch (PerformState) {
                case "1"://同意
                    holder.llActionLayout.setVisibility(View.GONE);
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvStatus.setText(R.string.confirmed);
                    break;
                case "2"://拒绝
                    holder.llActionLayout.setVisibility(View.GONE);
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvStatus.setText(R.string.rejected);
                    break;
                case "0"://未处理
                default:
                    holder.llActionLayout.setVisibility(View.VISIBLE);
                    holder.tvStatus.setVisibility(View.GONE);
                    holder.tvStatus.setText("");
                    break;
            }
        }

        Glide.with(mContext).load(showModel.getHeadImg()).error(R.drawable.venue_instead_pic).into(holder.ivHeader);
        holder.tvName.setText(showModel.getPerformerName());
        holder.tvScore.setText(Utils.getStar(showModel.getStarLevel()));
        holder.tvPerformType.setText(showModel.getPerformType());
        holder.tvCreateTime.setText(showModel.getCreateDate());
        holder.tvPerformDate.setText(showModel.getPerformDate() + " " + showModel.getPerformTime());
        //onclick
        holder.btDetails.setTag(position);
        holder.btConfirm.setTag(position);
        holder.btReject.setTag(position);

        holder.btDetails.setOnClickListener(this);
        holder.btConfirm.setOnClickListener(this);
        holder.btReject.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        VenueApplyModel showModel = mList.get(pos);
        switch (v.getId()) {
            case R.id.item_notification_tv_details://详情
                Bundle bundle = new Bundle();
                bundle.putParcelable("key_show_bean", showModel);
                VenueApplyDetailsFragment.launch(mContext, bundle);
                break;
            case R.id.item_notification_tv_confirm://同意
                agreeApply(showModel.getPerformID(), pos);
                break;
            case R.id.item_notification_tv_reject://拒绝
                rejectApply(showModel.getPerformID(), pos);
                break;
        }
    }

    /**
     * 拒绝申请
     *
     * @param performID
     */
    private void rejectApply(String performID, final int position) {
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.refusePerformApply(performID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultResponse<Object>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UIUtils.showBaseToast(e.toString());
                        KLog.d(e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                        if (response.success) {
                            UIUtils.showBaseToast("Success！");
                            mList.get(position).setPerformState("2");
                            notifyItemChanged(position);
                        } else {
                            UIUtils.showBaseToast("Failed");
                        }
                    }
                });
    }

    /**
     * 同意申请
     *
     * @param performID
     */
    private void agreeApply(String performID, final int position) {
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.agreePerformApply(performID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultResponse<Object>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UIUtils.showBaseToast(e.toString());
                        KLog.d(e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                        if (response.success) {
                            UIUtils.showBaseToast("Apply successfully.");
                            mList.get(position).setPerformState("1");
                            notifyItemChanged(position);
                        } else {
                            UIUtils.showBaseToast("Apply failed.");
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }


    static class NotifyHolder extends RecyclerView.ViewHolder {

        private final View btConfirm;
        private final View btReject;
        private final View llActionLayout;
        private final TextView tvPerformDate;
        private final View btDetails;
        private final TextView tvMsg;
        private final TextView tvStatus;
        private final TextView tvScore;
        private final TextView tvName;
        private final TextView tvPerformType;
        private final ImageView ivHeader;
        private final TextView tvCreateTime;

        public NotifyHolder(View itemView) {
            super(itemView);
            tvCreateTime = (TextView) findViewById(R.id.item_notification_tv_createTime);
            ivHeader = (ImageView) findViewById(R.id.item_notification_iv_header);
            tvName = (TextView) findViewById(R.id.item_notification_tv_name);
            tvScore = (TextView) findViewById(R.id.item_notification_tv_score);
            tvStatus = (TextView) findViewById(R.id.item_notification_tv_status);
            tvPerformType = (TextView) findViewById(R.id.item_favorite_tv_type);
            tvMsg = (TextView) findViewById(R.id.item_notification_tv_msg);
            btDetails = findViewById(R.id.item_notification_tv_details);
            tvPerformDate = (TextView) findViewById(R.id.item_notification_tv_perform_date);
            llActionLayout = findViewById(R.id.item_notification_ll_action);
            btReject = findViewById(R.id.item_notification_tv_reject);
            btConfirm = findViewById(R.id.item_notification_tv_confirm);
        }

        private View findViewById(int res) {
            return itemView.findViewById(res);
        }
    }
}
