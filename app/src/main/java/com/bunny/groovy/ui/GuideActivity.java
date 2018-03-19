package com.bunny.groovy.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.base.BaseApp;
import com.bunny.groovy.base.BasePresenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 引导页
 * Created by wjy on 2018/03/09.
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    @Bind(R.id.btn_start)
    Button mStartButton;

    private MyViewPagerAdapter myViewPagerAdapter;
    private static final int[] LAYOUT_RES;

    static {
        LAYOUT_RES = new int[]{R.drawable.login_bg, R.drawable.login_bg, R.drawable.login_bg};
    }

    public static void launch(Context activity) {
        Intent intent = new Intent(BaseApp.getContext(), GuideActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApp.getContext().startActivity(intent);
        for (int i = 0; i < mActivities.size(); i++) {
            if (!(mActivities.get(i) instanceof GuideActivity))
                mActivities.get(i).finish();
        }
    }

    @OnClick(R.id.btn_start)
    void start() {
        RoleChooseActivity.launch(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_guide_layout;
    }

    @Override
    public void initView() {
        super.initView();
        myViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(myViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mStartButton.setVisibility(position == LAYOUT_RES.length - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class MyViewPagerAdapter extends PagerAdapter {

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(LAYOUT_RES[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return LAYOUT_RES.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}