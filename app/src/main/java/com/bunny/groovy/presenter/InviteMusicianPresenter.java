package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class InviteMusicianPresenter extends BasePresenter<ISetFileView> {
    public InviteMusicianPresenter(ISetFileView view) {
        super(view);
    }

    /**
     * 获取表演style
     */
    public void inviteMusician(String performerID, String performStartDate,String performEndDate, String desc) {
        addSubscription(apiService.addPerformInvitation(performerID,performStartDate,performEndDate,desc), new SubscriberCallBack<List<StyleModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<StyleModel> response) {
                UIUtils.showBaseToast("Invite success!");
                mView.get().finish();
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast("Invite failed!");
            }
        });
    }

}
