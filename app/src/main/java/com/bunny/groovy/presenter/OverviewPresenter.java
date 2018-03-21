package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IOverView;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class OverviewPresenter extends BasePresenter<IOverView> {
    public OverviewPresenter(IOverView view) {
        super(view);
    }

    public void requestNextShow() {
        addSubscription(apiService.getNextShow(), new SubscriberCallBack<List<ShowModel>>(null) {
            @Override
            protected void onSuccess(List<ShowModel> response) {
                if (response != null && response.size() > 0) {
                    mView.initNextView(response.get(0));
                } else {
                    mView.showEmptyNextShow();
                }
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.showEmptyNextShow();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.showEmptyNextShow();
            }
        });
    }

    /**
     * 获取user数据
     */
    public void requestUserData() {
        addSubscription(apiService.getPerformerInfo(), new SubscriberCallBack<PerformerUserModel>(mView.get()) {
            @Override
            protected void onSuccess(PerformerUserModel response) {
                response.setUserType(String.valueOf(AppConstants.USER_TYPE_MUSICIAN));
                //缓存到本地
                Utils.initLoginData(mView.get(), response);
                //设置数据
                mView.setView(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }
}
