package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.listener.VerifyEvent;
import com.bunny.groovy.presenter.ForgetPwdPresenter;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IForgetView;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置密码页面
 * <p>
 * Created by bayin on 2018/1/20.
 */

public class ConfirmPwdActivity extends BaseActivity<ForgetPwdPresenter> implements IForgetView {

    @Bind(R.id.confirm_pwd_et_code)
    XEditText etCode;
    @Bind(R.id.confirm_pwd_et_password_1)
    XEditText etPwd_1;
    @Bind(R.id.confirm_pwd_et_password_2)
    XEditText etPwd_2;

    @OnClick(R.id.confirm_pwd_tv_submit)
    public void submit() {
        String pwd_1 = etPwd_1.getTrimmedString();
        String pwd_2 = etPwd_2.getTrimmedString();
        if (TextUtils.isEmpty(etCode.getTrimmedString())) {
            UIUtils.showBaseToast("Please input password.");
        } else if (pwd_1.length() < 8 || pwd_1.length() < 8) {
            UIUtils.showBaseToast("Password length less than 8.");
        } else if (!pwd_2.equals(pwd_1)) {
            UIUtils.showBaseToast("Password not same.");
        } else {
            if ("0".equals(type)) {//手机
                //验证code
                VerifyEvent.verifyCode(etCode.getTrimmedString());
            } else if ("1".equals(type)) {//邮箱
                mPresenter.checkMailCode(etCode.getTrimmedString(), account, type, etPwd_1.getTrimmedString());
            }
        }
    }

    @OnClick(R.id.confirm_pwd_tv_login)
    public void login() {
        int type = Integer.parseInt(AppCacheData.getPerformerUserModel().getUserType());
        LoginActivity.launch(this, type);
    }

    public static String KEY_ACCOUNT = "key_account";
    public static String KEY_TYPE = "key_type";
    public static String KEY_USER_TYPE = "key_user_type";
    private String account;
    private String type;
    private int mUserType;


    @Override
    public void initView() {
        super.initView();
        try {
            Intent intent = getIntent();
            account = intent.getStringExtra(KEY_ACCOUNT);
            type = intent.getStringExtra(KEY_TYPE);
            mUserType = intent.getIntExtra(KEY_USER_TYPE, 0);
        } catch (Exception e) {
            finish();
        }
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

    /**
     * 验证码结果回调
     *
     * @param result 结果
     */
    @Subscribe
    public void onVerifyEvent(String result) {
        switch (result) {
            case AppConstants.Code_Verify_Correct:
                mPresenter.updateNewPassword(account, type, etPwd_1.getTrimmedString());
                break;
            case AppConstants.Code_Verify_Invalid:
                UIUtils.showBaseToast("Check code incorrect.");
                break;
            case AppConstants.Code_Send_ServerError:
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
        LoginActivity.launch(this, mUserType);
    }

    @Override
    protected ForgetPwdPresenter createPresenter() {
        return new ForgetPwdPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_confirm_pwd;
    }
}
