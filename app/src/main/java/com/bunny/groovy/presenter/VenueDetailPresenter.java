package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IVenueView;

/****************************************
 * 功能说明:  音乐厅详情页控制器
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class VenueDetailPresenter extends BasePresenter<IVenueView> {
    public VenueDetailPresenter(IVenueView view) {
        super(view);
    }


    /**
     * 获取演出厅详情
     *
     * @param venueid
     */
    public void getVenueDetail(String venueid) {
        addSubscription(apiService.getVenueDetail(venueid), new SubscriberCallBack<VenueModel>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(VenueModel response) {
                mView.setView(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }


    /**
     * 收藏演出厅
     *
     * @param venueID
     */
    public void collectionVenue(String venueID){
        addSubscription(apiService.collectionVenue(venueID), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                UIUtils.showToast("Collection successfully.");
                mView.favorite();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }

    /**
     * 取消收藏演出厅
     *
     * @param venueID
     */
    public void cancleCollectionVenue(String venueID){
        addSubscription(apiService.cancelCollectionVenue(venueID), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                UIUtils.showToast("Cancel collection successfully.");
                mView.cancleFavorite();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }
}
