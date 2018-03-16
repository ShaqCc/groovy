package com.bunny.groovy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.MainTabAdapter;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.ui.fragment.MeFragment;
import com.bunny.groovy.ui.fragment.OverviewFragment;
import com.bunny.groovy.ui.fragment.ScheduleFragment;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.weidget.BottomBarItem;
import com.bunny.groovy.weidget.BottomBarLayout;
import com.bunny.groovy.weidget.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.vp_content)
    NoScrollViewPager noScrollViewPager;

    @Bind(R.id.bottom_bar)
    BottomBarLayout bottomBarLayout;

    @Bind(R.id.main_rl_titlebar)
    RelativeLayout actionBarLayout;

    @Bind(R.id.main_tv_title)
    TextView tvTitle;

    private List<BaseFragment> mFragments;
    private MainTabAdapter mTabAdapter;

    /**
     * @return 获取标题布局
     */
    public RelativeLayout getActionBarLayout() {
        return actionBarLayout;
    }

    /**
     * @return 标题
     */
    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTitleVisible(int visible) {
        actionBarLayout.setVisibility(visible);
    }

    /**
     * 设置页面标题
     *
     * @param title
     */
    public void setPageTitle(String title) {
        if (Utils.isEnglish(title))
            tvTitle.setText(title.toUpperCase());
        else tvTitle.setText(title);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        intiFragments();
    }

    @Override
    public void initListener() {
        super.initListener();
        mTabAdapter = new MainTabAdapter(mFragments, getSupportFragmentManager());
        noScrollViewPager.setAdapter(mTabAdapter);
        noScrollViewPager.setOffscreenPageLimit(mFragments.size());
        bottomBarLayout.setViewPager(noScrollViewPager);
        //设置条目点击的监听
        bottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int position) {
                switch (position) {
                    case 0:
                        setTitleVisible(View.VISIBLE);
                        mFragments.get(0).refreshUI();
                        break;
                    case 1:
                        setTitleVisible(View.VISIBLE);
                        setPageTitle("SCHEDULE");
                        mFragments.get(1).refreshUI();
                        break;
                    case 2:
                        setTitleVisible(View.GONE);
                        mFragments.get(2).refreshUI();
                        break;
                }
            }
        });
        //eventbus
        registerEventBus(this);
    }

    /**
     * 用户登出
     *
     * @param code
     */
    @Subscribe
    public void onLogout(String code) {
        if (AppConstants.EVENT_LOGIN_OUT.equals(code))
            finish();
    }

    private void intiFragments() {
        mFragments = new ArrayList<>(3);
        mFragments.add(new OverviewFragment());
        mFragments.add(new ScheduleFragment());
        mFragments.add(new MeFragment());

    }

    /**
     * 启动本页面
     *
     * @param activity
     */
    public static void launch(Context activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        for (Activity ac :
                mActivities) {
            if (ac instanceof MainActivity) {

            } else ac.finish();
        }
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }
}
