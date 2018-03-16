package com.bunny.groovy.view;

import android.app.Activity;

import com.bunny.groovy.model.MusicianDetailModel;

import java.util.List;

/**
 * 打赏接口
 *
 * Created by Administrator on 2017/12/17.
 */

public interface IRewardView {
    Activity get();
    void onTokenGet(String token);
    void setViewList(List<MusicianDetailModel.TransactionRecord> list);
}
