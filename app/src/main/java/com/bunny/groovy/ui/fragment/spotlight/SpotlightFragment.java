package com.bunny.groovy.ui.fragment.spotlight;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  推广界面
 *
 *
 * Author: Created by bayin on 2018/1/11.
 ****************************************/

public class SpotlightFragment extends BaseFragment<SpotlightPresenter> implements ISpotLightView {


    @Bind(R.id.spotlight_tv_per)
    TextView mSpotlightTvPer;
    @Bind(R.id.spotlight_et_number)
    EditText mSpotlightEtNumber;
    @Bind(R.id.spotlight_tv_total)
    TextView mSpotlightTvTotal;
    @Bind(R.id.tv_spotlight)
    TextView mTvSpotlight;
    private double mAmount;
    private double mPackagePrice;

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SPOTLIGHT");
        FragmentContainerActivity.launch(from, SpotlightFragment.class, bundle);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    protected SpotlightPresenter createPresenter() {
        return new SpotlightPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_spotlight_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mPackagePrice = Double.parseDouble(AppCacheData.getGlobalModel().getPackagePrice());
        mSpotlightTvPer.setText("$" + mPackagePrice + "per");
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initListener() {
        super.initListener();
        mSpotlightEtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (TextUtils.isEmpty(s.toString())) {
                        mSpotlightTvTotal.setText("0");
                    } else {
                        int number = Integer.parseInt(s.toString());
                        mAmount = number * mPackagePrice;
                        mSpotlightTvTotal.setText(String.valueOf(mAmount));
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tv_spotlight)
    public void onViewClicked() {
        String number = mSpotlightEtNumber.getText().toString();
        if (!TextUtils.isEmpty(number)) {
            HashMap<String, String> ma = new HashMap<>();
            //@Field("userID") String userID, @Field("num") String num,
            //@Field("amount") String amount, @Field("payMethod") String payMethod,
            //@Field("payment_method_nonce") String nouce
            ma.put("userID", AppCacheData.getPerformerUserModel().getUserID());
            ma.put("num", number);
            ma.put("amount", String.valueOf(mAmount));
            ma.put("payMethod", AppConstants.PAY_STYLE_BALANCE);
            mPresenter.buySpotLight(ma);
        }
    }
}
