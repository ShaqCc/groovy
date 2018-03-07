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
import com.bunny.groovy.model.VenueInViteModel;
import com.bunny.groovy.utils.MusicBox;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****************************************
 * 功能说明:  演出厅用户--查看邀请详情界面
 *
 ****************************************/

public class VenueInviteDetailsFragment extends BaseFragment {

    public static String KEY_SHOW_BEAN = "key_show_bean";
    private static VenueInViteModel sModel;

    public static void launch(Activity activity, Bundle bundle) {
        sModel = bundle.getParcelable(KEY_SHOW_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "DETAILS");
        FragmentContainerActivity.launch(activity, VenueInviteDetailsFragment.class, bundle);
    }

    @Bind(R.id.invite_tv_notify)
    TextView mTvNotify;
    @Bind(R.id.invite_ll_action)
    LinearLayout llAction;

    @Bind(R.id.performer_detail_tv_venueName)
    TextView mTvVenueName_2;

    @Bind(R.id.performer_detail_iv_head)
    ImageView mHead;

    @Bind(R.id.performer_center_tv_style)
    TextView mTvDesDetail;
    @Bind(R.id.performer_center_tv_score)
    TextView mTvStars;


    @Bind(R.id.invite_layout)
    LinearLayout mInviteLayout;
    @Bind(R.id.apply_des)
    TextView mApplyDes;


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
        apiService.refusePerformApply(sModel.getPerformerID())
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
                            UIUtils.showBaseToast("拒绝成功！");
                        } else {
                            UIUtils.showBaseToast("拒绝失败！请重试");
                        }
                    }
                });
    }

    @OnClick(R.id.invite_tv_confirm)
    public void confirm() {//同意
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.agreePerformApply(sModel.getPerformerID())
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
                            UIUtils.showBaseToast("同意申请！");
                        } else {
                            UIUtils.showBaseToast("同意失败！请重试");
                        }
                    }
                });
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mInviteLayout.setVisibility(View.GONE);
        if (sModel != null) {
            StringBuilder stringBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(sModel.getUserName())) {
                stringBuilder.append(sModel.getUserName());
            }
            if (!TextUtils.isEmpty(sModel.getPerformDate())) {
                stringBuilder.append(" on ");
                stringBuilder.append(sModel.getPerformDate());
            }
            if (!TextUtils.isEmpty(sModel.getPerformTime())) {
                stringBuilder.append(" ");
                stringBuilder.append(sModel.getPerformTime());
            }
            mApplyDes.setText(stringBuilder.toString());
            mTvDesDetail.setText(sModel.getInvitationDesc());
            mTvVenueName_2.setText(sModel.getUserName());
            mTvStars.setText(sModel.getStarLevel());
            Glide.with(mActivity).load(sModel.getHeadImg()).placeholder(R.mipmap.venue_instead_pic).error(R.mipmap.venue_instead_pic)
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
        if (!TextUtils.isEmpty(sModel.getMusicFile())) {
            MusicBox.getInstance().playOnLineMusic(sModel.getMusicFile(), mActivity);
        } else {
            UIUtils.showBaseToast("Play failed");
        }
    }

    @OnClick(R.id.item_notification_tv_details)
    public void performDetail() {
        // TODO: 2018/3/4 0004 跳转表演者详情页
    }

}
