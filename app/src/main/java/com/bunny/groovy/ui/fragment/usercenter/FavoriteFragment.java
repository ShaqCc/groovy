package com.bunny.groovy.ui.fragment.usercenter;

import android.app.Activity;

import com.bunny.groovy.adapter.FavoriteListAdapter;
import com.bunny.groovy.base.BaseListFragment;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.presenter.ListPresenter;
import com.bunny.groovy.view.IListPageView;

import java.util.List;

/**
 * 个人中心 收藏fragment
 * Created by Administrator on 2017/12/21.
 */

public class FavoriteFragment extends BaseListFragment<ListPresenter, FavoriteListAdapter> implements IListPageView<List<VenueModel>> {

    @Override
    public Activity get() {
        return getActivity();
    }

    @Override
    public void setView(List<VenueModel> o) {
        setNormalView();
        if (mAdapter == null) {
            mAdapter = new FavoriteListAdapter(o);
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


    @Override
    protected void loadData() {
        mPresenter.getFavoriteList();
    }

    @Override
    protected ListPresenter createPresenter() {
        return new ListPresenter(this);
    }
}
