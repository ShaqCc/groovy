package com.bunny.groovy.ui.fragment.notify;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.ReportPresenter;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 举报
 * Created by Administrator on 2017/12/16.
 */

public class ReportFragment extends BaseFragment<ReportPresenter> implements ISetFileView {

    @Bind(R.id.et_edittext)
    EditText mContentView;
    private static String mReportId, mBeReportId;


    public static void launch(Activity from, String reportId, String beReportId) {
        mReportId = reportId;
        mBeReportId = beReportId;
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "REPORT");
        FragmentContainerActivity.launch(from, ReportFragment.class, bundle);
    }


    @OnClick(R.id.et_report)
    public void invite() {
        //判断空
        if (UIUtils.isEdittextEmpty(mContentView)) {
            UIUtils.showBaseToast("Please fill in the contents.");
            return;
        }
        mPresenter.reportPerformer(mBeReportId, mReportId, mContentView.getText().toString().trim());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_report_layout;
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public Activity get() {
        return getActivity();
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
    }
}
