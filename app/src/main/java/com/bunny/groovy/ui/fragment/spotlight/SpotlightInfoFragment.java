package com.bunny.groovy.ui.fragment.spotlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  推广说明界面
 *
 ****************************************/

public class SpotlightInfoFragment extends BaseFragment<SpotlightPresenter> implements ISpotLightView {


    @Bind(R.id.tv_spotlight)
    TextView mTvSpotlight;
    private double mAmount;

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SPOTLIGHT THIS SHOW");
        FragmentContainerActivity.launch(from, SpotlightInfoFragment.class, bundle);
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
        return R.layout.fragment_spotlight_info_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tv_spotlight)
    public void onViewClicked() {
        SpotlightFragment.launch(getActivity());
        get().finish();
    }
}
