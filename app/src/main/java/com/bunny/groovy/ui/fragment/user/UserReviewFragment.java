package com.bunny.groovy.ui.fragment.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.UserMePresenter;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IMeView;
import com.bunny.groovy.weidget.StarGradeView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 普通用户编辑设置页面
 * <p>
 * Created by Administrator on 2017/12/24.
 */

public class UserReviewFragment extends BaseFragment<UserMePresenter> implements IMeView {

    @Bind(R.id.performer_review_content)
    EditText mEtContent;
    @Bind(R.id.star_grade)
    StarGradeView mStarView;
    private static String performId;

    @OnClick(R.id.tv_submit)
    public void submit() {
        String content = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            UIUtils.showBaseToast("Please input content");
            return;
        }
        float star = mStarView.getGrade();
        if (star == 0) {
            UIUtils.showBaseToast("Please select star");
            return;
        }
        mPresenter.evaluatePerformer(performId, String.valueOf(star), content);
    }

    public static void launch(Activity from, String performID) {
        if(TextUtils.isEmpty(performID)) return;
        performId = performID;
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "REVIEW MUSICIAN");
        FragmentContainerActivity.launch(from, UserReviewFragment.class, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mStarView.setChangeGradeEnable(true);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setUserView(PerformerUserModel model) {

    }


    @Override
    public void showStylePop(List<StyleModel> modelList) {

    }

    @Override
    protected UserMePresenter createPresenter() {
        return new UserMePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_review_layout;
    }

    @Override
    protected void loadData() {

    }

}
