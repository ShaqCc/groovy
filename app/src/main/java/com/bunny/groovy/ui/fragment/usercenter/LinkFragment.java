package com.bunny.groovy.ui.fragment.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.PatternUtils;
import com.bunny.groovy.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/12.
 ****************************************/

public class LinkFragment extends BaseFragment<LinkPresenter> implements ILinkView {
    @Bind(R.id.link_et_content)
    EditText mLinkEtContent;
    @Bind(R.id.link_tv_next)
    TextView mLinkTvNext;

    public static String KEY = "key";
    private static String sType;

    public static void launch(Activity from, Bundle bundle) {
        sType = bundle.getString(KEY);
        FragmentContainerActivity.launch(from, LinkFragment.class, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        if (sType.equals("phone")) {
            mLinkEtContent.setHint("PHONE");
        } else {
            mLinkEtContent.setHint("EMAIL");
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        registerEventBus(this);
    }

    @Override
    protected LinkPresenter createPresenter() {
        return new LinkPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_phone;
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.link_tv_next)
    public void onViewClicked() {

        String content = mLinkEtContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            if (sType.equals("phone") && (PatternUtils.isUSphonenumber(content) || PatternUtils.isCNPhone(content))) {
                mPresenter.sendPhoneCode(content);
            } else if (sType.equals("email") && PatternUtils.isValidEmail(content)) {
                mPresenter.sendEmailCode(content);
            } else {
                UIUtils.showBaseToast("Invalid input.");
            }
        }
    }

    @Subscribe
    public void onRecevieSms(String code) {
        switch (code) {
            case AppConstants.Code_Send_Success://发送成功，跳转到下一个页面
                afterSendCode();
                break;
            case AppConstants.Code_Send_InvalidPhone://发送失败
                UIUtils.showBaseToast("Phone number is wrong.");
                break;
        }
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void afterSendCode() {
        String value = mLinkEtContent.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("key", sType);
        bundle.putString("value", value);
        CheckCodeFragment.launch(mActivity, bundle);
    }
}
