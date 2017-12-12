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

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 表演者注册页面
 * <p>
 * Created by Administrator on 2017/12/9.
 */

public class SignUpActivity extends BaseActivity<SingUpPresenter> implements ISingUpView {
    @Bind(R.id.et_signup_phone_or_email)
    XEditText etPhoneEmail;
    @Bind(R.id.et_signup_password)
    XEditText etPassword;
    @Bind(R.id.et_signup_password_again)
    XEditText etPasswordAgain;

    private int mAccountType = 0;//账号类型
    private WeakReference<Activity> mWeakReference = new WeakReference<Activity>(this);

    //下一步
    @OnClick(R.id.tv_signup_next)
    void next() {
        String pwd = etPassword.getTrimmedString();
        String pwdAgain = etPasswordAgain.getTrimmedString();
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
            UIUtils.showBaseToast("PASSWORD MUST NOT BE NULL!");
        } else if (pwd.length() < 8 || pwdAgain.length() < 8) {
            UIUtils.showBaseToast("PASSWORD LENGTH AT LEAST 8");
        } else if (!pwd.equals(pwdAgain)) {
            UIUtils.showBaseToast("PASSWORD NOT SAME!");
        } else {
            //检查账户
            String account = etPhoneEmail.getTrimmedString();
            if (TextUtils.isEmpty(account)) {
                UIUtils.showBaseToast("ACCOUNT MUST NOT BE NULL!");
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
        intent.putExtra(SignUp2Activity.KEY_ACCOUNT, etPhoneEmail.getTrimmedString());
        intent.putExtra(SignUp2Activity.KEY_TYPE, mAccountType);
        intent.putExtra(SignUp2Activity.KEY_PASSWORD,etPassword.getTrimmedString());
        intent.setClass(this, SignUp2Activity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public Activity get() {
        return mWeakReference.get();
    }

    @Override
    public void registerSuccess() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppConstants.ACTIVITY_FINISH) {
            finish();
        }
    }
}
