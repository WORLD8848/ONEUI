package com.buxuan.baseoneui.sesl.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;

import com.buxuan.baseoneui.R;

import java.util.List;

// MEMO: World8848. add. public
public class SeslColorSwatchView extends View {


    // region AREA: Variables                                       ////////////////////////////////
    private static final int MAX_SWATCH_VIEW_ID = 110;
    private static final int SWATCH_ITEM_COLUMN = 11;
    private static final int SWATCH_ITEM_ROW = 10;
    private static final float SWATCH_ITEM_SIZE_ROUNDING_VALUE = 0.5F;
    private static String TAG;
    private int[][] mColorBrightness;
    private int[][] mColorSwatch;
    private StringBuilder[][] mColorSwatchDescription;
    private Context mContext;
    private GradientDrawable mCursorDrawable;
    private Point mCursorIndex;
    private Rect mCursorRect;
    private boolean mFromUser;
    private boolean mIsColorInSwatch;
    private OnColorSwatchChangedListener mListener;
    private Resources mResources;
    private int mSelectedVirtualViewId;
    private float mSwatchItemHeight;
    private float mSwatchItemWidth;
    private SeslColorSwatchViewTouchHelper mTouchHelper;
    // endregion                                                    ////////////////////////////////



    // region AREA: Constructor                                     ////////////////////////////////


    public SeslColorSwatchView(Context var1) {
        this(var1, (AttributeSet) null);
    }


    public SeslColorSwatchView(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }


    public SeslColorSwatchView(Context var1, AttributeSet var2, int var3) {
        this(var1, var2, var3, 0);
    }


