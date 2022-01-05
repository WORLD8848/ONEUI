package androidx.appcompat.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.RestrictTo;

import com.buxuan.baseoneui.R;


public class SeslRoundedCorner {

    // region AREA: Variables                                       ////////////////////////////////

    public static final int ROUNDED_CORNER_NONE         = 0;
    public static final int ROUNDED_CORNER_TOP_LEFT     = 1;
    public static final int ROUNDED_CORNER_TOP_RIGHT    = 2;
    public static final int ROUNDED_CORNER_BOTTOM_LEFT  = 4;
    public static final int ROUNDED_CORNER_BOTTOM_RIGHT = 8;
    public static final int ROUNDED_CORNER_ALL          = 15;

    static final String TAG = "SeslRoundedCorner";
    private static final int RADIUS = 26;

    // region AREA: Round corner images
    Drawable mBottomLeftRound;
    Drawable mBottomRightRound;
    Drawable mTopLeftRound;
    Drawable mTopRightRound;
    // endregion

    int                         mRoundRadius                    = -1;
    Rect                        mRoundedCornerBounds            = new Rect();
    int                         mRoundedCornerMode;
    int mX;
    int mY;
    @ColorInt
    private int mBottomLeftRoundColor;
    @ColorInt
    private int mBottomRightRoundColor;
    private Context mContext;
    private boolean mIsMutate = false;
    private Resources mRes;
    @ColorInt
    private int mTopLeftRoundColor;
    @ColorInt
    private int mTopRightRoundColor;
    // endregion                                                    ////////////////////////////////



    // region AREA: Constructors                                    ////////////////////////////////
    public SeslRoundedCorner(Context context) {
        this.mContext   = context;
        this.mRes       = context.getResources();
        initRoundedCorner();
    }

    public SeslRoundedCorner(Context context, boolean isMutate) {
        this.mContext   = context;
        this.mRes       = context.getResources();
        this.mIsMutate  = isMutate;
        initRoundedCorner();
    }
    // endregion                                                    ////////////////////////////////



    // region AREA: Private Methods                                 ////////////////////////////////

