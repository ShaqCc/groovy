package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISearchVenueList;

import java.util.List;

/****************************************
 * 功能说明:  搜索音乐厅控制器
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class SearchVenueListPresenter extends BasePresenter<ISearchVenueList> {

    public SearchVenueListPresenter(ISearchVenueList view) {
        super(view);
    }

    /**
     * 搜索音乐厅
     *
     * @param keyword
     */
    public void searchVenue(String keyword) {
        addSubscription(apiService.getVenueList(keyword), new SubscriberCallBack<List<VenueModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<VenueModel> response) {
                if (response != null && response.size() > 0)
                    mView.setListView(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }
        });
    }
}
