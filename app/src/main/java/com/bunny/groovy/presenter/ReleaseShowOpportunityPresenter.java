package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;

import java.util.Map;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class ReleaseShowOpportunityPresenter extends BasePresenter<ISetFileView> {
    public ReleaseShowOpportunityPresenter(ISetFileView view) {
        super(view);
    }

    /**
     * 演出发布
     *
     * @param fieldMap
     * @param type     2-演出厅用户端发布
     */
    public void releaseShow(Map<String, String> fieldMap, int type) {
        addSubscription(type == 2 ? apiService.releaseVenueShow(fieldMap) : apiService.releaseShow(fieldMap), new SubscriberCallBack(mView.get()) {
            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("发布成功");
                mView.get().finish();
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

    /**
     * 演出机会发布
     */
    public void releaseShow(String startTime, String endTime, String desc) {
        addSubscription(apiService.addPerformOpportunity(startTime, endTime,desc), new SubscriberCallBack(mView.get()) {
            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Release success!");
                mView.get().finish();
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