    public SeslColorSwatchView(Context context, AttributeSet attributeSet, int var3, int var4) {

        super(context, attributeSet, var3, var4);

        // region AREA: Variables
        this.mSelectedVirtualViewId     = -1;
        this.mFromUser                  = false;
        this.mIsColorInSwatch           = true;
        this.mColorSwatchDescription    = new StringBuilder[11][10];
        this.mContext                   = context;
        this.mResources                 = this.mContext.getResources();
        // endregion


        // region AREA: Color swatch. color int -> hexColor
            // region AREA: Color swatch 1열
            // Log.d("88888888_COLUMN_1", "****************************************************************************=");
            // Log.d("88888888_COLUMN_1", "  -1                =  #"+Integer.toHexString(-1                ).substring(2));    // #ffffff
            // Log.d("88888888_COLUMN_1", "  -3355444          =  #"+Integer.toHexString(-3355444          ).substring(2));    // #cccccc
            // Log.d("88888888_COLUMN_1", "  -5000269          =  #"+Integer.toHexString(-5000269          ).substring(2));    // #b3b3b3
            // Log.d("88888888_COLUMN_1", "  -6710887          =  #"+Integer.toHexString(-6710887          ).substring(2));    // #999999
            // Log.d("88888888_COLUMN_1", "  -8224126          =  #"+Integer.toHexString(-8224126          ).substring(2));    // #828282
            // Log.d("88888888_COLUMN_1", "  -10066330         =  #"+Integer.toHexString(-10066330         ).substring(2));    // #666666
            // Log.d("88888888_COLUMN_1", "  -11711155         =  #"+Integer.toHexString(-11711155         ).substring(2));    // #4d4d4d
            // Log.d("88888888_COLUMN_1", "  -13421773         =  #"+Integer.toHexString(-13421773         ).substring(2));    // #333333
            // Log.d("88888888_COLUMN_1", "  -15066598         =  #"+Integer.toHexString(-15066598         ).substring(2));    // #1a1a1a
            // Log.d("88888888_COLUMN_1", "  -16777216         =  #"+Integer.toHexString(-16777216         ).substring(2));    // #000000
            // Log.d("88888888_COLUMN_1", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 2열
            // Log.d("88888888_COLUMN_2", "****************************************************************************=");
            // Log.d("88888888_COLUMN_2", "  -22360            =  #"+Integer.toHexString(-22360            ).substring(2));    // #ffa8a8
            // Log.d("88888888_COLUMN_2", "  -38037            =  #"+Integer.toHexString(-38037            ).substring(2));    // #ff6b6b
            // Log.d("88888888_COLUMN_2", "  -38037            =  #"+Integer.toHexString(-38037            ).substring(2));    // #ff6b6b
            // Log.d("88888888_COLUMN_2", "  -60396            =  #"+Integer.toHexString(-60396            ).substring(2));    // #ff1414
            // Log.d("88888888_COLUMN_2", "  -65536            =  #"+Integer.toHexString(-65536            ).substring(2));    // #ff0000
            // Log.d("88888888_COLUMN_2", "  -393216           =  #"+Integer.toHexString(-393216           ).substring(2));    // #fa0000
            // Log.d("88888888_COLUMN_2", "  -2424832          =  #"+Integer.toHexString(-2424832          ).substring(2));    // #db0000
            // Log.d("88888888_COLUMN_2", "  -5767168          =  #"+Integer.toHexString(-5767168          ).substring(2));    // #a80000
            // Log.d("88888888_COLUMN_2", "  -10747904         =  #"+Integer.toHexString(-10747904         ).substring(2));    // #5c0000
            // Log.d("88888888_COLUMN_2", "  -13434880         =  #"+Integer.toHexString(-13434880         ).substring(2));    // #330000
            // Log.d("88888888_COLUMN_2", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 3열
            // Log.d("88888888_COLUMN_3", "****************************************************************************=");
            // Log.d("88888888_COLUMN_3", "  -11096            =  #"+Integer.toHexString(-11096            ).substring(2));    // #ffd4a8
            // Log.d("88888888_COLUMN_3", "  -19093            =  #"+Integer.toHexString(-19093            ).substring(2));    // #ffb56b
            // Log.d("88888888_COLUMN_3", "  -25544            =  #"+Integer.toHexString(-25544            ).substring(2));    // #ff9c38
            // Log.d("88888888_COLUMN_3", "  -30705            =  #"+Integer.toHexString(-30705            ).substring(2));    // #ff880f
            // Log.d("88888888_COLUMN_3", "  -32768            =  #"+Integer.toHexString(-32768            ).substring(2));    // #ff8000
            // Log.d("88888888_COLUMN_3", "  -361216           =  #"+Integer.toHexString(-361216           ).substring(2));    // #fa7d00
            // Log.d("88888888_COLUMN_3", "  -2396672          =  #"+Integer.toHexString(-2396672          ).substring(2));    // #db6e00
            // Log.d("88888888_COLUMN_3", "  -5745664          =  #"+Integer.toHexString(-5745664          ).substring(2));    // #a85400
            // Log.d("88888888_COLUMN_3", "  -10736128         =  #"+Integer.toHexString(-10736128         ).substring(2));    // #5c2e00
            // Log.d("88888888_COLUMN_3", "  -13428224         =  #"+Integer.toHexString(-13428224         ).substring(2));    // #331a00
            // Log.d("88888888_COLUMN_3", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 4열
            // Log.d("88888888_COLUMN_4", "****************************************************************************=");
            // Log.d("88888888_COLUMN_4", "  -88               =  #"+Integer.toHexString(-88               ).substring(2));    // #ffffa8
            // Log.d("88888888_COLUMN_4", "  -154              =  #"+Integer.toHexString(-154              ).substring(2));    // #ffff66
            // Log.d("88888888_COLUMN_4", "  -200              =  #"+Integer.toHexString(-200              ).substring(2));    // #ffff38
            // Log.d("88888888_COLUMN_4", "  -256              =  #"+Integer.toHexString(-256              ).substring(2));    // #ffff00
            // Log.d("88888888_COLUMN_4", "  -256              =  #"+Integer.toHexString(-256              ).substring(2));    // #ffff00
            // Log.d("88888888_COLUMN_4", "  -329216           =  #"+Integer.toHexString(-329216           ).substring(2));    // #fafa00
            // Log.d("88888888_COLUMN_4", "  -2368768          =  #"+Integer.toHexString(-2368768          ).substring(2));    // #dbdb00
            // Log.d("88888888_COLUMN_4", "  -6053120          =  #"+Integer.toHexString(-6053120          ).substring(2));    // #a3a300
            // Log.d("88888888_COLUMN_4", "  -10724352         =  #"+Integer.toHexString(-10724352         ).substring(2));    // #5c5c00
            // Log.d("88888888_COLUMN_4", "  -13421824         =  #"+Integer.toHexString(-13421824         ).substring(2));    // #333300
            // Log.d("88888888_COLUMN_4", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 5열
            // Log.d("88888888_COLUMN_5", "****************************************************************************=");
            // Log.d("88888888_COLUMN_5", "  -5701720          =  #"+Integer.toHexString(-5701720          ).substring(2));    // #a8ffa8
            // Log.d("88888888_COLUMN_5", "  -10027162         =  #"+Integer.toHexString(-10027162         ).substring(2));    // #66ff66
            // Log.d("88888888_COLUMN_5", "  -13041864         =  #"+Integer.toHexString(-13041864         ).substring(2));    // #38ff38
            // Log.d("88888888_COLUMN_5", "  -16056566         =  #"+Integer.toHexString(-16056566         ).substring(2));    // #0aff0a
            // Log.d("88888888_COLUMN_5", "  -16711936         =  #"+Integer.toHexString(-16711936         ).substring(2));    // #00ff00
            // Log.d("88888888_COLUMN_5", "  -16713216         =  #"+Integer.toHexString(-16713216         ).substring(2));    // #00fa00
            // Log.d("88888888_COLUMN_5", "  -16721152         =  #"+Integer.toHexString(-16721152         ).substring(2));    // #00db00
            // Log.d("88888888_COLUMN_5", "  -16735488         =  #"+Integer.toHexString(-16735488         ).substring(2));    // #00a300
            // Log.d("88888888_COLUMN_5", "  -16753664         =  #"+Integer.toHexString(-16753664         ).substring(2));    // #005c00
            // Log.d("88888888_COLUMN_5", "  -16764160         =  #"+Integer.toHexString(-16764160         ).substring(2));    // #003300
            // Log.d("88888888_COLUMN_5", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 6열
            // Log.d("88888888_COLUMN_6", "****************************************************************************=");
            // Log.d("88888888_COLUMN_6", "  -5701685          =  #"+Integer.toHexString(-5701685          ).substring(2));    // #a8ffcb
            // Log.d("88888888_COLUMN_6", "  -10027101         =  #"+Integer.toHexString(-10027101         ).substring(2));    // #66ffa3
            // Log.d("88888888_COLUMN_6", "  -3041784          =  #"+Integer.toHexString(-13041784         ).substring(2));    // #38ff88
            // Log.d("88888888_COLUMN_6", "  -15728785         =  #"+Integer.toHexString(-15728785         ).substring(2));    // #0fff6f
            // Log.d("88888888_COLUMN_6", "  -16711834         =  #"+Integer.toHexString(-16711834         ).substring(2));    // #00ff66
            // Log.d("88888888_COLUMN_6", "  -16714398         =  #"+Integer.toHexString(-16714398         ).substring(2));    // #00f562
            // Log.d("88888888_COLUMN_6", "  -16721064         =  #"+Integer.toHexString(-16721064         ).substring(2));    // #00db58
            // Log.d("88888888_COLUMN_6", "  -16735423         =  #"+Integer.toHexString(-16735423         ).substring(2));    // #00a341
            // Log.d("88888888_COLUMN_6", "  -16753627         =  #"+Integer.toHexString(-16753627         ).substring(2));    // #005c25
            // Log.d("88888888_COLUMN_6", "  -16764140         =  #"+Integer.toHexString(-16764140         ).substring(2));    // #003314
            // Log.d("88888888_COLUMN_6", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 7열
            // Log.d("88888888_COLUMN_7", "****************************************************************************=");
            // Log.d("88888888_COLUMN_7", "  -5701633          =  #"+Integer.toHexString(-5701633          ).substring(2));    // #a8ffff
            // Log.d("88888888_COLUMN_7", "  -10027009         =  #"+Integer.toHexString(-10027009         ).substring(2));    // #66ffff
            // Log.d("88888888_COLUMN_7", "  -12713985         =  #"+Integer.toHexString(-12713985         ).substring(2));    // #3dffff
            // Log.d("88888888_COLUMN_7", "  -16056321         =  #"+Integer.toHexString(-16056321         ).substring(2));    // #0affff
            // Log.d("88888888_COLUMN_7", "  -16711681         =  #"+Integer.toHexString(-16711681         ).substring(2));    // #00ffff
            // Log.d("88888888_COLUMN_7", "  -16714251         =  #"+Integer.toHexString(-16714251         ).substring(2));    // #00f5f5
            // Log.d("88888888_COLUMN_7", "  -16720933         =  #"+Integer.toHexString(-16720933         ).substring(2));    // #00dbdb
            // Log.d("88888888_COLUMN_7", "  -16735325         =  #"+Integer.toHexString(-16735325         ).substring(2));    // #00a3a3
            // Log.d("88888888_COLUMN_7", "  -16753572         =  #"+Integer.toHexString(-16753572         ).substring(2));    // #005c5c
            // Log.d("88888888_COLUMN_7", "  -16764109         =  #"+Integer.toHexString(-16764109         ).substring(2));    // #003333
            // Log.d("88888888_COLUMN_7", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 8열
            // Log.d("88888888_COLUMN_8", "****************************************************************************=");
            // Log.d("88888888_COLUMN_8", "  -5712641          =  #"+Integer.toHexString(-5712641          ).substring(2));    // #a8d4ff
            // Log.d("88888888_COLUMN_8", "  -9718273          =  #"+Integer.toHexString(-9718273          ).substring(2));    // #6bb5ff
            // Log.d("88888888_COLUMN_8", "  -13067009         =  #"+Integer.toHexString(-13067009         ).substring(2));    // #389cff
            // Log.d("88888888_COLUMN_8", "  -15430913         =  #"+Integer.toHexString(-15430913         ).substring(2));    // #148aff
            // Log.d("88888888_COLUMN_8", "  -16744193         =  #"+Integer.toHexString(-16744193         ).substring(2));    // #0080ff
            // Log.d("88888888_COLUMN_8", "  -16744966         =  #"+Integer.toHexString(-16744966         ).substring(2));    // #007dfa
            // Log.d("88888888_COLUMN_8", "  -16748837         =  #"+Integer.toHexString(-16748837         ).substring(2));    // #006edb
            // Log.d("88888888_COLUMN_8", "  -16755544         =  #"+Integer.toHexString(-16755544         ).substring(2));    // #0054a8
            // Log.d("88888888_COLUMN_8", "  -16764575         =  #"+Integer.toHexString(-16764575         ).substring(2));    // #003161
            // Log.d("88888888_COLUMN_8", "  -16770509         =  #"+Integer.toHexString(-16770509         ).substring(2));    // #001a33
            // Log.d("88888888_COLUMN_8", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 9열
            // Log.d("88888888_COLUMN_9", "****************************************************************************=");
            // Log.d("88888888_COLUMN_9", "  -5723905          =  #"+Integer.toHexString(-5723905          ).substring(2));    // #a8a8ff
            // Log.d("88888888_COLUMN_9", "  -9737217          =  #"+Integer.toHexString(-9737217          ).substring(2));    // #6b6bff
            // Log.d("88888888_COLUMN_9", "  -13092609         =  #"+Integer.toHexString(-13092609         ).substring(2));    // #3838ff
            // Log.d("88888888_COLUMN_9", "  -16119041         =  #"+Integer.toHexString(-16119041         ).substring(2));    // #0a0aff
            // Log.d("88888888_COLUMN_9", "  -16776961         =  #"+Integer.toHexString(-16776961         ).substring(2));    // #0000ff
            // Log.d("88888888_COLUMN_9", "  -16776966         =  #"+Integer.toHexString(-16776966         ).substring(2));    // #0000fa
            // Log.d("88888888_COLUMN_9", "  -16776997         =  #"+Integer.toHexString(-16776997         ).substring(2));    // #0000db
            // Log.d("88888888_COLUMN_9", "  -16777048         =  #"+Integer.toHexString(-16777048         ).substring(2));    // #0000a8
            // Log.d("88888888_COLUMN_9", "  -16777119         =  #"+Integer.toHexString(-16777119         ).substring(2));    // #000061
            // Log.d("88888888_COLUMN_9", "  -16777165         =  #"+Integer.toHexString(-16777165         ).substring(2));    // #000033
            // Log.d("88888888_COLUMN_9", "****************************************************************************=");
            // endregion

            // region AREA: Color swatch 10열
            // Log.d("88888888_COLUMN_10","****************************************************************************=");
            // Log.d("88888888_COLUMN_10","  -3430145          =  #"+Integer.toHexString(-3430145          ).substring(2));    // #cba8ff
            // Log.d("88888888_COLUMN_10","  -5870593          =  #"+Integer.toHexString(-5870593          ).substring(2));    // #a66bff
            // Log.d("88888888_COLUMN_10","  -7849729          =  #"+Integer.toHexString(-7849729          ).substring(2));    // #8838ff
            // Log.d("88888888_COLUMN_10","  -9498625          =  #"+Integer.toHexString(-9498625          ).substring(2));    // #6f0fff
            // Log.d("88888888_COLUMN_10","  -10092289         =  #"+Integer.toHexString(-10092289         ).substring(2));    // #6600ff
            // Log.d("88888888_COLUMN_10","  -10223366         =  #"+Integer.toHexString(-10223366         ).substring(2));    // #6400fa
            // Log.d("88888888_COLUMN_10","  -11009829         =  #"+Integer.toHexString(-11009829         ).substring(2));    // #5800db
            // Log.d("88888888_COLUMN_10","  -12386136         =  #"+Integer.toHexString(-12386136         ).substring(2));    // #4300a8
            // Log.d("88888888_COLUMN_10","  -14352292         =  #"+Integer.toHexString(-14352292         ).substring(2));    // #25005c
            // Log.d("88888888_COLUMN_10","  -15466445         =  #"+Integer.toHexString(-15466445         ).substring(2));    // #140033
            // Log.d("88888888_COLUMN_10","****************************************************************************=");
            // endregion

            // region AREA: Color swatch 11열
            // Log.d("88888888_COLUMN_11","****************************************************************************=");
            // Log.d("88888888_COLUMN_11","  -22273            =  #"+Integer.toHexString(-22273            ).substring(2));    // #ffa8ff
            // Log.d("88888888_COLUMN_11","  -39169            =  #"+Integer.toHexString(-39169            ).substring(2));    // #ff66ff
            // Log.d("88888888_COLUMN_11","  -50945            =  #"+Integer.toHexString(-50945            ).substring(2));    // #ff38ff
            // Log.d("88888888_COLUMN_11","  -61441            =  #"+Integer.toHexString(-61441            ).substring(2));    // #ff0fff
            // Log.d("88888888_COLUMN_11","  -65281            =  #"+Integer.toHexString(-65281            ).substring(2));    // #ff00ff
            // Log.d("88888888_COLUMN_11","  -392966           =  #"+Integer.toHexString(-392966           ).substring(2));    // #fa00fa
            // Log.d("88888888_COLUMN_11","  -2424613          =  #"+Integer.toHexString(-2424613          ).substring(2));    // #db00db
            // Log.d("88888888_COLUMN_11","  -5767000          =  #"+Integer.toHexString(-5767000          ).substring(2));    // #a800a8
            // Log.d("88888888_COLUMN_11","  -10420127         =  #"+Integer.toHexString(-10420127         ).substring(2));    // #610061
            // Log.d("88888888_COLUMN_11","  -13434829         =  #"+Integer.toHexString(-13434829         ).substring(2));    // #330033
            // Log.d("88888888_COLUMN_11","****************************************************************************=");
            // endregion
        // endregion


        // region AREA: Color swatch. 가로 11 X 세로 10
        int[] var12         = new int[]{    -22360,     -38037,     -38037,     -60396,     -65536,     -393216,    -2424832,   -5767168,   -10747904,  -13434880};     // MEMO: World8848. Color swatch column 2
        int[] var5          = new int[]{    -5701685,   -10027101,  -13041784,  -15728785,  -16711834,  -16714398,  -16721064,  -16735423,  -16753627,  -16764140};     // MEMO: World8848. Color swatch column 6
        int[] var6          = new int[]{    -5712641,   -9718273,   -13067009,  -15430913,  -16744193,  -16744966,  -16748837,  -16755544,  -16764575,  -16770509};     // MEMO: World8848. Color swatch column 8
        int[] var7          = new int[]{    -3430145,   -5870593,   -7849729,   -9498625,   -10092289,  -10223366,  -11009829,  -12386136,  -14352292,  -15466445};     // MEMO: World8848. Color swatch column 10
        this.mColorSwatch   = new int[][]{
                {-1,        -3355444,   -5000269,   -6710887,   -8224126,   -10066330,  -11711155,  -13421773,  -15066598,  -16777216},     // MEMO: World8848. Color swatch column 1
                var12,                                                                                                                      // MEMO: World8848. Color swatch column 2
                {-11096,    -19093,     -25544,     -30705,     -32768,     -361216,    -2396672,   -5745664,   -10736128,  -13428224},     // MEMO: World8848. Color swatch column 3
                {-88,       -154,       -200,       -256,       -256,       -329216,    -2368768,   -6053120,   -10724352,  -13421824},     // MEMO: World8848. Color swatch column 4
                {-5701720,  -10027162,  -13041864,  -16056566,  -16711936,  -16713216,  -16721152,  -16735488,  -16753664,  -16764160},     // MEMO: World8848. Color swatch column 5
                var5,                                                                                                                       // MEMO: World8848. Color swatch column 6
                {-5701633,  -10027009,  -12713985,  -16056321,  -16711681,  -16714251,  -16720933,  -16735325,  -16753572,  -16764109},     // MEMO: World8848. Color swatch column 7
                var6,                                                                                                                       // MEMO: World8848. Color swatch column 8
                {-5723905,  -9737217,   -13092609,  -16119041,  -16776961,  -16776966,  -16776997,  -16777048,  -16777119,  -16777165},     // MEMO: World8848. Color swatch column 9
                var7,                                                                                                                       // MEMO: World8848. Color swatch column 10
                {-22273,    -39169,     -50945,     -61441,     -65281,     -392966,    -2424613,   -5767000,   -10420127,  -13434829}      // MEMO: World8848. Color swatch column 11
        };
        // endregion


        // region AREA: Color swatch. 가로 11 X 세로 10. Color Brightness
        var12       = new int[]{100, 80, 70, 60, 51, 40, 30, 20, 10,  0};
        var5        = new int[]{83,  71, 62, 54, 50, 49, 43, 33, 18, 10};
        var6        = new int[]{83,  70, 61, 52, 50, 49, 43, 32, 18, 10};
        var7        = new int[]{83,  70, 61, 53, 50, 48, 43, 32, 18, 10};
        int[] var8  = new int[]{83,  70, 62, 52, 50, 48, 43, 32, 18, 10};
        int[] var9  = new int[]{83,  71, 61, 54, 50, 49, 43, 33, 19, 10};
        int[] var10 = new int[]{83,  71, 61, 53, 50, 49, 43, 33, 18, 10};
        int[] var11 = new int[]{83,  70, 61, 53, 50, 49, 43, 33, 19, 10};
        this.mColorBrightness = new int[][]{
                var12,
                var5,
                {83, 71, 61, 53, 50, 49, 43, 33, 18, 10},
                {83, 70, 61, 50, 50, 49, 43, 32, 18, 10},
                var6,
                var7,
                var8,
                var9,
                {83, 71, 61, 52, 50, 49, 43, 33, 19, 10},
                var10,
                var11};
        // endregion


        // region AREA: initCursorDrawable
        this.initCursorDrawable();
        // endregion


        // region AREA: initAccessibility
        this.initAccessibility();
        // endregion


        // region AREA: Color swatch width, height
        this.mSwatchItemHeight  = this.mResources.getDimension(R.dimen.sesl_color_picker_color_swatch_view_height) / 10.0F;
        this.mSwatchItemWidth   = this.mResources.getDimension(R.dimen.sesl_color_picker_color_swatch_view_width) / 11.0F;
        // endregion


        // region AREA: Cursor init
        this.mCursorIndex = new Point(-1, -1);
        // endregion

    }

