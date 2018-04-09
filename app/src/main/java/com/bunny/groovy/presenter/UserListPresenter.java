package com.bunny.groovy.presenter;

import com.bunny.groovy.api.ApiConstants;
import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.LocationModel;
import com.bunny.groovy.model.MusicianModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.model.UserMainModel;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IUserMainView;
import com.bunny.groovy.weidget.ProgressHUD;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 列表页面公用的控制器
 * <p>
 * Created by Administrator on 2017/12/21.
 */

public class UserListPresenter extends BasePresenter<IUserMainView> {
    public UserListPresenter(IUserMainView view) {
        super(view);
    }

    /**
     * 获取我收藏的所有表演者列表
     */
    public void getFavoriteList() {
        addSubscription(apiService.getUserCollectionPerformerList(AppCacheData.getPerformerUserModel().getUserID()),
                new SubscriberCallBack<List<MusicianModel>>(mView.get()) {
                    @Override
                    protected void onSuccess(List<MusicianModel> response) {
                        if (response != null && response.size() > 0)
                            mView.setView(response);
                        else mView.setNodata();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {
                        mView.setError();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setError();
                    }
                });
    }

    /**
     * 获取表演信息列表
     */
    public void getPerformViewList() {
        addSubscription(apiService.getPerformViewList(AppCacheData.getPerformerUserModel().getUserID()),
                new SubscriberCallBack<List<ShowModel>>(mView.get()) {
                    @Override
                    protected void onSuccess(List<ShowModel> response) {
                        if (response != null && response.size() > 0)
                            mView.setView(response);
                        else mView.setNodata();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {
                        mView.setError();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setError();
                    }
                });
    }

    //请求周边地点数据
    public void getPerformList(Map<String, String> map) {
        addSubscription(apiService.getPerformList(map), new SubscriberCallBack<UserMainModel>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(UserMainModel response) {
                mView.setView(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }

        });
    }

    //请求周边地点数据
    public void search(String location, String keyword) {
        addSubscription(apiService.getSearchPlaceList(location, "50000", keyword, ApiConstants.GOOGLE_MAP_APP_KEY)
                , new Subscriber<LocationModel>() {
                    ProgressHUD mProgressHUD;

                    @Override
                    public void onStart() {
                        super.onStart();
                        mProgressHUD = ProgressHUD.show(mView.get(), "", true, true, null);
                        mProgressHUD.setCancelable(false);
                        mProgressHUD.setMessage("");
                        mProgressHUD.show();
                    }

                    @Override
                    public void onCompleted() {
                        if (mProgressHUD != null) mProgressHUD.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mProgressHUD != null) mProgressHUD.dismiss();
                        UIUtils.showBaseToast(e.toString());
                    }

                    @Override
                    public void onNext(LocationModel model) {
                        try {
                            mView.setSearchView(model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    /**
     * 加入到show history
     */
    public static void addPerformViewer(String performId) {
        ApiRetrofit.getInstance().getApiService()
                .addPerformViewer(AppCacheData.getPerformerUserModel().getUserID(), performId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultResponse<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                    }
                });
    }

}