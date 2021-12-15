package com.buxuan.baseoneui.sesl.colorpicker;

import static android.view.MotionEvent.ACTION_DOWN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RestrictTo;
import androidx.core.content.ContextCompat;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.view.SeekBar;

import java.util.ArrayList;


public class SeslColorPicker extends LinearLayout {


    // region AREA: Variables                                       ////////////////////////////////
    static final int RECENT_COLOR_SLOT_COUNT = 6;
    private static final int CURRENT_COLOR_VIEW = 0;
    private static final int NEW_COLOR_VIEW = 1;
    private static final int RIPPLE_EFFECT_OPACITY = 61;
    private static final float SCALE_LARGE = 1.2F;
    private final Context mContext;
    private final OnClickListener mImageButtonClickListener;
    private final SeslRecentColorInfo mRecentColorInfo;
    private final ArrayList<Integer> mRecentColorValues;
    private final Resources mResources;
    private final int[] mSmallestWidthDp = new int[]{320, 360, 411};
    private String[] mColorDescription;
    private SeslColorSwatchView mColorSwatchView;
    private GradientDrawable mCurrentColorBackground;
    private View mCurrentColorContainer;
    private ImageView mCurrentColorView;
    private float mCurrentFontScale;
    private boolean mIsInputFromUser;
    private boolean mIsLightTheme;
    private boolean mIsOpacityBarEnabled;
    private OnColorChangedListener mOnColorChangedListener;
    private SeslOpacitySeekBar mOpacitySeekBar;
    private FrameLayout mOpacitySeekBarContainer;
    private PickedColor mPickedColor;
    private View mPickedColorContainer;
    private ImageView mPickedColorView;
    private LinearLayout mRecentColorListLayout;
    private View mRecentlyDivider;
    private TextView mRecentlyText;
    private GradientDrawable mSelectedColorBackground;
    // endregion                                                    ////////////////////////////////



    // region AREA: Constructor                                     ////////////////////////////////

    /**
     * SeslColorPicker
     * 사용자의 Input이 없는 상태로 초기화한다.
     * OpacitySeekBar의 사용 유무를 할당한다. 기본값으로는 false가 할당된다.
     * Color description을 초기화(null 값)한다.
     * Recent Color의 Image click listener를 등록한다.
     * Light theme 인지를 확인한다.
     * Font 크기를 할당한다.
     * Layout(sesl_color_picker_layout.xml)을 참조한다.
     * '최근 사용한 색상', '선택한 색상'과 관련한 변수를 할당한다.
     * 초기화 관련 메소드들을 호출한다.
     *  initDialogPadding(), 		initCurrentColorView(), 		initColorSwatchView(),
     *  initOpacitySeekBar()		initRecentColorLayout(), 		updateCurrentColor()
     *  setInitialColors()
     * @param context   Context
     * @param attributeSet  AttributeSet
     */
    public SeslColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        boolean var3                    = false;

        // region AREA: 사용자 input 없음
        this.mIsInputFromUser           = false;
        // endregion


        // region AREA: OpacitySeekBar 사용 유무
        this.mIsOpacityBarEnabled       = false;
        // endregion


        // region AREA: mColorDescription. 초기화
        this.mColorDescription          = null;     // init value. null
        // endregion



