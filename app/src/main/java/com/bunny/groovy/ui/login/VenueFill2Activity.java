package com.bunny.groovy.ui.login;

import android.app.Activity;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.SetFilePresenter;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.view.ISetFileView;
import com.xw.repo.XEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by mysty on 2018/2/26.
 */

public class VenueFill2Activity extends BaseActivity<SetFilePresenter> implements ISetFileView {

    @OnClick(R.id.tv_go)
    void go() {
        AppCacheData.getFileMap().put("facebookAccount", etFaceBook.getTrimmedString());
        AppCacheData.getFileMap().put("twitterAccount", etTwitter.getTrimmedString());

        mPresenter.updateUserInfo(AppCacheData.getFileMap());
    }

    @Bind(R.id.et_facebook)
    XEditText etFaceBook;
    @Bind(R.id.et_twitter)
    XEditText etTwitter;

    @Override
    public Activity get() {
        return getCurrentActivity();
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {

    }

    @Override
    protected SetFilePresenter createPresenter() {
        return new SetFilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_venue_info_fill_second;
    }
}
