package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ITimeView;

import java.util.List;

/****************************************
 * 功能说明:  
 ****************************************/

public class TimePresenter extends BasePresenter<ITimeView> {

    public TimePresenter(ITimeView view) {
        super(view);
    }

    /**
     * 查看演出厅营业时间安排
     */
    public void getVenueSchedule() {
        addSubscription(apiService.getVenueSchedule(),
                new SubscriberCallBack<List<VenueModel.ScheduleListBean>>(mView.get()) {

                    @Override
                    protected boolean isShowProgress() {
                        return true;
                    }

                    @Override
                    protected void onSuccess(List<VenueModel.ScheduleListBean> response) {
                        mView.setView(response);
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }

    /**
     * 修改演出厅营业时间
     */
    public void updateScheduleDate(final String scheduleID, final String startDate, final String endDate) {
        addSubscription(apiService.updateScheduleDate(scheduleID, startDate, endDate), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                mView.updateScheduleView(scheduleID, startDate, endDate);
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast(response.errorMsg);
            }
        });
    }

    /**
     * 修改演出厅是否有服务费状态
     */
    public void updateScheduleIsHaveCharges(final String scheduleID, final String isHaveCharges) {
        addSubscription(apiService.updateScheduleIsHaveCharges(scheduleID, isHaveCharges), new SubscriberCallBack(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                mView.updateScheduleIsHaveCharges(scheduleID, isHaveCharges);
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast(response.errorMsg);
            }
        });
    }
}