        // TODO: Additional works "SeslColorPicker - mImageButtonClickListener checking"
        this.mImageButtonClickListener  = new OnClickListener() {
            public void onClick(View var1) {

                int var2 = SeslColorPicker.this.mRecentColorValues.size();

                for (int var3 = 0; var3 < var2 && var3 < 6; ++var3) {
                    if (SeslColorPicker.this.mRecentColorListLayout.getChildAt(var3).equals(var1)) {
                        SeslColorPicker.this.mIsInputFromUser = true;
                        int var4 = (Integer) SeslColorPicker.this.mRecentColorValues.get(var3);
                        SeslColorPicker.this.mPickedColor.setColor(var4);
                        try {
                            SeslColorPicker.this.mapColorOnColorWheel(var4);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        if (SeslColorPicker.this.mOnColorChangedListener != null) {
                            SeslColorPicker.this.mOnColorChangedListener.onColorChanged(var4);
                        }
                    }
                }

            }
        };

        this.mContext                   = context;
        this.mResources                 = this.getResources();


        // region AREA: isLightTheme 여부를 확인
        TypedValue typedValue           = new TypedValue();
        this.mContext.getTheme().resolveAttribute(R.attr.isLightTheme, typedValue, true);
        // MEMO: World8848. typedValue.data is 0 (dark mode), -1 (light mode)
        if (typedValue.data != 0) {
            var3 = true;
        }

        // MEMO: World8848. isLightTheme
        this.mIsLightTheme = var3;
        // endregion


        // region AREA: font 크기를 확인
        // MEMO: World8848. fontScale. 1.0
        this.mCurrentFontScale = this.mResources.getConfiguration().fontScale;
        // endregion


        // region AREA: View inflate
        // MEMO: World8848. 실제 Dialog 생성 시 보여지는 화면 레이아웃
        LayoutInflater.from(context).inflate(R.layout.sesl_color_picker_layout, this);
        // endregion


        // region AREA: Recent color
        this.mRecentColorInfo           = new SeslRecentColorInfo();
        this.mRecentColorValues         = this.mRecentColorInfo.getRecentColorInfo();
        // endregion


        // region AREA: Picked color
        this.mPickedColor               = new PickedColor();
        // endregion

        // region AREA: init
        this.initDialogPadding();
        this.initCurrentColorView();
        this.initColorSwatchView();
        this.initOpacitySeekBar();
        this.initRecentColorLayout();
        this.updateCurrentColor();
        this.setInitialColors();
        // endregion

    }

    // endregion                                                    ////////////////////////////////



    // region AREA: Functions                                       ////////////////////////////////


