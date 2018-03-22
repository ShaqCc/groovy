package com.bunny.groovy.ui;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.presenter.SplashPresenter;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.SharedPreferencesUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.ISplashView;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/14.
 ****************************************/

public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashView {
    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.acitivty_splash_layout;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void initData() {
        super.initData();
        boolean isLogin = (boolean) SharedPreferencesUtils.getUserParam(this, AppConstants.KEY_LOGIN, false);
        String userID = (String) SharedPreferencesUtils.getUserParam(this, AppConstants.KEY_USERID, "");
        if (isLogin && !TextUtils.isEmpty(userID)) {
            int useType = Utils.parseInt((String) SharedPreferencesUtils.getUserParam(this, AppConstants.KEY_USER_TYPE, "1"));
//            //已登录
//            //请求表演者资料
            mPresenter.requestUserInfo(userID, useType);
        } else {
            //未登录
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean isFirst = (boolean) SharedPreferencesUtils.getAppParam(SplashActivity.this, AppConstants.KEY_FIRST_RUN, true);
                    if(isFirst) GuideActivity.launch(SplashActivity.this);
                    else RoleChooseActivity.launch(SplashActivity.this);
                }
            }, 2000);
        }
    }

    @Override
    public Activity get() {
        return getCurrentActivity();
    }

    @Override
    public void requestFailed() {
        RoleChooseActivity.launch(this);
    }
}
