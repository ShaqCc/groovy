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
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.ui.fragment.apply.ConfirmInviteFragment;
import com.bunny.groovy.ui.fragment.notify.InviteDetailsFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ShowDetailFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/27.
 */

public class NotifyListAdapter extends RecyclerView.Adapter<NotifyListAdapter.NotyfyHolder> implements View.OnClickListener {
    private List<ShowModel> mList;
    private int mTYPE;
    private Activity mContext;
    private String Status_OK = "CONFIRMED";
    private String Status_NO = "REJECTED";

    private String Opp_confirm_perform = "has confirmed your application";
    private String Opp_reject_perform = "has rejected your application";

    public NotifyListAdapter(List<ShowModel> list, int type) {
        mList = list;
        mTYPE = type;
    }

    public void refresh(List<ShowModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public NotyfyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_notification_layout, null, false);

        return new NotyfyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NotyfyHolder holder, int position) {
        ShowModel showModel = mList.get(position);
        switch (mTYPE) {
            case 0:
                holder.llActionLayout.setVisibility(View.GONE);
                String applyState = showModel.getApplyState();
                if (!TextUtils.isEmpty(applyState))
                    switch (applyState) {
                        case "1"://同意
                            holder.tvStatus.setText(R.string.confirmed);
                            holder.tvMsg.setVisibility(View.VISIBLE);
                            holder.tvMsg.setText(R.string.msg_confirm_application);
                            break;
                        case "2":
                            holder.tvStatus.setText(R.string.rejected);
                            holder.tvMsg.setVisibility(View.VISIBLE);
                            holder.tvMsg.setText(R.string.msg_reject_application);
                            break;
                        case "0"://未处理
                        default:
                            holder.tvStatus.setText(R.string.verification);
                            holder.tvMsg.setVisibility(View.GONE);
                            break;
                    }
                break;
            case 1:
                String invitationState = showModel.getInvitationState();
                if (!TextUtils.isEmpty(invitationState)) {
                    holder.tvMsg.setVisibility(View.VISIBLE);
                    holder.tvMsg.setText(R.string.invite_you_for_a_show);
                    switch (invitationState) {
                        case "1"://同意
                            holder.llActionLayout.setVisibility(View.GONE);
                            holder.tvStatus.setText(R.string.confirmed);
                            break;
                        case "2"://拒绝
                            holder.llActionLayout.setVisibility(View.GONE);
                            holder.tvStatus.setText(R.string.rejected);
                            break;
                        case "0"://未处理
                        default:
                            holder.llActionLayout.setVisibility(View.VISIBLE);
                            holder.tvStatus.setText("");
                            break;
                    }
                }
                break;
            case 2:
                holder.llActionLayout.setVisibility(View.GONE);
                String PerformState = showModel.getPerformState();
                if (!TextUtils.isEmpty(PerformState))
                    switch (PerformState) {
                        case "1"://同意
                            holder.tvStatus.setText(R.string.confirmed);
                            holder.tvMsg.setVisibility(View.VISIBLE);
                            holder.tvMsg.setText(R.string.msg_confirm_application);
                            break;
                        case "2":
                            holder.tvStatus.setText(R.string.rejected);
                            holder.tvMsg.setVisibility(View.VISIBLE);
                            holder.tvMsg.setText(R.string.msg_reject_application);
                            break;
                        case "0"://未处理
                        default:
                            holder.tvStatus.setText(R.string.verification);
                            holder.tvMsg.setVisibility(View.GONE);
                            break;
                    }
                break;
        }

        Glide.with(mContext).load(showModel.getHeadImg()).error(R.drawable.venue_instead_pic).into(holder.ivHeader);
        holder.tvName.setText(showModel.getVenueName());
        holder.tvScore.setText(showModel.getVenueScore());
        holder.tvCreateTime.setText(showModel.getCreateDate());
        holder.tvPerformDate.setText(showModel.getPerformDate() + " " + showModel.getPerformTime());
        //onclick
        holder.btDetails.setTag(position);
        holder.btConfirm.setTag(position);
        holder.btReject.setTag(position);
        holder.btEmail.setTag(position);
        holder.btPhone.setTag(position);

        holder.btDetails.setOnClickListener(this);
        holder.btConfirm.setOnClickListener(this);
        holder.btReject.setOnClickListener(this);
        holder.btEmail.setOnClickListener(this);
        holder.btPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        ShowModel showModel = mList.get(pos);
        switch (v.getId()) {
            case R.id.item_notification_tv_details://详情
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
                break;
            case R.id.item_notification_iv_email://发邮箱
                Utils.sendEmail(mContext, showModel.getVenueEmail());
                break;
            case R.id.item_notification_iv_phone://打电话
                Utils.CallPhone(mContext, showModel.getPhoneNumber());
                break;
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
                            mList.get(position).setInvitationState("2");
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


    static class NotyfyHolder extends RecyclerView.ViewHolder {

        private final View btConfirm;
        private final View btReject;
        private final View llActionLayout;
        private final TextView tvPerformDate;
        private final View btDetails;
        private final View btEmail;
        private final View btPhone;
        private final TextView tvMsg;
        private final TextView tvStatus;
        private final TextView tvScore;
        private final TextView tvName;
        private final ImageView ivHeader;
        private final TextView tvCreateTime;

        public NotyfyHolder(View itemView) {
            super(itemView);
            tvCreateTime = (TextView) findViewById(R.id.item_notification_tv_createTime);
            ivHeader = (ImageView) findViewById(R.id.item_notification_iv_header);
            tvName = (TextView) findViewById(R.id.item_notification_tv_name);
            tvScore = (TextView) findViewById(R.id.item_notification_tv_score);
            tvStatus = (TextView) findViewById(R.id.item_notification_tv_status);
            tvMsg = (TextView) findViewById(R.id.item_notification_tv_msg);
            btPhone = findViewById(R.id.item_notification_iv_phone);
            btEmail = findViewById(R.id.item_notification_iv_email);
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
