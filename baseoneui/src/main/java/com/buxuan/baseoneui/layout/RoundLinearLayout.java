package com.buxuan.baseoneui.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.appcompat.util.SeslRoundedCorner;

import com.buxuan.baseoneui.R;


public class RoundLinearLayout extends LinearLayout {

    // region AREA: Variables                                       ////////////////////////////////
    SeslRoundedCorner   mSeslRoundedCorner;
    // endregion                                                    ////////////////////////////////


    // region AREA: Constructors                                    ////////////////////////////////

    // region AREA: RoundLinearLayout

    public RoundLinearLayout(Context context) {
        super(context);
    }


    /**
     * RoundLinearLayout
     * RoundLinearLayout_roundedCorners 정보
     * (기본값은 15, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT 모두 Rounded Corner 처리)를 가지고 Rounded Corner를 설정한다.
     * @param context Context
     * @param attrs AttributeSet
     */
    public RoundLinearLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray obtainStyledAttributes   = context.obtainStyledAttributes(attrs, R.styleable.RoundLinearLayout);

        // MEMO: World8848. Round corner type, default 15 is ROUNDED_CORNER_ALL
        int roundedCorners                  = obtainStyledAttributes.getInt(R.styleable.RoundLinearLayout_roundedCorners, 15);

        // MEMO: World8848.  SeslRoundedCorner instance
        mSeslRoundedCorner                  = new SeslRoundedCorner(context);

        // MEMO: World8848. Rounded corners type을 할당
        mSeslRoundedCorner.setRoundedCorners(roundedCorners);

        obtainStyledAttributes.recycle();
    }


    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // endregion

    // endregion                                                    ////////////////////////////////


    // region AREA: Protected Methods                               ////////////////////////////////
    /**
     * dispatchDraw
     * ViewGroup인 RoundLinearLayout의 child View를 모두 다시 draw한 후
     * drawRoundedCorner()를 호출하여 Rounded Corner를 생성한다.
     * @param canvas Canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mSeslRoundedCorner.drawRoundedCorner(canvas);
    }
    // endregion                                                    ////////////////////////////////

}
