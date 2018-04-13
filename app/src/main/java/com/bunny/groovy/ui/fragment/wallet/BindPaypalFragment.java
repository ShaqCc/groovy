package com.bunny.groovy.ui.fragment.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.utils.UIUtils;
import com.xw.repo.XEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  绑定PayPal页面
 *
 * Author: Created by bayin on 2018/1/10.
 ****************************************/

public class BindPaypalFragment extends BaseFragment<PayPalPresenter> implements IPayPalView {

    @Bind(R.id.bind_paypal_et_account)
    XEditText mBindPaypalEtAccount;
    @Bind(R.id.bind_paypal_tv_bind)
    TextView mBindPaypalTvBind;

    public static void launch(Activity from,String account){
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE,"BIND PAYPAL");
        bundle.putString("account",account);
        FragmentContainerActivity.launch(from,BindPaypalFragment.class,bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        String account = getArguments().getString("account");
        if(!TextUtils.isEmpty(account)){
            mBindPaypalEtAccount.setText(account);
        }
    }

    @Override
    protected PayPalPresenter createPresenter() {
        return new PayPalPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_bind_paypal_layout;
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.bind_paypal_tv_bind)
    public void onViewClicked() {
        //判断空
        String account = mBindPaypalEtAccount.getTrimmedString();
        if (TextUtils.isEmpty(account)) {
            UIUtils.showBaseToast("Please input PayPal account.");
            return;
        }

        mPresenter.bindPayPal(account);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setView(PerformerUserModel userModel) {

    }
}
