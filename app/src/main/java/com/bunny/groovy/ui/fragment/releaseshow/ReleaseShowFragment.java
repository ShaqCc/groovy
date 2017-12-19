package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.StyleGridAdatper;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.presenter.ReleasePresenter;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发布演出页面
 * Created by Administrator on 2017/12/16.
 */

public class ReleaseShowFragment extends BaseFragment<ReleasePresenter> implements ISetFileView {

    private PopupWindow mPopupWindow;
    private StyleGridAdatper mAdatper;
    private List<StyleModel> styleList;

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "RELEASE SHOW");
        FragmentContainerActivity.launch(from, ReleaseShowFragment.class, bundle);
    }

    @Bind(R.id.release_et_venue)
    EditText etVenue;

    @Bind(R.id.release_et_style)
    EditText etStyle;

    @Bind(R.id.release_et_time)
    EditText etTime;

    @Bind(R.id.release_et_bio)
    EditText etBio;

    @OnClick(R.id.tv_release)
    public void release() {

    }

    /**
     * 跳转到搜索音乐厅界面
     */
    @OnClick(R.id.release_tv_search)
    public void search() {
        SearchVenueFragment.launchForResult(mActivity, new Bundle(), 1);
    }

    @OnClick(R.id.release_et_style)
    public void showPop() {
        if (styleList == null || styleList.size() == 0)
            mPresenter.requestStyle();
        else showStylePop(styleList);
    }

    @OnClick(R.id.release_et_time)
    public void selectTime(){

    }

    private void closePop() {
        if (mPopupWindow != null) mPopupWindow.dismiss();
    }

    /**
     * 接收选择的演出厅
     *
     * @param model
     */
    @Subscribe
    public void onChooseVenue(VenueModel model) {
        if (model != null) etVenue.setText(model.getVenueName());
    }


    @Override
    protected ReleasePresenter createPresenter() {
        return new ReleasePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_release_show_layout;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        //禁用编辑
        etVenue.setFocusable(false);
        etStyle.setFocusable(false);
        etTime.setFocusable(false);
    }

    @Override
    public void initListener() {
        super.initListener();
        registerEventBus(this);
    }

    @Override
    public Activity get() {
        return getActivity();
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
        UIUtils.hideSoftInput(etStyle);
        styleList = modelList;
        if (mPopupWindow == null)
            initPopWindow(modelList);
        mPopupWindow.showAtLocation(etBio, Gravity.CENTER,0,0);
    }

    private void initPopWindow(List<StyleModel> modelList) {
        mPopupWindow = new PopupWindow(getActivity());
        View popview = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_grid_layout, null, false);
        mPopupWindow.setContentView(popview);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setWidth(UIUtils.getScreenWidth()-UIUtils.dip2Px(32));
        RecyclerView recyclerview = (RecyclerView) popview.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdatper = new StyleGridAdatper(modelList, etStyle.getText().toString().trim());
        recyclerview.setAdapter(mAdatper);
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
                etStyle.setText(mAdatper.getSelectStyles());
            }
        });
        // 按下android回退物理键 PopipWindow消失解决
        recyclerview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });

    }
}