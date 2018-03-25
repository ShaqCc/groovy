package com.bunny.groovy.ui.fragment.notify;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.VenueOpportunityDetailAdapter;
import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.api.ApiService;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.VenueOpportunityModel;
import com.bunny.groovy.utils.UIUtils;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueOpportunityDetailFragment extends BaseFragment {

    public static String KEY_SHOW_BEAN = "key_show_bean";
    private static VenueOpportunityModel sModel;
    private int mPosition = 0;
    private List<VenueOpportunityModel.ApplyList> mList;

    public static void launch(Activity activity, Bundle bundle) {
        sModel = bundle.getParcelable(KEY_SHOW_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "DETAILS");
        FragmentContainerActivity.launch(activity, VenueOpportunityDetailFragment.class, bundle);
    }

    @Bind(R.id.base_recyclerview)
    RecyclerView mRecycleView;
    @Bind(R.id.opportunity_date)
    TextView mOpportunityDate;
    @Bind(R.id.opportunity_choose_layout)
    LinearLayout mChooseLayout;
    @Bind(R.id.opportunity_tv_choose)
    TextView mTvChoose;
    @Bind(R.id.venue_des)
    TextView mTvDes;


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        if (sModel != null) {
            String status = sModel.getOpportunityState();
            mChooseLayout.setVisibility(TextUtils.equals("0", status) ? View.VISIBLE : View.GONE);
            mList = sModel.getApplyList();
            mTvChoose.setText(mList.size() > 0 ? "CHOOSE" : "No one is involved yet.");
            mTvDes.setText(sModel.getPerformDesc());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Show Opportunity on ").append(sModel.getPerformDate()).append(" ").append(sModel.getPerformTime());
            mOpportunityDate.setText(stringBuilder.toString());
            mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            final VenueOpportunityDetailAdapter adapter = new VenueOpportunityDetailAdapter(mList, !TextUtils.equals(status, "0"));
            adapter.setOnSelectListener(new VenueOpportunityDetailAdapter.OnSelectListener() {
                @Override
                public void select(int position) {
                    adapter.selectPosition(position);
                    mPosition = position;
                }
            });
            mRecycleView.setAdapter(adapter);
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_opportunity_adapter_layout;
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.opportunity_tv_choose)
    public void reject() {//opportunity-选择表演者
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        if (mList.size() != 0 && mList.size() >= mPosition) {
            apiService.choosePerformer(mList.get(mPosition).getApplyID())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResultResponse<Object>>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            UIUtils.showBaseToast(e.toString());
                            KLog.d(e.toString());
                        }

                        @Override
                        public void onNext(ResultResponse<Object> response) {
                            if (response.success) {
                                UIUtils.showBaseToast("Choose successfully.");
                                mActivity.finish();
                            } else {
                                UIUtils.showBaseToast("Choose failed.");
                            }
                        }
                    });
        }
    }
}
