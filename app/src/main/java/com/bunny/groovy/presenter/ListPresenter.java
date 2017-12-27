package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ShowHistoryModel;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.view.IListPageView;

import java.util.List;

/**
 * 列表页面公用的控制器
 * <p>
 * Created by Administrator on 2017/12/21.
 */

public class ListPresenter extends BasePresenter<IListPageView> {
    public ListPresenter(IListPageView view) {
        super(view);
    }

    /**
     * 获取收藏列表
     */
    public void getFavoriteList() {
        addSubscription(apiService.getMyFavorite(),
                new SubscriberCallBack<List<VenueModel>>(mView.get()) {
                    @Override
                    protected void onSuccess(List<VenueModel> response) {
                        if (response != null && response.size() > 0)
                            mView.setView(response);
                        else mView.setNodata();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {
                        mView.setError();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setError();
                    }
                });
    }

    /**
     * 获取历史演出记录
     */
    public void getHistoryList() {
        addSubscription(apiService.getHistoryList(),
                new SubscriberCallBack<List<ShowHistoryModel>>(mView.get()) {
                    @Override
                    protected void onSuccess(List<ShowHistoryModel> response) {
                        if (response != null && response.size() > 0)
                            mView.setView(response);
                        else mView.setNodata();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {
                        mView.setError();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setError();
                    }
                });
    }


    /**
     * 获取申请的表演机会列表
     */
    public void getOpportunityList() {
        addSubscription(apiService.getApplyOpportunityList(), new SubscriberCallBack<List<ShowModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<ShowModel> response) {
                if (response != null && response.size() > 0)
                    mView.setView(response);
                else mView.setNodata();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.setError();
            }
        });
    }

    /**
     * 获取邀请列表
     */
    public void getInviteList() {
        addSubscription(apiService.getInviteList(), new SubscriberCallBack<List<ShowModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<ShowModel> response) {
                if (response != null && response.size() > 0)
                    mView.setView(response);
                else mView.setNodata();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.setError();
            }
        });
    }

    /**
     * 获取发布的列表
     */
    public void getReleaseList() {
        addSubscription(apiService.getReleaseShowList(), new SubscriberCallBack<List<ShowModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<ShowModel> response) {
                if (response != null && response.size() > 0)
                    mView.setView(response);
                else mView.setNodata();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.setError();
            }
        });
    }
}
