package com.bunny.groovy.ui.fragment.notify;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.api.ApiService;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.ui.fragment.apply.ConfirmInviteFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****************************************
 * 功能说明:  查看邀请详情界面
 *
 * Author: Created by bayin on 2018/1/4.
 ****************************************/

public class InviteDetailsFragment extends BaseFragment {

    public static String KEY_SHOW_BEAN = "key_show_bean";
    private static ShowModel sModel;

    public static void launch(Activity activity, Bundle bundle) {
        sModel = bundle.getParcelable(KEY_SHOW_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "DETAILS");
        FragmentContainerActivity.launch(activity, InviteDetailsFragment.class, bundle);
    }

    @Bind(R.id.invite_tv_date)
    TextView tvDate;
    @Bind(R.id.invite_tv_venue_name)
    TextView tvVenueName1;
    @Bind(R.id.invite_tv_notify)
    TextView mTvNotify;
    @Bind(R.id.invite_ll_action)
    LinearLayout llAction;

    @Bind(R.id.include_detail_tv_venueName)
    TextView mTvVenueName_2;

    @Bind(R.id.include_detail_tv_venueStar)
    TextView mTvVenueScore;

    @Bind(R.id.include_detail_tv_venueAddress)
    TextView mTvAddress;

    @Bind(R.id.include_detail_tv_tel)
    TextView mTvTel;

    @Bind(R.id.include_detail_tv_email)
    TextView mTvEmail;

    @Bind(R.id.include_detail_iv_head)
    ImageView mHead;

    @Bind(R.id.include_detail_tv_21plus)
    TextView tv21Plus;
    @Bind(R.id.include_detail_tv_Alcohol)
    TextView tvAlcohol;
    @Bind(R.id.include_detail_tv_food)
    TextView tvFood;
    @Bind(R.id.include_detail_tv_Cover_Charge)
    TextView tvCoverCharge;

    @OnClick({R.id.invite_iv_phone, R.id.include_detail_tv_tel})
    public void call() {
        Utils.CallPhone(mActivity, sModel.getPhoneNumber());
    }

    @OnClick(R.id.facebook_page)
    public void facebook() {
        Utils.openFacebook(mActivity, sModel.getFacebookAccount());
    }

    @OnClick(R.id.twitter_page)
    public void twitter() {
        Utils.openTwitter(mActivity, sModel.getTwitterAccount());
    }

    @OnClick(R.id.invite_iv_email)
    public void email() {
        if (sModel!=null)
        Utils.sendEmail(mActivity, sModel.getVenueEmail());
    }

    @OnClick(R.id.invite_tv_reject)
    public void reject() {//拒绝
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.rejectPerformInvite(sModel.getInviteID())
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
                            UIUtils.showBaseToast("Success!");
                        } else {
                            UIUtils.showBaseToast("Failed!");
                        }
                    }
                });
    }

    @OnClick(R.id.invite_tv_confirm)
    public void confirm() {//同意
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConfirmInviteFragment.KEY_VENUE_BEAN, sModel);
        ConfirmInviteFragment.launch(mActivity, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        if (sModel != null) {
            tvDate.setText(sModel.getPerformDate());
            tvVenueName1.setText(sModel.getVenueName());
            mTvVenueName_2.setText(sModel.getVenueName());
            mTvVenueScore.setText(Utils.getStar(sModel.getVenueScore()));
            mTvAddress.setText(sModel.getVenueAddress());
            mTvTel.setText(sModel.getPhoneNumber());
            mTvEmail.setText(sModel.getWebSiteAddress());
            Glide.with(mActivity).load(sModel.getHeadImg()).placeholder(R.drawable.venue_instead_pic)
                    .error(R.drawable.venue_instead_pic)
                    .into(mHead);
            mTvNotify.setVisibility(View.VISIBLE);
            llAction.setVisibility(View.GONE);
            String invitationState = sModel.getInvitationState();
            if ("1".equals(invitationState)) {
                mTvNotify.setText(R.string.confirmed);
            } else if ("2".equals(invitationState)) {
                mTvNotify.setText(R.string.rejected);
            } else {
                llAction.setVisibility(View.VISIBLE);
                mTvNotify.setVisibility(View.GONE);
            }

            //设置演出厅提供服务
            String venueTypeName = sModel.getVenueTypeName();
            if (!TextUtils.isEmpty(venueTypeName)) {
                tvFood.setEnabled(venueTypeName.contains("Food"));
                tvAlcohol.setEnabled(venueTypeName.contains("Alcohol"));
            } else {
                tvFood.setEnabled(false);
                tvAlcohol.setEnabled(false);
            }
            tv21Plus.setEnabled(Utils.is21Enabled(sModel.getVenueID(), sModel.getVenueTypeName(), sModel.getPerformDesc()));
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_invite_detial_layout;
    }

    @Override
    protected void loadData() {

    }
}