    // region AREA: initColorSwatchView
    /**
     * Color Swatch View를 연결하고 색상변경에 따른 ChangedListener를 설정한다.
     */
    private void initColorSwatchView() {

        // region AREA: Color swatch view 연결
        this.mColorSwatchView = (SeslColorSwatchView) this.findViewById(R.id.sesl_color_picker_color_swatch_view);
        // endregion

        this.mColorSwatchView.setOnColorSwatchChangedListener(new SeslColorSwatchView.OnColorSwatchChangedListener() {
            public void onColorSwatchChanged(int var1) {
                // MEMO: World8848. 사용자가 색상을 변경 선택했기 때문에 mIsInputFromUser 값이 true로 변경
                SeslColorPicker.this.mIsInputFromUser = true;
                SeslColorPicker.this.mPickedColor.setColor(var1);
                try {
                    SeslColorPicker.this.updateCurrentColor();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
    // endregion


    // region AREA: initCurrentColorView
    private void initCurrentColorView() {

        // region AREA: '현재 색' '새로운 색' 부문. View 연결
        this.mCurrentColorView  = (ImageView) this.findViewById(R.id.sesl_color_picker_current_color_view);
        this.mPickedColorView   = (ImageView) this.findViewById(R.id.sesl_color_picker_picked_color_view);
        // endregion


        // region AREA: '현재 색' '새로운 색' 부문. text. Color
        /*
         *  MEMO: World8848. origin
         *  var2 = var1.getColor(R.color.sesl_color_picker_selected_color_item_text_color, null);
         */

        int var2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var2 = this.mResources.getColor(R.color.sesl_color_picker_selected_color_item_text_color, null);
        } else {
            var2 = ContextCompat.getColor(mContext,R.color.sesl_color_picker_selected_color_item_text_color);
        }
        // endregion


        // region AREA: '현재 색' '새로운 색' 부문. text. setTextColor
        TextView var5 = (TextView) this.findViewById(R.id.sesl_color_picker_current_color_text);
        var5.setTextColor(var2);
        TextView var3 = (TextView) this.findViewById(R.id.sesl_color_picker_picked_color_text);
        var3.setTextColor(var2);
        // endregion


        // region AREA: '현재 색' '새로운 색' 부문. text. setTextSize
        if (this.mCurrentFontScale > 1.2F) {
            float var4 = (float) this.mResources.getDimensionPixelOffset(R.dimen.sesl_color_picker_selected_color_text_size);
            var5.setTextSize(0, (float) Math.floor(Math.ceil((double) (var4 / this.mCurrentFontScale)) * 1.2000000476837158D));
            var3.setTextSize(0, (float) Math.floor(Math.ceil((double) (var4 / this.mCurrentFontScale)) * 1.2000000476837158D));
        }
        // endregion


        // region AREA: '현재 색' Text와 색상 이미지 부문, '새로운 색' Text와 색상 이미지 부문. view 연결
        this.mCurrentColorContainer = this.findViewById(R.id.sesl_color_picker_current_color_focus);
        this.mPickedColorContainer  = this.findViewById(R.id.sesl_color_picker_picked_color_focus);
        // endregion


        // region AREA: '현재 색', '새로운 색' Text 옆 이미지의 background
        this.mCurrentColorBackground    = (GradientDrawable) this.mCurrentColorView.getBackground();

        this.mSelectedColorBackground   = (GradientDrawable) this.mPickedColorView.getBackground();
        // endregion


        // region AREA: '새로운 색' Text 옆 이미지 background. 색상 선택 시 색상 변경
        Integer var6 = this.mPickedColor.getColor();
        if (var6 != null) {
            this.mSelectedColorBackground.setColor(var6);
        }
        // endregion

    }
    // endregion


    // region AREA: initDialogPadding
    private void initDialogPadding() {

        // MEMO: World8848. 화면이 세로 방향
        if (this.mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            DisplayMetrics var1 = this.mResources.getDisplayMetrics();

            // MEMO: World8848. DEV. Device. var1.density 는 2.0
            float var2 = var1.density;
            if (var2 % 1.0F != 0.0F) {

                // MEMO: World8848. DEV. Device. var1.widthPixels 는 720
                float var3 = (float) var1.widthPixels;

                // MEMO: World8848. mSmallestWidthDp (320, 360, 411) 중 하나인 경우 실행, var3/var2 = 360
                if (this.isContains((int) (var3 / var2))) {

                    // MEMO: World8848. DEV. Device. var4 = 644
                    int var4 = this.mResources.getDimensionPixelSize(R.dimen.sesl_color_picker_seekbar_width);

                    if (var3 < (float) (this.mResources.getDimensionPixelSize(R.dimen.sesl_color_picker_dialog_padding_left) * 2 + var4)) {
                        int var5 = (int) ((var3 - (float) var4) / 2.0F);
                        int var6 = this.mResources.getDimensionPixelSize(R.dimen.sesl_color_picker_dialog_padding_top);
                        var4 = this.mResources.getDimensionPixelSize(R.dimen.sesl_color_picker_dialog_padding_bottom);

                        ((LinearLayout) this.findViewById(R.id.sesl_color_picker_main_content_container)).setPadding(var5, var6, var5, var4);
                    }
                }
            }
        }
    }
    // endregion


    // region AREA: initOpacitySeekBar
    /**
     * OpacitySeekBar, OpacitySeekBarContainer View를 연결한다.
     * OpacityBar의 Visibility, PickedColor로 초기화, Listener(change, touch) 및 content description을 할당한다.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initOpacitySeekBar() {

        // region AREA: OpacitySeekBar와 OpacitySeekBarContainer View 연결
        this.mOpacitySeekBar            = (SeslOpacitySeekBar) this.findViewById(R.id.sesl_color_picker_opacity_seekbar);
        this.mOpacitySeekBarContainer   = (FrameLayout) this.findViewById(R.id.sesl_color_picker_opacity_seekbar_container);
        // endregion


        // region AREA: OpacityBar. Visibility 할당
        if (!this.mIsOpacityBarEnabled) {
            this.mOpacitySeekBar.setVisibility(View.GONE);
            this.mOpacitySeekBarContainer.setVisibility(View.GONE);
        }
        // endregion


        // region AREA: OpacityBar. init
        // MEMO: World8848. this.mPickedColor.getColor(). null
        this.mOpacitySeekBar.init(this.mPickedColor.getColor());
        // endregion


        // region AREA: OpacityBar. change listener
        this.mOpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    SeslColorPicker.this.mIsInputFromUser = true;
                }

                // region AREA: progress 정보를 picked color의 alpha 값에 반영하고 picked color 값을 조회
                SeslColorPicker.this.mPickedColor.setAlpha(progress);
                Integer pickedColorInt = SeslColorPicker.this.mPickedColor.getColor();
                // endregion

                // region AREA:


                // endregion
                if (pickedColorInt != null) {
                    // MEMO: World8848. '새로운 색' 부분의 이미지 색상에 picked color 반영
                    if (SeslColorPicker.this.mSelectedColorBackground != null) {
                        SeslColorPicker.this.mSelectedColorBackground.setColor(pickedColorInt);
                    }

                    // MEMO: World8848. onColorChangedListener에 picked color 반영
                    if (SeslColorPicker.this.mOnColorChangedListener != null) {
                        SeslColorPicker.this.mOnColorChangedListener.onColorChanged(pickedColorInt);
                    }
                }

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        // endregion


        // region AREA: OpacityBar. onTouchListener
        // TODO: Additional works "SeslColorPicker -  initOpacitySeekBar   ------->  @SuppressLint("ClickableViewAccessibility")"
        this.mOpacitySeekBar.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                /*
                 *  MEMO: World8848. Origin
                 *  if (action != ACTION_DOWN) {
                 *      if (action != ACTION_UP && action != ACTION_CANCEL) {}
                 *          return false;
                 *      } else {
                 *          return true;
                 *      }
                 */
                return action == ACTION_DOWN;
            }
        });
        // endregion


        // region AREA: OpacityBar. contentDescription
        FrameLayout opacitySeekBarContainer = this.mOpacitySeekBarContainer;
        String stringBuilder = this.mResources.getString(R.string.pen_string_opacity)
                + ", "
                + this.mResources.getString(R.string.pen_string_slider)
                + ", "
                + this.mResources.getString(R.string.sesl_color_picker_double_tap_to_select);
        opacitySeekBarContainer.setContentDescription(stringBuilder);
        // endregion

    }
    // endregion


