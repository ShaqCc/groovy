package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.listener.VerifyEvent;
import com.bunny.groovy.manager.LoginBlock;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.presenter.LoginPresenter;
import com.bunny.groovy.ui.MainActivity;
import com.bunny.groovy.ui.setfile.SetFile1Activity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.PatternUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ILoginView;
import com.bunny.groovy.weidget.ProgressHUD;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 第三方登录，绑定账户
 * <p>
 * Created by bayin on 2018/1/14.
 */

public class BindAccountFragment extends BaseFragment<LoginPresenter> implements ILoginView {

    @Bind(R.id.bind_account_et_account)
    XEditText etAccount;
    @Bind(R.id.bind_account_et_code)
    XEditText etCode;
    @Bind(R.id.bind_account_et_email)
    XEditText etEmail;
    private static String logintype;
    private static String uid;
    private static String username;
    private static String userType;
    private ProgressHUD mSendProgress;

    @OnClick({R.id.bind_account_tv_ok, R.id.bind_account_tv_send})
    public void onViewClick(View view) {
        UIUtils.hideSoftInput(view);
        String account = etAccount.getTrimmedString();
        switch (view.getId()) {
            case R.id.bind_account_tv_ok:
                //输入了邮箱
                if (!TextUtils.isEmpty(etEmail.getTrimmedString())) {
                    if (PatternUtils.isValidEmail(etEmail.getTrimmedString()))
                        //加入邮箱参数
                        AppCacheData.getPerformerUserModel().setUserEmail(etEmail.getTrimmedString());
                    else {
                        UIUtils.showBaseToast("invalid Email.");
                        return;
                    }
                }

                String code = etCode.getTrimmedString();
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(code)) {
                    if (PatternUtils.isCNPhone(account) || PatternUtils.isUSphonenumber(account)) {
                        //手机账户
                        VerifyEvent.verifyCode(code);
                    } else {
                        //非法账户
                        UIUtils.showBaseToast("invalid account.");
                    }
                }
                break;
            case R.id.bind_account_tv_send:
                if (PatternUtils.isCNPhone(account) || PatternUtils.isUSphonenumber(account)) {
                    //发送手机验证码
                    if (mSendProgress != null && !mSendProgress.isShowing()) mSendProgress.show();
                    VerifyEvent.initSinch(mActivity, account);
                }
                break;
        }
    }

    public static void launch(Activity from, Bundle bundle) {
        username = bundle.getString("username");
        uid = bundle.getString("uid");
        logintype = bundle.getString("logintype");
        userType = bundle.getString("userType");
        FragmentContainerActivity.launch(from, BindAccountFragment.class, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        if (mActivity instanceof FragmentContainerActivity)
            ((FragmentContainerActivity) mActivity).getToolBar().setVisibility(View.GONE);

        //验证码dialog
        mSendProgress = ProgressHUD.show(mActivity, "sending...", true, true, null);
        registerEventBus(this);
    }

    /**
     * 验证码结果回调
     *
     * @param result 结果
     */
    @Subscribe
    public void onVerifyEvent(String result) {
        if (mSendProgress != null && mSendProgress.isShowing()) mSendProgress.dismiss();
        switch (result) {
            case AppConstants.Code_Verify_Correct:
                mPresenter.socialLogin(logintype, uid, username, etAccount.getTrimmedString(),userType);
                break;
            case AppConstants.Code_Verify_Invalid:
                UIUtils.showBaseToast("check code incorrect.");
                break;
            case AppConstants.Code_Send_ServerError:
                UIUtils.showBaseToast("server error!");
                break;
        }
    }

    @Override
    public void loginSuccess(PerformerUserModel userModel) {

    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void launchMainPage(int type) {
        LoginBlock.getInstance().handleCheckSuccess(String.valueOf(type));
        getActivity().finish();
    }

    @Override
    public void launchToSetFile() {
        startActivityForResult(new Intent(mActivity, SetFile1Activity.class), AppConstants.REQUESTCODE_SETFILE);
    }


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_bind_account;
    }

    @Override
    protected void loadData() {

    }
}
