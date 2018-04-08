package com.bunny.groovy.ui.fragment.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.UserMainListAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.divider.HLineDecoration;
import com.bunny.groovy.model.LocationModel;
import com.bunny.groovy.model.PerformDetail;
import com.bunny.groovy.model.UserMainModel;
import com.bunny.groovy.presenter.UserListPresenter;
import com.bunny.groovy.ui.fragment.apply.UserFilterFragment;
import com.bunny.groovy.ui.fragment.releaseshow.UserShowDetailFragment;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IUserMainView;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 普通用户首页
 * <p>
 * Created by Administrator on 2017/12/17.
 */

public class UserMainFragment extends BaseFragment<UserListPresenter> implements
        OnMapReadyCallback, TextWatcher,
        IUserMainView<UserMainModel> {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;
    int FILTER_REQUEST_CODE = 1;
    int PLACE_PICKER_REQUEST = 2;
    int OPEN_GPS_REQUEST_CODE = 1024;

    private GoogleMap mGoogleMap;
    private List<PerformDetail> performDetailList = new ArrayList<>();
    private PerformDetail mCurrentBean;//当前选中的演出
    private List<Marker> mMarkerList = new ArrayList<>();
    private String mDistance = "25";//距离默认50mi
    private String mStartDate, mEndDate;//表演时间
    private String mVenueType;//表演厅类型（多选，英文逗号隔开）
    private String mPerformType;//表演类型（多选，英文逗号隔开）
    private Location mLastLocation;
    private boolean isMarkerShowing = false;

    @Bind(R.id.map_fl_marker_layout)
    FrameLayout mMarkerLayout;
    @Bind(R.id.marker_tv_date)
    TextView mTvDate;
    @Bind(R.id.marker_iv_head)
    ImageView mHeadImg;
    @Bind(R.id.marker_tv_score)
    TextView mTvScore;
    @Bind(R.id.marker_tv_venue_name)
    TextView mTvName;
    @Bind(R.id.marker_tv_venue_address)
    TextView mTvadd;
    @Bind(R.id.marker_tv_time)
    TextView mTvTime;
    @Bind(R.id.marker_tv_style)
    TextView mTvStyle;
    @Bind(R.id.marker_tv_distance)
    TextView mTvDistance;
    @Bind(R.id.opp_recyclerview)
    RecyclerView mRecyclerView;
    private UserMainListAdapter mAdapter;
    @Bind(R.id.map_layout)
    RelativeLayout mapLayout;
    @Bind(R.id.map_et_search)
    EditText etSearch;
    @Bind(R.id.map_search_bar)
    View mapSearchBar;

    private FusedLocationProviderClient mLocationClient;
    private LocationRequest mLocationRequest;
    private String mKeyword;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!TextUtils.isEmpty(mKeyword)) {
                String location = null;
                if (mLastLocation != null) {
                    location = String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude());
                } else {
                    //定位失败默认使用美国旧金山的代码
                    location = String.valueOf(AppConstants.DEFAULT_LATITUDE) + "," + String.valueOf(AppConstants.DEFAULT_LONGITUDE);
                }
                mPresenter.search(location, mKeyword);
            } else {

            }
        }
    };

    @OnClick(R.id.marker_tv_venue_detail)
    public void venueDetail() {
        UserShowDetailFragment.launch(mActivity, mCurrentBean, false);
    }


    @OnClick(R.id.marker_tv_go)
    public void go() {
        Utils.openWebGoogleNavi(getActivity(), mCurrentBean.getVenueLatitude(), mCurrentBean.getVenueLongitude());
        UserListPresenter.addPerformViewer(mCurrentBean.getPerformID());
    }

    @OnClick(R.id.map_filter)
    public void mapFilter() {
        filter();
    }


    /**
     * 跳转到条件页面
     */
    private void filter() {
        Bundle bundle = new Bundle();
        bundle.putInt(UserFilterFragment.KEY_DISTANCE, Utils.parseInt(mDistance));
        bundle.putString(UserFilterFragment.KEY_START_TIME, mStartDate);
        bundle.putString(UserFilterFragment.KEY_END_TIME, mEndDate);
        bundle.putString(UserFilterFragment.KEY_VENUE_TYPE, mVenueType);
        bundle.putString(UserFilterFragment.KEY_PERFORM_TYPE, mPerformType);
        UserFilterFragment.launchForResult(mActivity, bundle, FILTER_REQUEST_CODE);
        hideMarkLayout();
    }

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "EXPLORE SHOW OPPORTUNITY");
        FragmentContainerActivity.launch(from, UserMainFragment.class, bundle);
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mMarkerLayout.setVisibility(View.GONE);
        etSearch.addTextChangedListener(this);
        //初始化map
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.map_container, supportMapFragment, "map_fragment").commit();
        supportMapFragment.getMapAsync(this);

        checkPermission(true);
