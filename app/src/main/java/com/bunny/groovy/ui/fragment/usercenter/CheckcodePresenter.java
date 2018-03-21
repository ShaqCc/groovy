package com.bunny.groovy.ui.fragment.usercenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.listener.VerifyEvent;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/12.
 ****************************************/

class CheckcodePresenter extends BasePresenter<ICheckCodeView> {

    CheckcodePresenter(ICheckCodeView view) {
        super(view);
    }

    void checkCode(String code){
        VerifyEvent.verifyCode(code);
    }

    void bindPhone(String phone) {
        addSubscription(apiService.bindPhone(AppCacheData.getPerformerUserModel().getUserID(), phone), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("success");
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }


    public void bindEmail(String email, String code) {
        addSubscription(apiService.bindEmail(AppCacheData.getPerformerUserModel().getUserID(), email, code), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("success");
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }
}
