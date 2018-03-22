package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.MusicianScheduleAdapter;
import com.bunny.groovy.adapter.PerformDetailListAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.MusicianDetailModel;
import com.bunny.groovy.model.PerformDetail;
import com.bunny.groovy.presenter.UserListPresenter;
import com.bunny.groovy.service.MusicService;
import com.bunny.groovy.ui.fragment.apply.MusicianDetailFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 演出厅用户端----表演详情 日程表详情
 * <p>
 * Created by Administrator on 2017/12/25.
 */

public class UserShowDetailFragment extends BaseFragment {

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
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.show_detail_go)
    TextView mGoView;
    @Bind(R.id.detail_next)
    TextView mNextView;
    private PerformDetailListAdapter mAdapter;
    private MusicianScheduleAdapter mMusicianAdapter;

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

    @OnClick(R.id.show_detail_go)
    public void go() {
        Utils.openWebGoogleNavi(getActivity(), model.getVenueLatitude(), model.getVenueLongitude());
        UserListPresenter.addPerformViewer(model.getPerformID());
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

    private static PerformDetail model;
    private static boolean isHistory;
    public static String KEY_SHOW_BEAN = "key_show_bean";

    public static void launch(Context from, PerformDetail performDetail, boolean history) {
        model = performDetail;
        isHistory = history;
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SHOW DETAILS");
        FragmentContainerActivity.launch(from, UserShowDetailFragment.class, bundle);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_user_show_detial_layout;
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
            mTvDistance.setText((TextUtils.isEmpty(model.getDistance()) ? "--" : model.getDistance()) + "mi");
            mTvDesc.setText(model.getPerformDesc());
            mTvVenueScore.setText(Utils.getStar(model.getVenueScore()));
            mTvAddress.setText(model.getVenueAddress());
            mTvTel.setText(model.getVenueBookingPhone());
            mTvEmail.setText(model.getVenueWebSite());
            Glide.with(mActivity).load(model.getVenueImg())
                    .error(R.drawable.venue_instead_pic)
                    .into(mHead);
            mTvPerformerName2.setText(model.getPerformerName());
            mTvPerformerType.setText(model.getPerformerSignature());
            mTvPerformerStars.setText(Utils.getStar(model.getPerformerScore()));
            Glide.with(mActivity).load(model.getPerformerImg()).error(R.drawable.head)
                    .into(mPerformerHead);
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
            if (!TextUtils.isEmpty(model.getPerformerMusic())) initMusicService();

            if (isHistory) {
                mGoView.setVisibility(View.GONE);
                mNextView.setText("MY EVALUATION");
                if (!TextUtils.isEmpty(model.getEvaluateContent())) {
                    if (mMusicianAdapter == null) {
                        MusicianDetailModel.PerformViewer performViewer = new MusicianDetailModel.PerformViewer();
                        performViewer.evaluateContent = model.getEvaluateContent();
                        performViewer.evaluateDate = model.getEvaluateDate();
                        performViewer.performerStarLevel = model.getPerformerStarLevel();
                        performViewer.userName = model.getPerformerName();
                        List<MusicianDetailModel.PerformViewer> list = new ArrayList<>();
                        list.add(performViewer);
                        mMusicianAdapter = new MusicianScheduleAdapter(list);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                        mRecyclerView.setAdapter(mMusicianAdapter);
                    } else {
                        mAdapter.refresh(model.getPerformList());
                    }
                }
            } else {
                mNextView.setVisibility(View.GONE);
                if (mAdapter == null) {
                    mAdapter = new PerformDetailListAdapter(model.getPerformList());
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.refresh(model.getPerformList());
                }
            }

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
