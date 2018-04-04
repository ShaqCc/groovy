package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.listener.VerifyEvent;
import com.bunny.groovy.presenter.ForgetPwdPresenter;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.PatternUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IForgetView;
import com.bunny.groovy.weidget.ProgressHUD;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by bayin on 2018/1/20.
 */

public class ForgetActivity extends BaseActivity<ForgetPwdPresenter> implements IForgetView {

    private String type = "0";

    @Bind(R.id.forget_et_account)
    XEditText etAccount;
    private ProgressHUD progress;
    private int userType = 0;

    @OnClick({R.id.forget_tv_login, R.id.forget_tv_next})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.forget_tv_login:
                finish();
                break;
            case R.id.forget_tv_next:
                String account = etAccount.getTrimmedString();
                if (!TextUtils.isEmpty(account)) {
                    if (PatternUtils.isUSphonenumber(account) || PatternUtils.isCNPhone(account)) {
                        type = "0";
                        //正确的手机号
                        etAccount.setCheckStatus(XEditText.CheckStatus.CORRECT);
                        //发送验证码
                        if (progress != null)
                            progress.show();
                        VerifyEvent.initSinch(this, account);
                    } else if (PatternUtils.isValidEmail(account)) {
                        type = "1";
                        //正确的邮箱
                        etAccount.setCheckStatus(XEditText.CheckStatus.CORRECT);
                        //发送邮箱验证码
                        mPresenter.sendCode(account);
                    } else {
                        //不合法的账户
                        etAccount.setCheckStatus(XEditText.CheckStatus.INVALID);
                        UIUtils.showBaseToast("Invalid account.");
                    }
                } else {
                    UIUtils.showBaseToast("Please input account.");
                }
                break;
        }
    }

    @Override
    public void initView() {
        super.initView();
        progress = ProgressHUD.show(this, "Sending...", false, true, null);
        userType = getIntent().getIntExtra("userType", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerEventBus(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterEventBus(this);
    }

    @Subscribe
    public void onRecevieSms(String code) {
        switch (code) {
            case AppConstants.Code_Send_Success://发送成功，跳转到下一个页面
                if (progress != null && progress.isShowing())
                    progress.dismiss();
                next();
                break;
            case AppConstants.Code_Send_InvalidPhone://发送失败
                UIUtils.showBaseToast("Phone number invalid.");
                break;
            case "5000"://网络错误
                UIUtils.showBaseToast("Server wrong.");
                break;
        }
    }

    @Override
    public Activity get() {
        return this;
    }

    @Override
    public void next() {
        Intent intent = new Intent();
        intent.setClass(this, ConfirmPwdActivity.class);
        intent.putExtra(ConfirmPwdActivity.KEY_ACCOUNT, etAccount.getTrimmedString());
        intent.putExtra(ConfirmPwdActivity.KEY_TYPE, type);
        intent.putExtra(ConfirmPwdActivity.KEY_USER_TYPE, userType);
        startActivity(intent);
    }

    @Override
    protected ForgetPwdPresenter createPresenter() {
        return new ForgetPwdPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.forget_pwd;
    }
}
