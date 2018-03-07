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
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.utils.MusicBox;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 演出厅用户端----表演详情 日程表详情
 * <p>
 * Created by Administrator on 2017/12/25.
 */

public class VenueShowDetailFragment extends BaseFragment {

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

    @Bind(R.id.performer_detail_tv_venueName)
    TextView mTvPerformerName2;
    @Bind(R.id.performer_detail_iv_head)
    ImageView mPerformerHead;
    @Bind(R.id.performer_center_tv_style)
    TextView mTvPerformerType;
    @Bind(R.id.performer_center_tv_score)
    TextView mTvPerformerStars;

    @OnClick(R.id.include_detail_tv_tel)
    public void call() {
        Utils.CallPhone(mActivity, model.getVenueBookingPhone());
    }

    @OnClick(R.id.facebook_page)
    public void facebook() {
        if (!TextUtils.isEmpty(model.getVenueFacebook()))
            Utils.openFacebook(mActivity, model.getVenueFacebook());
    }

    @OnClick(R.id.twitter_page)
    public void twitter() {
        if (!TextUtils.isEmpty(model.getVenueTwitter()))
            Utils.openFacebook(mActivity, model.getVenueTwitter());
    }

    @OnClick(R.id.performer_facebook_page)
    public void performerFacebook() {
        if (!TextUtils.isEmpty(model.getPerformerFacebook()))
            Utils.openFacebook(mActivity, model.getPerformerFacebook());
    }

    @OnClick(R.id.performer_twitter_page)
    public void performerTwitter() {
        if (!TextUtils.isEmpty(model.getPerformerTwitter()))
            Utils.openTwitter(mActivity, model.getPerformerTwitter());
    }

    @OnClick(R.id.performer_me_tv_cloud)
    public void performerCloud() {
        if (!TextUtils.isEmpty(model.getPerformerSoundcloud()))
            Utils.openSoundCloud(mActivity, model.getPerformerSoundcloud());
    }

    private static VenueShowModel model;
    public static String KEY_SHOW_BEAN = "key_show_bean";

    public static void launch(Activity from, Bundle bundle) {
        model = bundle.getParcelable(KEY_SHOW_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "DETAILS");
        FragmentContainerActivity.launch(from, VenueShowDetailFragment.class, bundle);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_venue_show_detial_layout;
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
            mTvDesc.setText(model.getPerformDesc());
            mTvVenueScore.setText(model.getVenueScore());
            mTvAddress.setText(model.getVenueAddress());
            mTvTel.setText(model.getVenueBookingPhone());
            mTvEmail.setText(model.getVenueWebSite());
            Glide.with(mActivity).load(model.getVenueImg()).placeholder(R.mipmap.venue_instead_pic).error(R.mipmap.venue_instead_pic)
                    .into(mHead);
            mTvPerformerName2.setText(model.getPerformerName());
            mTvPerformerType.setText(model.getPerformerSignature());
            String performScore = model.getPerformerScore();
            if(performScore.contains(".")){
                int index = model.getPerformerScore().indexOf(".");
                performScore = performScore.substring(0,index+2);
            }
            mTvPerformerStars.setText(performScore);
            Glide.with(mActivity).load(model.getPerformerImg()).placeholder(R.drawable.head).error(R.drawable.head)
                    .into(mPerformerHead);
             /*mTvNotify.setVisibility(View.VISIBLE);
            String applyState = model.getPerformState();
            if ("1".equals(applyState)) {
                mTvNotify.setText(R.string.confirmed);
            } else if ("2".equals(applyState)) {
                mTvNotify.setText(R.string.rejected);
            } else {
                mTvNotify.setText(R.string.verification);
            }*/

            //设置演出厅提供服务
            String venueTypeName = model.getVenueTypeName();
            if (!TextUtils.isEmpty(venueTypeName)) {
                tv21Plus.setEnabled(!venueTypeName.contains("21"));
                tvFood.setEnabled(venueTypeName.contains("Food"));
                tvAlcohol.setEnabled(venueTypeName.contains("Alcohol"));
            } else {
                tv21Plus.setEnabled(true);
                tvFood.setEnabled(false);
                tvAlcohol.setEnabled(false);
            }
        }
    }

    @OnClick(R.id.performer_me_play_music)
    public void playMusic() {
        if (!TextUtils.isEmpty(model.getPerformerMusic())) {
            MusicBox.getInstance().playOnLineMusic(model.getPerformerMusic(), mActivity);
        } else {
            UIUtils.showBaseToast("Play failed");
        }
    }

    @Override
    protected void loadData() {

    }
}
