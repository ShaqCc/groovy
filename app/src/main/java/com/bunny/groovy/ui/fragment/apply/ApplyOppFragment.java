package com.bunny.groovy.ui.fragment.apply;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.StyleGridAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.OpportunityModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.ExplorerOpptnyPresenter;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IExploreView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  申请演出机会
 *
 * Author: Created by bayin on 2017/12/28.
 ****************************************/

public class ApplyOppFragment extends BaseFragment<ExplorerOpptnyPresenter> implements IExploreView {
    private static OpportunityModel oppBean;
    public static String KEY_OPP_BEAN = "key_opp";
    private static String sVenueID, performStartDate, performEndDate, opportunityID, performDate, performTime;
    private List<StyleModel> styleList;
    private PopupWindow mPopupWindow;
    private StyleGridAdapter mAdapter;

    public static void launch(Activity from, Bundle bundle) {
        oppBean = bundle.getParcelable(KEY_OPP_BEAN);
        if (oppBean != null) {
            sVenueID = oppBean.getVenueID();
            performStartDate = oppBean.getStartDate();
            performEndDate = oppBean.getEndDate();
            opportunityID = oppBean.getOpportunityID();
            performDate = oppBean.getPerformDate();
            performTime = oppBean.getPerformTime();
        } else {
            sVenueID = bundle.getString("venueID");
            performStartDate = bundle.getString("performStartDate");
            performEndDate = bundle.getString("performEndDate");
            opportunityID = bundle.getString("opportunityID");
            performDate = bundle.getString("performDate");
            performTime = bundle.getString("performTime");
        }
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "APPLY");
        FragmentContainerActivity.launch(from, ApplyOppFragment.class, bundle);
    }

    @Bind(R.id.apply_et_time)
    EditText etTime;
    @Bind(R.id.apply_et_style)
    EditText etStyle;
    @Bind(R.id.apply_et_bio)
    EditText etDesc;

    @OnClick(R.id.apply_et_style)
    public void showStyle() {
        if (styleList == null || styleList.size() == 0)
            mPresenter.requestStyle();
        else showStylePop(styleList);
    }

    @OnClick(R.id.apply_tv_apply)
    public void apply() {
        //拦截判空
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            UIUtils.showBaseToast("Please input description.");
            return;
        }

        if (TextUtils.isEmpty(etStyle.getText().toString())) {
            UIUtils.showBaseToast("Please choose perform style.");
            return;
        }

        //set params
        //   venueID
        //   performType
        //   performStartDate
        //   performEndDate
        //   performDesc
        //   performerID
        //   opportunityID
        HashMap<String, String> map = new HashMap<>();
//        if (oppBean != null) {
//            map.put("venueID", oppBean.getVenueID());
//            map.put("performStartDate", oppBean.getStartDate());
//            map.put("performEndDate", oppBean.getEndDate());
//            map.put("opportunityID", oppBean.getOpportunityID());
//        } else {
        map.put("venueID", sVenueID);
        map.put("performStartDate", performStartDate);
        map.put("performEndDate", performEndDate);
        map.put("opportunityID", opportunityID);
//        }
        map.put("performType", etStyle.getText().toString());
        map.put("performDesc", etDesc.getText().toString());
        map.put("isOpportunity", "0");
        mPresenter.applyOpportunity(map);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        etStyle.setFocusable(false);
        etTime.setFocusable(false);
        etTime.setText(performDate + " " + performTime);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setListData(List<OpportunityModel> list) {

    }

    @Override
    public void applyResult(boolean success, String msg) {
        if (success) {
            UIUtils.showBaseToast("Apply Success!");
            mActivity.finish();
        } else {
            UIUtils.showBaseToast("Apply Failure!\n" + msg);
        }
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
        UIUtils.hideSoftInput(etStyle);
        styleList = modelList;
        if (mPopupWindow == null)
            initPopWindow(modelList);
        mPopupWindow.showAtLocation(etStyle, Gravity.CENTER, 0, 0);
    }

    private void initPopWindow(List<StyleModel> modelList) {
        mPopupWindow = new PopupWindow(getActivity());
        View popview = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_grid_layout, null, false);
        mPopupWindow.setContentView(popview);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setWidth(UIUtils.getScreenWidth() - UIUtils.dip2Px(32));
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerview = popview.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdapter = new StyleGridAdapter(modelList, etStyle.getText().toString().trim());
        recyclerview.setAdapter(mAdapter);
        popview.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
            }
        });
        popview.findViewById(R.id.pop_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
                etStyle.setText(mAdapter.getSelectStyles());
            }
        });
        // 按下android回退物理键 PopipWindow消失解决
        popview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    closePop();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 关闭选择style窗口
     */
    private void closePop() {
        if (mPopupWindow != null) mPopupWindow.dismiss();
    }


    @Override
    protected ExplorerOpptnyPresenter createPresenter() {
        return new ExplorerOpptnyPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_apply_opp_layout;
    }

    @Override
    protected void loadData() {

    }
}