    // endregion                                                    ////////////////////////////////



    // region AREA: Functions                                       ////////////////////////////////

    // region AREA: getColorSwatchDescriptionAt
    /**
     * ColorInt 값(예, Black: -16777216, White: -1 등)으로 Cursor가 위치한 Positon x, y 좌표를 구한 후 해당 좌표의 '색상, 밝기' 문자열을 반환한다.
     * @param colorInt ColorInt 값(예, Black: -16777216, White: -1 등)
     * @return  Cursor가 위치한 Positon x, y 좌표를 구한 후 해당 좌표의 '색상, 밝기' 문자열
     */
    StringBuilder getColorSwatchDescriptionAt(int colorInt) {

        Point var2 = this.getCursorIndexAt(colorInt);

        if (this.mFromUser) {

            return this.mColorSwatchDescription[var2.x][var2.y] == null ? this.mTouchHelper.getItemDescription(var2.x + var2.y * 11) : this.mColorSwatchDescription[var2.x][var2.y];

        } else {
            return null;
        }
    }
    // endregion


    // region AREA: getCursorIndexAt
    /**
     * Cursor index를 반환한다.
     * 전달받은 ColorInt 값으로부터 색상 ARGB 값을 생성한다.
     * Point 정보를 (-1, -1)로 초기화한다.
     * Color Swatch에 Color-int 값이 존재하는 확인한 후 존재하면 Color Swatch의 위치 값을 Point에 할당해서 반환한다.
     * @param colorInt int
     * @return Color swatch에서 cursor의 위치 정보
     */
    Point getCursorIndexAt(int colorInt) {

        // region AREA: ColorInt 값으로부터 ARGB 생성
        int var2            = Color.argb(255, colorInt >> 16 & 255, colorInt >> 8 & 255, colorInt & 255);
        // endregion

        // region AREA: Point 초기화
        Point colorIntPoint = new Point(-1, -1);
        // endregion


        // region AREA: Color-int 값이 mColorSwatch 배열 집합 어디에 있는지 전체 확인, 일치하면 Point 변수에 mColorSwatch의 배열 위치를 할당한다.
        this.mFromUser = false;

        // MEMO: World8848. Color-int 값이 mColorSwatch 배열 집합 머디에 있는지 전체 확인한다.
        for (colorInt = 0; colorInt < 11; ++colorInt) {
            for (int var4 = 0; var4 < 10; ++var4) {
                // MEMO: World8848. Color-int와 값이 일치하면 Point 변수에 mColorSwatch의 배열 위치를 할당한다.
                if (this.mColorSwatch[colorInt][var4] == var2) {
                    colorIntPoint.set(colorInt, var4);
                    this.mFromUser = true;
                }
            }
        }
        // endregion

        // region AREA: 예외 처리, 사용자가 선택하지 않았는데 포인트 위치가 (-1, -1)이 아닌 경우 Color swatch에 존재하지 않는 색상이다.
        this.mIsColorInSwatch = true;

        if (!this.mFromUser && !this.mCursorIndex.equals(-1, -1)) {
            this.mIsColorInSwatch = false;
            this.invalidate();
        }
        // endregion

        // region AREA: Cursor index 반환한다.
        return colorIntPoint;
        // endregion

    }
    // endregion