//        updateCurrentLocation();
    }

    private void checkPermission(boolean needRequestPermission) {
        //检查权限
        if (ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            if (needRequestPermission)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            checkLocationSettings();
        }
    }

    /**
     * 检查位置服务是否开启
     */
    protected void checkLocationSettings() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(get());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(get(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                setUpLocationRequest();
            }
        });

        task.addOnFailureListener(get(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(get(), OPEN_GPS_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    /**
     * 初始化位置请求，如果得到位置为空，启动位置更新服务
     */
    private void setUpLocationRequest() {
        mLocationClient = LocationServices.getFusedLocationProviderClient(get());
        mLocationClient.getLastLocation().addOnSuccessListener(get(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.w("获取位置success", location.getLatitude() + "   " + location.getLongitude());
                    mLastLocation = location;
                    //更新
                    updateCurrentLocation();
                } else {
                    startLocationUpdates();
                }
            }
        });
    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (int i = locationResult.getLocations().size() - 1; i > 0; i--) {
                if (locationResult.getLocations().get(i) != null) {
                    mLastLocation = locationResult.getLocations().get(i);
                    Log.d("callback获取的最新位置", mLastLocation.getLatitude() + "  " + mLastLocation.getLongitude());
                    //更新地图位置
                    updateCurrentLocation();
                    //停止位置监听
                    stopLocationUpdates();
                    break;
                }
            }
        }
    };

    /**
     * 启动位置更新监听
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
        if (mGoogleMap != null)
            mGoogleMap.setMyLocationEnabled(true);
    }

    /**
     * 停止位置更新监听
     */
    private void stopLocationUpdates() {
        if (mLastLocation != null)
            mLocationClient.removeLocationUpdates(locationCallback);
    }


    /**
     * 设置marker窗口的信息
     *
     * @param bean
     */
    private void setMarkerData(PerformDetail bean) {
        mCurrentBean = bean;
        mTvDate.setText(bean.getPerformDate());
        mTvName.setText(bean.getPerformerName() + "@" + bean.getVenueName());
        mTvadd.setText(bean.getVenueAddress());
        mTvTime.setText(bean.getPerformTime());
        mTvDistance.setText(bean.getDistance() + "mi");
        mTvStyle.setText(bean.getPerformType());
        mTvScore.setText(Utils.getStar(bean.getVenueScore()));
        Glide.with(mActivity).load(bean.getPerformerImg()).error(R.drawable.icon_default_photo).into(mHeadImg);
    }

    private int lastMarkerSelected = -2;//上一个显示的marker index

    @Override
    public void onMapReady(final GoogleMap map) {
        mGoogleMap = map;
        if (ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                mActivity, R.raw.map_style));

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMarkerLayout.getVisibility() == View.VISIBLE) {
                    try {
                        mMarkerLayout.setVisibility(View.GONE);
                        mMarkerList.get(lastMarkerSelected).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_show));
                    } catch (Exception e) {
                    }
                }
            }
        });
        //点击监听
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                PerformDetail info = (PerformDetail) marker.getTag();
                int clickedIndex = mMarkerList.indexOf(marker);
                if (lastMarkerSelected == clickedIndex) {
                    //点击的是当前marker
                    if (info != null) {
                        if (isMarkerShowing) {
                            mMarkerLayout.setVisibility(View.GONE);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_show));
                        } else {
                            setMarkerData(info);
                            mMarkerLayout.setVisibility(View.VISIBLE);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_show_selected));
                        }
                        isMarkerShowing = !isMarkerShowing;
                    }
                } else {
                    //点击的是其他marker
                    if (info != null) {
                        setMarkerData(info);
                        mMarkerLayout.setVisibility(View.VISIBLE);
                        isMarkerShowing = true;
                        //把上个marker的icon设置小图标
                        if (lastMarkerSelected >= 0)
                            mMarkerList.get(lastMarkerSelected).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_show));
                        //设置当前点击的marker大图片
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_show_selected));
                    }
                }
                lastMarkerSelected = clickedIndex;
                return false;
            }
        });
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
//        updateLoc();
        //设置当前位置
        updateCurrentLocation();
    }

    /**
     * 设置当前位置
     */
    private void updateCurrentLocation() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            LatLng myLoc = null;
            if (mLastLocation != null) {
                myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } else {
                //定位不到使用旧金山默认坐标
                myLoc = new LatLng(AppConstants.DEFAULT_LATITUDE, AppConstants.DEFAULT_LONGITUDE);
            }
            mGoogleMap.addMarker(new MarkerOptions().position(myLoc)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                    .draggable(true));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
        }
        //请求机会数据
        requestAroundList();
    }

    /**
     * 请求周边数据
     */
    private void requestAroundList() {
        HashMap<String, String> map = new HashMap();

        map.put("userID", AppCacheData.getPerformerUserModel().getUserID());
        if (mLastLocation != null) {
            map.put("lon", String.valueOf(mLastLocation.getLongitude()));
            map.put("lat", String.valueOf(mLastLocation.getLatitude()));
        } else {
            //定位失败默认使用美国旧金山的代码
            map.put("lon", String.valueOf(AppConstants.DEFAULT_LONGITUDE));
            map.put("lat", String.valueOf(AppConstants.DEFAULT_LATITUDE));
        }
        map.put("distance", mDistance);
        if (!TextUtils.isEmpty(mVenueType)) map.put("venueType", mVenueType);
        if (!TextUtils.isEmpty(mStartDate)) map.put("startDate", mStartDate);
        if (!TextUtils.isEmpty(mEndDate)) map.put("endDate", mEndDate);
        if (!TextUtils.isEmpty(mPerformType)) map.put("performType", mPerformType);
        mPresenter.getPerformList(map);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkLocationSettings();
                } else {
                    UIUtils.showToast("Positioning function is not available, please go to set to open the positioning.");
                    updateCurrentLocation();
                }
            }
        }
    }

    private boolean showMap = true;//显示的形式是列表还是map

    /**
     * 清空地图是原有的标记，添加当前位置
     */
    private void resetMap() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            LatLng myLoc = null;
            if (mLastLocation != null) {
                KLog.a("当前位置：" + mLastLocation.getLatitude() + " -- " + mLastLocation.getLongitude());
                myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } else {
                //定位不到使用旧金山默认坐标
                myLoc = new LatLng(AppConstants.DEFAULT_LATITUDE, AppConstants.DEFAULT_LONGITUDE);
            }
            mGoogleMap.addMarker(new MarkerOptions().position(myLoc)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                    .draggable(true));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
        }
    }


    public void switchListOrMap(boolean isMap) {
        if (isMap) {
            showMap = true;
            mRecyclerView.setVisibility(View.GONE);
            mapLayout.setVisibility(View.VISIBLE);
            mapSearchBar.setVisibility(View.VISIBLE);
        } else {
            showMap = false;
            mRecyclerView.setVisibility(View.VISIBLE);
            mapLayout.setVisibility(View.GONE);
            mapSearchBar.setVisibility(View.GONE);
            hideMarkLayout();
        }
    }

    private boolean mFirst;

    @Override
    public void onResume() {
        super.onResume();
        if (mFirst) mFirst = false;
        else if (mLastLocation == null) checkPermission(false);
    }

    public void hideMarkLayout() {
        if (lastMarkerSelected >= 0) {
            mMarkerList.get(lastMarkerSelected).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_show));
            lastMarkerSelected = -2;
        }
        mMarkerLayout.setVisibility(View.INVISIBLE);
        isMarkerShowing = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore_menu_filter:
                filter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            HashMap<String, String> map = new HashMap<>();
            map.put("userID", AppCacheData.getPerformerUserModel().getUserID());
            String dis = data.getStringExtra(UserFilterFragment.KEY_DISTANCE);
            if (!TextUtils.isEmpty(dis)) {
                mDistance = dis;
            }
            map.put("distance", mDistance);
            String startDate1 = data.getStringExtra(UserFilterFragment.KEY_START_TIME);
            if (!TextUtils.isEmpty(startDate1)) {
                mStartDate = startDate1;
                map.put("startDate", mStartDate);
            }
            String endDate1 = data.getStringExtra(UserFilterFragment.KEY_END_TIME);
            if (!TextUtils.isEmpty(endDate1)) {
                mEndDate = endDate1;
                map.put("endDate", mEndDate);
            }
            String venueType1 = data.getStringExtra(UserFilterFragment.KEY_VENUE_TYPE);
            if (!TextUtils.isEmpty(venueType1)) {
                mVenueType = venueType1;
                map.put("venueType", mVenueType);
            }
            String performType1 = data.getStringExtra(UserFilterFragment.KEY_PERFORM_TYPE);
            if (!TextUtils.isEmpty(performType1)) {
                mPerformType = performType1;
                map.put("performType", mPerformType);
            }
            if (mLastLocation != null) {
                map.put("lon", String.valueOf(mLastLocation.getLongitude()));
                map.put("lat", String.valueOf(mLastLocation.getLatitude()));
            } else {
                map.put("lon", String.valueOf(AppConstants.DEFAULT_LONGITUDE));
                map.put("lat", String.valueOf(AppConstants.DEFAULT_LATITUDE));
            }
            mPresenter.getPerformList(map);
        } else if (requestCode == OPEN_GPS_REQUEST_CODE) {
            //请求开启gps服务成功，开始请求当前位置信息
            setUpLocationRequest();
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //选取地址
                Place place = PlacePicker.getPlace(data, get());
                String toastMsg = String.format("Place: %s", place.getName());
                KLog.a("Place:" + toastMsg);
                etSearch.setText(place.getName());
                //更新当前位置
                mLastLocation.setLatitude(place.getLatLng().latitude);
                mLastLocation.setLongitude(place.getLatLng().longitude);
                updateCurrentLocation();
            } else {
                etSearch.setText("");
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected UserListPresenter createPresenter() {
        return new UserListPresenter(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public Activity get() {
        return getActivity();
    }

    @Override
    public void setView(UserMainModel userMainModel) {
        performDetailList.clear();
        mMarkerList.clear();
        performDetailList = userMainModel.allPerformList;

        //判断是list / map
        if (showMap) {
            mapLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            //列表显示
            mapLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }


        //设置当前位置
        resetMap();
        if (performDetailList != null) {
            //设置marker
            isMarkerShowing = false;
            mMarkerLayout.setVisibility(View.GONE);
            if (mGoogleMap != null) {
                LatLng loc;
                for (int i = 0; i < performDetailList.size(); i++) {
                    PerformDetail model = performDetailList.get(i);
                    loc = new LatLng(Double.parseDouble(model.getVenueLatitude()), Double.parseDouble(model.getVenueLongitude()));
                    Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(loc)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_show)));
                    marker.setTag(model);
                    mMarkerList.add(marker);
                }
            }
        }
        //列表数据
        if (mAdapter == null) {
            mAdapter = new UserMainListAdapter(userMainModel.allPerformList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.addItemDecoration(new HLineDecoration(mActivity, HLineDecoration.VERTICAL_LIST,
                    R.drawable.shape_item_divider_line));
            mRecyclerView.setAdapter(mAdapter);
        } else mAdapter.refresh(userMainModel.allPerformList);
    }

    @Override
    public void setSearchView(LocationModel model) {

    }

    @Override
    public void setNormalView() {

    }

    @Override
    public void setNodata() {

    }

    @Override
    public void setError() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_user_map_layout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mKeyword = s.toString();
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