    // region AREA: drawRoundedCornerInternal
    /**
     * drawRoundedCornerInternal
     * Rect의 left, right, top, bottom 정보를 할당한다.
     * RoundedCornerMode에 따라 TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT 각각의 Drawable Image을 할당한다.
     * @param canvas Canvas
     */
    private void drawRoundedCornerInternal(Canvas canvas) {

        // region AREA: mRoundedCornerBounds의 left, right, top, bottom 정보 할당
        Rect rect       = this.mRoundedCornerBounds;
        int rectLeft    = rect.left;
        int rectRight   = rect.right;
        int rectTop     = rect.top;
        int rectBottom  = rect.bottom;
        // endregion

        // region AREA: Rounded corner mode 기준으로 TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT 이미지 할당
        /*
         *  MEMO: World8848
         *  mRoundedCornerMode = 3인 경우
         *  this.mRoundedCornerMode & 1 = 1 이기 때문에  TOP_LEFT        Round
         *  this.mRoundedCornerMode & 2 = 2 이기 때문에  TOP_RIGHT       Round
         *  this.mRoundedCornerMode & 4 = 0 이기 때문에  BOTTOM_LEFT     Rect
         *  this.mRoundedCornerMode & 8 = 0 이기 때문에  BOTTOM_RIGHT    Rect
         *
         *  mRoundedCornerMode = 12인 경우
         *  this.mRoundedCornerMode & 1 = 0 이기 때문에  TOP_LEFT        Rect
         *  this.mRoundedCornerMode & 2 = 0 이기 때문에  TOP_RIGHT       Rect
         *  this.mRoundedCornerMode & 4 = 4 이기 때문에  BOTTOM_LEFT     Round
         *  this.mRoundedCornerMode & 8 = 8 이기 때문에  BOTTOM_RIGHT    Round
         */

        if ((this.mRoundedCornerMode & 1) != 0) {
            Drawable drawableTopLeftRound   = this.mTopLeftRound;
            int roundRadius                 = this.mRoundRadius;
            drawableTopLeftRound.setBounds(rectLeft, rectTop, rectLeft + roundRadius, roundRadius + rectTop);
            this.mTopLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 2) != 0) {
            Drawable drawableTopRightRound  = this.mTopRightRound;
            int roundRadius                 = this.mRoundRadius;
            drawableTopRightRound.setBounds(rectRight - roundRadius, rectTop, rectRight, roundRadius + rectTop);
            this.mTopRightRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 4) != 0) {
            Drawable drawableBottomLeftRound    = this.mBottomLeftRound;
            int roundRadius                     = this.mRoundRadius;
            drawableBottomLeftRound.setBounds(rectLeft, rectBottom - roundRadius, roundRadius + rectLeft, rectBottom);
            this.mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            Drawable drawableBottomRightRound   = this.mBottomRightRound;
            int roundRadius                     = this.mRoundRadius;
            drawableBottomRightRound.setBounds(rectRight - roundRadius, rectBottom - roundRadius, rectRight, rectBottom);
            this.mBottomRightRound.draw(canvas);
        }
        // endregion
    }
    // endregion


    // region AREA: initRoundedCorner
    /**
     * initRoundedCorner
     * RoundRadius를 초기화(26.0f)한다.
     * Rounded corner image 및 ColorFilter를 할당한다. Round color에 window background 정보를 할당한다.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initRoundedCorner() {

        // MEMO: World8848. 단위 변환. 26.0f를 px로 변환
        this.mRoundRadius           = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26.0f, this.mRes.getDisplayMetrics());

        // region AREA: Theme에 따라 Rounded corner image 설정
        Resources.Theme theme       = this.mContext.getTheme();
        if (this.mIsMutate) {
            this.mTopLeftRound      = this.mRes.getDrawable(R.drawable.sesl_top_left_round,     theme).mutate();
            this.mTopRightRound     = this.mRes.getDrawable(R.drawable.sesl_top_right_round,    theme).mutate();
            this.mBottomLeftRound   = this.mRes.getDrawable(R.drawable.sesl_bottom_left_round,  theme).mutate();
            this.mBottomRightRound  = this.mRes.getDrawable(R.drawable.sesl_bottom_right_round, theme).mutate();
        } else {
            this.mTopLeftRound      = this.mRes.getDrawable(R.drawable.sesl_top_left_round,     theme);
            this.mTopRightRound     = this.mRes.getDrawable(R.drawable.sesl_top_right_round,    theme);
            this.mBottomLeftRound   = this.mRes.getDrawable(R.drawable.sesl_bottom_left_round,  theme);
            this.mBottomRightRound  = this.mRes.getDrawable(R.drawable.sesl_bottom_right_round, theme);
        }
        // endregion

        // region AREA: Round color에 window background color 정보를 할당한다.
        // MEMO: World8848. color. color에 window background 정보를 할당한다.
        int color = getColor(mContext, android.R.attr.windowBackground);

        this.mBottomRightRoundColor     = color;
        this.mBottomLeftRoundColor      = color;
        this.mTopRightRoundColor        = color;
        this.mTopLeftRoundColor         = color;
        // endregion

        // region AREA: Round Image에 Color filter를 할당한다.
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(this.mTopLeftRoundColor, PorterDuff.Mode.SRC_IN);
        this.mTopLeftRound.setColorFilter(porterDuffColorFilter);
        this.mTopRightRound.setColorFilter(porterDuffColorFilter);
        this.mBottomLeftRound.setColorFilter(porterDuffColorFilter);
        this.mBottomRightRound.setColorFilter(porterDuffColorFilter);
        // endregion

    }
    // endregion


    // region AREA: getColor
    /**
     * getColor
     * color를 반환한다.
     * @param context Context
     * @param colorResId int
     * @return int
     */
    private int getColor(Context context, int colorResId) {

        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{colorResId});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();

        return color;
    }
    // endregion


    // region AREA: removeRoundedCorner
    private void removeRoundedCorner(int i) {
        if ((i & 1) != 0) {
            this.mTopLeftRound = null;
        }
        if ((i & 2) != 0) {
            this.mTopRightRound = null;
        }
        if ((i & 4) != 0) {
            this.mBottomLeftRound = null;
        }
        if ((i & 8) != 0) {
            this.mBottomRightRound = null;
        }
    }
    // endregion

    // endregion                                                    ////////////////////////////////



    // region AREA: Public Methods                                  ////////////////////////////////

    // region AREA: drawRoundedCorner
    /**
     * drawRoundedCorner
     * canvas.getClipBounds()와 drawRoundedCornerInternal()을 호출한다.
     * @param canvas Canvas
     */
    public void drawRoundedCorner(Canvas canvas) {
        canvas.getClipBounds(this.mRoundedCornerBounds);
        drawRoundedCornerInternal(canvas);
    }


    /**
     * drawRoundedCorner
     * x, y 값을 설정하고 사각형을 생성한다. drawRoundedCornerInternal()을 호출한다.
     * @param view View
     * @param canvas Canvas
     */
    public void drawRoundedCorner(View view, Canvas canvas) {

        // region AREA: mX, mY 값을 할당한다.
        if (view.getTranslationY() != 0.0f) {
            this.mX = Math.round(view.getX());
            this.mY = Math.round(view.getY());
            canvas.translate((view.getX() - ((float) this.mX)) + 0.5f, (view.getY() - ((float) this.mY)) + 0.5f);
        } else {
            this.mX = view.getLeft();
            this.mY = view.getTop();
        }
        // endregion

        // region AREA: 사각형을 생성한다.
        Rect rect = this.mRoundedCornerBounds;
        int i = this.mX;
        rect.set(i, this.mY, view.getWidth() + i, this.mY + view.getHeight());
        // endregion

        drawRoundedCornerInternal(canvas);
    }
    // endregion


    // region AREA: getRoundedCornerColor
    /**
     * getRoundedCornerColor
     * Rounded Corner의 색상 정보를 반환한다.
     * •1 : TOP_LEFT	•2 : TOP_RIGHT		•3 : BOTTOM_LEFT	•4 : BOTTOM_RIGHT
     * 그외의 값이 들어가면 IllegalArgumentException 처리된다.
     * @param roundedCornerPosition Int, 1 : TOP_LEFT, 2 : TOP_RIGHT, 3 : BOTTOM_LEFT, 4 : BOTTOM_RIGHT
     * @return int
     */
    @ColorInt
    public int getRoundedCornerColor(int roundedCornerPosition ) {
        if (roundedCornerPosition == 0) {
            throw new IllegalArgumentException("There is no rounded corner on = " + this);
        } else if (roundedCornerPosition  == 1 || roundedCornerPosition  == 2 || roundedCornerPosition  == 4 || roundedCornerPosition  == 8) {
            return (roundedCornerPosition  & 1) != 0 ? this.mTopLeftRoundColor : (roundedCornerPosition  & 2) != 0 ? this.mTopRightRoundColor : (roundedCornerPosition  & 4) != 0 ? this.mBottomLeftRoundColor : this.mBottomRightRoundColor;
        } else {
            throw new IllegalArgumentException("Use multiple rounded corner as param on = " + this);
        }
    }
    // endregion


    // region AREA: getRoundedCornerRadius
    /**
     * getRoundedCornerRadius
     * Rounded corners의 Radius를 반환한다.
     * 단위는 px이다. 기본은 52px이다.
     * @return int, Radius, px
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
    public int getRoundedCornerRadius() {
        return this.mRoundRadius;
    }
    // endregion


    // region AREA: getRoundedCorners
    /**
     * getRoundedCorners
     * RoundedCornerMode를 반환한다.
     * @return int
     */
    public int getRoundedCorners() {
        return this.mRoundedCornerMode;
    }
    // endregion


    // region AREA: setRoundedCornerColor
    /**
     * setRoundedCornerColor
     * Round corner type(1~15)에 따라 해당 위치의 Corner 색상을 설정한다.
     * @param roundedCornerType int
     * @param roundedCornerColor @ColorInt int
     */
    public void setRoundedCornerColor(int roundedCornerType, @ColorInt int roundedCornerColor) {
        if (roundedCornerType == 0) {
            throw new IllegalArgumentException("There is no rounded corner on = " + this);
        } else if ((roundedCornerType & -16) == 0) {
            if (this.mTopLeftRound == null || this.mTopRightRound == null || this.mBottomLeftRound == null || this.mBottomRightRound == null) {
                initRoundedCorner();
            }
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(roundedCornerColor, PorterDuff.Mode.SRC_IN);
            if ((roundedCornerType & 1) != 0) {
                this.mTopLeftRoundColor = roundedCornerColor;
                this.mTopLeftRound.setColorFilter(porterDuffColorFilter);
            }
            if ((roundedCornerType & 2) != 0) {
                this.mTopRightRoundColor = roundedCornerColor;
                this.mTopRightRound.setColorFilter(porterDuffColorFilter);
            }
            if ((roundedCornerType & 4) != 0) {
                this.mBottomLeftRoundColor = roundedCornerColor;
                this.mBottomLeftRound.setColorFilter(porterDuffColorFilter);
            }
            if ((roundedCornerType & 8) != 0) {
                this.mBottomRightRoundColor = roundedCornerColor;
                this.mBottomRightRound.setColorFilter(porterDuffColorFilter);
            }
        } else {
            throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + roundedCornerType);
        }
    }
    // endregion


    // region AREA: setRoundedCorners
    /**
     * setRoundedCorners
     * 전달하는 인수 i의 값이 0 ~ 15의 범위에 있으면 RoundedCornerMode는 1로 할당한다.
     * TopLeft, To-pRight, BottomLeft, BottomRight 중 하나라도 null이 있으면 initRoundedCorner()를 호출하고 그렇지 않으면 호출하지 않는다.
     * @param roundedCornerType int
     */
    public void setRoundedCorners(int roundedCornerType) {
        if ((roundedCornerType & -16) == 0) {
            this.mRoundedCornerMode = roundedCornerType;
            if (this.mTopLeftRound == null || this.mTopRightRound == null || this.mBottomLeftRound == null || this.mBottomRightRound == null) {
                initRoundedCorner();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + roundedCornerType);
    }
    // endregion

    // endregion                                                    ////////////////////////////////

}