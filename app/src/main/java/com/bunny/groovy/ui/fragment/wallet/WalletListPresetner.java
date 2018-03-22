package com.bunny.groovy.ui.fragment.wallet;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.WalletBean;
import com.bunny.groovy.utils.AppCacheData;

import java.util.List;

/**
 * Created by bayin on 2018/1/15.
 */

public class WalletListPresetner extends BasePresenter<IWalletListView> {
    public WalletListPresetner(IWalletListView view) {
        super(view);
    }

    public void getWalletList() {
        addSubscription(apiService.getWalletList(), new SubscriberCallBack<List<WalletBean>>(mView.get()) {

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(List<WalletBean> response) {
                if (response != null && response.size() > 0) {
                    mView.setListData(response);
                } else mView.noData();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.noData();
            }
        });
    }

    public void getUserTransactionRecord() {
        addSubscription(apiService.getUserTransactionRecord(AppCacheData.getPerformerUserModel().getUserID()), new SubscriberCallBack<List<WalletBean>>(mView.get()) {

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(List<WalletBean> response) {
                if (response != null && response.size() > 0) {
                    mView.setListData(response);
                } else mView.noData();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.noData();
            }
        });
    }

    public void getNormalUserTransactionRecord() {
        addSubscription(apiService.getNormalUserTransactionRecord(AppCacheData.getPerformerUserModel().getUserID()), new SubscriberCallBack<List<WalletBean>>(mView.get()) {

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(List<WalletBean> response) {
                if (response != null && response.size() > 0) {
                    mView.setListData(response);
                } else mView.noData();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.noData();
            }
        });
    }
}
