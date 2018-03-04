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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.MusicianListAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.presenter.SearchMusicianListPresenter;
import com.bunny.groovy.view.ISearchMusicianList;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;

import static android.app.Activity.RESULT_OK;

/****************************************
 * 功能说明: 搜索表演者页面
 ****************************************/

public class SearchMusicianFragment extends BaseFragment<SearchMusicianListPresenter> implements ISearchMusicianList {

    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.venue_list)
    RecyclerView mRecyclerView;
    private MusicianListAdapter mVenueListAdapter;

    public static void launchForResult(Activity activity, Bundle bundle, int requestCode) {
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SEARCH MUSICIAN");
        FragmentContainerActivity.launchForResult(activity, SearchMusicianFragment.class, bundle, requestCode);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        etSearch.setHint("Search the musician by name or phone");
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
                    mPresenter.searchPerformer(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected SearchMusicianListPresenter createPresenter() {
        return new SearchMusicianListPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.framgent_search_venue_list_layout;
    }

    @Override
    protected void loadData() {

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
    public void search() {
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
                KLog.a("Place:" + toastMsg);
                mActivity.setResult(RESULT_OK, data);
                mActivity.finish();
            }
        }
    }

    @Override
    public FragmentActivity get() {
        return getActivity();
    }

    @Override
    public void setListView(List<PerformerUserModel> list) {
        if (mVenueListAdapter == null) {
            mVenueListAdapter = new MusicianListAdapter(list, etSearch.getText().toString().trim());
            mRecyclerView.setAdapter(mVenueListAdapter);
        } else mVenueListAdapter.refresh(list, etSearch.getText().toString().trim());
    }
}
