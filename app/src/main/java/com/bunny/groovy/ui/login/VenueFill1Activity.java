package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.text.TextUtils;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.SetFilePresenter;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;
import com.xw.repo.XEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mysty on 2018/2/26.
 */

public class VenueFill1Activity extends BaseActivity<SetFilePresenter> implements ISetFileView {

    @Bind(R.id.venue_info_website)
    XEditText mWebsite;
    @Bind(R.id.venue_info_head_pic)
    CircleImageView mHeadView;

    @Override
    protected SetFilePresenter createPresenter() {
        return new SetFilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_venue_info_fill_first;
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {

    }

    @Override
    public Activity get() {
        return getCurrentActivity();
    }


    @OnClick(R.id.perfect_info_tv_next)
    void next() {
        //拦截
        if (TextUtils.isEmpty(mWebsite.getTrimmedString())) {
            UIUtils.showBaseToast("请输入网站");
            return;
        }
        //保存数据
        AppCacheData.getFileMap().put("webSiteAddress", mWebsite.getTrimmedString());
//        AppCacheData.getFileMap().put("userID", AppCacheData.getUserInfo().getUserID());
    }
}
