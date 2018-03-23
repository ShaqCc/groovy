package com.bunny.groovy.ui.fragment.apply;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.bunny.groovy.R;
import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.ReportPresenter;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.SharedPreferencesUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 举报演出厅
 * Created by Administrator on 2017/12/16.
 */

public class ReportVenueFragment extends BaseFragment<BasePresenter> {

    @Bind(R.id.et_edittext)
    EditText mContentView;
    private static String venueID;


    public static void launch(Activity from, String reportId) {
        venueID = reportId;
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "REPORT");
        FragmentContainerActivity.launch(from, ReportVenueFragment.class, bundle);
    }


    @OnClick(R.id.et_report)
    public void invite() {
        //判断空
        if (UIUtils.isEdittextEmpty(mContentView)) {
            UIUtils.showBaseToast("Please fill in the contents.");
            return;
        }
        report();
    }

    private void report() {
        ApiRetrofit.getInstance().getApiService()
                .reportVenue(venueID, AppCacheData.getPerformerUserModel().getUserID(), mContentView.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultResponse<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                        if (response.isSuccess()) {
                            UIUtils.showBaseToast("Report successfully.");
                        } else {
                            UIUtils.showBaseToast("Report failed.");
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected ReportPresenter createPresenter() {
        return null;
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
}