    // region AREA: initAccessibility
    private void initAccessibility() {
        this.mTouchHelper = new SeslColorSwatchViewTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, this.mTouchHelper);
        this.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
    }
    // endregion


    // region AREA: initCursorDrawable
    private void initCursorDrawable() {
        this.mCursorDrawable = (GradientDrawable) this.mResources.getDrawable(R.drawable.sesl_color_swatch_view_cursor, null);
        this.mCursorRect = new Rect();
    }
    // endregion


    // region AREA: setCursorIndexAt
    /**
     * Color Swatch 내에서 ColorInt 색상의 위치 정보를 찾아 CursorIndex에 할당한다.
     * @param colorInt int
     */
    private void setCursorIndexAt(int colorInt) {

        Point cursorIndexAt = this.getCursorIndexAt(colorInt);

        if (this.mFromUser) {
            this.mCursorIndex.set(cursorIndexAt.x, cursorIndexAt.y);
        }

    }
    // endregion


    // region AREA: setCursorRect
    /**
     * Color Swatch 내에서 사용될 사각형 Cursor를 생성한다.
     * @param rect Rect
     */
    private void setCursorRect(Rect rect) {
        rect.set((int) ((float)  this.mCursorIndex.x * this.mSwatchItemWidth + 0.5F     ),
                (int) ((float)  this.mCursorIndex.y * this.mSwatchItemHeight + 0.5F    ),
                (int) ((float) (this.mCursorIndex.x + 1) * this.mSwatchItemWidth + 0.5F),
                (int) ((float) (this.mCursorIndex.y + 1) * this.mSwatchItemHeight + 0.5F));
    }
    // endregion


    // region AREA: setOnColorSwatchChangedListener
    void setOnColorSwatchChangedListener(OnColorSwatchChangedListener onColorSwatchChangedListener) {
        this.mListener = onColorSwatchChangedListener;
    }
    // endregion


    // region AREA: setSelectedVirtualViewId
    /**
     * Color swatch의 색상 순서대로 선택한 색상에 가상 ViewID를 할당한다.
     */
    private void setSelectedVirtualViewId() {

        this.mSelectedVirtualViewId = this.mCursorIndex.y * 11 + this.mCursorIndex.x;
    }
    // endregion


    // region AREA: updateCursorPosition
    /**
     * 전달받은 colorInt 값으로 Cursor 위치 정보를 할당하고, 해당 위치에 사각형을 생성한다.
     * 선택한 위치를 mSelectedVirtualViewId에 할당한다. mSelectedVirtualViewId의 초기값은 -1이다.
     * @param colorInt int
     */
    public void updateCursorPosition(int colorInt) {
        // MEMO: World8848. Cursor x, y 정보 할당
        this.setCursorIndexAt(colorInt);

        if (this.mFromUser) {
            this.setCursorRect(this.mCursorRect);
            this.invalidate();
            this.setSelectedVirtualViewId();
        } else {
            // MEMO: World8848. mSelectedVirtualViewId. 초기화
            this.mSelectedVirtualViewId = -1;
        }
    }
    // endregion

    // endregion                                                    ////////////////////////////////






    private boolean setCursorIndexAt(float var1, float var2) {
        float var3 = this.mSwatchItemWidth * 11.0F;
        float var4 = this.mSwatchItemHeight * 10.0F;
        if (var1 >= var3) {
            --var3;
        } else {
            var3 = var1;
            if (var1 < 0.0F) {
                var3 = 0.0F;
            }
        }

        if (var2 >= var4) {
            var1 = var4 - 1.0F;
        } else {
            var1 = var2;
            if (var2 < 0.0F) {
                var1 = 0.0F;
            }
        }

        Point var5 = new Point(this.mCursorIndex.x, this.mCursorIndex.y);
        this.mCursorIndex.set((int) (var3 / this.mSwatchItemWidth), (int) (var1 / this.mSwatchItemHeight));
        return var5.equals(this.mCursorIndex) ^ true;
    }



    protected boolean dispatchHoverEvent(MotionEvent var1) {
        boolean var2;
        if (!this.mTouchHelper.dispatchHoverEvent(var1) && !super.dispatchHoverEvent(var1)) {
            var2 = false;
        } else {
            var2 = true;
        }

        return var2;
    }











    protected void onDraw(Canvas var1) {
        Paint var2 = new Paint();

        for (int var3 = 0; var3 < 11; ++var3) {
            int var4 = 0;

            while (var4 < 10) {
                var2.setColor(this.mColorSwatch[var3][var4]);
                float var5 = this.mSwatchItemWidth;
                float var6 = (float) ((int) ((float) var3 * var5 + 0.5F));
                float var7 = this.mSwatchItemHeight;
                float var8 = (float) ((int) ((float) var4 * var7 + 0.5F));
                var5 = (float) ((int) (var5 * (float) (var3 + 1) + 0.5F));
                ++var4;
                var1.drawRect(var6, var8, var5, (float) ((int) (var7 * (float) var4 + 0.5F)), var2);
            }
        }

        if (this.mIsColorInSwatch) {
            this.mCursorDrawable.setBounds(this.mCursorRect);
            this.mCursorDrawable.draw(var1);
        }

    }

    public boolean onTouchEvent(MotionEvent var1) {
        int var2 = var1.getAction();
        if (var2 != 0 && var2 != 1 && var2 == 2 && this.getParent() != null) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (this.setCursorIndexAt(var1.getX(), var1.getY()) || !this.mIsColorInSwatch) {
            this.setCursorRect(this.mCursorRect);
            this.setSelectedVirtualViewId();
            this.invalidate();
            OnColorSwatchChangedListener var3 = this.mListener;
            if (var3 != null) {
                var3.onColorSwatchChanged(this.mColorSwatch[this.mCursorIndex.x][this.mCursorIndex.y]);
            }
        }

        return true;
    }






    // region AREA: Interface. OnColorSwatchChangedListener     ////////////////////////////////////
    interface OnColorSwatchChangedListener {
        void onColorSwatchChanged(int var1);
    }
    // endregion                                                ////////////////////////////////////


    // region AREA: Sub Class. SeslColorSwatchViewTouchHelper   ////////////////////////////////////
    private class SeslColorSwatchViewTouchHelper extends ExploreByTouchHelper {

        // region AREA: Variables
        private final Rect mVirtualViewRect;
        private final String[][] mColorDescription;
        private int mVirtualCursorIndexX;
        private int mVirtualCursorIndexY;
        // endregion


        // region AREA: SeslColorSwatchViewTouchHelper
        public SeslColorSwatchViewTouchHelper(@NonNull View view) {
            super(view);
            String white                    = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_white);
            String gray_light               = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_light_gray);
            String gray                     = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_gray);
            String gray_dark                = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_dark_gray);
            String black                    = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_black);
            String red_light                = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_red);
            String red                      = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_red);
            String red_dark                 = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_red);
            String orange_light             = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_orange);
            String orange                   = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_orange);
            String orange_dark              = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_orange);
            String yellow_light             = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_light_yellow);
            String yellow                   = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_yellow);
            String yellow_dark              = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_yellow);
            String green_light              = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_light_green);
            String green                    = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_green);
            String green_dark               = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_dark_green);
            // String[] var17                 = new String[]{SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_light_green), SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_green), SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_dark_green)};
            String spring_green_light       = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_spring_green);
            String spring_green             = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_spring_green);
            String spring_green_dark        = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_spring_green);
            String cyan_light               = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_cyan);
            String cyan                     = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_cyan);
            String cyan_dark                = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_cyan);
            String azure_light              = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_azure);
            String azure                    = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_azure);
            String azure_dark               = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_azure);
            String blue_light               = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_blue);
            String blue                     = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_blue);
            String blue_dark                = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_blue);
            String violet_light             = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_violet);
            String violet                   = SeslColorSwatchView.this.mResources.getString(R.string.pen_palette_color_violet);
            String violet_dark              = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_violet);
            String magenta_light            = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_light_magenta);
            String magenta                  = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_magenta);
            String magenta_dark             = SeslColorSwatchView.this.mResources.getString(R.string.pen_swatch_color_dark_magenta);
            this.mColorDescription          = new String[][]{
                    {white,                 gray_light,     gray,               gray_dark,      black},
                    {red_light,             red,            red_dark},
                    {orange_light,          orange,         orange_dark},
                    {yellow_light,          yellow,         yellow_dark},
                    {green_light,           green,          green_dark},
//                    var17,
                    {spring_green_light,    spring_green,   spring_green_dark},
                    {cyan_light,            cyan,           cyan_dark},
                    {azure_light,           azure,          azure_dark},
                    {blue_light,            blue,           blue_dark},
                    {violet_light,          violet,         violet_dark},
                    {magenta_light,         magenta,        magenta_dark}};
            this.mVirtualViewRect   = new Rect();
        }
        // endregion


        // region AREA: getFocusedVirtualViewId
        private int getFocusedVirtualViewId() {
            return this.mVirtualCursorIndexX + this.mVirtualCursorIndexY * 11;
        }
        // endregion


        // region AREA: getItemDescription
        /**
         * Color Swatch의 Item 설명을 반환한다.
         * @param colorSwatchPosition Color Swatch의 Position(1번째 줄: [0][0]~[10][0], 2번째 줄: [0][1]~[10][1], ..., 10번째 줄: [0][9]~ [10][9])
         * @return Color의 명칭과 밝기를 '색상, 밝기' 의 문자열
         */
        private StringBuilder getItemDescription(int colorSwatchPosition) {

            // MEMO: World8848. colorSwatchPosition은 swatch의 순서. 1번째 줄 [0][0] ~ [10][0], 2번째 줄 [0][1] ~ [10][1] ....

            // region AREA: colorSwatchPosition으로 선택한 color가 위치하는 11 x 10 color swatch x, y 좌표를 생성한다.
            this.setVirtualCursorIndexAt(colorSwatchPosition);
            // endregion


            // region AREA: Color description에서 x, y 기준으로 명칭, 밝기를 조회한다.
            if (SeslColorSwatchView.this.mColorSwatchDescription[this.mVirtualCursorIndexX][this.mVirtualCursorIndexY] == null) {
                StringBuilder var2  = new StringBuilder();

                // region AREA: Color description에서 x, y 기준으로 명칭을 조회한다.
                colorSwatchPosition = this.mVirtualCursorIndexX;
                int var3;
                if (colorSwatchPosition == 0) {
                    var3 = this.mVirtualCursorIndexY;
                    if (var3 == 0) {
                        var2.append(this.mColorDescription[colorSwatchPosition][0]);
                    } else if (var3 < 3) {
                        var2.append(this.mColorDescription[colorSwatchPosition][1]);
                    } else if (var3 < 6) {
                        var2.append(this.mColorDescription[colorSwatchPosition][2]);
                    } else if (var3 < 9) {
                        var2.append(this.mColorDescription[colorSwatchPosition][3]);
                    } else {
                        var2.append(this.mColorDescription[colorSwatchPosition][4]);
                    }
                } else {
                    var3 = this.mVirtualCursorIndexY;
                    if (var3 < 3) {
                        var2.append(this.mColorDescription[colorSwatchPosition][0]);
                    } else if (var3 < 6) {
                        var2.append(this.mColorDescription[colorSwatchPosition][1]);
                    } else {
                        var2.append(this.mColorDescription[colorSwatchPosition][2]);
                    }
                }
                // endregion

                var2.append(", ");

                // region AREA: Color description에서 x, y 기준으로 밝기를 조회한다.
                var2.append(SeslColorSwatchView.this.mColorBrightness[this.mVirtualCursorIndexX][this.mVirtualCursorIndexY]);
                // endregion

                SeslColorSwatchView.this.mColorSwatchDescription[this.mVirtualCursorIndexX][this.mVirtualCursorIndexY] = var2;
            }
            // endregion


            return SeslColorSwatchView.this.mColorSwatchDescription[this.mVirtualCursorIndexX][this.mVirtualCursorIndexY];
        }
        // endregion


        // region AREA: getVirtualViewAt
        protected int getVirtualViewAt(float var1, float var2) {
            this.setVirtualCursorIndexAt(var1, var2);
            return this.getFocusedVirtualViewId();
        }
        // endregion


        // region AREA: getVisibleVirtualViews
        protected void getVisibleVirtualViews(List<Integer> var1) {
            for (int var2 = 0; var2 < 110; ++var2) {
                var1.add(var2);
            }

        }
        // endregion


        // region AREA: onVirtualViewClick
        private void onVirtualViewClick(int var1) {
            if (SeslColorSwatchView.this.mListener != null) {
                SeslColorSwatchView.this.mListener.onColorSwatchChanged(var1);
            }

            SeslColorSwatchView.this.mTouchHelper.sendEventForVirtualView(SeslColorSwatchView.this.mSelectedVirtualViewId, 1);
        }
        // endregion


        // region AREA: setVirtualCursorIndexAt
        private void setVirtualCursorIndexAt(float var1, float var2) {
            float var3 = SeslColorSwatchView.this.mSwatchItemWidth * 11.0F;
            float var4 = SeslColorSwatchView.this.mSwatchItemHeight * 10.0F;
            if (var1 >= var3) {
                --var3;
            } else {
                var3 = var1;
                if (var1 < 0.0F) {
                    var3 = 0.0F;
                }
            }

            if (var2 >= var4) {
                var1 = var4 - 1.0F;
            } else {
                var1 = var2;
                if (var2 < 0.0F) {
                    var1 = 0.0F;
                }
            }

            this.mVirtualCursorIndexX = (int) (var3 / SeslColorSwatchView.this.mSwatchItemWidth);
            this.mVirtualCursorIndexY = (int) (var1 / SeslColorSwatchView.this.mSwatchItemHeight);
        }


        /**
         * MEMO: World8848
         * 색상표(Color Swatch)의 Position 정보로 x, y 좌표를 생성한다.
         * x좌표는 mVirtualCursorIndexX, y좌표는 mVirtualCursorIndexY에 할당한다.
         * @param colorSwatchPosition 상표(Color Swatch)의 Position 정보
         */
        private void setVirtualCursorIndexAt(int colorSwatchPosition) {
            this.mVirtualCursorIndexX = colorSwatchPosition % 11;
            this.mVirtualCursorIndexY = colorSwatchPosition / 11;
        }
        // endregion


        // region AREA: setVirtualCursorRect
        private void setVirtualCursorRect(Rect var1) {
            var1.set((int) ((float) this.mVirtualCursorIndexX * SeslColorSwatchView.this.mSwatchItemWidth + 0.5F), (int) ((float) this.mVirtualCursorIndexY * SeslColorSwatchView.this.mSwatchItemHeight + 0.5F), (int) ((float) (this.mVirtualCursorIndexX + 1) * SeslColorSwatchView.this.mSwatchItemWidth + 0.5F), (int) ((float) (this.mVirtualCursorIndexY + 1) * SeslColorSwatchView.this.mSwatchItemHeight + 0.5F));
        }
        // endregion


        // region AREA: onPerformActionForVirtualView
        protected boolean onPerformActionForVirtualView(int var1, int var2, @Nullable Bundle var3) {
            if (var2 == 16) {
                this.setVirtualCursorIndexAt(var1);
                this.onVirtualViewClick(SeslColorSwatchView.this.mColorSwatch[this.mVirtualCursorIndexX][this.mVirtualCursorIndexY]);
            }

            return false;
        }
        // endregion


        // region AREA: onPopulateEventForVirtualView
        protected void onPopulateEventForVirtualView(int var1, @NonNull AccessibilityEvent var2) {
            var2.setContentDescription(this.getItemDescription(var1));
        }
        // endregion


        // region AREA: onPopulateNodeForVirtualView
        protected void onPopulateNodeForVirtualView(int var1, @NonNull AccessibilityNodeInfoCompat var2) {
            this.setVirtualCursorIndexAt(var1);
            this.setVirtualCursorRect(this.mVirtualViewRect);
            var2.setContentDescription(this.getItemDescription(var1));
            var2.setBoundsInParent(this.mVirtualViewRect);
            var2.addAction(16);
            var2.setClassName(Button.class.getName());
            if (SeslColorSwatchView.this.mSelectedVirtualViewId != -1 && var1 == SeslColorSwatchView.this.mSelectedVirtualViewId) {
                var2.addAction(4);
                var2.setClickable(true);
                var2.setCheckable(true);
                var2.setChecked(true);
            }

        }
        // endregion

    }
    // endregion                                        ////////////////////////////////////////////

}
