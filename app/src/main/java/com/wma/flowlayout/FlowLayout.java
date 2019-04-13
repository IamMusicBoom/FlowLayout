package com.wma.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class FlowLayout extends FrameLayout {
    private int mWidth, mHeight;
    private int mMaxWidth, mMaxHeight;
    public FlowLayout(Context context) {
        this(context, null);
    }
    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMaxWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMaxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int totalWidth = 0, totalHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = (LayoutParams) childAt.getLayoutParams();
            int childHeight = childAt.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
            int childWidth = childAt.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            totalWidth += childWidth;
            totalHeight = Math.max(totalHeight, childHeight);
        }
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mWidth = Math.min(MeasureSpec.getSize(widthMeasureSpec), totalWidth);
                break;
            case MeasureSpec.EXACTLY:
                mWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mHeight = Math.min(totalHeight, MeasureSpec.getSize(heightMeasureSpec));
                break;
            case MeasureSpec.EXACTLY:
                mHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
    }
    private int totalHeight = 0;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int l = 0, t = 0, r = 0, b = 0;
        int curWidth = 0,  lineHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            LayoutParams lp = (LayoutParams) childAt.getLayoutParams();
            int childWidth = childAt.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = childAt.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (curWidth + childWidth > mMaxWidth) {//换行
                totalHeight += lineHeight;
                lineHeight = childHeight;
                curWidth = childWidth;
            } else {//不换行
                lineHeight = Math.max(lineHeight, childHeight);
                curWidth = curWidth + childWidth;
            }
            l = curWidth - childWidth + lp.leftMargin;
            t = totalHeight  + lp.topMargin;
            r = curWidth - lp.rightMargin;
            b = totalHeight +childHeight - lp.bottomMargin;
//            Log.d("WMA-WMA","width = " + childAt.getMeasuredWidth() + "  height = " + childAt.getMeasuredHeight());
//            Log.d("WMA-WMA", "onLayout: " + " l = " + l + " t = " + t + " r = " + r + " b = " + b);
            childAt.layout(l, t, r, b);
        }
    }
}
