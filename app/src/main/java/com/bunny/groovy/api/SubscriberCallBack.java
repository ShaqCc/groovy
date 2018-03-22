package com.bunny.groovy.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.ui.RoleChooseActivity;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.weidget.ProgressHUD;

import rx.Subscriber;

/**
 * @author ChayChan
 * @description: 抽取CallBack
 * @date 2017/6/18  21:37
 */
public abstract class SubscriberCallBack<T> extends Subscriber<ResultResponse<T>> {
    private Context mContext;
    private ProgressHUD mProgressHUD;

    public SubscriberCallBack(Context context) {
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.mContext != null && isShowProgress() && this.mContext instanceof Activity) {
            mProgressHUD = ProgressHUD.show(mContext, "", true, true, null);
            mProgressHUD.setCancelable(false);
            mProgressHUD.setMessage("");
            mProgressHUD.show();
        }
    }

    /**
     * 是否展示进度框浮层
     *
     * @return
     */
    protected boolean isShowProgress() {
        return false;
    }

    @Override
    public void onNext(ResultResponse response) {
        if (mProgressHUD != null && mProgressHUD.isShowing()) mProgressHUD.dismiss();
        if (response.success) {
            onSuccess((T) response.resultData);
        } else {
            if ("201".equals(response.errorCode)) {//未登录
                UIUtils.showBaseToast("You have not signed in or expired");
                RoleChooseActivity.launch(mContext);
            } else {
                UIUtils.showBaseToast(response.errorMsg);
                Log.i("http", "onError错误！" + response.errorMsg);
                onFailure(response);
            }
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (mProgressHUD != null && mProgressHUD.isShowing()) mProgressHUD.dismiss();
        UIUtils.showToast("Server wrong!");
        e.printStackTrace();
        System.out.print("onError错误！" + e.toString());
//        onError();
    }

    protected abstract void onSuccess(T response);

//    protected abstract void onError();

    protected abstract void onFailure(ResultResponse response);

}
