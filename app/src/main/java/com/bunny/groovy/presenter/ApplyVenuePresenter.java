package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IApplyVenueView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class ApplyVenuePresenter extends BasePresenter<IApplyVenueView> {

    public ApplyVenuePresenter(IApplyVenueView view) {
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
     * 申请演出厅演出
     *
     * @param fieldMap
     */
    public void applyVenue(Map<String, String> fieldMap) {
        addSubscription(apiService.releaseShow(fieldMap), new SubscriberCallBack(mView.get()) {
            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Apply successfully.");
                mView.get().finish();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast("Apply failed.");
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }
        });
    }

    /**
     * 确认邀请
     *
     * @param inviteID
     * @param performDesc
     * @param performStyle
     */
    public void confirmInvite(String inviteID, String performDesc, String performStyle) {
        addSubscription(apiService.agreePerformerInvite(inviteID, performStyle, performDesc), new SubscriberCallBack(mView.get()) {
            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Confirm successfully.");
                mView.get().finish();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast("Confirm failed.");
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }
        });
    }


    public void editPerform(final Map<String, String> fieldMap) {
        addSubscription(apiService.updatePerform(fieldMap), new SubscriberCallBack(mView.get()) {
            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Update successfully.");
                int type = Integer.parseInt(AppCacheData.getPerformerUserModel().getUserType());
                if (type == 2) {
                    //编辑后刷新演出厅用户日程列表
                    VenueShowModel model = new VenueShowModel();
                    model.setPerformID(fieldMap.get("performID"));
                    model.setPerformDesc(fieldMap.get("performDesc"));
                    model.setPerformType(fieldMap.get("performType"));
                    model.setPerformStartDate(fieldMap.get("performStartDate"));
                    model.setPerformEndDate(fieldMap.get("performEndDate"));
                    EventBus.getDefault().post(model);
                }
                mView.get().finish();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast("Update failed.");
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }
        });
    }

}
