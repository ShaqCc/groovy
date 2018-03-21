package com.bunny.groovy.ui.fragment.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/12.
 ****************************************/

public class FeedbackFragment extends BaseFragment<FdbPresenter> implements IFView {

    @Bind(R.id.feedback_tv_content)
    EditText mFeedbackTvContent;
    @Bind(R.id.feedback_tv_submit)
    TextView mFeedbackTvSubmit;

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();

        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "FEEDBACK");
        FragmentContainerActivity.launch(from, FeedbackFragment.class, bundle);
    }

    @Override
    protected FdbPresenter createPresenter() {
        return new FdbPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.feedback_tv_submit)
    public void onViewClicked() {
        String string = mFeedbackTvContent.getText().toString();
        if (!TextUtils.isEmpty(string)) {
            mPresenter.feedback(string);
        } else {
            UIUtils.showToast("Please input content.");
        }
    }

    @Override
    public Activity get() {
        return mActivity;
    }
}
