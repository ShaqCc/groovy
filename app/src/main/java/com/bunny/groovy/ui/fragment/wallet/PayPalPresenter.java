package com.bunny.groovy.ui.fragment.wallet;

import android.text.TextUtils;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;

/****************************************
 * 功能说明:wallet页面控制器
 *
 * Author: Created by bayin on 2018/1/10.
 ****************************************/

public class PayPalPresenter extends BasePresenter<IPayPalView> {

    public PayPalPresenter(IPayPalView view) {
        super(view);
    }

    /**
     * 绑定
     *
     * @param account
     */
    public void bindPayPal(String account) {
        addSubscription(apiService.bindPaypal(AppCacheData.getPerformerUserModel().getUserID(), account),
                new SubscriberCallBack(mView.get()) {

                    @Override
                    protected boolean isShowProgress() {
                        return true;
                    }

                    @Override
                    protected void onSuccess(Object response) {
                        UIUtils.showBaseToast("Bind success.");
                        mView.get().finish();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }


    public void withDraw(String amount) {
        addSubscription(AppConstants.USER_TYPE_MUSICIAN == Utils.parseInt(AppCacheData.getPerformerUserModel().getUserType()) ?
                apiService.getWithdrawals(amount) : apiService.userWithdrawals(AppCacheData.getPerformerUserModel().getUserID(), amount), new SubscriberCallBack(mView.get()) {
            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Success.");
                mView.get().finish();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }


    /**
     * 获取用户数据
     */
    public void updateUserData(final String type) {
        addSubscription(TextUtils.equals(type, String.valueOf(AppConstants.USER_TYPE_MUSICIAN))
                        ? apiService.getPerformerInfo() : (TextUtils.equals(type, String.valueOf(AppConstants.USER_TYPE_VENUE))
                        ? apiService.getVenueDetailInfo() : apiService.getUserInfo(AppCacheData.getPerformerUserModel().getUserID())),
                new SubscriberCallBack<PerformerUserModel>(mView.get()) {
                    @Override
                    protected void onSuccess(PerformerUserModel response) {
                        //缓存到本地
                        response.setUserType(String.valueOf(type));
                        Utils.initLoginData(mView.get(), response);
                        mView.setView(response);
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }
}
