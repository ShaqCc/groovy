package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.text.TextUtils;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.presenter.VenueRegisterPresenter;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.view.ISingUpView;
import com.xw.repo.XEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by mysty on 2018/2/26.
 */

public class VenueFile2Activity extends BaseActivity<VenueRegisterPresenter> implements ISingUpView {

    @OnClick(R.id.tv_go)
    void go() {
        if (!TextUtils.isEmpty(etFaceBook.getTrimmedString())) {
            AppCacheData.getFileMap().put("facebookAccount", etFaceBook.getTrimmedString());
        }
        if (!TextUtils.isEmpty(etTwitter.getTrimmedString())) {
            AppCacheData.getFileMap().put("twitterAccount", etTwitter.getTrimmedString());
        }
        mPresenter.updateVenueInfo(AppCacheData.getFileMap());
    }

    @Bind(R.id.et_facebook)
    XEditText etFaceBook;
    @Bind(R.id.et_twitter)
    XEditText etTwitter;

    @Override
    public void showCheckResult(boolean invalid, int accountType, String msg) {

    }

    @Override
    public void nextStep() {

    }

    @Override
    public Activity get() {
        return getCurrentActivity();
    }

    @Override
    public void registerSuccess() {

    }

    @Override
    protected VenueRegisterPresenter createPresenter() {
        return new VenueRegisterPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_venue_info_fill_second;
    }
}
