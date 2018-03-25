package com.bunny.groovy.weidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by wjy on 2016/4/7.
 */
public class StarGradeView extends View {

    private Paint mPaint;
    private float mStarHeight = 34;
    private float mStarWidth = 36;

    private int mGrade = 0;
    private float mStarSpace = 15;
    private boolean mChangeGradeEnable = false;
    private static Bitmap sFullStar, sStrokeStar;
    private Rect mSrcRect, mDstRect;
    private Context mContext;
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;

    public StarGradeView(Context context) {
        super(context);
        init(null, context);
    }

    public StarGradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public StarGradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mSrcRect = new Rect(0, 0, 72, 68);
        mDstRect = new Rect();

        if (attrs != null) {
            TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.StarGradeAttr);
            if (values != null) {
                mStarWidth = values.getDimension(R.styleable.StarGradeAttr_star_width, -1);
                mStarHeight = values.getDimension(R.styleable.StarGradeAttr_star_height, -1);
                mStarSpace = values.getDimension(R.styleable.StarGradeAttr_star_space, -1);
                values.recycle();
            }
        }

        mContext = context;

        new Thread() {
            @Override
            public void run() {
                try {
                    boolean needRefresh = false;
                    if (sFullStar == null || sFullStar.isRecycled()) {
                        sFullStar = Glide.with(mContext)
                                .load(R.drawable.icon_review_selected)
                                .asBitmap()
                                .centerCrop()
                                .into(72, 68)
                                .get();
                        needRefresh = true;
                    }

                    if (sStrokeStar == null || sStrokeStar.isRecycled()) {
                        sStrokeStar = Glide.with(mContext)
                                .load(R.drawable.icon_review)
                                .asBitmap() //必须
                                .into(72, 68)
                                .get();
                        needRefresh = true;
                    }

                    if (needRefresh) {
                        postInvalidate();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        for (int i = 0; i < 5; i++) {
            float startX = i * (mStarWidth + mStarSpace);
            mDstRect.set((int) startX, 0, (int) (startX + mStarWidth), (int) mStarHeight);
            if (sFullStar != null && sStrokeStar != null) {
                canvas.drawBitmap(mGrade > i ? sFullStar : sStrokeStar, mSrcRect, mDstRect, mPaint);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private static final int FILL_STAR = 0, HALF_STAR = 1, STROKE_STAR = 2;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mChangeGradeEnable) {
                float x = event.getX();
                if(x < 0){
                    mGrade = 0;
                }else {
                    int count = (int) (x / (mStarWidth + mStarSpace)) + 1;
                    count = count > 5 ? 5 : count;
                    //没有半星
                    mGrade = count;
                }
                invalidate();
            }
        }
        return true;
    }

    //set the grade and the full is 100
    public void setGrade(int grade) {
        mGrade = grade;
        invalidate();
    }

    //full is 10
    public void setGrade(float grade) {
        mGrade = (int) (grade);
        invalidate();
    }

    public int getGrade() {
        return mGrade;
    }

    public void setChangeGradeEnable(boolean enable) {
        mChangeGradeEnable = enable;
    }

}
