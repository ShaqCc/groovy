package com.bunny.groovy.ui;

import android.content.Context;
import android.content.Intent;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.base.BaseApp;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.listener.PermissionListener;
import com.bunny.groovy.ui.login.LoginActivity;
import com.bunny.groovy.utils.AppConstants;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.OnClick;
import flyn.Eyes;

/**
 * 角色选择页面
 * <p>
 * Created by Administrator on 2017/12/2.
 */

public class RoleChooseActivity extends BaseActivity {


    @OnClick(R.id.tv_musician)
    void loginMusician() {
        startActivity(new Intent(this, LoginActivity.class).putExtra("type", AppConstants.USER_TYPE_MUSICIAN));
    }

    @OnClick(R.id.tv_venue)
    void loginVenue() {
        startActivity(new Intent(this, LoginActivity.class).putExtra("type", AppConstants.USER_TYPE_VENUE));
    }

    @OnClick(R.id.tv_showgoer)
    void loginNormal() {
        startActivity(new Intent(this, LoginActivity.class).putExtra("type", AppConstants.USER_TYPE_NORMAL));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_role_choose_layout;
    }

    @Override
    public void initView() {
        super.initView();
        Eyes.translucentStatusBar(this, true);
    }

    public static void launch(Context activity) {
        Intent intent = new Intent(BaseApp.getContext(), RoleChooseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApp.getContext().startActivity(intent);
        for (int i = 0; i < mActivities.size(); i++) {
            if (!(mActivities.get(i) instanceof RoleChooseActivity))
                mActivities.get(i).finish();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        requestRuntimePermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermissions) {

            }
        });
        registerEventBus(this);
    }

    @Subscribe
    public void onLoginSuccess(String result) {
        if (AppConstants.EVENT_LOGIN_SUCCESS.equals(result))
            finish();
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

}
