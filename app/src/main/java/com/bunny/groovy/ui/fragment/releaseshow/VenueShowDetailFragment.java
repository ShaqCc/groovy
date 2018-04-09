package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.service.MusicService;
import com.bunny.groovy.ui.fragment.apply.MusicianDetailFragment;
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
    @Bind(R.id.performer_me_play_music)
    ImageView mMusicView;

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
            Utils.openTwitter(mActivity, model.getVenueTwitter());
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
            mTvVenueScore.setText(Utils.getStar(model.getVenueScore()));
            mTvAddress.setText(model.getVenueAddress());
            mTvTel.setText(model.getVenueBookingPhone());
            mTvEmail.setText(model.getVenueWebSite());
            Glide.with(mActivity).load(model.getVenueImg()).placeholder(R.drawable.venue_instead_pic)
                    .error(R.drawable.venue_instead_pic)
                    .into(mHead);
            mTvPerformerName2.setText(model.getPerformerName());
            mTvPerformerType.setText(model.getPerformerSignature());
            mTvPerformerStars.setText(Utils.getStar(model.getPerformerScore()));
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
            if(model.getPerformDesc() != null && model.getPerformDesc().contains("18+")){
                tv21Plus.setEnabled(false);
            }
            if (!TextUtils.isEmpty(model.getPerformerMusic())) initMusicService();
        }
    }

    @OnClick(R.id.performer_me_play_music)
    public void playMusic() {
        if (TextUtils.isEmpty(model.getPerformerMusic())) {
            UIUtils.showBaseToast("No music.");
            return;
        }
        handleMusic();
    }

    /**
     * 控制音乐播放
     */
    private void handleMusic() {
        if (callBack != null) {
            boolean isPlay = callBack.isPlayerMusic();
            mMusicView.setImageResource(isPlay ? R.drawable.login_stop : R.drawable.login_play);
        }
    }

    private MusicService.CallBack callBack;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            callBack = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            callBack = null;
        }
    };

    private void initMusicService() {
        /** 构造启动音乐播放服务的Intent，设置音乐资源 */
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.putExtra("music_path", model.getPerformerMusic());
        getActivity().startService(intent);
        getActivity().bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.item_notification_tv_details)
    public void detail() {
        MusicianDetailFragment.launch(mActivity, model.getPerformerID());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callBack != null && callBack.isPlaying()) {
            callBack.isPlayerMusic();
            mMusicView.setImageResource(R.drawable.login_play);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callBack != null && callBack.isPlaying()) {
            callBack.isPlayerMusic();
            mMusicView.setImageResource(R.drawable.login_play);
        }
    }
}
