package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.VenueListAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.presenter.SearchVenueListPresenter;
import com.bunny.groovy.view.ISearchVenueList;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;

import static android.app.Activity.RESULT_OK;

/****************************************
 * 功能说明: 搜索音乐厅页面
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class SearchVenueFragment extends BaseFragment<SearchVenueListPresenter> implements ISearchVenueList {

    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.venue_list)
    RecyclerView mRecyclerView;
    private VenueListAdapter mVenueListAdapter;

    public static void launchForResult(Activity activity, Bundle bundle, int requestCode) {
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SEARCH VENUE");
        FragmentContainerActivity.launchForResult(activity, SearchVenueFragment.class, bundle, requestCode);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void initListener() {
        super.initListener();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s))
                    mPresenter.searchVenue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected SearchVenueListPresenter createPresenter() {
        return new SearchVenueListPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.framgent_search_venue_list_layout;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.release_menu, menu);
    }

    int PLACE_PICKER_REQUEST = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        search();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 进入google自动搜索地址页面
     */
    public void search(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(mActivity), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //地点不为空，setData，finish自己
                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());
                KLog.a("Place:"+toastMsg);
                mActivity.setResult(RESULT_OK,data);
                mActivity.finish();
            }
        }
    }

    @Override
    public FragmentActivity get() {
        return getActivity();
    }

    @Override
    public void setListView(List<VenueModel> list) {
        if (mVenueListAdapter == null) {
            mVenueListAdapter = new VenueListAdapter(list, etSearch.getText().toString().trim());
            mRecyclerView.setAdapter(mVenueListAdapter);
        } else mVenueListAdapter.refresh(list, etSearch.getText().toString().trim());
    }
}
