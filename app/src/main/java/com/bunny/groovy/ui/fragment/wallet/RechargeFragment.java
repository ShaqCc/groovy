package com.bunny.groovy.ui.fragment.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  充值界面
 *
 * Author: Created by bayin on 2018/1/10.
 ****************************************/

public class RechargeFragment extends BaseFragment<RechargePresenter> implements IRechargeView {
    private static final int REQUEST_CODE = 888;
    @Bind(R.id.recharge_tv_paypal_value)
    TextView mRechargeTvPaypalValue;
    @Bind(R.id.recharge_tv_paypal_max)
    TextView mRechargeTvPaypalMax;
    @Bind(R.id.recharge_et_balance)
    EditText mRechargeEtBalance;
    @Bind(R.id.tv_recharge)
    TextView mTvRecharge;
    private BraintreeFragment mBraintreeFragment;
    private double mAmount;

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "RECHARGE");
        FragmentContainerActivity.launch(from, RechargeFragment.class, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mRechargeTvPaypalValue.setText(AppCacheData.getPerformerUserModel().getPaypalAccount());
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    /**
     * 充值获取到token后
     *
     * @param token
     */
    @Override
    public void onTokenGet(String token) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(token);
        startActivityForResult(dropInRequest.getIntent(mActivity), REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected RechargePresenter createPresenter() {
        return new RechargePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_recharge_layout;
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
        //判断金额
        mAmount = Double.parseDouble(mRechargeEtBalance.getText().toString());
        if (mAmount <= 0) {
            UIUtils.showBaseToast("Input incorrect.");
            return;
        } else if (mAmount > 999) {
            UIUtils.showBaseToast("Recharge At most $999.");
            return;
        }
        mPresenter.getToken();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                mPresenter.recharge(String.valueOf(mAmount), result.getPaymentMethodNonce().getNonce());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }
}
