package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.view.IOverView;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class VenueOverviewPresenter extends BasePresenter<IOverView> {
    public VenueOverviewPresenter(IOverView view) {
        super(view);
    }

    public void requestNextShow(String venueId) {
        addSubscription(apiService.getNextPerformInfo(venueId), new SubscriberCallBack<ShowModel>(null) {
            @Override
            protected void onSuccess(ShowModel response) {
                if (response != null) {
                    mView.initNextView(response);
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
        addSubscription(apiService.getVenueDetailInfo(), new SubscriberCallBack<PerformerUserModel>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(PerformerUserModel response) {
                mView.setView(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }
}
