package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.view.IListPageView;

import java.util.List;

/**
 * 列表页面公用的控制器
 * <p>
 * Created by Administrator on 2017/12/21.
 */

public class VenueListPresenter extends BasePresenter<IListPageView> {
    public VenueListPresenter(IListPageView view) {
        super(view);
    }

    /**
     * 获取收藏的表演者列表
     */
    public void getCollectionPerformerList(String userID) {
        addSubscription(apiService.getCollectionPerformerList(userID),
                new SubscriberCallBack<List<PerformerUserModel>>(mView.get()) {
                    @Override
                    protected void onSuccess(List<PerformerUserModel> response) {
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
     * 获取表演者历史演出记录
     */
    public void getPerformHistoryList() {
        addSubscription(apiService.getPerformHistoryList(),
                new SubscriberCallBack<List<VenueShowModel>>(mView.get()) {
                    @Override
                    protected void onSuccess(List<VenueShowModel> response) {
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

}
