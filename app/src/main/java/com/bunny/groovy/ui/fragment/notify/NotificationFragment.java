package com.bunny.groovy.ui.fragment.notify;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.UserCenterAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BaseListFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.weidget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 用户通知界面
 * <p>
 * Created by Administrator on 2017/12/26.
 */

public class NotificationFragment extends BaseFragment {

    @Bind(R.id.notification_pagetabstrip)
    SlidingTabLayout slidingTabLayout;

    @Bind(R.id.notification_viewpager)
    ViewPager viewPager;

    private int mType;

    private String[] titleArray = new String[]{"OPPORTUNITY", "INVITE", "APPLY"};
    private String[] titleAVenueArray = new String[]{"OPPORTUNITY", "APPLY", "INVITE"};

    private List<BaseListFragment> mFragments = new ArrayList<>();

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "NOTIFICATIONS");
        FragmentContainerActivity.launch(from, NotificationFragment.class, bundle);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_notification_lalyout;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);

    }

    @Override
    public void initData() {
         mType = Integer.parseInt(AppCacheData.getPerformerUserModel().getUserType());
         if(mType == 2){
             //设置viewpager
             VenueNotify1ListFragment opp = new VenueNotify1ListFragment();
             mFragments.add(opp);

             VenueNotify2ListFragment invite = new VenueNotify2ListFragment();
             mFragments.add(invite);

             VenueNotify3ListFragment release = new VenueNotify3ListFragment();
             mFragments.add(release);
         }else{
             //设置viewpager
             NotifyListFragment opp = new NotifyListFragment();
             Bundle args1 = new Bundle();
             args1.putInt(NotifyListFragment.KEY_TYPE,0);
             opp.setArguments(args1);
             mFragments.add(opp);

             NotifyListFragment invite = new NotifyListFragment();
             Bundle args2 = new Bundle();
             args2.putInt(NotifyListFragment.KEY_TYPE,1);
             invite.setArguments(args2);
             mFragments.add(invite);

             NotifyListFragment release = new NotifyListFragment();
             Bundle args3 = new Bundle();
             args3.putInt(NotifyListFragment.KEY_TYPE,2);
             release.setArguments(args3);
             mFragments.add(release);
         }

        UserCenterAdapter adapter = new UserCenterAdapter(mFragments, mType == 2 ? titleAVenueArray : titleArray, getChildFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabView(R.layout.custorm_tab_layout,R.id.tv_tab);
        slidingTabLayout.setSelectedIndicatorColors(getActivity().getResources().getColor(R.color.white));
        slidingTabLayout.setTitleTextColor(getResources().getColor(R.color.white), getResources().getColor(R.color.white));
        slidingTabLayout.setTabStripWidth(UIUtils.getScreenWidth() / 3);
        slidingTabLayout.setSelectedIndicatorColors(getActivity().getResources().getColor(R.color.white));
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFragments.get(position).loadAgain();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slidingTabLayout.setViewPager(viewPager);
    }
}
