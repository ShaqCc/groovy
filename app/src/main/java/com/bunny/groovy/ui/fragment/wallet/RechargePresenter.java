package com.bunny.groovy.ui.fragment.wallet;

import android.text.TextUtils;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.socks.library.KLog;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/11.
 ****************************************/

public class RechargePresenter extends BasePresenter<IRechargeView> {
    public RechargePresenter(IRechargeView view) {
        super(view);
    }


    /**
     * 充值
     */
    public void getToken() {
        addSubscription(apiService.getPaypalToken(AppCacheData.getPerformerUserModel().getUserID()), new SubscriberCallBack<String>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(String response) {
                KLog.d("token", response);
                if (!TextUtils.isEmpty(response)) {
                    //调用paypal api
                    mView.onTokenGet(response);
                }
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }


    /**
     * 充值
     *
     * @param amount
     * @param nounce
     */
    public void recharge(String amount, String nounce) {
        addSubscription(apiService.paypalRecharge(AppCacheData.getPerformerUserModel().getUserID(), amount, nounce),
                new SubscriberCallBack<String>(mView.get()) {
                    @Override
                    protected boolean isShowProgress() {
                        return true;
                    }

                    @Override
                    protected void onSuccess(String response) {
                        UIUtils.showBaseToast("Recharge success.");
                        mView.get().finish();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }
}
