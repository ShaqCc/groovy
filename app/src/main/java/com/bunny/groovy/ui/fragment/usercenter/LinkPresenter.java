package com.bunny.groovy.ui.fragment.usercenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.listener.VerifyEvent;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.AppCacheData;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/12.
 ****************************************/

public class LinkPresenter extends BasePresenter<ILinkView> {

    public LinkPresenter(ILinkView view) {
        super(view);
    }

    public void sendPhoneCode(String phone) {
        VerifyEvent.initSinch(mView.get(), phone);
    }


    public void sendEmailCode(String email) {
        addSubscription(apiService.sendEmailCode(AppCacheData.getPerformerUserModel().getUserID(), email), new SubscriberCallBack(mView.get()) {

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                mView.afterSendCode();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }
}
