package com.bunny.groovy.presenter;

import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.api.ApiService;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.ScheduleModel;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.model.VenueScheduleModel;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IScheduleVenueView;
import com.bunny.groovy.view.IScheduleView;
import com.bunny.groovy.weidget.ProgressHUD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/28.
 */
public class VenueSchedulePresenter extends BasePresenter<IScheduleVenueView> {
    public VenueSchedulePresenter(IScheduleVenueView view) {
        super(view);
    }

    private VenueScheduleModel mVenueScheduleModel;
    private Gson mVenueGson = new Gson();

    public void requestVenueWeekList(String startDate, String endTime) {
        //show progress
        final ProgressHUD show = ProgressHUD.show(mView.get(), "", false, false, null);
        addSubscription(apiService.getVenuePerformScheduleList(startDate, endTime), new Subscriber<String>() {
            @Override
            public void onCompleted() {
                if (show != null && show.isShowing()) show.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                if (show != null) show.show();
            }

            @Override
            public void onError(Throwable e) {
                if (show != null && show.isShowing()) show.dismiss();
                UIUtils.showBaseToast(e.toString());
                mView.setFailureView();
            }

            @Override
            public void onNext(String json) {
                mVenueScheduleModel = new VenueScheduleModel();
                try {
                    JSONObject oJson = new JSONObject(json);
                    String success = oJson.getString("success");
                    if (success.equals("true")) {
                        JSONArray resultArray = oJson.optJSONArray("resultData");
                        int size = resultArray.length();
                        for (int i = 0; i < size; i++) {
                            JSONObject object = resultArray.getJSONObject(i);
                            JSONArray performObject = object.getJSONArray("performList");
                            int listSize = performObject.length();
                            if (listSize > 0) {
                                List<VenueShowModel> listModel;
                                Type type = new TypeToken<List<VenueShowModel>>() {
                                }.getType();
                                listModel = mVenueGson.fromJson(performObject.toString(), type);
                                mVenueScheduleModel.setMap(String.valueOf(i + 1), listModel);
                            }
                        }
                        mView.setVenueSuccessView(mVenueScheduleModel);
                    } else {
                        mView.setFailureView();
                        UIUtils.showBaseToast(oJson.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mView.setFailureView();
                    UIUtils.showBaseToast(e.toString());
                }

            }
        });
    }

    /**
     * 推广
     */
    public void spotlightPerform(String performID,String userID) {
        ApiService apiService = ApiRetrofit.getInstance().getApiService();
        apiService.spotlightPerform(performID, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultResponse<Object>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UIUtils.showBaseToast(e.toString());
                        KLog.d(e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<Object> response) {
                        if (response.success) {
                            UIUtils.showBaseToast("推广成功！");
                        } else {
                            UIUtils.showBaseToast("推广失败！请重试");
                        }
                    }
                });
    }
}
