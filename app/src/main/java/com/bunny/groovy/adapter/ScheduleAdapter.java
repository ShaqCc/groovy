package com.bunny.groovy.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.model.ShowHistoryModel;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.ui.fragment.apply.EditPerformFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ShowDetailFragment;
import com.bunny.groovy.ui.fragment.spotlight.SpotlightFragment;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.weidget.HeightLightTextView;
import com.socks.library.KLog;

import java.util.List;

/****************************************
 * 功能说明:  schedule时间计划 列表适配器
 *
 * Author: Created by bayin on 2017/12/25.
 ****************************************/

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.HisHolder> {
    private List<ShowModel> mList;
    private Activity mContext;

    public ScheduleAdapter(List<ShowModel> list) {
        mList = list;
    }

    public void refresh(List<ShowModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public HisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.include_recent_show_layout, null, false);
        return new HisHolder(inflate);
    }

    @Override
    public void onBindViewHolder(HisHolder holder, int position) {
        final ShowModel bean = mList.get(position);
        String performState = bean.getPerformState();
        holder.tvStatus.setVisibility(View.VISIBLE);
        holder.ivEdit.setVisibility(View.VISIBLE);
        switch (performState) {
            case "0"://待验证
                holder.tvStatus.setText("Verification");
                break;
            case "1"://已发布
                holder.tvStatus.setText("Confirmed");
                break;
            case "2"://已取消
                holder.tvStatus.setText("Rejected");
                break;
        }
        Glide.with(mContext).load(bean.getHeadImg())
                .placeholder(R.drawable.venue_instead_pic)
                .error(R.drawable.venue_instead_pic)
                .into(holder.mIvHead);
        holder.mTvStar.setText(bean.getVenueScore());
        holder.mTvName.setText(bean.getVenueName());
        holder.mTvStyle.setText(bean.getPerformType());
        holder.mTvShowTime.setText(bean.getPerformDate() + " " + bean.getPerformTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ShowDetailFragment.KEY_SHOW_BEAN, bean);
                ShowDetailFragment.launch(mContext, bundle);
            }
        });


        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取view位置
                final PopupWindow popupWindow = new PopupWindow(mContext);
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_edit, null);
                popupWindow.setContentView(inflate);
                popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(true);
                inflate.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });


                inflate.findViewById(R.id.edit_spotlight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //推广
                        SpotlightFragment.launch(mContext);
                        popupWindow.dismiss();
                    }
                });

                inflate.findViewById(R.id.eidt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //编辑
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EditPerformFragment.KEY_VENUE_BEAN, bean);
                        EditPerformFragment.launch(mContext, bundle);
                        popupWindow.dismiss();
                    }
                });
                int[] loca = calculatePopWindowPos(v, inflate);
                popupWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, loca[0], loca[1]);
            }
        });
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = UIUtils.getScreenHeight();
        final int screenWidth = UIUtils.getScreenWidth();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        windowPos[0] = screenWidth - windowWidth;
        windowPos[1] = anchorLoc[1] - windowHeight;
//        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
//        if (isNeedShowUp) {
//
//        } else {
//            windowPos[0] = screenWidth - windowWidth;
//            windowPos[1] = anchorLoc[1] + anchorHeight;
//        }
        return windowPos;
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    static class HisHolder extends RecyclerView.ViewHolder {
        private final ImageView mIvHead;
        private final HeightLightTextView mTvName;
        private final TextView mTvStar;
        private final TextView mTvStyle;
        private final TextView mTvShowTime;
        private final ImageView ivEdit;
        private final TextView tvStatus;

        public HisHolder(View itemView) {
            super(itemView);
            mIvHead = (ImageView) itemView.findViewById(R.id.nextshow_iv_head);
            mTvName = (HeightLightTextView) itemView.findViewById(R.id.nextshow_tv_performerName);
            mTvStar = (TextView) itemView.findViewById(R.id.nextshow_tv_performerStar);
            mTvStyle = (TextView) itemView.findViewById(R.id.nextshow_tv_address);
            mTvShowTime = (TextView) itemView.findViewById(R.id.nextshow_tv_time);
            tvStatus = (TextView) itemView.findViewById(R.id.nextshow_tv_status);
            ivEdit = (ImageView) itemView.findViewById(R.id.nextshow_iv_edit);
        }
    }
}
