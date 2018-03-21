package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.listener.VerifyEvent;
import com.bunny.groovy.presenter.SingUpPresenter;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.PatternUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISingUpView;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 注册第二步页面
 * <p>
 * Created by Administrator on 2017/12/11.
 */

public class SignUp2Activity extends BaseActivity<SingUpPresenter> implements ISingUpView {
    public static String KEY_ACCOUNT = "key_account";
    private String mAccount;
    public static String KEY_TYPE = "key_type";
    private int mType;
    public static String KEY_PASSWORD = "key_password";
    private String mPassword;
    @Bind(R.id.signup_et_code)
    XEditText etCode;
    @Bind(R.id.signup_et_phone)
    XEditText etPhone;
    @Bind(R.id.signup_et_email)
    XEditText etEmail;

    @OnClick(R.id.bt_sign_up)
    void signUp() {
        //验证码为空
        if (TextUtils.isEmpty(etCode.getTrimmedString())) {
            UIUtils.showBaseToast("Code must not be null.");
            return;
        }

        if (mType == AppConstants.ACCOUNT_TYPE_PHONE) {
            if (TextUtils.isEmpty(etEmail.getTrimmedString())) {
                //邮箱为空
                UIUtils.showBaseToast("E-mail must not be null.");
                return;
            } else if (!PatternUtils.isValidEmail(etEmail.getTrimmedString())) {
                //邮箱不合法
                UIUtils.showBaseToast("E-mail invalid.");
                return;
            }
            VerifyEvent.verifyCode(etCode.getTrimmedString());
        } else if (mType == AppConstants.ACCOUNT_TYPE_EMAIL) {
            if (TextUtils.isEmpty(etPhone.getTrimmedString()))
            //手机号为空
            {
                UIUtils.showBaseToast("Phone must not be null.");
                return;
            } else if (!PatternUtils.isUSphonenumber(etPhone.getTrimmedString()) || !PatternUtils.isCNPhone(etPhone.getTrimmedString())) {
                UIUtils.showBaseToast("Phone invalid.");
                return;
            }
            mPresenter.checkEmailCode(etCode.getTrimmedString(), URLEncoder.encode(mAccount));
        }

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
                mPresenter.register(mAccount, mPassword, etPhone.getTrimmedString(), etEmail.getTrimmedString());
                break;
            case AppConstants.Code_Verify_Invalid:
                UIUtils.showBaseToast("check code incorrect.");
                break;
            case AppConstants.Code_Send_ServerError:
                UIUtils.showBaseToast("server error!");
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        if (intent != null) {
            mAccount = intent.getStringExtra(KEY_ACCOUNT);
            mType = intent.getIntExtra(KEY_TYPE, 0);
            mPassword = intent.getStringExtra(KEY_PASSWORD);
        } else finish();

        //set view
        if (AppConstants.ACCOUNT_TYPE_EMAIL == mType) {
            etEmail.setText(mAccount);
            etEmail.setFocusable(false);
            etEmail.setTextColor(Color.GRAY);
        } else if (AppConstants.ACCOUNT_TYPE_PHONE == mType) {
            etPhone.setText(mAccount);
            etPhone.setFocusable(false);
            etPhone.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        registerEventBus(this);
    }

    @Override
    protected SingUpPresenter createPresenter() {
        return new SingUpPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register_second_layout;
    }

    @Override
    public void showCheckResult(boolean invalid, int accountType, String msg) {

    }

    @Override
    public void nextStep() {

    }

    @Override
    public Activity get() {
        return getCurrentActivity();
    }

    /**
     * 注册成功，返回登录页面
     */
    @Override
    public void registerSuccess() {
//        setResult(AppConstants.ACTIVITY_FINISH);
//        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
