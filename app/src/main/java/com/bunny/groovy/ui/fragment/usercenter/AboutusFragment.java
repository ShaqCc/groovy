package com.bunny.groovy.ui.fragment.usercenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.AppCacheData;

import butterknife.Bind;
import butterknife.ButterKnife;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/12.
 ****************************************/

public class AboutusFragment extends BaseFragment {
    @Bind(R.id.aboutus_tv_phone)
    TextView mAboutusTvPhone;
    @Bind(R.id.aboutus_tv_email)
    TextView mAboutusTvEmail;
    @Bind(R.id.aboutus_tv_version)
    TextView tvVersion;


    public static void launch(Activity from) {
        Bundle bundle = new Bundle();

        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "ABOUT US");
        FragmentContainerActivity.launch(from, AboutusFragment.class, bundle);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_about_us_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mAboutusTvPhone.setText(AppCacheData.getGlobalModel().getServicePhone());
        mAboutusTvEmail.setText(AppCacheData.getGlobalModel().getServiceEmail());
        tvVersion.setText("V " + getLocalVersionName(getActivity()));
    }

    public String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
