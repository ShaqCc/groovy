package com.bunny.groovy.ui.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.TransactionRecordListAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.MusicianDetailModel;
import com.bunny.groovy.presenter.RewardPresenter;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IRewardView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****************************************
 * 功能说明:  打赏页面
 *
 *
 * Author: Created by bayin on 2018/1/11.
 ****************************************/

public class RewardFragment extends BaseFragment<RewardPresenter> implements IRewardView {

    private final static int REQUEST_PAYPAL_AUTHORITY = 888;
    @Bind(R.id.et_reward)
    EditText mEtReward;
    @Bind(R.id.rb_reward_1)
    RadioButton mRb1;
    @Bind(R.id.rb_reward_2)
    RadioButton mRb2;
    @Bind(R.id.rb_reward_5)
    RadioButton mRb3;
    @Bind(R.id.rb_reward_10)
    RadioButton mRb4;
    @Bind(R.id.rb_pay_mode)
    RadioGroup mRgPayMode;
    @Bind(R.id.rb_balance)
    RadioButton mRbBalance;
    @Bind(R.id.tv_reward)
    TextView mRewardView;
    @Bind(R.id.tv_reward1)
    TextView mRewardView1;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_history)
    TextView mHistoryView;

    private TransactionRecordListAdapter mAdapter;

    private float mAmount, mBalance;
    private static String performerID;
    private static boolean isHistory;

    public static void launch(Activity from, String performerId, boolean history) {
        performerID = performerId;
        isHistory = history;
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "REWARD");
        FragmentContainerActivity.launch(from, RewardFragment.class, bundle);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void onTokenGet(String token) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(token);
        startActivityForResult(dropInRequest.getIntent(mActivity), REQUEST_PAYPAL_AUTHORITY);
    }

    @Override
    public void setViewList(List<MusicianDetailModel.TransactionRecord> list) {
        //set list
        if (mAdapter == null) {
            mAdapter = new TransactionRecordListAdapter(list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(list);
        }
    }

    @Override
    protected RewardPresenter createPresenter() {
        return new RewardPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_user_reward_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mBalance = Float.parseFloat(AppCacheData.getPerformerUserModel().getBalance());
        mRbBalance.setText("BALANCE " + mBalance + " $");
        if (isHistory) {
            mHistoryView.setVisibility(View.VISIBLE);
            mRewardView1.setVisibility(View.VISIBLE);
            mRewardView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {
        int userType = Utils.parseInt(AppCacheData.getPerformerUserModel().getUserType());
        mPresenter.requestUserData(userType);
        if (isHistory) {
            HashMap<String, String> map = new HashMap<>();
            map.put("performerID", performerID);
            if (AppConstants.USER_TYPE_NORMAL != userType) {
                map.put("userID", AppCacheData.getPerformerUserModel().getUserID());
            }
            mPresenter.getrewardPerformerRecord(map);
        }
    }

    @OnClick({R.id.rb_reward_1, R.id.rb_reward_2, R.id.rb_reward_5, R.id.rb_reward_10})
    void clickRadioButton(View view) {
        switch (view.getId()) {
            case R.id.rb_reward_1:
                mEtReward.setText("1");
                mRb2.setChecked(false);
                mRb3.setChecked(false);
                mRb4.setChecked(false);
                break;
            case R.id.rb_reward_2:
                mEtReward.setText("2");
                mRb1.setChecked(false);
                mRb3.setChecked(false);
                mRb4.setChecked(false);
                break;
            case R.id.rb_reward_5:
                mEtReward.setText("5");
                mRb1.setChecked(false);
                mRb2.setChecked(false);
                mRb4.setChecked(false);
                break;
            case R.id.rb_reward_10:
                mEtReward.setText("10");
                mRb1.setChecked(false);
                mRb2.setChecked(false);
                mRb3.setChecked(false);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_reward, R.id.tv_reward1})
    void reward() {
        String text = mEtReward.getText().toString();
        Float f = null;
        if (!TextUtils.isEmpty(text)) f = Float.valueOf(text);
        if (f == null) {
            UIUtils.showToast("not a number");
            return;
        }
        mAmount = f;
        if (mAmount <= 0) {
            UIUtils.showToast("should more than 0");
            return;
        }
        if (mAmount > 999) {
            UIUtils.showToast("should less than 999");
            return;
        }
        if (mRgPayMode.getCheckedRadioButtonId() == R.id.rb_balance) {
            if (mBalance < mAmount) {
                UIUtils.showToast("Insufficient account balance.");
                return;
            }
            doRewardBalance();
        } else {
            //获取验证支付码
            mPresenter.getToken();
        }
    }

    //余额打赏
    private void doRewardBalance() {
        HashMap<String, String> map = new HashMap<>();
        map.put("payMethod", AppConstants.PAY_STYLE_BALANCE);
        doReward(map);
    }

    //用paypal打赏
    private void doRewardPayPal(String checkCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("payMethod", AppConstants.PAY_STYLE_PAYPAL);
        map.put("checkCode", checkCode);
        doReward(map);
    }

    private void doReward(HashMap<String, String> map) {
        map.put("performerID", performerID);
        if (!TextUtils.equals(String.valueOf(AppConstants.USER_TYPE_NORMAL), AppCacheData.getPerformerUserModel().getUserType())) {
            map.put("userID", AppCacheData.getPerformerUserModel().getUserID());
        }
        map.put("amount", String.valueOf(mAmount));

        mPresenter.rewardPerformer(map);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PAYPAL_AUTHORITY:
                if (resultCode == Activity.RESULT_OK) {
                    DropInResult dropInResult = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                    if (dropInResult == null || dropInResult.getPaymentMethodNonce() == null) {
                        return;
                    }
                    String nonce = dropInResult.getPaymentMethodNonce().getNonce();
                    doRewardPayPal(nonce);
                }
                break;
        }
    }

}
