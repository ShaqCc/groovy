package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.MusicianDetailModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.view.IMusicianView;
import com.bunny.groovy.view.IVenueView;

/****************************************
 * 功能说明:  表演者详情页控制器
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class MusicianDetailPresenter extends BasePresenter<IMusicianView> {
    public MusicianDetailPresenter(IMusicianView view) {
        super(view);
    }


    /**
     * 获取表演者详情
     * @param performerID
     * @param userID
     */
    public void getSingPerformerDetail(String performerID,String userID) {
        addSubscription(apiService.getSingPerformerDetail(performerID, userID), new SubscriberCallBack<MusicianDetailModel>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(MusicianDetailModel response) {
                mView.setView(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }


    /**
     * 收藏表演者
     */
    public void collectionPerformer(String performerID,String userID){
        addSubscription(apiService.collectionPerformer(performerID, userID), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                mView.favorite();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }

    /**
     * 取消收藏表演者
     */
    public void cancelCollectionPerformer(String performerID,String userID){
        addSubscription(apiService.cancelCollectionPerformer(performerID,userID), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                mView.cancelFavorite();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }
}
