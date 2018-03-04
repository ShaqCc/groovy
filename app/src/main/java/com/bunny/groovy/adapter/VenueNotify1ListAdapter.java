package com.bunny.groovy.adapter;

import android.app.Activity;
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
import com.bunny.groovy.model.VenueOpportunityModel;
import com.bunny.groovy.utils.UIUtils;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueNotify1ListAdapter extends RecyclerView.Adapter<VenueNotify1ListAdapter.NotifyHolder> implements View.OnClickListener {
    private List<VenueOpportunityModel> mList;
    private Activity mContext;
    private String Status_OK = "CONFIRMED";
    private String Status_NO = "REJECTED";

    private String Opp_confirm_perform = "has confirmed your application";
    private String Opp_reject_perform = "has rejected your application";

    public VenueNotify1ListAdapter(List<VenueOpportunityModel> list) {
        mList = list;
    }

    public void refresh(List<VenueOpportunityModel> list) {
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
        VenueOpportunityModel showModel = mList.get(position);
        holder.llActionLayout.setVisibility(View.GONE);
        String applyState = showModel.getOpportunityState();
        if (!TextUtils.isEmpty(applyState))
            switch (applyState) {
                case "1"://同意
                    holder.tvMsg.setText("has confirmed your invitation");
                    break;
                case "2":
                    holder.tvMsg.setText("has rejected your invitation");
                    break;
                case "0"://未处理
                default:
                    holder.tvMsg.setText("verification");
                    break;
            }
        List<VenueOpportunityModel.ApplyList> list = showModel.getApplyList();
        if(list.size() > 0){
            VenueOpportunityModel.ApplyList applyList = list.get(0);
            Glide.with(mContext).load(applyList.getHeadImg()).error(R.mipmap.venue_instead_pic).into(holder.ivHeader);
            holder.tvName.setText(applyList.getStageName());
            holder.tvScore.setText(applyList.getStarLevel());
            holder.tvPerformType.setText(applyList.getPerformType());
        }
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
        VenueOpportunityModel showModel = mList.get(pos);
        switch (v.getId()) {
            /*case R.id.item_notification_tv_details://详情
                Bundle bundle = new Bundle();
                bundle.putParcelable(ShowDetailFragment.KEY_SHOW_BEAN, showModel);
                if (mTYPE == 1) {
                    InviteDetailsFragment.launch(mContext, bundle);
                } else {
                    bundle.putInt("type", mTYPE);
                    ShowDetailFragment.launch(mContext, bundle);
                }

                break;
            case R.id.item_notification_tv_confirm://同意
                Bundle arg = new Bundle();
                arg.putParcelable(ConfirmInviteFragment.KEY_VENUE_BEAN, showModel);
                ConfirmInviteFragment.launch(mContext, arg);
                break;
            case R.id.item_notification_tv_reject://拒绝
                rejectInvite(showModel.getInviteID(), pos);
                break;*/
        }
    }

    /**
     * 拒绝邀请
     *
     * @param inviteID
     */
    private void rejectInvite(String inviteID, final int position) {
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.rejectPerformInvite(inviteID)
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
                            UIUtils.showBaseToast("确认成功！");
                            mList.get(position).setOpportunityState("2");
                            notifyItemChanged(position);
                        } else {
                            UIUtils.showBaseToast("确认失败！请重试");
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
