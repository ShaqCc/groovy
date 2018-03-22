package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.MusicianModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.model.UserMainModel;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.view.IListPageView;

import java.util.List;
import java.util.Map;

/**
 * 列表页面公用的控制器
 * <p>
 * Created by Administrator on 2017/12/21.
 */

public class UserListPresenter extends BasePresenter<IListPageView> {
    public UserListPresenter(IListPageView view) {
        super(view);
    }

    /**
     * 获取我收藏的所有表演者列表
     */
    public void getFavoriteList() {
        addSubscription(apiService.getUserCollectionPerformerList(AppCacheData.getPerformerUserModel().getUserID()),
                new SubscriberCallBack<List<MusicianModel>>(mView.get()) {
                    @Override
                    protected void onSuccess(List<MusicianModel> response) {
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
     * 获取表演信息列表
     */
    public void getPerformViewList() {
        addSubscription(apiService.getPerformViewList(AppCacheData.getPerformerUserModel().getUserID()),
                new SubscriberCallBack<List<ShowModel>>(mView.get()) {
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

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setError();
                    }
                });
    }

    //请求周边地点数据
    public void getPerformList(Map<String, String> map) {
        addSubscription(apiService.getPerformList(map), new SubscriberCallBack<UserMainModel>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(UserMainModel response) {
                mView.setView(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }

        });
    }

}