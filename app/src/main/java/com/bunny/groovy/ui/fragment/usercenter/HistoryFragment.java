package com.bunny.groovy.ui.fragment.usercenter;

import android.app.Activity;

import com.bunny.groovy.adapter.ShowHistoryAdapter;
import com.bunny.groovy.base.BaseListFragment;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.presenter.ListPresenter;
import com.bunny.groovy.view.IListPageView;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/22.
 ****************************************/

public class HistoryFragment extends BaseListFragment<ListPresenter, ShowHistoryAdapter> implements IListPageView<List<ShowModel>> {
    @Override
    public Activity get() {
        return getActivity();
    }

    @Override
    public void setView(List<ShowModel> o) {
        setNormalView();
        if (mAdapter == null) {
            mAdapter = new ShowHistoryAdapter(o);
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
        mPresenter.getHistoryList();
    }

    @Override
    protected ListPresenter createPresenter() {
        return new ListPresenter(this);
    }
}
