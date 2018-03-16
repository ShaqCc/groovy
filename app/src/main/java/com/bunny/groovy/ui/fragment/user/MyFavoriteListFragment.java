package com.bunny.groovy.ui.fragment.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.NotifyListAdapter;
import com.bunny.groovy.adapter.UserFavoriteListAdapter;
import com.bunny.groovy.base.BaseListFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.divider.HLineDecoration;
import com.bunny.groovy.model.MusicianModel;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.presenter.ListPresenter;
import com.bunny.groovy.presenter.UserListPresenter;
import com.bunny.groovy.ui.fragment.spotlight.SpotlightFragment;
import com.bunny.groovy.view.IListPageView;

import java.util.List;

/**
 * 我收藏的所有表演者列表
 * <p>
 * Created by Administrator on 2017/12/26.
 */

public class MyFavoriteListFragment extends BaseListFragment<UserListPresenter, UserFavoriteListAdapter> implements IListPageView<List<MusicianModel>> {

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "MY FAVORITE");
        FragmentContainerActivity.launch(from, MyFavoriteListFragment.class, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mRecyclerView.addItemDecoration(new HLineDecoration(mActivity, LinearLayoutManager.VERTICAL, R.drawable.shape_item_divider_line));
    }

    @Override
    protected void loadData() {}

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getFavoriteList();
    }

    @Override
    protected UserListPresenter createPresenter() {
        return new UserListPresenter(this);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setView(List<MusicianModel> o) {
        setNormalView();
        if (mAdapter == null) {
            mAdapter = new UserFavoriteListAdapter(o);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(o);
        }
    }

    @Override
    public void setNormalView() {
        setPageState(PageState.NORMAL);
    }

    @Override
    public void setNodata() {
        setPageState(PageState.NODATA);
    }

    @Override
    public void setError() {
        setPageState(PageState.ERROR);
    }
}
