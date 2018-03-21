package com.bunny.groovy.ui.fragment.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  验证码
 *
 * Author: Created by bayin on 2018/1/12.
 ****************************************/

public class CheckCodeFragment extends BaseFragment<CheckcodePresenter> implements ICheckCodeView {
    @Bind(R.id.checkcode_et_code)
    XEditText mCheckcodeEtCode;
    private static String sType;
    private static String sValue;


    public static void launch(Activity from, Bundle bundle) {
        sType = bundle.getString("key");
        sValue = bundle.getString("value");
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "VERIFICATION CODE");
        FragmentContainerActivity.launch(from, CheckCodeFragment.class, bundle);
    }

    @Override
    public void initListener() {
        super.initListener();
        registerEventBus(this);
    }

    @Override
    protected CheckcodePresenter createPresenter() {
        return new CheckcodePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_checkcode;
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.checkcode_tv_finish)
    public void onViewClicked() {
        String code = mCheckcodeEtCode.getTrimmedString();

        if (!TextUtils.isEmpty(code))
            if (sType.equals("phone")) {
                mPresenter.checkCode(code);
            } else {
                mPresenter.bindEmail(sValue, code);
            }
    }

    @Subscribe
    public void onVerifyEvent(String result) {
        switch (result) {
            case AppConstants.Code_Verify_Correct:
                mPresenter.bindPhone(sValue);
                break;
            case AppConstants.Code_Verify_Invalid:
                UIUtils.showBaseToast("验证码不正确");
                break;
            case AppConstants.Code_Send_ServerError:
                UIUtils.showBaseToast("服务器出错");
                break;
            default:
                break;
        }
    }

    @Override
    public Activity get() {
        return mActivity;
    }
}