    // region AREA: initRecentColorLayout
    /**
     * initRecentColorLayout
     * 최근에 사용한 색상'과 관련된 View를 참조한다.
     * 전체 Container Layout은 'sesl_color_picker_used_color_group.xml', '최근에 사용한 색상' 텍스트와
     * Divider는 'sesl_color_picker_layout.xml'을 참조한다. Color Description을 생성한다.
     * Recent Color View 6개를 생성하고 색상, focus, clickable, font 크기와 색상 속성을 할당한다.
     */
    private void initRecentColorLayout() {

        // region AREA: Layout view 참조
        this.mRecentColorListLayout = (LinearLayout) this.findViewById(R.id.sesl_color_picker_used_color_item_list_layout);
        this.mRecentlyText          = (TextView) this.findViewById(R.id.sesl_color_picker_used_color_divider_text);
        this.mRecentlyDivider       = this.findViewById(R.id.sesl_color_picker_recently_divider);
        // endregion

        // region AREA: Color description
        this.mColorDescription = new String[]{this.mResources.getString(R.string.sesl_color_picker_color_one), this.mResources.getString(R.string.sesl_color_picker_color_two), this.mResources.getString(R.string.sesl_color_picker_color_three), this.mResources.getString(R.string.sesl_color_picker_color_four), this.mResources.getString(R.string.sesl_color_picker_color_five), this.mResources.getString(R.string.sesl_color_picker_color_six)};
        // endregion

        // region AREA: Recent color view 생성
        Context context = this.mContext;
        int var2;
        // MEMO: World8848. Recent color empty slot color
        int usedColorItemEmptySlotColor = ContextCompat.getColor(context, R.color.sesl_color_picker_used_color_item_empty_slot_color);

        // MEMO: World8848. Recent color view 6개 생성
        for (var2 = 0; var2 < 6; ++var2) {
            View recentColorView = this.mRecentColorListLayout.getChildAt(var2);
            this.setImageColor(recentColorView, usedColorItemEmptySlotColor);
            recentColorView.setFocusable(false);
            recentColorView.setClickable(false);
        }
        // endregion

        // region AREA: Font size
        if (this.mCurrentFontScale > 1.2F) {
            var2 = this.mResources.getDimensionPixelOffset(R.dimen.sesl_color_picker_selected_color_text_size);
            this.mRecentlyText.setTextSize(0, (float) Math.floor(Math.ceil((double) ((float) var2 / this.mCurrentFontScale)) * 1.2000000476837158D));
        }
        // endregion

        // region AREA: Font color
        int usedColorTextColor = ContextCompat.getColor(context, R.color.sesl_color_picker_used_color_text_color);
        this.mRecentlyText.setTextColor(usedColorTextColor);
        this.mRecentlyDivider.getBackground().setTint(usedColorTextColor);
        // endregion

    }
    // endregion


