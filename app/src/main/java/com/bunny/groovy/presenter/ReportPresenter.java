package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;

import java.util.List;

/****************************************
 * 功能说明:  举报表演者
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class ReportPresenter extends BasePresenter<ISetFileView> {
    public ReportPresenter(ISetFileView view) {
        super(view);
    }

    /**
     * 获取表演style
     */
    public void reportPerformer(String beReportedID, String reporterID,String reportContent) {
        addSubscription(apiService.reportPerformer(beReportedID,reporterID,reportContent), new SubscriberCallBack<List<Object>>(mView.get()) {
            @Override
            protected void onSuccess(List<Object> response) {
                UIUtils.showBaseToast("Report success!");
                mView.get().finish();
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast("Report failed!");
            }
        });
    }

}
