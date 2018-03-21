package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/****************************************
 * 功能说明:  搜索演出地址页面
 *
 * Author: Created by bayin on 2017/12/25.
 ****************************************/

public class SearchAddFragment extends BaseFragment {

    @Bind(R.id.search_root)
    FrameLayout mContainer;

    @OnClick(R.id.et_search)
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
    private  int PLACE_PICKER_REQUEST = 1;

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SEARCH");
        FragmentContainerActivity.launch(from, SearchAddFragment.class, bundle);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_search_layout;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    public void afterAttach() {
//        SupportPlaceAutocompleteFragment autocompleteFragment = new SupportPlaceAutocompleteFragment();
//        getChildFragmentManager().beginTransaction().add(R.id.search_root, autocompleteFragment).commit();
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                KLog.d(place.getLatLng());
//            }
//
//            @Override
//            public void onError(Status status) {
//                KLog.a("error  --"+status.getStatusMessage());
//            }
//        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(mActivity, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}

