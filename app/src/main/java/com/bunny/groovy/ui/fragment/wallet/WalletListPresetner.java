package com.bunny.groovy.ui.fragment.wallet;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.WalletBean;

import java.util.List;

/**
 * Created by bayin on 2018/1/15.
 */

public class WalletListPresetner extends BasePresenter<IWalletListView> {
    public WalletListPresetner(IWalletListView view) {
        super(view);
    }

    public void fetchList(){
        addSubscription(apiService.getWalletList(), new SubscriberCallBack<List<WalletBean>>(mView.get()) {

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(List<WalletBean> response) {
                if (response!=null && response.size()>0){
                    mView.setListData(response);
                }else mView.noData();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.noData();
            }
        });
    }

    public void getUserTransactionRecord(){
        addSubscription(apiService.getUserTransactionRecord(), new SubscriberCallBack<List<WalletBean>>(mView.get()) {

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(List<WalletBean> response) {
                if (response!=null && response.size()>0){
                    mView.setListData(response);
                }else mView.noData();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.noData();
            }
        });
    }
}