    // region AREA: isContains
    private boolean isContains(int var1) {
        int[] var2  = this.mSmallestWidthDp;
        int var3    = var2.length;

        for (int i : var2) {
            if (i == var1) {
                return true;
            }
        }

        return false;
    }
    // endregion


    // region AREA: mapColorOnColorWheel
    /**
     * Color swatch에서 cursor의 position을 변경하고 사각형 테두리를 생성한다.
     * OpacitySeekBar의 색상을 변경한다.
     * @param colorInt int
     */
    private void mapColorOnColorWheel(int colorInt) {

        this.mPickedColor.setColor(colorInt);

        // region AREA: Color swatch에서 Cursor Position을 변경
        SeslColorSwatchView colorSwatchView = this.mColorSwatchView;
        if (colorSwatchView != null) {
            colorSwatchView.updateCursorPosition(colorInt);
        }
        // endregion


        // region AREA: OpacitySeekBar 색상을 변경
        SeslOpacitySeekBar opacitySeekBar = this.mOpacitySeekBar;
        if (opacitySeekBar != null) {
            opacitySeekBar.restoreColor(colorInt);
        }
        // endregion


        // region AREA: 배경색을 변경
        GradientDrawable var4 = this.mSelectedColorBackground;
        if (var4 != null) {
            var4.setColor(colorInt);
            this.setCurrentColorViewDescription(colorInt, "NEW");
        }
        // endregion
    }
    // endregion


    // region AREA: setCurrentColorViewDescription
    /**
     * ColorInt 정보로 '색상, 밝기' 문자열을 생성한다.
     * 선택한 색상이 없는 경우나 기본색인 경우는 '현재 색'을 반환하고 선택한 색상이 있는 경우는 '새로운 색, 색상, 밝기'를 반환한다.
     *
     * ColorInt 정보로 '색상, 밝기' 문자열을 생성한다.
     * 선택한 색상이 없는 경우나 기본색인 경우는 CurrentColorViewDescription에 '현재 색' 문자열을 할당한다.
     * 선택한 색상이 있는 경우는 CurrentColorViewDescription에 '새로운 색, 색상, 밝기' 문자열을 할당한다.
     *
     * @param colorInt  ColorInt 정보
     * @param strCURRENTorNEW   ColorInt가 CURRENT인지 NEW인지 구분하는 문자열, "CURRENT" or "NEW"
     */
    private void setCurrentColorViewDescription(int colorInt, String strCURRENTorNEW) {

        StringBuilder stringBuilder = new StringBuilder();

        // region AREA: Color Swatch의 "색상, 밝기" 문자열
        StringBuilder stringColorNLight = this.mColorSwatchView.getColorSwatchDescriptionAt(colorInt);
        // endregion

        if (stringColorNLight != null) {
            stringBuilder.append(", ");
            stringBuilder.append(stringColorNLight);
        }

        if (!strCURRENTorNEW.equals("CURRENT")) {
            // MEMO: World8848. White 선택 시 stringBuilder는 새로운 색, 흰색, 100
            if (strCURRENTorNEW.equals("NEW")) {
                stringBuilder.insert(0, this.mResources.getString(R.string.pen_string_color_new));
                this.mPickedColorContainer.setContentDescription(stringBuilder);
            }
        } else {
            stringBuilder.insert(0, this.mResources.getString(R.string.pen_string_color_current));
            this.mCurrentColorContainer.setContentDescription(stringBuilder);
        }

    }
    // endregion


