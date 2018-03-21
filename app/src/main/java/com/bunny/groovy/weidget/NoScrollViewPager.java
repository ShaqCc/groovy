package com.bunny.groovy.weidget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.yan.inflaterauto.AutoUtils;

/**
 * 自定义一个不能左右滑动的ViewPager
 *
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}


	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}

	/**
	 * 不拦截事件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}


	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		ViewGroup.LayoutParams vlp = super.generateLayoutParams(attrs);
		AutoUtils.autoLayout(vlp, getContext(), attrs);
		return vlp;
	}
}
