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
     * Rect??? left, right, top, bottom ????????? ????????????.
     * RoundedCornerMode??? ?????? TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT ????????? Drawable Image??? ????????????.
     * @param canvas Canvas
     */
    private void drawRoundedCornerInternal(Canvas canvas) {

        // region AREA: mRoundedCornerBounds??? left, right, top, bottom ?????? ??????
        Rect rect       = this.mRoundedCornerBounds;
        int rectLeft    = rect.left;
        int rectRight   = rect.right;
        int rectTop     = rect.top;
        int rectBottom  = rect.bottom;
        // endregion

        // region AREA: Rounded corner mode ???????????? TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT ????????? ??????
        /*
         *  MEMO: World8848
         *  mRoundedCornerMode = 3??? ??????
         *  this.mRoundedCornerMode & 1 = 1 ?????? ?????????  TOP_LEFT        Round
         *  this.mRoundedCornerMode & 2 = 2 ?????? ?????????  TOP_RIGHT       Round
         *  this.mRoundedCornerMode & 4 = 0 ?????? ?????????  BOTTOM_LEFT     Rect
         *  this.mRoundedCornerMode & 8 = 0 ?????? ?????????  BOTTOM_RIGHT    Rect
         *
         *  mRoundedCornerMode = 12??? ??????
         *  this.mRoundedCornerMode & 1 = 0 ?????? ?????????  TOP_LEFT        Rect
         *  this.mRoundedCornerMode & 2 = 0 ?????? ?????????  TOP_RIGHT       Rect
         *  this.mRoundedCornerMode & 4 = 4 ?????? ?????????  BOTTOM_LEFT     Round
         *  this.mRoundedCornerMode & 8 = 8 ?????? ?????????  BOTTOM_RIGHT    Round
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
     * RoundRadius??? ?????????(26.0f)??????.
     * Rounded corner image ??? ColorFilter??? ????????????. Round color??? window background ????????? ????????????.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initRoundedCorner() {

        // MEMO: World8848. ?????? ??????. 26.0f??? px??? ??????
        this.mRoundRadius           = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26.0f, this.mRes.getDisplayMetrics());

        // region AREA: Theme??? ?????? Rounded corner image ??????
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

        // region AREA: Round color??? window background color ????????? ????????????.
        // MEMO: World8848. color. color??? window background ????????? ????????????.
        int color = getColor(mContext, android.R.attr.windowBackground);

        this.mBottomRightRoundColor     = color;
        this.mBottomLeftRoundColor      = color;
        this.mTopRightRoundColor        = color;
        this.mTopLeftRoundColor         = color;
        // endregion

        // region AREA: Round Image??? Color filter??? ????????????.
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
     * color??? ????????????.
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
     * canvas.getClipBounds()??? drawRoundedCornerInternal()??? ????????????.
     * @param canvas Canvas
     */
    public void drawRoundedCorner(Canvas canvas) {
        canvas.getClipBounds(this.mRoundedCornerBounds);
        drawRoundedCornerInternal(canvas);
    }


    /**
     * drawRoundedCorner
     * x, y ?????? ???????????? ???????????? ????????????. drawRoundedCornerInternal()??? ????????????.
     * @param view View
     * @param canvas Canvas
     */
    public void drawRoundedCorner(View view, Canvas canvas) {

        // region AREA: mX, mY ?????? ????????????.
        if (view.getTranslationY() != 0.0f) {
            this.mX = Math.round(view.getX());
            this.mY = Math.round(view.getY());
            canvas.translate((view.getX() - ((float) this.mX)) + 0.5f, (view.getY() - ((float) this.mY)) + 0.5f);
        } else {
            this.mX = view.getLeft();
            this.mY = view.getTop();
        }
        // endregion

        // region AREA: ???????????? ????????????.
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
     * Rounded Corner??? ?????? ????????? ????????????.
     * ???1 : TOP_LEFT	???2 : TOP_RIGHT		???3 : BOTTOM_LEFT	???4 : BOTTOM_RIGHT
     * ????????? ?????? ???????????? IllegalArgumentException ????????????.
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
     * Rounded corners??? Radius??? ????????????.
     * ????????? px??????. ????????? 52px??????.
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
     * RoundedCornerMode??? ????????????.
     * @return int
     */
    public int getRoundedCorners() {
        return this.mRoundedCornerMode;
    }
    // endregion


    // region AREA: setRoundedCornerColor
    /**
     * setRoundedCornerColor
     * Round corner type(1~15)??? ?????? ?????? ????????? Corner ????????? ????????????.
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
     * ???????????? ?????? i??? ?????? 0 ~ 15??? ????????? ????????? RoundedCornerMode??? 1??? ????????????.
     * TopLeft, To-pRight, BottomLeft, BottomRight ??? ???????????? null??? ????????? initRoundedCorner()??? ???????????? ????????? ????????? ???????????? ?????????.
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