    // region AREA: setImageColor
    /**
     * View에 'sesl_color_picker_used_color_item_slot.xml'를 참조한다.
     * View의 색상에 전달받은 colorInt로 할당한다.
     * View에 Ripple effect와 click listener를 할당한다.
     * @param view View
     * @param colorInt Integer
     */
    private void setImageColor(View view, Integer colorInt) {


        // region AREA: 전달받은 View. 모양과 색상 할당
        /*
         *  MEMO: World8848. Origin
         *  Context var3            = this.mContext;
         *  GradientDrawable var5   = (GradientDrawable) var3.getDrawable(R.drawable.sesl_color_picker_used_color_item_slot);
         */

        /*
         *  MEMO: World8848. Warning. mContext.getDrawable.
         *  Origin
         *  GradientDrawable usedColorItemSlotDrawable = (GradientDrawable) this.mContext.getDrawable(R.drawable.sesl_color_picker_used_color_item_slot);
         */
        GradientDrawable usedColorItemSlotDrawable = (GradientDrawable) ContextCompat.getDrawable(this.mContext, R.drawable.sesl_color_picker_used_color_item_slot);

        // MEMO: World8848. add. 'usedColorItemSlotDrawable != null'
        if (colorInt != null && usedColorItemSlotDrawable != null) {
            usedColorItemSlotDrawable.setColor(colorInt);
        }
        // endregion


        // region AREA: 전달받은 View. Ripple effect
        int var1;
        var1 = Color.argb(61, 0, 0, 0);
        view.setBackground(new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{var1}), usedColorItemSlotDrawable, (Drawable) null));
        // endregion


        // region AREA: 전달받은 View. onClickListener
        view.setOnClickListener(this.mImageButtonClickListener);
        // endregion

    }
    // endregion


    // region AREA: setInitialColors
    /**
     * 사용자로부터 Picked된 색상이 있는 경우 mapColorOnColorWheel()을 호출하여 색상을 초기화한다.
     */
    private void setInitialColors() {
        Integer pickedColorColor = this.mPickedColor.getColor();
        if (pickedColorColor != null) {
            this.mapColorOnColorWheel(pickedColorColor);
        }
    }
    // endregion


    // region AREA: updateCurrentColor
    /**
     * ColorInt 정보를 조회한다.
     * 선택한 색상이 있는 경우
     * OpacitySeekBar의 BaseColor를 선택한 색상으로 변경한다.
     * 상단의 '새로운 색' 부분의 배경 이미지 및 이미지 색상에 맞는 ContentColorViewDescription을 변경한다.
     */
    private void updateCurrentColor() {

        Integer pickedColor = this.mPickedColor.getColor();

        // MEMO: World8848. 선택한 색상이 있는 경우
        if (pickedColor != null) {

            // region AREA: seslOpacitySeekBar. 선택한 색상으로 BaseColor를 변경한다.
            SeslOpacitySeekBar seslOpacitySeekBar = this.mOpacitySeekBar;
            if (seslOpacitySeekBar != null) {
                seslOpacitySeekBar.changeColorBase(pickedColor);
            }
            // endregion

            GradientDrawable selectedColorBackground = this.mSelectedColorBackground;
            if (selectedColorBackground != null) {

                // MEMO: World8848. 선택한 색상으로 상단의 '새로운 색' 부분의 배경 이미지를 변경한다.
                selectedColorBackground.setColor(pickedColor);

                // MEMO: World8848. 선택한 색상으로 CurrentColorViewDescription을 변경한다.
                this.setCurrentColorViewDescription(pickedColor, "NEW");
            }

            OnColorChangedListener colorChangedListener = this.mOnColorChangedListener;
            if (colorChangedListener != null) {
                colorChangedListener.onColorChanged(pickedColor);
            }
        }
    }
    // endregion


    // endregion                                                    ////////////////////////////////



    // region AREA: OnColorChangedListener                          ////////////////////////////////
    public interface OnColorChangedListener {
        void onColorChanged(int colorInt);
    }
    // endregion                                                    ////////////////////////////////






    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public SeslRecentColorInfo getRecentColorInfo() {
        return this.mRecentColorInfo;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isUserInputValid() {
        return this.mIsInputFromUser;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void saveSelectedColor() {
        Integer var1 = this.mPickedColor.getColor();
        if (var1 != null) {
            this.mRecentColorInfo.saveSelectedColor(var1);
        }

    }

    public void setOnColorChangedListener(OnColorChangedListener var1) {
        this.mOnColorChangedListener = var1;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setOpacityBarEnabled(boolean var1) {
        this.mIsOpacityBarEnabled = var1;
        if (this.mIsOpacityBarEnabled) {
            this.mOpacitySeekBar.setVisibility(View.VISIBLE);
            this.mOpacitySeekBarContainer.setVisibility(View.VISIBLE);
        }

    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void updateRecentColorLayout() {
        ArrayList var1 = this.mRecentColorValues;
        int var2;
        if (var1 != null) {
            var2 = var1.size();
        } else {
            var2 = 0;
        }

        StringBuilder var8 = new StringBuilder();
        var8.append(", ");
        var8.append(this.mResources.getString(R.string.sesl_color_picker_option));
        String var3 = var8.toString();

        for (int var4 = 0; var4 < 6; ++var4) {
            View var5 = this.mRecentColorListLayout.getChildAt(var4);
            if (var4 < var2) {
                int var6 = (Integer) this.mRecentColorValues.get(var4);
                this.setImageColor(var5, var6);
                StringBuilder var7 = new StringBuilder();
                var7.append(this.mColorSwatchView.getColorSwatchDescriptionAt(var6));
                var8 = new StringBuilder();
                var8.append(this.mColorDescription[var4]);
                var8.append(var3);
                var8.append(", ");
                var7.insert(0, var8.toString());
                var5.setContentDescription(var7);
                var5.setFocusable(true);
                var5.setClickable(true);
            }
        }

        if (this.mRecentColorInfo.getCurrentColor() != null) {
            var2 = this.mRecentColorInfo.getCurrentColor();

            this.mCurrentColorBackground.setColor(var2);
            this.setCurrentColorViewDescription(var2, "CURRENT");
            this.mSelectedColorBackground.setColor(var2);
            this.mapColorOnColorWheel(var2); // MEMO: World8848. SeslOpacitySeekBar. restore
        } else if (var2 != 0) {
            var2 = (Integer) this.mRecentColorValues.get(0);
            this.mCurrentColorBackground.setColor(var2);
            this.setCurrentColorViewDescription(var2, "CURRENT");
            this.mSelectedColorBackground.setColor(var2);
            this.mapColorOnColorWheel(var2);
        }

        if (this.mRecentColorInfo.getNewColor() != null) {
            var2 = this.mRecentColorInfo.getNewColor();
            this.mSelectedColorBackground.setColor(var2);
            this.mapColorOnColorWheel(var2);
        }

    }





    // region AREA: Subclass. PickedColor                           ////////////////////////////////
    private static class PickedColor {

        // region AREA: Variables
        private int mAlpha      = 255;
        private Integer mColor  = null;
        private float[] mHsv    = new float[3];
        // endregion


        // region AREA: Constructor
        public PickedColor() {
        }
        // endregion


        // region AREA: Alpha
        public int getAlpha() {
            return this.mAlpha;
        }

        public void setAlpha(int var1) {
            this.mAlpha = var1;
            this.mColor = Color.HSVToColor(this.mAlpha, this.mHsv);
        }
        // endregion


        // region AREA: Color
        public Integer getColor() {
            return this.mColor;
        }

        public void setColor(int var1) {
            this.mColor = var1;
            this.mAlpha = Color.alpha(this.mColor);
            Color.colorToHSV(this.mColor, this.mHsv);
        }
        // endregion


        // region AREA: HSV
        public float getV() {
            return this.mHsv[2];
        }

        public void setV(float var1) {
            float[] var2 = this.mHsv;
            var2[2] = var1;
            this.mColor = Color.HSVToColor(this.mAlpha, var2);
        }

        public void setHS(float var1, float var2) {
            float[] var3 = this.mHsv;
            var3[0] = var1;
            var3[1] = var2;
            var3[2] = 1.0F;
            this.mColor = Color.HSVToColor(this.mAlpha, var3);
        }
        // endregion

    }
    // endregion                                                    ////////////////////////////////


}
