package com.bunny.groovy.ui.fragment.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  个人中心钱包页面
 *
 * Author: Created by bayin on 2018/1/10.
 ****************************************/

public class WalletFragment extends BaseFragment<PayPalPresenter> implements IPayPalView {


    @Bind(R.id.wallet_tv_balance)
    TextView mWalletTvBalance;
    @Bind(R.id.wallet_tv_recharge)
    TextView mWalletTvRecharge;
    @Bind(R.id.wallet_tv_withdraw)
    TextView mWalletTvWithdraw;
    @Bind(R.id.wallet_tv_bind_paypal)
    TextView mWalletTvBindPaypal;

    private String dollarTag = "$ %s";

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "WALLET");
        FragmentContainerActivity.launch(from, WalletFragment.class, bundle);
    }

    @Override
    protected PayPalPresenter createPresenter() {
        return new PayPalPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_wallet_layout;
    }

    @Override
    protected void loadData() {
        mPresenter.updateUserData(AppCacheData.getPerformerUserModel().getUserType());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstEnter()) {
            loadData();
        }
    }

    @OnClick({R.id.wallet_tv_recharge, R.id.wallet_tv_withdraw, R.id.wallet_tv_bind_paypal})
    public void onViewClicked(View view) {
        String paypalAccount = AppCacheData.getPerformerUserModel().getPaypalAccount();
        switch (view.getId()) {
            case R.id.wallet_tv_recharge:
                //充值
                RechargeFragment.launch(mActivity);
                break;
            case R.id.wallet_tv_withdraw:
                //提现
                if (TextUtils.isEmpty(paypalAccount)) {
                    UIUtils.showBaseToast("Please bind PayPal.");
                    return;
                }
                WithDrawFragment.launch(mActivity);
                break;
            case R.id.wallet_tv_bind_paypal:
                //绑定
                BindPaypalFragment.launch(mActivity, paypalAccount);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.wallet_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.wallet_list) {
            WalletListFragment.launch(mActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setView(PerformerUserModel userModel) {
        mWalletTvBalance.setText(String.format(dollarTag, userModel.getBalance()));
    }
}
