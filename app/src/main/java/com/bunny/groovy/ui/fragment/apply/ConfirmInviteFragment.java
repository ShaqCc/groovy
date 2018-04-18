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
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.StyleGridAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.ApplyVenuePresenter;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IApplyVenueView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  同意邀请界面
 *
 *
 * Author: Created by bayin on 2018/1/4.
 ****************************************/

public class ConfirmInviteFragment extends BaseFragment<ApplyVenuePresenter> implements IApplyVenueView {

    public static String KEY_VENUE_BEAN = "KEY_VENUE_BEAN";
    private static ShowModel sShowModel;
    private List<StyleModel> styleList;
    private PopupWindow mPopupWindow;
    private StyleGridAdapter mAdapter;


    public static void launch(Activity from, Bundle bundle) {
        sShowModel = bundle.getParcelable(KEY_VENUE_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "CONFIRM");
        FragmentContainerActivity.launch(from, ConfirmInviteFragment.class, bundle);
    }

    @Bind(R.id.apply_et_time)
    EditText etTime;
    @Bind(R.id.apply_et_style)
    EditText etStyle;
    @Bind(R.id.apply_et_bio)
    EditText etDesc;
    @Bind(R.id.apply_tv_apply)
    TextView mTvConfirm;


    //弹出选择style窗口
    @OnClick(R.id.apply_et_style)
    public void showStyle() {
        if (styleList == null || styleList.size() == 0)
            mPresenter.requestStyle();
        else showStylePop(styleList);
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        etStyle.setFocusable(false);
        etTime.setFocusable(false);
        etTime.setText(sShowModel.getPerformDate() + " " + sShowModel.getPerformTime());
        mTvConfirm.setText("CONFIRM");
    }


    //申请
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
        mPresenter.confirmInvite(sShowModel.getInviteID(), etDesc.getText().toString(), etStyle.getText().toString());
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
        UIUtils.hideSoftInput(etStyle);
        styleList = modelList;
        if (mPopupWindow == null)
            initStylePop(modelList);
        mPopupWindow.showAtLocation(etStyle, Gravity.CENTER, 0, 0);
    }


    /**
     * 关闭选择style窗口
     */
    private void closePop() {
        if (mPopupWindow != null) mPopupWindow.dismiss();
    }

    private void initStylePop(List<StyleModel> modelList) {
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

    @Override
    protected ApplyVenuePresenter createPresenter() {
        return new ApplyVenuePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_apply_opp_layout;
    }

    @Override
    protected void loadData() {
    }
}
