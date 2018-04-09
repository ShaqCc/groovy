package com.bunny.groovy.ui.fragment.releaseshow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.NearByOppListAdapter;
import com.bunny.groovy.adapter.SearchListAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.divider.HLineDecoration;
import com.bunny.groovy.model.LocationModel;
import com.bunny.groovy.model.OpportunityModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.ExplorerOpptnyPresenter;
import com.bunny.groovy.ui.fragment.apply.ApplyOppFragment;
import com.bunny.groovy.ui.fragment.apply.FilterFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IExploreView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.socks.library.KLog;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 申请表演机会，地图
 * <p>
 * Created by Administrator on 2017/12/17.
 */

public class ExploreShowFragment extends BaseFragment<ExplorerOpptnyPresenter> implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, TextWatcher,
        IExploreView {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;
    int FILTER_REQUEST_CODE = 1;
    int PLACE_PICKER_REQUEST = 2;
    int OPEN_GPS_REQUEST_CODE = 1024;

    private GoogleMap mGoogleMap;
    private List<OpportunityModel> mOpportunityModelList = new ArrayList<>();
    private OpportunityModel mCurrentBean;//当前选中的演出机会bean
    private List<Marker> mMarkerList = new ArrayList<>();
    private String distance = "25";//距离默认25mi
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
    @Bind(R.id.marker_tv_distance)
    TextView mTvDistance;
    @Bind(R.id.opp_recyclerview)
    RecyclerView mRecyclerView;
    private NearByOppListAdapter mAdapter;
    @Bind(R.id.map_layout)
    RelativeLayout mapLayout;
    @Bind(R.id.map_et_search)
    TextView etSearch;
    @Bind(R.id.map_search_bar)
    View mapSearchBar;
    @Bind(R.id.map_ll_search)
    LinearLayout searchLayout;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private FusedLocationProviderClient mLocationClient;
    private LocationRequest mLocationRequest;

    private String mKeyword;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mGoogleApiClient.isConnected()) {
                    if (!TextUtils.isEmpty(mKeyword)) {
                        LatLngBounds bounds = new LatLngBounds(
                                new LatLng(mLastLocation.getLatitude() - 0.02, mLastLocation.getLongitude() - 0.02),
                                new LatLng(mLastLocation.getLatitude() + 0.02, mLastLocation.getLongitude() + 0.02));
                        final PendingResult<AutocompletePredictionBuffer> results =
                                Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, mKeyword,
                                        bounds, new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AutocompletePredictionBuffer autocompletePredictions = (AutocompletePredictionBuffer) results.await();
                                int count = autocompletePredictions.getCount();
                                if (mLocationList != null) mLocationList.clear();
                                else mLocationList = new ArrayList<>();
                                for (AutocompletePrediction autocompletePrediction : autocompletePredictions) {
                                    LocationModel model = new LocationModel();
                                    model.id = autocompletePrediction.getPlaceId();
                                    model.name = autocompletePrediction.getPrimaryText(null);
                                    model.summary = autocompletePrediction.getSecondaryText(null);
                                    mLocationList.add(model);
                                }
                                mHandler.sendEmptyMessage(2);
                                autocompletePredictions.release();
                            }
                        }).start();
                    }
                }
            } else {
                showLocationPopupWindow();
            }
        }
    };

    @OnClick(R.id.marker_tv_venue_detail)
    public void venueDetail() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(OpportunityDetailFragment.KEY_OPPORTUNITY_BEAN, mCurrentBean);
        OpportunityDetailFragment.launch(mActivity, bundle);
    }

    @OnClick(R.id.marker_iv_phone)
    public void call() {
        Utils.CallPhone(mActivity, mCurrentBean.getPhoneNumber());
    }

    @OnClick(R.id.marker_iv_email)
    public void email() {
        Utils.sendEmail(mActivity, mCurrentBean.getVenueEmail());
    }

    /**
     * 申请机会
     */
    @OnClick(R.id.marker_tv_apply)
    public void apply() {
        //跳转到申请页面
        Bundle bundle = new Bundle();
        bundle.putParcelable(ApplyOppFragment.KEY_OPP_BEAN, mCurrentBean);
        ApplyOppFragment.launch(mActivity, bundle);
    }

    @OnClick(R.id.map_filter)
    public void mapFilter() {
        filter();
    }

    /*@OnClick(R.id.map_ll_search)
    public void searchAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(mActivity), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 跳转到条件页面
     */
    private void filter() {
        Bundle bundle = new Bundle();
        bundle.putInt(FilterFragment.KEY_DISTANCE, Integer.parseInt(distance));
        FilterFragment.launchForResult(mActivity, bundle, FILTER_REQUEST_CODE);
    }

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "EXPLORE SHOW OPPORTUNITY");
        FragmentContainerActivity.launch(from, ExploreShowFragment.class, bundle);
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();
        mMarkerLayout.setVisibility(View.GONE);
        etSearch.addTextChangedListener(this);
        //初始化map
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.map_container, supportMapFragment, "map_fragment").commit();
        supportMapFragment.getMapAsync(this);

        //检查权限
        if (ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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
    private void setMarkerData(OpportunityModel bean) {
        mCurrentBean = bean;
        mTvDate.setText(bean.getPerformDate());
        mTvName.setText(bean.getVenueName());
        mTvadd.setText(bean.getVenueAddress());
        mTvTime.setText(bean.getPerformTime());
        mTvDistance.setText(bean.getDistance() + "mi");
        mTvScore.setText(bean.getVenueScore());
        Glide.with(mActivity).load(bean.getHeadImg()).error(R.drawable.venue_instead_pic).into(mHeadImg);
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

        //点击监听
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                OpportunityModel info = (OpportunityModel) marker.getTag();
                int clickedIndex = mMarkerList.indexOf(marker);
                if (lastMarkerSelected == clickedIndex) {
                    //点击的是当前marker
                    if (info != null) {
                        if (isMarkerShowing) {
                            mMarkerLayout.setVisibility(View.GONE);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_opportunity));
                        } else {
                            setMarkerData(info);
                            mMarkerLayout.setVisibility(View.VISIBLE);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_opportunity_selected));
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
                            mMarkerList.get(lastMarkerSelected).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_opportunity));
                        //设置当前点击的marker大图片
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_opportunity_selected));
                    }
                }
                lastMarkerSelected = clickedIndex;
                return false;
            }
        });
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        //设置当前位置
        updateCurrentLocation();
    }

    /**
     * 设置当前位置
     */
    private void updateCurrentLocation() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            if (mLastLocation != null) {
                LatLng myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(myLoc)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                        .draggable(true));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
            } else {
                //默认位置旧金山
                LatLng defaultLoc = new LatLng(37.774930, -122.419416);
                mGoogleMap.addMarker(new MarkerOptions().position(defaultLoc)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                        .draggable(true));

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 15));
            }


        }
        //请求机会数据
        requestAroundList();
    }

    /**
     * 请求周边数据
     */
    private void requestAroundList() {
        HashMap<String, String> map = new HashMap<>();
        if (mLastLocation != null) {
            map.put("longitude", String.valueOf(mLastLocation.getLongitude()));
            map.put("latitude", String.valueOf(mLastLocation.getLatitude()));
            map.put("distance", distance);
            mPresenter.requestOpportunityList(map);
        }
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
                    //设置默认位置
                    //Latitude:37.774930 longitude:-122.419416
                    UIUtils.showBaseToast("Positioning function is not available, please go to set to open the positioning.");
                }
            }
        }
    }

    private boolean showMap = true;//显示的形式是列表还是map

    @Override
    public void setListData(List<OpportunityModel> list) {
        mOpportunityModelList.clear();
        mMarkerList.clear();
        mOpportunityModelList = list;

        //判断是list / map
        if (showMap && mGoogleMap != null) {
            mapLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            //列表显示
            mapLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        //设置当前位置
        resetMap();
        if (list != null) {
            //设置marker
            isMarkerShowing = false;
            mMarkerLayout.setVisibility(View.GONE);
            LatLng loc;
            for (int i = 0; i < list.size(); i++) {
                OpportunityModel model = list.get(i);
                loc = new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()));
                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(loc)
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_opportunity)));
                marker.setTag(model);
                mMarkerList.add(marker);
            }
        }
        //列表数据
        if (mAdapter == null) {
            mAdapter = new NearByOppListAdapter(mOpportunityModelList);
            mAdapter.setPresenter(mPresenter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.addItemDecoration(new HLineDecoration(mActivity, HLineDecoration.VERTICAL_LIST,
                    R.drawable.shape_item_divider_line));
            mRecyclerView.setAdapter(mAdapter);
        } else mAdapter.refresh(mOpportunityModelList);
    }

    /**
     * 清空地图是原有的标记，添加当前位置
     */
    private void resetMap() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            if (mLastLocation != null) {
                LatLng myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(myLoc)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                        .draggable(true));

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
            } else {
                LatLng defaultLoc = new LatLng(37.774930, -122.419416);
                mGoogleMap.addMarker(new MarkerOptions().position(defaultLoc)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
                        .draggable(true));

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 15));
            }
        }
    }

    @Override
    public void applyResult(boolean success, String msg) {
        if (success) {
            UIUtils.showBaseToast("Apply successfully.");
        } else {
            UIUtils.showBaseToast("Apply failed.");
        }
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {

    }

    private Menu mMenu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_explore_opp, menu);
        mMenu = menu;
        mMenu.findItem(R.id.explore_menu_list).setVisible(true);
        mMenu.findItem(R.id.explore_menu_map).setVisible(false);
        mMenu.findItem(R.id.explore_menu_filter).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore_menu_filter:
                //过滤器
                filter();
                return true;
            case R.id.explore_menu_list:
                //列表显示
                showMap = false;
                mMenu.findItem(R.id.explore_menu_filter).setVisible(true);
                mMenu.findItem(R.id.explore_menu_map).setVisible(true);
                mMenu.findItem(R.id.explore_menu_list).setVisible(false);
                mRecyclerView.setVisibility(View.VISIBLE);
                mapLayout.setVisibility(View.GONE);
                mapSearchBar.setVisibility(View.GONE);
                return true;
            case R.id.explore_menu_map:
                //地图显示
                showMap = true;
                mMenu.findItem(R.id.explore_menu_list).setVisible(true);
                mMenu.findItem(R.id.explore_menu_filter).setVisible(false);
                mMenu.findItem(R.id.explore_menu_map).setVisible(false);
                mRecyclerView.setVisibility(View.GONE);
                mapLayout.setVisibility(View.VISIBLE);
                mapSearchBar.setVisibility(View.VISIBLE);
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
            String dis = data.getStringExtra("distance");
            if (!TextUtils.isEmpty(dis)) {
                distance = dis;
            }
            map.put("distance", distance);

            String performStartDate = data.getStringExtra("performStartDate");
            if (!TextUtils.isEmpty(performStartDate)) {
                map.put("performStartDate", performStartDate);
            }

            if (mLastLocation != null) {
                map.put("longitude", String.valueOf(mLastLocation.getLongitude()));
                map.put("latitude", String.valueOf(mLastLocation.getLatitude()));
            }
            mPresenter.requestOpportunityList(map);
        } else if (requestCode == OPEN_GPS_REQUEST_CODE && resultCode == RESULT_OK) {
            //请求开启gps服务成功，开始请求当前位置信息
            setUpLocationRequest();
        } else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            //选取地址
            Place place = PlacePicker.getPlace(data, get());
            String toastMsg = String.format("Place: %s", place.getName());
            KLog.a("Place:" + toastMsg);
            etSearch.setText(place.getName());
            //更新当前位置
            mLastLocation.setLatitude(place.getLatLng().latitude);
            mLastLocation.setLongitude(place.getLatLng().longitude);
            updateCurrentLocation();
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
    protected ExplorerOpptnyPresenter createPresenter() {
        return new ExplorerOpptnyPresenter(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public Activity get() {
        return getActivity();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_map_layout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mKeyword = s.toString();
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private PopupWindow mPopupWindow;
    private RecyclerView mPopupRecyclerView;
    private SearchListAdapter mRecyclerViewAdapter;

    /**
     * 弹出选择号码的对话框
     */
    private void showLocationPopupWindow() {
        if (mLocationList != null && mLocationList.size() > 0) {
            initRecyclerView(mLocationList);
            if (mPopupWindow == null) {
                mPopupWindow = new PopupWindow(mPopupRecyclerView, searchLayout.getWidth() - 4, etSearch.getWidth() * 3 / 5);
                mPopupWindow.setOutsideTouchable(true);   // 设置外部可以被点击
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                mPopupWindow.setFocusable(true);    // 使PopupWindow可以获得焦点
            }
            // 显示在输入框的左下角
            mPopupWindow.showAsDropDown(searchLayout, 2, 50);
        } else {
            UIUtils.showBaseToast("No search for content.");
        }


    }

    /**
     * 初始化RecyclerView，模仿ListView下拉列表的效果
     */
    private List<LocationModel> mLocationList;

    private void initRecyclerView(List<LocationModel> list) {
        if (mPopupRecyclerView == null) {
            mPopupRecyclerView = new RecyclerView(getContext());
            mPopupRecyclerView.setMinimumWidth(mapSearchBar.getWidth() - 4);
            //设置布局管理器
            mPopupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            //设置Adapter
            mRecyclerViewAdapter = new SearchListAdapter(list);
            mRecyclerViewAdapter.setKeyword(mKeyword);
            mRecyclerViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LocationModel model = mLocationList.get(position);
                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, model.id)
                            .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                        final Place myPlace = places.get(0);
                                        mLastLocation.setLatitude(myPlace.getLatLng().latitude);
                                        mLastLocation.setLongitude(myPlace.getLatLng().longitude);
                                        updateCurrentLocation();
                                    }
                                    places.release();
                                }
                            });
                    mPopupWindow.dismiss();
                }
            });
            mPopupRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else {
            mRecyclerViewAdapter.setKeyword(mKeyword);
            mRecyclerViewAdapter.refresh(list);
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
}
