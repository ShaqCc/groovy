package com.bunny.groovy.ui.fragment.notify;

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
import com.bunny.groovy.model.VenueApplyModel;
import com.bunny.groovy.service.MusicService;
import com.bunny.groovy.ui.fragment.apply.MusicianDetailFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****************************************
 * 功能说明:  演出厅用户--查看申请详情界面
 *
 ****************************************/

public class VenueApplyDetailsFragment extends BaseFragment {

    public static String KEY_SHOW_BEAN = "key_show_bean";
    private static VenueApplyModel sModel;

    public static void launch(Activity activity, Bundle bundle) {
        sModel = bundle.getParcelable(KEY_SHOW_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "DETAILS");
        FragmentContainerActivity.launch(activity, VenueApplyDetailsFragment.class, bundle);
    }

    @Bind(R.id.show_detail_tv_date)
    TextView mTvDate;
    @Bind(R.id.invite_tv_date)
    TextView tvName;
    @Bind(R.id.invite_tv_venue_name)
    TextView tvVenueName1;
    @Bind(R.id.invite_tv_notify)
    TextView mTvNotify;
    @Bind(R.id.invite_ll_action)
    LinearLayout llAction;

    @Bind(R.id.performer_detail_tv_venueName)
    TextView mTvVenueName_2;

    @Bind(R.id.performer_detail_iv_head)
    ImageView mHead;

    @Bind(R.id.show_detail_tv_style)
    TextView mTvStyle;

    @Bind(R.id.show_detail_tv_time)
    TextView mTvTime;

    @Bind(R.id.show_detail_tv_distance)
    TextView mTvDistance;

    @Bind(R.id.show_detail_tv_desc)
    TextView mTvDesc;
    @Bind(R.id.performer_center_tv_style)
    TextView mTvDesDetail;
    @Bind(R.id.performer_center_tv_score)
    TextView mTvStars;

    @Bind(R.id.apply_layout)
    LinearLayout mApplyLayout;

    @Bind(R.id.performer_me_play_music)
    ImageView mMusicView;


    @OnClick(R.id.performer_facebook_page)
    public void facebook() {
        if (!TextUtils.isEmpty(sModel.getFacebookAccount()))
            Utils.openFacebook(mActivity, sModel.getFacebookAccount());
    }

    @OnClick(R.id.performer_twitter_page)
    public void twitter() {
        if (!TextUtils.isEmpty(sModel.getTwitterAccount()))
            Utils.openTwitter(mActivity, sModel.getTwitterAccount());
    }

    @OnClick(R.id.performer_me_tv_cloud)
    public void cloud() {
        if (!TextUtils.isEmpty(sModel.getSoundcloudAccount()))
            Utils.openSoundCloud(mActivity, sModel.getSoundcloudAccount());
    }

    @OnClick(R.id.invite_tv_reject)
    public void reject() {//拒绝
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.refusePerformApply(sModel.getPerformID())
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
                        UIUtils.showBaseToast("Reject failed.");
                        KLog.d(e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                        if (response.success) {
                            UIUtils.showBaseToast("Reject successfully.");
                        }
                    }
                });
    }

    @OnClick(R.id.invite_tv_confirm)
    public void confirm() {//同意
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.agreePerformApply(sModel.getPerformID())
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
                        UIUtils.showBaseToast("Apply failed.");
                        KLog.d(e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                        if (response.success) {
                            UIUtils.showBaseToast("Apply successfully");
                        }
                    }
                });
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mApplyLayout.setVisibility(View.GONE);
        if (sModel != null) {
            mTvDate.setText(sModel.getPerformDate());
            tvName.setText(sModel.getPerformerName());
            mTvStyle.setText(sModel.getPerformType());
            mTvTime.setText(sModel.getPerformType());
            mTvTime.setText(sModel.getPerformTime());
            if (!TextUtils.isEmpty(sModel.getDistance())) {
                mTvDistance.setText(sModel.getDistance() + "mi");
            }
            mTvDesDetail.setText(sModel.getPerformDesc());
            mTvDesc.setText(sModel.getPerformDesc());
            tvVenueName1.setText(sModel.getVenueName());
            mTvVenueName_2.setText(sModel.getPerformerName());
            mTvStars.setText(Utils.getStar(sModel.getStarLevel()));
            Glide.with(mActivity).load(sModel.getHeadImg()).placeholder(R.drawable.venue_instead_pic).error(R.drawable.venue_instead_pic)
                    .into(mHead);
            mTvNotify.setVisibility(View.VISIBLE);
            llAction.setVisibility(View.GONE);
            String invitationState = sModel.getPerformState();
            if ("1".equals(invitationState)) {
                mTvNotify.setText(R.string.confirmed);
            } else if ("2".equals(invitationState)) {
                mTvNotify.setText(R.string.rejected);
            } else {
                llAction.setVisibility(View.VISIBLE);
                mTvNotify.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(sModel.getMusicFile())) initMusicService();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_show_perform_detial_layout;
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.performer_me_play_music)
    public void playMusic() {
        if (TextUtils.isEmpty(sModel.getMusicFile())) {
            UIUtils.showBaseToast("Not found music.");
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
        intent.putExtra("music_path", sModel.getMusicFile());
        getActivity().startService(intent);
        getActivity().bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.item_notification_tv_details)
    public void performDetail() {
        MusicianDetailFragment.launch(mActivity, sModel.getPerformerID());
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
