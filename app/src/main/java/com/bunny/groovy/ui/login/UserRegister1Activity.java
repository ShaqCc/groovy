package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.presenter.SingUpPresenter;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISingUpView;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 普通用户注册页面
 * <p>
 * Created by Administrator on 2017/12/9.
 */

public class UserRegister1Activity extends BaseActivity<SingUpPresenter> implements ISingUpView {
    @Bind(R.id.et_signup_phone_or_email)
    XEditText etPhoneEmail;
    @Bind(R.id.et_signup_password)
    XEditText etPassword;
    @Bind(R.id.et_signup_password_again)
    XEditText etPasswordAgain;
    @Bind(R.id.et_signup_name)
    XEditText etName;

    private int mAccountType = 0;//账号类型
    private WeakReference<Activity> mWeakReference = new WeakReference<Activity>(this);

    //下一步
    @OnClick(R.id.tv_signup_next)
    void next() {
        String pwd = etPassword.getTrimmedString();
        String pwdAgain = etPasswordAgain.getTrimmedString();
        String name = etName.getTrimmedString();
        if (TextUtils.isEmpty(name)) {
            UIUtils.showBaseToast("Please input your name");
        } else if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
            UIUtils.showBaseToast("Please input your code");
        } else if (pwd.length() < 8 || pwdAgain.length() < 8) {
            UIUtils.showBaseToast("密码至少8位");
        } else if (!pwd.equals(pwdAgain)) {
            UIUtils.showBaseToast("密码输入不一致");
        } else {
            //检查账户
            String account = etPhoneEmail.getTrimmedString();
            if (TextUtils.isEmpty(account)) {
                UIUtils.showBaseToast("Please input account!");
                return;
            }
            mPresenter.checkAccount(account, true);
        }
    }

    //登陆
    @OnClick(R.id.tv_signup_login)
    void login() {
        finish();
    }

    @Override
    public void initView() {
        super.initView();
        etName.setVisibility(View.VISIBLE);
    }

    @Override
    protected SingUpPresenter createPresenter() {
        return new SingUpPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register_first_layout;
    }

    @Override
    public void initListener() {
        super.initListener();
        //账户输入框的监听
        etPhoneEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String account = etPhoneEmail.getText().toString();
                if (!hasFocus && !TextUtils.isEmpty(account)) {
                    //检查是否合法
                    mPresenter.checkAccount(account, false);
                }
            }
        });
        //event bus
        registerEventBus(this);
    }


    @Subscribe
    public void onRecevieSms(String code) {
        switch (code) {
            case AppConstants.Code_Send_Success://发送成功，跳转到下一个页面
                nextStep();
                break;
            case AppConstants.Code_Send_InvalidPhone://发送失败
                UIUtils.showBaseToast("手机号码不正确");
                break;
            case "5000"://网络错误
                UIUtils.showBaseToast("服务器出错");
                break;
            default:
                break;
        }
    }

    @Override
    public void showCheckResult(boolean invalid, int AccountType, String msg) {
        mAccountType = AccountType;
        if (invalid) etPhoneEmail.setCheckStatus(XEditText.CheckStatus.CORRECT);
        else {
            etPhoneEmail.setCheckStatus(XEditText.CheckStatus.INVALID);
            UIUtils.showBaseToast(msg);
        }
    }

    @Override
    public void nextStep() {
        Intent intent = new Intent();
        intent.putExtra(UserRegister2Activity.KEY_ACCOUNT, etPhoneEmail.getTrimmedString());
        intent.putExtra(UserRegister2Activity.KEY_TYPE, mAccountType);
        intent.putExtra(UserRegister2Activity.KEY_PASSWORD, etPassword.getTrimmedString());
        intent.putExtra(UserRegister2Activity.KEY_NAME, etName.getTrimmedString());
        intent.setClass(this, UserRegister2Activity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    public Activity get() {
        return mWeakReference.get();
    }

    @Override
    public void registerSuccess() {
        //第二步才用到
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == AppConstants.ACTIVITY_FINISH) {
            finish();
        }
    }
}
