package com.bunny.groovy.ui.fragment.venue;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.presenter.VenueOverviewPresenter;
import com.bunny.groovy.ui.fragment.notify.NotificationFragment;
import com.bunny.groovy.ui.fragment.releaseshow.DiscoverMusicianFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ReleaseShowFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ReleaseShowOpportunityFragment;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IOverView;
import com.bunny.groovy.weidget.HeightLightTextView;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明: 演播厅主页
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class VenueOverviewFragment extends BaseFragment<VenueOverviewPresenter> implements IOverView {
    @Bind(R.id.nextshow_layout)
    View nextShowLayout;
    @Bind(R.id.nextshow_iv_head)
    ImageView ivHead;
    @Bind(R.id.nextshow_tv_performerName)
    HeightLightTextView tvName;
    @Bind(R.id.nextshow_tv_performerStar)
    TextView tvStar;
    @Bind(R.id.nextshow_tv_address)
    TextView tvPerformType;
    @Bind(R.id.nextshow_tv_time)
    TextView tvTime;

    private ShowModel model;

    @OnClick(R.id.nextshow_layout)
    public void showDetail() {
        Bundle bundle = new Bundle();
//        bundle.putParcelable(ShowDetailFragment.KEY_SHOW_BEAN, model);
//        ShowDetailFragment.launch(mActivity, bundle);
    }

    @OnClick(R.id.tv_release_show)
    void releaseShow() {
        ReleaseShowFragment.launch(getActivity());
    }

    @OnClick(R.id.tv_notifications)
    public void notifications() {
        NotificationFragment.launch(mActivity);
    }

    @OnClick(R.id.tv_discover_musician)
    void discoverMusician() {
        DiscoverMusicianFragment.launch(getActivity());
    }

    @OnClick(R.id.tv_release_opportunity)
    void releaseOpportunity() {
        ReleaseShowOpportunityFragment.launch(getActivity());
    }

    @Override
    protected VenueOverviewPresenter createPresenter() {
        return new VenueOverviewPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_venue_overview_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void loadData() {
        //请求用户数据
        mPresenter.requestUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        //请求最近一场演出
        mPresenter.requestNextShow(AppCacheData.getPerformerUserModel().getUserID());
    }

    @Override
    public void refreshUI() {
        mPresenter.requestUserData();
        mPresenter.requestNextShow(AppCacheData.getPerformerUserModel().getUserID());
    }


    @Override
    public void initNextView(ShowModel showModel) {
        model = showModel;
        nextShowLayout.setVisibility(View.VISIBLE);
        Glide.with(this).load(showModel.getHeadImg())
                .placeholder(R.mipmap.venue_instead_pic).error(R.mipmap.venue_instead_pic).into(ivHead);
        tvName.setText(showModel.getVenueName());
        tvStar.setText(showModel.getVenueScore());
        tvPerformType.setText(showModel.getPerformType());
        tvTime.setText(showModel.getPerformDate() + " " + showModel.getPerformTime());
    }

    @Override
    public void showEmptyNextShow() {
        nextShowLayout.setVisibility(View.GONE);
    }

    @Override
    public FragmentActivity get() {
        return getActivity();
    }

    @Override
    public void setView(PerformerUserModel userModel) {
    }
}
