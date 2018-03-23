package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IForgetView;

/**
 * Created by bayin on 2018/1/20.
 */

public class ForgetPwdPresenter extends BasePresenter<IForgetView> {

    public ForgetPwdPresenter(IForgetView view) {
        super(view);
    }


    public void sendCode(String email) {
        addSubscription(apiService.forgetPwdSendCode(email), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Send successfully.");
                mView.next();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }

    public void checkMailCode(String code, final String account, final String type, final String password) {
        addSubscription(apiService.checkMailCode(code), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                //code正确
                UIUtils.showBaseToast("Code successfully.");
                //设置密码
                updateNewPassword(account, type, password);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }

        });
    }

    public void updateNewPassword(String account, String accountType, String passwrod) {
        addSubscription(apiService.updatePwd(account, accountType, passwrod), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Password reset successfully.");
                mView.next();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }
}
