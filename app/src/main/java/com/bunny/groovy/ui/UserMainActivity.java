package com.bunny.groovy.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.manager.LoginBlock;
import com.bunny.groovy.ui.fragment.user.MyFavoriteListFragment;
import com.bunny.groovy.ui.fragment.user.MyHistoryListFragment;
import com.bunny.groovy.ui.fragment.user.UserMainFragment;
import com.bunny.groovy.ui.fragment.usercenter.SettingsFragment;
import com.bunny.groovy.ui.fragment.usercenter.UserDataFragment;
import com.bunny.groovy.ui.fragment.wallet.WalletFragment;
import com.bunny.groovy.ui.login.LoginActivity;
import com.bunny.groovy.utils.AppCacheData;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_list)
    TextView mNavList;
    @Bind(R.id.ib_filter)
    ImageButton mFilterButton;
    @Bind(R.id.mydrawerlayout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    private boolean mIsMap = true;
    private BaseFragment mMapFragment;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.tv_list)
    public void switchFragment() {
        mIsMap = !mIsMap;
        mNavList.setText(mIsMap ? "LIST" : "MAP");
        mFilterButton.setVisibility(mIsMap ? View.GONE : View.VISIBLE);
        if (mMapFragment instanceof UserMainFragment) {
            ((UserMainFragment) mMapFragment).switchListOrMap(mIsMap);
        }
    }

    @OnClick(R.id.ib_filter)
    public void ib_filter() {
        if (mMapFragment instanceof UserMainFragment) {
            ((UserMainFragment) mMapFragment).mapFilter();
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_main_layout;
    }

    @Override
    public void initData() {
        intiFragments();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    private void intiFragments() {

        mMapFragment = new UserMainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mMapFragment).commit();
    }

    @Override
    public void initView() {
        super.initView();
        mToolbar.setNavigationIcon(R.drawable.icon_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });
        ViewGroup.LayoutParams params = mNavigationView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
        mNavigationView.setLayoutParams(params);
        View headerView = mNavigationView.getHeaderView(0);
        CircleImageView mHeadImage = headerView.findViewById(R.id.nav_head);
        TextView mUserName = headerView.findViewById(R.id.nav_name);
        Glide.with(this).load(AppCacheData.getPerformerUserModel().getHeadImg()).into(mHeadImage);
        mUserName.setText(AppCacheData.getPerformerUserModel().getUserName());
        mHeadImage.setOnClickListener(this);
        headerView.findViewById(R.id.nav_wallet).setOnClickListener(this);
        headerView.findViewById(R.id.nav_history).setOnClickListener(this);
        headerView.findViewById(R.id.nav_favortie).setOnClickListener(this);
        headerView.findViewById(R.id.nav_switch).setOnClickListener(this);
        headerView.findViewById(R.id.nav_settings).setOnClickListener(this);
    }

    private void refresh() {
        if (mNavigationView != null) {
            View headerView = mNavigationView.getHeaderView(0);
            CircleImageView mHeadImage = headerView.findViewById(R.id.nav_head);
            TextView mUserName = headerView.findViewById(R.id.nav_name);
            Glide.with(this).load(AppCacheData.getPerformerUserModel().getHeadImg())
                    .placeholder(R.drawable.icon_default_photo).into(mHeadImage);
            mUserName.setText(AppCacheData.getPerformerUserModel().getUserName());
        }

    }

    public static void start(Context outerContext) {
        Intent intent = new Intent(outerContext, UserMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        outerContext.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMapFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_head:
                UserDataFragment.launch(this);
                break;
            case R.id.nav_wallet:
                WalletFragment.launch(this);
                break;
            case R.id.nav_history:
                MyHistoryListFragment.launch(this);
                break;
            case R.id.nav_favortie:
                MyFavoriteListFragment.launch(this);
                break;
            case R.id.nav_switch:
                startActivity(new Intent(this, LoginActivity.class).putExtra("switch_type", true));
                finish();
                break;
            case R.id.nav_settings:
                SettingsFragment.launch(this);
                break;
        }
    }
}
