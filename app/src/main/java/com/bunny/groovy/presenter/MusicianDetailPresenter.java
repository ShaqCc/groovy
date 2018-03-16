package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.MusicianDetailModel;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.ui.fragment.releaseshow.InviteMusicianFragment;
import com.bunny.groovy.view.IMusicianView;

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
     * 获取表演者详情
     * @param performerID
     */
    public void getSingPerformerDetail(String performerID) {
        addSubscription(apiService.getSingPerformerDetail(performerID), new SubscriberCallBack<MusicianDetailModel>(mView.get()) {
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

    /**
     * 收藏表演者
     */
    public void collectionPerformer(String performerID){
        addSubscription(apiService.collectionPerformer(performerID), new SubscriberCallBack(mView.get()) {
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
    public void cancelCollectionPerformer(String performerID){
        addSubscription(apiService.cancelCollectionPerformer(performerID), new SubscriberCallBack(mView.get()) {
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


    /**
     * 取消收藏表演者
     */
    public void invite(MusicianDetailModel musicianDetailModel){
        PerformerUserModel model = new PerformerUserModel();
        model.setUserID(musicianDetailModel.userID);
        model.setHeadImg(musicianDetailModel.headImg);
        model.setStarLevel(musicianDetailModel.starLevel);
        model.setUserName(musicianDetailModel.userName);
        model.setPerformTypeName(musicianDetailModel.performTypeName);
        model.setPhoneNumber(musicianDetailModel.telephone);
        InviteMusicianFragment.launch(mView.get(), model);
    }
}
