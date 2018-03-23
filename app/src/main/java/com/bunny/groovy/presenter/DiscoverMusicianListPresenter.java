package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IDiscoverSearchMusicianList;

import java.util.List;

/****************************************
 * 功能说明:  发现表演者控制器
 ****************************************/

public class DiscoverMusicianListPresenter extends BasePresenter<IDiscoverSearchMusicianList> {

    public DiscoverMusicianListPresenter(IDiscoverSearchMusicianList view) {
        super(view);
    }

    /**
     * 获取表演style
     */
    public void requestStyle() {
        addSubscription(apiService.getPerformStyle(), new SubscriberCallBack<List<StyleModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<StyleModel> response) {
                if (response != null && response.size() > 0) {
                    mView.showStylePop(response);
                } else {
                    UIUtils.showBaseToast("Get Style Failed");
                }
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast("Get Style Failed");
            }
        });
    }
    /**
     * 搜索表演者
     */
    public void searchPerformer(String keyword, String  orderType, String performType) {
        addSubscription(apiService.getSearchPerformerList(keyword, orderType, performType), new SubscriberCallBack<List<PerformerUserModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<PerformerUserModel> response) {
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
