package com.buxuan.baseoneui.sesl.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.view.SeekBar;

import java.util.Arrays;

/**
 * SeslOpacitySeekBar
 * SeslOpacitySeekBar는 SeekBar를 확장한 Class이다.
 *
 * @since  Yandroid/OneUI
 * @author World8848. 1.0. 2021.12.15
 * @version 1.0
 */
public class SeslOpacitySeekBar extends SeekBar {


    // region AREA: Variables                                       ////////////////////////////////
    private static final int SEEKBAR_MAX_VALUE  = 255;
    private static final String TAG             = "SeslOpacitySeekBar";
    private final int[] mColors                 = new int[]{-1, -16777216};
    private GradientDrawable mProgressDrawable;
    // endregion                                                    ////////////////////////////////



    // region AREA: Constructor                                     ////////////////////////////////

    // region AREA: SeslOpacitySeekBar
    public SeslOpacitySeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    // endregion

    // endregion                                                    ////////////////////////////////



    // region AREA: Functions                                       ////////////////////////////////

    // region AREA: changeColorBase
    /**
     * SeslOpacitySeekBar의 기본 색상을 변경한다.
     * 시작 색상과 종료 색상으로 이루어진 Gradient 효과는 종료 색상이 var1으로 변경된다.
     * @param var1 int, ColorInt
     */
    void changeColorBase(int var1) {
        GradientDrawable var2 = this.mProgressDrawable;
        if (var2 != null) {
            int[] var3 = this.mColors;
            var3[1] = var1;
            var2.setColors(var3);
            this.setProgressDrawable(this.mProgressDrawable);
            this.setProgress(this.getMax());
        }
    }
    // endregion


    // region AREA: init
    /**
     * SeslOpacitySeekBar의 Max 값은 255로 할당하고 colorInt로 Color를 초기화한다.
     * Progress drawable resource는 sesl_color_picker_opacity_seekbar이다.
     * Thumb drawable resource는 sesl_color_picker_seekbar_cursor이고 ThumbOffset은 '0'이다.
     * @param colorInt Integer
     */
    void init(Integer colorInt) {

        // MEMO: World8848. SeslOpacitySeekBar. 최대값
        this.setMax(255);

        // MEMO: World8848. var1이 null이 아니면 SeslOpacitySeekBar color를 초기화한다.
        if (colorInt != null) {
            this.initColor(colorInt);
        }

        // region AREA: Progress drawable resource를 할당한다.
        /*
         *  MEMO: World8848. change
         *  from: this.mProgressDrawable = (GradientDrawable) this.getContext().getDrawable(R.drawable.sesl_color_picker_opacity_seekbar);
         *  to: this.mProgressDrawable = (GradientDrawable) AppCompatResources.getDrawable(getContext(),R.drawable.sesl_color_picker_opacity_seekbar);
         */
        this.mProgressDrawable = (GradientDrawable) AppCompatResources.getDrawable(getContext(),R.drawable.sesl_color_picker_opacity_seekbar);
        this.setProgressDrawable(this.mProgressDrawable);
        // endregion

        // region AREA: Thumb를 할당한다.
        /*
         *  MEMO: World8848. change. getDrawable
         *  From: this.setThumb(this.getContext().getResources().getDrawable(R.drawable.sesl_color_picker_seekbar_cursor, getContext().getTheme()));
         *  To: this.setThumb(ResourcesCompat.getDrawable(getResources(), R.drawable.sesl_color_picker_seekbar_cursor, getContext().getTheme()));
         */
        this.setThumb(ResourcesCompat.getDrawable(getResources(), R.drawable.sesl_color_picker_seekbar_cursor, getContext().getTheme()));
        this.setThumbOffset(0);
        // endregion

    }
    // endregion


    // region AREA: initColor
    /**
     * 전달받은 colorInt에서 HSV 값, Alpha 값을 추출한다.
     * OpacitySeekBar의 Range 값을 설정하기 위해 RGB 값은 동일하고 Alpha 값만 다른 최소값(Alpha 0), 최대값(Alpha 255)을 할당한다.
     * OpacitySeekBar의 기본 Progress 값을 255로 할당한다.
     * @param var1 int, Color Int
     */
    private void initColor(int var1) {

        float[] var2    = new float[3];
        Color.colorToHSV(var1, var2);

        var1            = Color.alpha(var1);

        // MEMO: World8848. mColors[0]은 opacitySeekBar의 최소, mColors[1]은 opacitySeekBar의 최대
        this.mColors[0] = Color.HSVToColor(0,   var2);
        this.mColors[1] = Color.HSVToColor(255, var2);

        this.setProgress(var1);
    }
    // endregion


    // region AREA: restoreColor
    /**
     * initColor()을 호출하여 색상을 다시 초기화한다. SeslOpacitySeekBar에 Gradient 효과를 적용한다.
     * @param var1 int
     */
    void restoreColor(int var1) {

        // region AREA: 색상을 초기화
        this.initColor(var1);
        // endregion


        // region AREA: Gradient 효과를 적용
        this.mProgressDrawable.setColors(this.mColors);
        // endregion


        this.setProgressDrawable(this.mProgressDrawable);
    }
    // endregion

    // endregion                                                    ////////////////////////////////

}
