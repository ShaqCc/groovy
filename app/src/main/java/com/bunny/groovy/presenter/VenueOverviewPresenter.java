package com.bunny.groovy.presenter;

import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.api.ApiService;
import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IVenueOverView;
import com.socks.library.KLog;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class VenueOverviewPresenter extends BasePresenter<IVenueOverView> {
    public VenueOverviewPresenter(IVenueOverView view) {
        super(view);
    }

    public void requestNextShow(String venueId) {
        addSubscription(apiService.getNextPerformInfo(venueId), new SubscriberCallBack<VenueShowModel>(null) {
            @Override
            protected void onSuccess(VenueShowModel response) {
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

    /**
     * 推广
     */
    public void spotlightPerform(String performID,String userID) {
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.spotlightPerform(performID, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultResponse<Object>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UIUtils.showBaseToast(e.toString());
                        KLog.d(e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                        if (response.success) {
                            UIUtils.showBaseToast("To promote success !");
                        } else {
                            UIUtils.showBaseToast(response.errorMsg);
                        }
                    }
                });
    }
}
