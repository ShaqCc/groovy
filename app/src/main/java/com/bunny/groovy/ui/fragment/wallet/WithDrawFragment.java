package com.bunny.groovy.ui.fragment.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  提现界面
 *
 * Author: Created by bayin on 2018/1/10.
 ****************************************/

public class WithDrawFragment extends BaseFragment<PayPalPresenter> implements IPayPalView {
    @Bind(R.id.recharge_tv_paypal_value)
    TextView mRechargeTvPaypalValue;
    @Bind(R.id.recharge_tv_paypal_max)
    TextView mRechargeTvPaypalMax;
    @Bind(R.id.recharge_et_balance)
    EditText mRechargeEtBalance;
    @Bind(R.id.tv_recharge)
    TextView mTvWithDraw;
    private String mBalance;

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "WITHDRAW");
        FragmentContainerActivity.launch(from, WithDrawFragment.class, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mRechargeTvPaypalValue.setText(AppCacheData.getPerformerUserModel().getPaypalAccount());
        mBalance = AppCacheData.getPerformerUserModel().getBalance();
        mRechargeTvPaypalMax.setText("The max is $" + mBalance);
        mTvWithDraw.setText("WITHDRAW");
        mRechargeEtBalance.setHint("WITHDRAW AMOUNT");
        Utils.controlEditText(mRechargeEtBalance,2);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setView(PerformerUserModel userModel) {

    }

    @Override
    protected PayPalPresenter createPresenter() {
        return new PayPalPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_withdraw_layout;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tv_recharge)
    public void onViewClicked() {
        String withdraw = mRechargeEtBalance.getText().toString();
        if (TextUtils.isEmpty(withdraw)) {
            UIUtils.showBaseToast("Please input withdraw money.");
            return;
        }
        mPresenter.withDraw(withdraw);
    }
}
