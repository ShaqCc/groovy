package com.bunny.groovy.ui.fragment.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.bunny.groovy.R;
import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.presenter.SettingsPresenter;
import com.bunny.groovy.ui.RoleChooseActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.SharedPreferencesUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.ISettingView;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/22.
 ****************************************/

public class SettingsFragment extends BaseFragment<SettingsPresenter> implements ISettingView {

    @Bind(R.id.settings_sb_discover)
    com.suke.widget.SwitchButton sbDiscover;
    @Bind(R.id.sb_notify)
    com.suke.widget.SwitchButton sbNotify;
    @Bind(R.id.notification_layout)
    RelativeLayout mNotificationLayout;
    @Bind(R.id.discover_layout)
    RelativeLayout mDiscoverLayout;

    @OnClick(R.id.settings_tv_logout)
    public void logout() {
        mPresenter.logout();
    }

    @OnClick(R.id.settings_tv_feedback)
    public void feedback() {
        FeedbackFragment.launch(mActivity);
    }

    @OnClick(R.id.settings_tv_about)
    public void about() {
        AboutusFragment.launch(mActivity);
    }

    @OnClick(R.id.settings_tv_safe)
    public void safe() {
        SafeFragment.launch(mActivity);
    }

    @OnClick(R.id.settings_sb_discover)
    public void discover() {
    }

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SETTINGS");
        FragmentContainerActivity.launch(from, SettingsFragment.class, bundle);
    }

    @Override
    protected SettingsPresenter createPresenter() {
        return new SettingsPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_settings_layout;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        boolean isDiscover = (boolean) SharedPreferencesUtils.getUserParam(mActivity, AppConstants.KEY_DISCOVER, true);
        sbDiscover.setChecked(isDiscover);
        sbNotify.setChecked(true);
        if(AppConstants.USER_TYPE_NORMAL == Utils.parseInt(AppCacheData.getPerformerUserModel().getUserType())){
            mDiscoverLayout.setVisibility(View.GONE);
            mNotificationLayout.setVisibility(View.GONE);
        }
        sbDiscover.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    ApiRetrofit.getInstance().getApiService()
                            .updateDiscover("1")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ResultResponse<Object>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(ResultResponse<Object> response) {
                                    if (response.isSuccess()) {
                                        UIUtils.showBaseToast("Success.");
                                        SharedPreferencesUtils.setUserParam(mActivity, AppConstants.KEY_DISCOVER, true);
                                    } else {
                                        UIUtils.showBaseToast("Failure.");
                                        SharedPreferencesUtils.setUserParam(mActivity, AppConstants.KEY_DISCOVER, false);
                                    }
                                }
                            });
                }else {
                    ApiRetrofit.getInstance().getApiService()
                            .updateDiscover("0")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ResultResponse<Object>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(ResultResponse<Object> response) {
                                    if (response.isSuccess()) {
                                        UIUtils.showBaseToast("Success.");
                                        SharedPreferencesUtils.setUserParam(mActivity, AppConstants.KEY_DISCOVER, false);
                                    } else {
                                        UIUtils.showBaseToast("Failure.");
                                        SharedPreferencesUtils.setUserParam(mActivity, AppConstants.KEY_DISCOVER, true);
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void loginOut() {
        Utils.clearLoginData(get());
        //跳转到角色选择页面
        RoleChooseActivity.launch(getActivity());
//        //退出setting页面
        getActivity().finish();
//        //退出MainActivity
        EventBus.getDefault().post(AppConstants.EVENT_LOGIN_OUT);
    }

    @Override
    public Activity get() {
        return getActivity();
    }
}
