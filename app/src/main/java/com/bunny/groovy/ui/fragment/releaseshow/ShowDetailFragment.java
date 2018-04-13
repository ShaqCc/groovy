package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 表演详情 日程表详情
 * <p>
 * Created by Administrator on 2017/12/25.
 */

public class ShowDetailFragment extends BaseFragment {

    @Bind(R.id.show_detail_tv_date)
    TextView mTvDate;

    @Bind(R.id.show_detail_tv_performer_name)
    TextView mTvPerformerName;

    @Bind(R.id.show_detail_tv_venue_name)
    TextView mTvVenueName_1;

    @Bind(R.id.show_detail_tv_style)
    TextView mTvStyle;

    @Bind(R.id.show_detail_tv_time)
    TextView mTvTime;

    @Bind(R.id.show_detail_tv_distance)
    TextView mTvDistance;

    @Bind(R.id.show_detail_tv_desc)
    TextView mTvDesc;

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

    @Bind(R.id.show_detail_tv_notify)
    TextView mTvNotify;

    @Bind(R.id.include_detail_tv_21plus)
    TextView tv21Plus;
    @Bind(R.id.include_detail_tv_Alcohol)
    TextView tvAlcohol;
    @Bind(R.id.include_detail_tv_food)
    TextView tvFood;
    @Bind(R.id.include_detail_tv_Cover_Charge)
    TextView tvCoverCharge;

    private static int type;

    @OnClick({R.id.show_detail_iv_phone, R.id.include_detail_tv_tel})
    public void call() {
        Utils.CallPhone(mActivity, model.getPhoneNumber());
    }

    @OnClick(R.id.show_detail_iv_email)
    public void email() {
        if (model!=null)
        Utils.sendEmail(mActivity, model.getVenueEmail());
    }

    @OnClick(R.id.facebook_page)
    public void facebook() {
        Utils.openFacebook(mActivity, model.getFacebookAccount());
    }

    @OnClick(R.id.twitter_page)
    public void twitter() {
        Utils.openTwitter(mActivity, model.getTwitterAccount());
    }

    private static ShowModel model;
    public static String KEY_SHOW_BEAN = "key_show_bean";

    public static void launch(Activity from, Bundle bundle) {
        model = bundle.getParcelable(KEY_SHOW_BEAN);
        if(model == null || TextUtils.isEmpty(model.getPerformID())) return;
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "DETAILS");
        type = bundle.getInt("type", -1);
        FragmentContainerActivity.launch(from, ShowDetailFragment.class, bundle);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_show_detial_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        if (model != null) {
            mTvDate.setText(model.getPerformDate());
            mTvPerformerName.setText(model.getPerformerName());
            mTvVenueName_1.setText(model.getVenueName());
            mTvVenueName_2.setText(model.getVenueName());
            mTvStyle.setText(model.getPerformType());
            mTvTime.setText(model.getPerformTime());
            mTvDistance.setText(model.getDistance() + "mi");
            mTvDesc.setText(model.getPerformDesc());
            mTvVenueScore.setText(Utils.getStar(model.getVenueScore()));
            mTvAddress.setText(model.getVenueAddress());
            mTvTel.setText(model.getPhoneNumber());
            mTvEmail.setText(model.getWebSiteAddress());
            Glide.with(mActivity).load(model.getHeadImg()).placeholder(R.drawable.venue_instead_pic).error(R.drawable.venue_instead_pic)
                    .into(mHead);
            switch (type) {
                case 0://演出机会
                    mTvNotify.setVisibility(View.VISIBLE);
                    String applyState = model.getApplyState();
                    if ("1".equals(applyState)) {
                        mTvNotify.setText(R.string.confirmed);
                    } else if ("2".equals(applyState)) {
                        mTvNotify.setText(R.string.rejected);
                    } else {
                        mTvNotify.setText(R.string.verification);
                    }
                    break;
                case 1://邀请
                    mTvNotify.setVisibility(View.VISIBLE);
                    String invitationState = model.getInvitationState();
                    if ("1".equals(invitationState)) {
                        mTvNotify.setText(R.string.confirmed);
                    } else if ("2".equals(invitationState)) {
                        mTvNotify.setText(R.string.rejected);
                    } else {
                        mTvNotify.setText(R.string.verification);
                    }
                    break;
                case 2://release
                    mTvNotify.setVisibility(View.VISIBLE);
                    String performState = model.getPerformState();
                    if ("1".equals(performState)) {
                        mTvNotify.setText(R.string.confirmed);
                    } else if ("2".equals(performState)) {
                        mTvNotify.setText(R.string.rejected);
                    } else {
                        mTvNotify.setText(R.string.verification);
                    }
                    break;
                default:
                    mTvNotify.setVisibility(View.GONE);
                    break;
            }

            //设置演出厅提供服务
            String venueTypeName = model.getVenueTypeName();
            if (!TextUtils.isEmpty(venueTypeName)) {
                tvFood.setEnabled(venueTypeName.contains("Food"));
                tvAlcohol.setEnabled(venueTypeName.contains("Alcohol"));
            } else {
                tvFood.setEnabled(false);
                tvAlcohol.setEnabled(false);
            }
            tv21Plus.setEnabled(Utils.is21Enabled(model.getVenueID(), model.getVenueTypeName(), model.getPerformDesc()));
            if (!TextUtils.isEmpty(model.getIsHaveCharges()) && model.getIsHaveCharges().equals("1")) {
                tvCoverCharge.setEnabled(true);
            } else {
                tvCoverCharge.setEnabled(false);
            }
        }
    }

    @Override
    protected void loadData() {

    }
}
