package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISearchMusicianList;

import java.util.List;

/****************************************
 * 功能说明:  搜索表演者控制器
 ****************************************/

public class SearchMusicianListPresenter extends BasePresenter<ISearchMusicianList> {

    public SearchMusicianListPresenter(ISearchMusicianList view) {
        super(view);
    }

    /**
     * 搜索表演者
     *
     * @param keyword
     */
    public void searchPerformer(String keyword) {
        addSubscription(apiService.getPerformerList(keyword), new SubscriberCallBack<List<PerformerUserModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<PerformerUserModel> response) {
                if (response != null && response.size() > 0)
                    mView.setListView(response);
                else
                    UIUtils.showBaseToast("查询结果为空，请重新输入");
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
