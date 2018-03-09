package com.bunny.groovy.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.presenter.OverviewPresenter;
import com.bunny.groovy.ui.MainActivity;
import com.bunny.groovy.ui.fragment.notify.NotificationFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ExploreShowFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ReleaseShowFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ShowDetailFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IOverView;
import com.bunny.groovy.weidget.HeightLightTextView;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明: 表演者主页
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class OverviewFragment extends BaseFragment<OverviewPresenter> implements IOverView {
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
    @Bind(R.id.overview_fl_pan)
    FrameLayout flPan;

    private ShowModel model;

    @OnClick(R.id.nextshow_layout)
    public void showDetail() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShowDetailFragment.KEY_SHOW_BEAN, model);
        ShowDetailFragment.launch(mActivity, bundle);
    }

    @OnClick(R.id.overview_tv_release_show)
    void releaseShow() {
        ReleaseShowFragment.launch(getActivity());
    }

    @OnClick(R.id.overview_tv_notification)
    public void notifications() {
        NotificationFragment.launch(mActivity);
    }

    @OnClick(R.id.overview_tv_explore_show)
    void exploreShow() {
        ExploreShowFragment.launch(getActivity());
    }

    @Override
    protected OverviewPresenter createPresenter() {
        return new OverviewPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_overview_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
//        int screenWidth = UIUtils.getScreenWidth();
//        ViewGroup.LayoutParams params = flPan.getLayoutParams();
//        params.width = (int) (screenWidth*0.9);
//        params.height = (int) (screenWidth*0.9);
//        flPan.setLayoutParams(params);
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
        mPresenter.requestNextShow();
    }

    @Override
    public void refreshUI() {
        mPresenter.requestUserData();
        mPresenter.requestNextShow();
    }

    @Override
    public void initNextView(ShowModel showModel) {
        model = showModel;
        nextShowLayout.setVisibility(View.VISIBLE);
        Glide.with(this).load(showModel.getHeadImg())
                .placeholder(R.drawable.venue_instead_pic).error(R.drawable.venue_instead_pic).into(ivHead);
        tvName.setText(showModel.getVenueName());
        tvStar.setText(Utils.getStar(showModel.getVenueScore()));
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
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).setPageTitle(userModel.getStageName());
        }
    }
}
