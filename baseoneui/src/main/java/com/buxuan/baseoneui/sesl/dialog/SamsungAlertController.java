package com.buxuan.baseoneui.sesl.dialog;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.sesl.utils.ReflectUtils;
import com.buxuan.baseoneui.sesl.utils.SamsungEdgeEffect;
import com.buxuan.baseoneui.view.NestedScrollView;

import java.lang.ref.WeakReference;

public class SamsungAlertController {

    //**********************************************************************************************
    // region AREA: Variables
    //**********************************************************************************************
    public final int mButtonIconDimen;
    public final Context mContext;
    public final AppCompatDialog mDialog;
    public final Window mWindow;
    public ListAdapter mAdapter;
    public int mAlertDialogLayout;
    public Button mButtonNegative;
    public Drawable mButtonNegativeIcon;
    public Message mButtonNegativeMessage;
    public CharSequence mButtonNegativeText;
    public Button mButtonNeutral;
    public Drawable mButtonNeutralIcon;
    public Message mButtonNeutralMessage;
    public CharSequence mButtonNeutralText;
    public int mButtonPanelLayoutHint = 0;
    public int mButtonPanelSideLayout;
    public Button mButtonPositive;
    public Drawable mButtonPositiveIcon;
    public Message mButtonPositiveMessage;
    public CharSequence mButtonPositiveText;
    public int mCheckedItem = -1;
    public View mCustomTitleView;
    public Handler mHandler;
    public final View.OnClickListener mButtonHandler = new View.OnClickListener() {
        public void onClick(View var1) {
            Message var3;
            label33:
            {
                SamsungAlertController var2 = SamsungAlertController.this;
                Message var4;
                if (var1 == var2.mButtonPositive) {
                    var4 = var2.mButtonPositiveMessage;
                    if (var4 != null) {
                        var3 = Message.obtain(var4);
                        break label33;
                    }
                }

                var2 = SamsungAlertController.this;
                if (var1 == var2.mButtonNegative) {
                    var4 = var2.mButtonNegativeMessage;
                    if (var4 != null) {
                        var3 = Message.obtain(var4);
                        break label33;
                    }
                }

                var2 = SamsungAlertController.this;
                if (var1 == var2.mButtonNeutral) {
                    var3 = var2.mButtonNeutralMessage;
                    if (var3 != null) {
                        var3 = Message.obtain(var3);
                        break label33;
                    }
                }

                var3 = null;
            }

            if (var3 != null) {
                var3.sendToTarget();
            }

            SamsungAlertController var5 = SamsungAlertController.this;
            var5.mHandler.obtainMessage(1, var5.mDialog).sendToTarget();
        }
    };
    public Drawable mIcon;
    public int mIconId = 0;
    public ImageView mIconView;
    public int mLastOrientation;
    public int mListItemLayout;
    public int mListLayout;
    public ListView mListView;
    public CharSequence mMessage;
    public TextView mMessageView;
    public int mMultiChoiceItemLayout;
    public NestedScrollView mScrollView;
    public boolean mShowTitle;
    public int mSingleChoiceItemLayout;
    public CharSequence mTitle;
    public TextView mTitleView;
    public View mView;
    public int mViewLayoutResId;
    public int mViewSpacingBottom;
    public int mViewSpacingLeft;
    public int mViewSpacingRight;
    public boolean mViewSpacingSpecified = false;
    public int mViewSpacingTop;
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Constructor
    //**********************************************************************************************
    public SamsungAlertController(Context context, AppCompatDialog appCompatDialog, Window window) {
        this.mContext                   = context;
        this.mDialog                    = appCompatDialog;
        this.mWindow                    = window;
        this.mHandler                   = new ButtonHandler(appCompatDialog);
        TypedArray typedArray           = context.obtainStyledAttributes((AttributeSet) null, R.styleable.SamsungAlertDialog, R.attr.alertDialogStyle, 0);
        this.mAlertDialogLayout         = typedArray.getResourceId(R.styleable.SamsungAlertDialog_android_layout, 0);
        this.mButtonPanelSideLayout     = typedArray.getResourceId(R.styleable.SamsungAlertDialog_buttonPanelSideLayout, 0);
        this.mListLayout                = typedArray.getResourceId(R.styleable.SamsungAlertDialog_listLayout, 0);
        this.mMultiChoiceItemLayout     = typedArray.getResourceId(R.styleable.SamsungAlertDialog_multiChoiceItemLayout, 0);
        this.mSingleChoiceItemLayout    = typedArray.getResourceId(R.styleable.SamsungAlertDialog_singleChoiceItemLayout, 0);
        this.mListItemLayout            = typedArray.getResourceId(R.styleable.SamsungAlertDialog_listItemLayout, 0);
        this.mShowTitle                 = typedArray.getBoolean(R.styleable.SamsungAlertDialog_showTitle, true);
        this.mButtonIconDimen           = typedArray.getDimensionPixelSize(R.styleable.SamsungAlertDialog_buttonIconDimen, 0);
        typedArray.recycle();
        window.setGravity(Gravity.BOTTOM);
        appCompatDialog.supportRequestWindowFeature(1);
    }
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Functions
    //**********************************************************************************************

    // region AREA: adjustButtonsPadding
    private void adjustButtonsPadding() {
        // MEMO: World8848. getDimensionPixelSize. dimen.xml에 정의한 dp값을 기기에 맞게 px로 변환하여 반올림한 값을 Int로 반환한다.
        int var1 = this.mContext.getResources().getDimensionPixelSize(R.dimen.sesl_dialog_button_text_size);
        if (this.mButtonPositive.getVisibility() != View.GONE) {    // MEMO: World8848. change from '8' to 'View.GONE'
            this.mButtonPositive.setTextSize(0, (float) var1);
            this.checkMaxFontScale(this.mButtonPositive, var1);
        }

        if (this.mButtonNegative.getVisibility() != View.GONE) {    // MEMO: World8848. change from '8' to 'View.GONE'
            this.mButtonNegative.setTextSize(0, (float) var1);
            this.checkMaxFontScale(this.mButtonNegative, var1);
        }

        if (this.mButtonNeutral.getVisibility() != View.GONE) {    // MEMO: World8848. change from '8' to 'View.GONE'
            this.mButtonNeutral.setTextSize(0, (float) var1);
            this.checkMaxFontScale(this.mButtonNeutral, var1);
        }
    }
    // endregion


    // region AREA: adjustParentPanelPadding
    private void adjustParentPanelPadding (View view) {
        view.setPadding(0, 0, 0, 0);
    }
    // endregion


    // region AREA: adjustTopPanelPadding
    private void adjustTopPanelPadding (View view) {
        View var2 = view.findViewById(R.id.title_template);
        Resources var3 = this.mContext.getResources();
        var2.setPadding(var3.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0, var3.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0);
    }
    // endregion


    // region AREA: canTextInput
    public static boolean canTextInput(View view) {
        // MEMO: World8848. onCheckIsTextEditor(). view가 text editor인지 확인
        if (view.onCheckIsTextEditor()) {
            return true;
        } else if (!(view instanceof ViewGroup)) {
            return false;
        } else {
            ViewGroup viewGroup = (ViewGroup) view;
            int viewGroupChildCount = viewGroup.getChildCount();

            int var2;
            do {
                if (viewGroupChildCount <= 0) {
                    return false;
                }

                var2 = viewGroupChildCount - 1;
                viewGroupChildCount = var2;
            } while (!canTextInput(viewGroup.getChildAt(var2)));

            return true;
        }
    }
    // endregion


    // region AREA: centerButton
    private void centerButton (Button button) {
        LinearLayout.LayoutParams var2 = (LinearLayout.LayoutParams) button.getLayoutParams();
        var2.gravity = 1;
        var2.weight = 0.5F;
        button.setLayoutParams(var2);
    }
    // endregion


    // region AREA: checkMaxFontScale
    private void checkMaxFontScale (TextView textView, int textSize) {
        float fontScale = this.mContext.getResources().getConfiguration().fontScale;

        if (fontScale > 1.3F) {
            textView.setTextSize(0, (float) textSize / fontScale * 1.3F);
        }
    }
    // endregion


    // region AREA: getButton
    public Button getButton(int buttonType) {
        if (buttonType != -3) {
            if (buttonType != -2) {
                return buttonType != -1 ? null : this.mButtonPositive;
            } else {
                return this.mButtonNegative;
            }
        } else {
            return this.mButtonNeutral;
        }
    }
    // endregion


    // region AREA: getIconAttributeResId
    public int getIconAttributeResId(int resId) {
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(resId, typedValue, true);
        return typedValue.resourceId;
    }
    // endregion


    // region AREA: getListView
    public ListView getListView() {
        return this.mListView;
    }
    // endregion


    // region AREA: installContent
    public void installContent () {
        int var1 = this.selectContentView();
        this.mDialog.setContentView(var1);
        this.setupView();
    }
    // endregion


    // region AREA: manageScrollIndicators
    public static void manageScrollIndicators(View var0, View var1, View var2) {
        byte var3 = 0;
        byte var4;
        if (var1 != null) {
            if (var0.canScrollVertically(-1)) {
                // MEMO: World8848. change from '0' to 'View.VISIBLE'
                var4 = View.VISIBLE;
            } else {
                // MEMO: World8848. change from '4' to 'View.INVISIBLE'
                var4 = View.INVISIBLE;
            }

            var1.setVisibility(var4);
        }

        if (var2 != null) {
            if (var0.canScrollVertically(1)) {
                var4 = var3;
            } else {
                // MEMO: World8848. change from '4' to 'View.INVISIBLE'
                var4 = View.INVISIBLE;
            }

            var2.setVisibility(var4);
        }

    }
    // endregion


    // region AREA: onKeyDown
    public boolean onKeyDown (int var1, KeyEvent keyEvent) {
        NestedScrollView var3 = this.mScrollView;
        boolean var4;
        if (var3 != null && var3.executeKeyEvent(keyEvent)) {
            var4 = true;
        } else {
            var4 = false;
        }
        return var4;
    }
    // endregion


    // region AREA: onKeyUp
    public boolean onKeyUp (int var1, KeyEvent keyEvent) {
        NestedScrollView var3 = this.mScrollView;
        boolean var4;
        if (var3 != null && var3.executeKeyEvent(keyEvent)) {
            var4 = true;
        } else {
            var4 = false;
        }
        return var4;
    }
    // endregion


    // region AREA: resolvePanel
    private ViewGroup resolvePanel (View view1, View view2) {
        if (view1 == null) {
            view1 = view2;
            if (view2 instanceof ViewStub) {
                view1 = ((ViewStub) view2).inflate();
            }
            return (ViewGroup) view1;
        } else {
            if (view2 != null) {
                ViewParent var3 = view2.getParent();
                if (var3 instanceof ViewGroup) {
                    ((ViewGroup) var3).removeView(view2);
                }
            }

            view2 = view1;
            if (view1 instanceof ViewStub) {
                view2 = ((ViewStub) view1).inflate();
            }

            return (ViewGroup) view2;
        }
    }
    // endregion


    // region AREA: selectContentView
    private int selectContentView() {
        int var1 = this.mButtonPanelSideLayout;

        if (var1 == 0) {
            return this.mAlertDialogLayout;
        } else {
            return this.mButtonPanelLayoutHint == 1 ? var1 : this.mAlertDialogLayout;
        }
    }
    // endregion


    // region AREA: setButton
    public void setButton(int buttonType, CharSequence buttonText, DialogInterface.OnClickListener clickListener, Message buttonMessage, Drawable buttonIcon) {
        Message varButtonMessage = buttonMessage;
        if (buttonMessage == null) {
            if (clickListener != null) {
                varButtonMessage = this.mHandler.obtainMessage(buttonType, clickListener);
            }
        }

        // MEMO: World8848. Button Type
        /*
        * -1    : Positive button
        * -2    : Negative button
        * -3    : Neutral button
        * */

        if (buttonType != -3) {
            if (buttonType != -2) {
                if (buttonType != -1) {
                    throw new IllegalArgumentException("Button does not exist");
                }

                this.mButtonPositiveText    = buttonText;
                this.mButtonPositiveMessage = varButtonMessage;
                this.mButtonPositiveIcon    = buttonIcon;
            } else {
                this.mButtonNegativeText    = buttonText;
                this.mButtonNegativeMessage = varButtonMessage;
                this.mButtonNegativeIcon    = buttonIcon;
            }
        } else {
            this.mButtonNeutralText         = buttonText;
            this.mButtonNeutralMessage      = varButtonMessage;
            this.mButtonNeutralIcon         = buttonIcon;
        }
    }
    // endregion


    // region AREA: setCustomTitle
    public void setCustomTitle (View view) {
        this.mCustomTitleView = view;
    }
    // endregion


    // region AREA: setIcon
    public void setIcon(int iconId) {
        this.mIcon      = null;
        this.mIconId    = iconId;
        ImageView iconImageView  = this.mIconView;
        if (iconImageView != null) {
            if (iconId != 0) {
                iconImageView.setVisibility(View.VISIBLE);  // MEMO: World8848. change from '0' to 'View.VISIBLE'
                this.mIconView.setImageResource(this.mIconId);
            } else {
                iconImageView.setVisibility(View.GONE);     // MEMO: World8848. change from '8' to 'View.GONE'
            }
        }
    }

    public void setIcon(Drawable iconImage) {
        this.mIcon      = iconImage;
        this.mIconId    = 0;
        ImageView iconImageView  = this.mIconView;
        if (iconImageView != null) {
            if (iconImage != null) {
                iconImageView.setVisibility(View.VISIBLE);  // MEMO: World8848. change from '0' to 'View.VISIBLE'
                this.mIconView.setImageDrawable(iconImage);
            } else {
                iconImageView.setVisibility(View.GONE);     // MEMO: World8848. change from '8' to 'View.GONE'
            }
        }
    }
    // endregion


    // region AREA: setMessage
    public void setMessage (CharSequence message) {
        this.mMessage               = message;
        TextView messageTextView    = this.mMessageView;
        if (messageTextView != null) {
            messageTextView.setText(message);
        }
    }
    // endregion


    // region AREA: setScrollIndicators
    private void setScrollIndicators(ViewGroup var1, View var2, int var3, int var4) {
        View var5 = this.mWindow.findViewById(R.id.scrollIndicatorUp);
        View var6 = this.mWindow.findViewById(R.id.scrollIndicatorDown);
        if (Build.VERSION.SDK_INT >= 23) {
            ViewCompat.setScrollIndicators(var2, var3, var4);
            if (var5 != null) {
                var1.removeView(var5);
            }

            if (var6 != null) {
                var1.removeView(var6);
            }
        } else {
            ListView var7 = null;
            var2 = var5;
            if (var5 != null) {
                var2 = var5;
                if ((var3 & 1) == 0) {
                    var1.removeView(var5);
                    var2 = null;
                }
            }

            if (var6 != null && (var3 & 2) == 0) {
                var1.removeView(var6);
                var6 = var7;
            }

            if (var2 != null || var6 != null) {
                if (this.mMessage != null) {
                    final View finalVar = var2;
                    final View finalVar1 = var6;
                    this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        public void onScrollChange(NestedScrollView var1, int var2x, int var3, int var4, int var5) {
                            SamsungAlertController.manageScrollIndicators(var1, finalVar, finalVar1);
                        }
                    });
                    final View finalVar2 = var2;
                    final View finalVar3 = var6;
                    this.mScrollView.post(new Runnable() {
                        public void run() {
                            SamsungAlertController.manageScrollIndicators(SamsungAlertController.this.mScrollView, finalVar2, finalVar3);
                        }
                    });
                } else {
                    var7 = this.mListView;
                    if (var7 != null) {
                        final View finalVar4 = var2;
                        final View finalVar5 = var6;
                        var7.setOnScrollListener(new AbsListView.OnScrollListener() {
                            public void onScroll(AbsListView var1, int var2x, int var3, int var4) {
                                SamsungAlertController.manageScrollIndicators(var1, finalVar4, finalVar5);
                            }

                            public void onScrollStateChanged(AbsListView var1, int var2x) {
                            }
                        });
                        final View finalVar6 = var2;
                        final View finalVar7 = var6;
                        this.mListView.post(new Runnable() {
                            public void run() {
                                SamsungAlertController.manageScrollIndicators(SamsungAlertController.this.mListView, finalVar6, finalVar7);
                            }
                        });
                    } else {
                        if (var2 != null) {
                            var1.removeView(var2);
                        }

                        if (var6 != null) {
                            var1.removeView(var6);
                        }
                    }
                }
            }
        }
    }
    // endregion


    // region AREA: setTitle
    public void setTitle (CharSequence title) {
        this.mTitle             = title;
        TextView titleTextView  = this.mTitleView;
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }
    // endregion


    // region AREA: setupButtons
    private void setupButtons (ViewGroup viewGroup) {
        // MEMO: World8848. ContentResolver
        ContentResolver contentResolver = this.mContext.getContentResolver();

        boolean var3 = true;
        boolean var4;

        if (contentResolver != null && Settings.System.getInt(contentResolver, "show_button_background", 0) == 1) {
            var4 = true;
        } else {
            var4 = false;
        }

        TypedValue var10 = new TypedValue();
        // MEMO: World8848. 16842801. colorBackground. https://developer.android.com/reference/android/R.attr#colorBackground
        this.mContext.getTheme().resolveAttribute(16842801, var10, true);
        int var5 = -1;

        if (var10.resourceId > 0) {
            // MEMO: World8848. origin. var5 = this.mContext.getResources().getColor(var10.resourceId, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var5 = this.mContext.getResources().getColor(var10.resourceId, null);
            } else {
                var5 = ContextCompat.getColor(this.mContext, var10.resourceId);
            }
        }

        // region AREA: mButtonPositive
        this.mButtonPositive = (Button) viewGroup.findViewById(R.id.button1);
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        if (Build.VERSION.SDK_INT > 26) {
            if (var10.resourceId > 0) {
                ReflectUtils.genericInvokeMethod(this.mButtonPositive, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetButtonShapeEnabled" : "semSetButtonShapeEnabled", var4, var5);
            } else {
                ReflectUtils.genericInvokeMethod(this.mButtonPositive, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetButtonShapeEnabled" : "semSetButtonShapeEnabled", var4);
            }
        } else if (var4) {
            this.mButtonPositive.setBackgroundResource(R.drawable.sesl_dialog_btn_show_button_shapes_background);
        }

        int var6;
        Drawable var7;
        if (TextUtils.isEmpty(this.mButtonPositiveText) && this.mButtonPositiveIcon == null) {
            this.mButtonPositive.setVisibility(View.GONE);      // MEMO: World8848. change from '8' to 'View.GONE'
            var6 = 0;
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            var7 = this.mButtonPositiveIcon;
            if (var7 != null) {
                var6 = this.mButtonIconDimen;
                var7.setBounds(0, 0, var6, var6);
                this.mButtonPositive.setCompoundDrawables(this.mButtonPositiveIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            }

            this.mButtonPositive.setVisibility(View.VISIBLE);   // MEMO: World8848. change from '0' to 'View.VISIBLE'
            var6 = 1;
        }
        // endregion

        // region AREA: mButtonNegative
        this.mButtonNegative = (Button) viewGroup.findViewById(R.id.button2);
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        if (Build.VERSION.SDK_INT > 26) {
            if (var10.resourceId > 0) {
                ReflectUtils.genericInvokeMethod(this.mButtonNegative, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetButtonShapeEnabled" : "semSetButtonShapeEnabled", var4, var5);
            } else {
                ReflectUtils.genericInvokeMethod(this.mButtonNegative, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetButtonShapeEnabled" : "semSetButtonShapeEnabled", var4);
            }
        } else if (var4) {
            this.mButtonNegative.setBackgroundResource(R.drawable.sesl_dialog_btn_show_button_shapes_background);
        }

        if (TextUtils.isEmpty(this.mButtonNegativeText) && this.mButtonNegativeIcon == null) {
            this.mButtonNegative.setVisibility(View.GONE);      // MEMO: World8848. change from '8' to 'View.GONE'
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            var7 = this.mButtonNegativeIcon;
            if (var7 != null) {
                int var8 = this.mButtonIconDimen;
                var7.setBounds(0, 0, var8, var8);
                this.mButtonNegative.setCompoundDrawables(this.mButtonNegativeIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            }

            this.mButtonNegative.setVisibility(View.VISIBLE);   // MEMO: World8848. change from '0' to 'View.VISIBLE'
            var6 |= 2;
        }
        // endregion


        // region AREA: mButtonNeutral
        this.mButtonNeutral = (Button) viewGroup.findViewById(R.id.button3);
        this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
        if (Build.VERSION.SDK_INT > 26) {
            if (var10.resourceId > 0) {
                ReflectUtils.genericInvokeMethod(this.mButtonNeutral, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetButtonShapeEnabled" : "semSetButtonShapeEnabled", var4, var5);
            } else {
                ReflectUtils.genericInvokeMethod(this.mButtonNeutral, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetButtonShapeEnabled" : "semSetButtonShapeEnabled", var4);
            }
        } else if (var4) {
            this.mButtonNeutral.setBackgroundResource(R.drawable.sesl_dialog_btn_show_button_shapes_background);
        }

        if (TextUtils.isEmpty(this.mButtonNeutralText) && this.mButtonNeutralIcon == null) {
            this.mButtonNeutral.setVisibility(View.GONE);       // MEMO: World8848. change from '8' to 'View.GONE'
        } else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            Drawable var11 = this.mButtonPositiveIcon;
            if (var11 != null) {
                var5 = this.mButtonIconDimen;
                var11.setBounds(0, 0, var5, var5);
                this.mButtonPositive.setCompoundDrawables(this.mButtonPositiveIcon, (Drawable) null, (Drawable) null, (Drawable) null);
            }

            this.mButtonNeutral.setVisibility(View.VISIBLE);    // MEMO: World8848. change from '0' to 'View.VISIBLE'
            var6 |= 4;
        }
        // endregion


        // region AREA: shouldCenterSingleButton
        if (shouldCenterSingleButton(this.mContext)) {
            if (var6 == 1) {
                this.centerButton(this.mButtonPositive);
            } else if (var6 == 2) {
                this.centerButton(this.mButtonNegative);
            } else if (var6 == 4) {
                this.centerButton(this.mButtonNeutral);
            }
        }
        // endregion


        // region AREA: Button Visibility
        boolean var13;
        if (var6 != 0) {
            var13 = true;
        } else {
            var13 = false;
        }

        if (!var13) {
            // MEMO: World8848. change from '8' to 'View.GONE'
            viewGroup.setVisibility(View.GONE);
        }

        // MEMO: World8848. change from '0' to 'View.VISIBLE'
        if (this.mButtonNeutral.getVisibility() == View.VISIBLE) {
            var13 = true;
        } else {
            var13 = false;
        }

        boolean var12;
        // MEMO: World8848. change from '0' to 'View.VISIBLE'
        if (this.mButtonPositive.getVisibility() == View.VISIBLE) {
            var12 = true;
        } else {
            var12 = false;
        }

        // MEMO: World8848. change from '0' to 'View.VISIBLE'
        if (this.mButtonNegative.getVisibility() != View.VISIBLE) {
            var3 = false;
        }

        View var9 = this.mWindow.findViewById(R.id.sem_divider2);
        if (var9 != null && (var13 && var12 || var13 && var3)) {
            // MEMO: World8848. change from '0' to 'View.VISIBLE'
            var9.setVisibility(View.VISIBLE);
        }

        var9 = this.mWindow.findViewById(R.id.sem_divider1);
        if (var9 != null && var12 && var3) {
            // MEMO: World8848. change from '0' to 'View.VISIBLE'
            var9.setVisibility(View.VISIBLE);
        }
        // endregion
    }
    // endregion


    // region AREA: setButtonPanelLayoutHint
    public void setButtonPanelLayoutHint (int var1) {
        this.mButtonPanelLayoutHint = var1;
    }
    // endregion


    // region AREA: setupContent
    @SuppressLint("ResourceType")
    private void setupContent (ViewGroup viewGroup) {
        this.mScrollView = (NestedScrollView) this.mWindow.findViewById(R.id.scrollView);
        this.mScrollView.setFocusable(false);
        this.mScrollView.setNestedScrollingEnabled(false);
        // MEMO: World8848. 16908299. https://developer.android.com/reference/android/R.id#message
        // MEMO: World8848. change. from '16908299' to 'R.id.message'. not working
        this.mMessageView = (TextView) viewGroup.findViewById(16908299);
        TextView var2 = this.mMessageView;
        if (var2 != null) {
            CharSequence var3 = this.mMessage;
            if (var3 != null) {
                var2.setText(var3);
            } else {
                // MEMO: World8848. change from '8' to 'View.GONE'
                var2.setVisibility(View.GONE);
                this.mScrollView.removeView(this.mMessageView);
                if (this.mListView != null) {
                    viewGroup = (ViewGroup) this.mScrollView.getParent();
                    int var4 = viewGroup.indexOfChild(this.mScrollView);
                    viewGroup.removeViewAt(var4);
                    viewGroup.addView(this.mListView, var4, new ViewGroup.LayoutParams(-1, -1));
                } else {
                    // MEMO: World8848. change from '8' to 'View.GONE'
                    viewGroup.setVisibility(View.GONE);
                }
            }
        }
    }
    // endregion


    // region AREA: setupCustomContent
    private void setupCustomContent(ViewGroup viewGroup) {
        View var2 = this.mView;
        boolean var3 = false;
        if (var2 == null) {
            if (this.mViewLayoutResId != 0) {
                var2 = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, viewGroup, false);
            } else {
                var2 = null;
            }
        }

        if (var2 != null) {
            var3 = true;
        }

        if (!var3 || !canTextInput(var2)) {
            this.mWindow.setFlags(131072, 131072);
        }

        if (var3) {
            FrameLayout var4 = (FrameLayout) this.mWindow.findViewById(R.id.custom);
            var4.addView(var2, new ViewGroup.LayoutParams(-1, -1));
            if (this.mViewSpacingSpecified) {
                var4.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }

            if (this.mListView != null) {
                if (viewGroup.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    ((LinearLayout.LayoutParams) viewGroup.getLayoutParams()).weight = 0.0F;
                } else {
                    ((androidx.appcompat.widget.LinearLayoutCompat.LayoutParams) viewGroup.getLayoutParams()).weight = 0.0F;
                }
            }
        } else {
            // MEMO: World8848. change from '8' to 'View.GONE'
            viewGroup.setVisibility(View.GONE);
        }
    }
    // endregion


    // region AREA: setupPaddings
    private void setupPaddings() {
        View var1 = this.mWindow.findViewById(R.id.parentPanel);
        View var2 = var1.findViewById(R.id.title_template);
        View var3 = var1.findViewById(R.id.scrollView);
        View var4 = var1.findViewById(R.id.buttonPanel);
        Resources var5 = this.mContext.getResources();
        ViewGroup var6 = (ViewGroup) var1.findViewById(R.id.customPanel);
        View var7 = var1.findViewById(R.id.topPanel);
        View var8 = var1.findViewById(R.id.contentPanel);
        boolean var9 = true;
        boolean var10;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var6 != null && var6.getVisibility() != View.GONE) {
            var10 = true;
        } else {
            var10 = false;
        }

        boolean var11;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var7 != null && var7.getVisibility() != View.GONE) {
            var11 = true;
        } else {
            var11 = false;
        }

        boolean var12;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var8 != null && var8.getVisibility() != View.GONE) {
            var12 = true;
        } else {
            var12 = false;
        }

        var7 = this.mCustomTitleView;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var7 == null || var7.getVisibility() == View.GONE) {
            var9 = false;
        }

        if ((!var10 || var11 || var12) && !var9) {
            var1.setPadding(0, var5.getDimensionPixelSize(R.dimen.sesl_dialog_title_padding_top), 0, 0);
        } else {
            var1.setPadding(0, 0, 0, 0);
        }

        if (var2 != null) {
            if (var10 && var11 && !var12) {
                var2.setPadding(var5.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0, var5.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0);
            } else {
                var2.setPadding(var5.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), 0, var5.getDimensionPixelSize(R.dimen.sesl_dialog_padding_horizontal), var5.getDimensionPixelSize(R.dimen.sesl_dialog_title_padding_bottom));
            }
        }

        if (var3 != null) {
            var3.setPadding(var5.getDimensionPixelSize(R.dimen.sesl_dialog_body_text_scroll_padding_start), 0, var5.getDimensionPixelSize(R.dimen.sesl_dialog_body_text_scroll_padding_end), var5.getDimensionPixelSize(R.dimen.sesl_dialog_body_text_padding_bottom));
        }

        if (var4 != null) {
            var4.setPadding(var5.getDimensionPixelSize(R.dimen.sesl_dialog_button_bar_padding_horizontal), 0, var5.getDimensionPixelSize(R.dimen.sesl_dialog_button_bar_padding_horizontal), var5.getDimensionPixelSize(R.dimen.sesl_dialog_button_bar_padding_bottom));
        }

    }
    // endregion


    // region AREA: setupTitle
    @SuppressLint("ResourceType")
    private void setupTitle (ViewGroup viewGroup) {
        if (this.mCustomTitleView != null) {
            ViewGroup.LayoutParams var2 = new ViewGroup.LayoutParams(-1, -2);
            viewGroup.addView(this.mCustomTitleView, 0, var2);
            // MEMO: World8848. change from '8' to 'View.GONE'
            this.mWindow.findViewById(R.id.title_template).setVisibility(View.GONE);
        } else {
            // MEMO: World8848. 16908294. https://developer.android.com/reference/android/R.id#icon
            // MEMO: World8848. change. from '16908294' to 'R.id.icon'. not working
            this.mIconView = (ImageView) this.mWindow.findViewById(16908294);
            if (TextUtils.isEmpty(this.mTitle) ^ true && this.mShowTitle) {
                this.mTitleView = (TextView) this.mWindow.findViewById(R.id.alertTitle);
                this.mTitleView.setText(this.mTitle);
                this.checkMaxFontScale(this.mTitleView, this.mContext.getResources().getDimensionPixelSize(R.dimen.sesl_dialog_title_text_size));
                int var3 = this.mIconId;
                if (var3 != 0) {
                    this.mIconView.setImageResource(var3);
                } else {
                    Drawable var4 = this.mIcon;
                    if (var4 != null) {
                        this.mIconView.setImageDrawable(var4);
                    } else {
                        this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
                        // MEMO: World8848. change from '8' to 'View.GONE'
                        this.mIconView.setVisibility(View.GONE);
                    }
                }
            } else {
                // MEMO: World8848. change from '8' to 'View.GONE'
                this.mWindow.findViewById(R.id.title_template).setVisibility(View.GONE);
                this.mIconView.setVisibility(View.GONE);
                viewGroup.setVisibility(View.GONE);
            }
        }
    }
//    @SuppressLint({"WrongConstant", "ResourceType"})
//    private void setupTitle(ViewGroup var1) {
//        if (this.mCustomTitleView != null) {
//            ViewGroup.LayoutParams var2 = new ViewGroup.LayoutParams(-1, -2);
//            var1.addView(this.mCustomTitleView, 0, var2);
//            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
//        } else {
//            this.mIconView = (ImageView) this.mWindow.findViewById(16908294);
//            if (TextUtils.isEmpty(this.mTitle) ^ true && this.mShowTitle) {
//                this.mTitleView = (TextView) this.mWindow.findViewById(R.id.alertTitle);
//                this.mTitleView.setText(this.mTitle);
//                this.checkMaxFontScale(this.mTitleView, this.mContext.getResources().getDimensionPixelSize(R.dimen.sesl_dialog_title_text_size));
//                int var3 = this.mIconId;
//                if (var3 != 0) {
//                    this.mIconView.setImageResource(var3);
//                } else {
//                    Drawable var4 = this.mIcon;
//                    if (var4 != null) {
//                        this.mIconView.setImageDrawable(var4);
//                    } else {
//                        this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
//                        this.mIconView.setVisibility(8);
//                    }
//                }
//            } else {
//                this.mWindow.findViewById(R.id.title_template).setVisibility(8);
//                this.mIconView.setVisibility(8);
//                var1.setVisibility(8);
//            }
//        }
//    }
    // endregion


    // region AREA: setupView
    private void setupView() {
        final View var1 = this.mWindow.findViewById(R.id.parentPanel);
        var1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View var1x, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
                var1x.post(new Runnable() {
                    public void run() {
                        int var1x = SamsungAlertController.this.mContext.getResources().getConfiguration().orientation;
                        SamsungAlertController var2 = SamsungAlertController.this;
                        if (var1x != var2.mLastOrientation) {
                            var2.setupPaddings();
                            var1.requestLayout();
                        }

                        var2 = SamsungAlertController.this;
                        var2.mLastOrientation = var2.mContext.getResources().getConfiguration().orientation;
                    }
                });
            }
        });
        View var2 = var1.findViewById(R.id.topPanel);
        View var3 = var1.findViewById(R.id.contentPanel);
        View var4 = var1.findViewById(R.id.buttonPanel);
        ViewGroup var5 = (ViewGroup) var1.findViewById(R.id.customPanel);
        this.setupCustomContent(var5);
        View var6 = var5.findViewById(R.id.topPanel);
        View var7 = var5.findViewById(R.id.contentPanel);
        View var8 = var5.findViewById(R.id.buttonPanel);
        ViewGroup var19 = this.resolvePanel(var6, var2);
        ViewGroup var20 = this.resolvePanel(var7, var3);
        ViewGroup var18 = this.resolvePanel(var8, var4);
        this.setupContent(var20);
        this.setupButtons(var18);
        this.setupTitle(var19);
        boolean var9;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var5 != null && var5.getVisibility() != View.GONE) {
            var9 = true;
        } else {
            var9 = false;
        }

        byte var10;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var19 != null && var19.getVisibility() != View.GONE) {
            var10 = 1;
        } else {
            var10 = 0;
        }

        boolean var11;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var18 != null && var18.getVisibility() != View.GONE) {
            var11 = true;
        } else {
            var11 = false;
        }

        boolean var12;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var2 != null && var2.getVisibility() != View.GONE) {
            var12 = true;
        } else {
            var12 = false;
        }

        boolean var13;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var3 != null && var3.getVisibility() != View.GONE) {
            var13 = true;
        } else {
            var13 = false;
        }

        var2 = this.mCustomTitleView;
        boolean var14;
        // MEMO: World8848. change from '8' to 'View.GONE'
        if (var2 != null && var2.getVisibility() != View.GONE) {
            var14 = true;
        } else {
            var14 = false;
        }

        if (var9 && !var12 && !var13 || var14) {
            this.adjustParentPanelPadding(var1);
        }

        if (var9 && var12 && !var13) {
            this.adjustTopPanelPadding(var1);
        }

        this.adjustButtonsPadding();
        if (var10 != 0) {
            NestedScrollView var15 = this.mScrollView;
            if (var15 != null) {
                var15.setClipToPadding(true);
            }
        }

        ListView var16 = this.mListView;
        if (var16 instanceof RecycleListView) {
            ((RecycleListView) var16).setHasDecor(var10 == 1, var11);
        }

        if (!var9) {
            Object var17 = this.mListView;
            if (var17 == null) {
                var17 = this.mScrollView;
            }

            if (var17 != null) {
                byte var22;
                if (var11) {
                    var22 = 2;
                } else {
                    var22 = 0;
                }

                this.setScrollIndicators(var20, (View) var17, var22 | var10, 3);
            }
        }

        var16 = this.mListView;
        if (var16 != null) {
            ListAdapter var21 = this.mAdapter;
            if (var21 != null) {
                var16.setAdapter(var21);

                if (Build.VERSION.SDK_INT >= 28)
                    ReflectUtils.genericInvokeMethod(AdapterView.class, var21, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetBottomColor" : "semSetBottomColor", 0);

                int var23 = this.mCheckedItem;
                if (var23 > -1) {
                    var16.setItemChecked(var23, true);
                    var16.setSelectionFromTop(var23, this.mContext.getResources().getDimensionPixelSize(R.dimen.sesl_select_dialog_padding_top));
                }
            }
        }

    }
    // endregion


    // region AREA: setView
    public void setView (int var1) {
        this.mView = null;
        this.mViewLayoutResId = var1;
        this.mViewSpacingSpecified = false;
    }

    public void setView (View var1) {
        this.mView = var1;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = false;
    }

    public void setView (View var1, int var2, int var3, int var4, int var5) {
        this.mView = var1;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = var2;
        this.mViewSpacingTop = var3;
        this.mViewSpacingRight = var4;
        this.mViewSpacingBottom = var5;
    }
    // endregion


    // region AREA: shouldCenterSingleButton
    public static boolean shouldCenterSingleButton (Context context) {
        TypedValue var1         = new TypedValue();
        Resources.Theme var4    = context.getTheme();
        @SuppressLint("PrivateResource")
        int var2                = R.attr.alertDialogCenterButtons;
        boolean var3            = true;
        var4.resolveAttribute(var2, var1, true);
        if (var1.data == 0) {
            var3 = false;
        }

        return var3;
    }
    // endregion


    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Class. AlertParams
    //**********************************************************************************************
    public static class AlertParams {

        // region AREA: Variables
        public final Context mContext;
        public final LayoutInflater mInflater;
        public ListAdapter mAdapter;
        public boolean mCancelable;
        public int mCheckedItem = -1;
        public boolean[] mCheckedItems;
        public Cursor mCursor;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public Drawable mIcon;
        public int mIconAttrId = 0;
        public int mIconId = 0;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public CharSequence mMessage;
        public Drawable mNegativeButtonIcon;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public Drawable mNeutralButtonIcon;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface.OnClickListener mOnClickListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public Drawable mPositiveButtonIcon;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public boolean mRecycleOnMeasure = true;
        public CharSequence mTitle;
        public View mView;
        public int mViewLayoutResId;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public boolean mViewSpacingSpecified = false;
        public int mViewSpacingTop;
        // endregion


        // region AREA: AlertParams
        @SuppressLint("WrongConstant")
        public AlertParams (Context context) {
            this.mContext       = context;
            this.mCancelable    = true;
            this.mInflater      = (LayoutInflater) context.getSystemService("layout_inflater");
        }
        // endregion


        // region AREA: apply
        public void apply(SamsungAlertController var1) {
            View var2 = this.mCustomTitleView;
            int var3;
            CharSequence var4;
            if (var2 != null) {
                var1.setCustomTitle(var2);
            } else {
                var4 = this.mTitle;
                if (var4 != null) {
                    var1.setTitle(var4);
                }

                Drawable var5 = this.mIcon;
                if (var5 != null) {
                    var1.setIcon(var5);
                }

                var3 = this.mIconId;
                if (var3 != 0) {
                    var1.setIcon(var3);
                }

                var3 = this.mIconAttrId;
                if (var3 != 0) {
                    var1.setIcon(var1.getIconAttributeResId(var3));
                }
            }

            var4 = this.mMessage;
            if (var4 != null) {
                var1.setMessage(var4);
            }

            if (this.mPositiveButtonText != null || this.mPositiveButtonIcon != null) {
                var1.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, (Message) null, this.mPositiveButtonIcon);
            }

            if (this.mNegativeButtonText != null || this.mNegativeButtonIcon != null) {
                var1.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, (Message) null, this.mNegativeButtonIcon);
            }

            if (this.mNeutralButtonText != null || this.mNeutralButtonIcon != null) {
                var1.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, (Message) null, this.mNeutralButtonIcon);
            }

            if (this.mItems != null || this.mCursor != null || this.mAdapter != null) {
                this.createListView(var1);
            }

            var2 = this.mView;
            if (var2 != null) {
                if (this.mViewSpacingSpecified) {
                    var1.setView(var2, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
                } else {
                    var1.setView(var2);
                }
            } else {
                var3 = this.mViewLayoutResId;
                if (var3 != 0) {
                    var1.setView(var3);
                }
            }

        }
        // endregion


        // region AREA: createListView
        @SuppressLint("ResourceType")
        private void createListView (final SamsungAlertController samsungAlertController) {
            final RecycleListView var2 = (RecycleListView) this.mInflater.inflate(samsungAlertController.mListLayout, (ViewGroup) null);
            Cursor var3;
            Object var6;
            if (this.mIsMultiChoice) {
                var3 = this.mCursor;
                if (var3 == null) {

                    var6 = new ArrayAdapter<CharSequence>(this.mContext, samsungAlertController.mMultiChoiceItemLayout, 16908308, this.mItems) {
                        public View getView(int var1, View var2x, ViewGroup var3) {
                            View var5 = super.getView(var1, var2x, var3);
                            boolean[] var4 = AlertParams.this.mCheckedItems;
                            if (var4 != null && var4[var1]) {
                                var2.setItemChecked(var1, true);
                            }

                            return var5;
                        }
                    };
                } else {
                    var6 = new CursorAdapter(this.mContext, var3, false) {
                        public final int mIsCheckedIndex;
                        public final int mLabelIndex;

                        {
                            Cursor var7 = this.getCursor();
                            this.mLabelIndex = var7.getColumnIndexOrThrow(AlertParams.this.mLabelColumn);
                            this.mIsCheckedIndex = var7.getColumnIndexOrThrow(AlertParams.this.mIsCheckedColumn);
                        }

                        @SuppressLint("ResourceType")
                        public void bindView(View var1x, Context var2x, Cursor var3) {
                            ((CheckedTextView) var1x.findViewById(16908308)).setText(var3.getString(this.mLabelIndex));
                            RecycleListView var7 = var2;
                            int var4 = var3.getPosition();
                            int var5 = var3.getInt(this.mIsCheckedIndex);
                            boolean var6 = true;
                            if (var5 != 1) {
                                var6 = false;
                            }

                            var7.setItemChecked(var4, var6);
                        }

                        public View newView(Context var1x, Cursor var2x, ViewGroup var3) {
                            return AlertParams.this.mInflater.inflate(samsungAlertController.mMultiChoiceItemLayout, var3, false);
                        }
                    };
                }
            } else {
                int var4;
                if (this.mIsSingleChoice) {
                    var4 = samsungAlertController.mSingleChoiceItemLayout;
                } else {
                    var4 = samsungAlertController.mListItemLayout;
                }

                var3 = this.mCursor;
                if (var3 != null) {
                    var6 = new SimpleCursorAdapter(this.mContext, var4, var3, new String[]{this.mLabelColumn}, new int[]{16908308});
                } else {
                    var6 = this.mAdapter;
                    if (var6 == null) {
                        var6 = new CheckedItemAdapter(this.mContext, var4, 16908308, this.mItems);
                    }
                }
            }

            OnPrepareListViewListener var5 = this.mOnPrepareListViewListener;
            if (var5 != null) {
                var5.onPrepareListView(var2);
            }

            samsungAlertController.mAdapter = (ListAdapter) var6;
            samsungAlertController.mCheckedItem = this.mCheckedItem;
            if (this.mOnClickListener != null) {
                var2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> var1x, View var2, int var3, long var4) {
                        AlertParams.this.mOnClickListener.onClick(samsungAlertController.mDialog, var3);
                        if (!AlertParams.this.mIsSingleChoice) {
                            samsungAlertController.mDialog.dismiss();
                        }

                    }
                });
            } else if (this.mOnCheckboxClickListener != null) {
                var2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> var1x, View var2x, int var3, long var4) {
                        boolean[] var6 = AlertParams.this.mCheckedItems;
                        if (var6 != null) {
                            var6[var3] = var2.isItemChecked(var3);
                        }

                        AlertParams.this.mOnCheckboxClickListener.onClick(samsungAlertController.mDialog, var3, var2.isItemChecked(var3));
                    }
                });
            }

            AdapterView.OnItemSelectedListener var7 = this.mOnItemSelectedListener;
            if (var7 != null) {
                var2.setOnItemSelectedListener(var7);
            }

            if (this.mIsSingleChoice) {
                var2.setChoiceMode(1);
            } else if (this.mIsMultiChoice) {
                var2.setChoiceMode(2);
            }

            samsungAlertController.mListView = var2;
        }
        // endregion


        // region AREA: OnPrepareListViewListener
        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView var1);
        }
        // endregion
    }
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Class. ButtonBarLayout
    //**********************************************************************************************
    public static class ButtonBarLayout extends LinearLayout {

        // region AREA: ButtonBarLayout
        public ButtonBarLayout (Context var1) {
            super(var1);
        }

        public ButtonBarLayout (Context var1, AttributeSet var2) {
            super(var1, var2);
        }

        public ButtonBarLayout (Context var1, AttributeSet var2, int var3) {
            super(var1, var2, var3);
        }

        public ButtonBarLayout (Context var1, AttributeSet var2, int var3, int var4) {
            super(var1, var2, var3, var4);
        }
        // endregion

        // region AREA: onMeasure
        @SuppressLint("WrongConstant")
        public void onMeasure(int var1, int var2) {
            super.onMeasure(var1, var2);
            int var3 = this.getChildCount();
            int var4 = MeasureSpec.getSize(var1) - this.getPaddingRight() - this.getPaddingLeft();
            int var5 = MeasureSpec.getSize(var2);
            int var6 = this.getPaddingTop();
            int var7 = this.getPaddingBottom();
            byte var8 = 0;
            int var9 = 0;
            int var10 = var9;
            int var11 = var9;

            int var12;
            View var13;
            int var16;
            for (var12 = var9; var9 < var3; var12 = var16) {
                var13 = this.getChildAt(var9);
                int var14 = var10;
                int var15 = var11;
                var16 = var12;
                if (var13.getVisibility() != 8) {
                    var14 = var10;
                    var15 = var11;
                    var16 = var12;
                    if (var13 instanceof Button) {
                        var15 = var11 + var13.getMeasuredWidth();
                        var13.measure(MeasureSpec.makeMeasureSpec(var4, -2147483648), MeasureSpec.makeMeasureSpec(var5 - var6 - var7, -2147483648));
                        var16 = var12 + var13.getMeasuredWidth();
                        var14 = var10 + 1;
                    }
                }

                ++var9;
                var10 = var14;
                var11 = var15;
            }

            boolean var18 = true;
            --var10;
            if (var10 > 0) {
                var10 = (int) ((float) var10 * this.getContext().getResources().getDisplayMetrics().density);
            } else {
                var10 = 0;
            }

            if (var11 >= var12 && var4 > var12 + var10) {
                if (this.getOrientation() != 0) {
                    this.setOrientation(0);
                    boolean var20;
                    if (this.findViewById(R.id.button1).getVisibility() == 0) {
                        var20 = true;
                    } else {
                        var20 = false;
                    }

                    boolean var19;
                    if (this.findViewById(R.id.button2).getVisibility() == 0) {
                        var19 = true;
                    } else {
                        var19 = false;
                    }

                    boolean var21;
                    if (this.findViewById(R.id.button3).getVisibility() == 0) {
                        var21 = var18;
                    } else {
                        var21 = false;
                    }

                    var13 = this.findViewById(R.id.sem_divider1);
                    View var17 = this.findViewById(R.id.sem_divider2);
                    if (var17 != null && (var21 && var20 || var21 && var19)) {
                        var17.setVisibility(0);
                    }

                    if (var13 != null && var20 && var19) {
                        var13.setVisibility(0);
                    }
                }
            } else if (this.getOrientation() != 1) {
                this.setOrientation(1);

                for (var10 = var8; var10 < var3; ++var10) {
                    var13 = this.getChildAt(var10);
                    if (var13.getVisibility() != 8 && !(var13 instanceof Button)) {
                        var13.setVisibility(8);
                    }
                }

                this.setGravity(17);
            }

            super.onMeasure(var1, var2);
        }
        // endregion

    }
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Class. ButtonHandler
    //**********************************************************************************************
    private static final class ButtonHandler extends Handler {

        public static final int MSG_DISMISS_DIALOG = 1;
        public WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface var1) {
            this.mDialog = new WeakReference(var1);
        }

        public void handleMessage (Message var1) {
            int var2 = var1.what;
            if (var2 != -3 && var2 != -2 && var2 != -1) {
                if (var2 == 1) {
                    ((DialogInterface) var1.obj).dismiss();
                }
            } else {
                ((DialogInterface.OnClickListener) var1.obj).onClick((DialogInterface) this.mDialog.get(), var1.what);
            }

        }
    }
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Class. CheckedItemAdapter
    //**********************************************************************************************
    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {

        public CheckedItemAdapter(Context var1, int var2, int var3, CharSequence[] var4) {
            super(var1, var2, var3, var4);
        }

        public long getItemId(int var1) {
            return (long) var1;
        }

        public boolean hasStableIds() {
            return true;
        }
    }
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Class. RecycleListView
    //**********************************************************************************************
    public static class RecycleListView extends ListView {
        public final int mPaddingBottomNoButtons;
        public final int mPaddingTopNoTitle;

        public RecycleListView(Context var1) {
            this(var1, (AttributeSet) null);
        }

        public RecycleListView(Context var1, AttributeSet var2) {
            super(var1, var2);
            TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.RecycleListView);
            this.mPaddingBottomNoButtons = var3.getDimensionPixelOffset(R.styleable.RecycleListView_paddingBottomNoButtons, -1);
            this.mPaddingTopNoTitle = var3.getDimensionPixelOffset(R.styleable.RecycleListView_paddingTopNoTitle, -1);
        }

        public void setHasDecor(boolean var1, boolean var2) {
            if (!var2 || !var1) {
                int var3 = this.getPaddingLeft();
                int var4;
                if (var1) {
                    var4 = this.getPaddingTop();
                } else {
                    var4 = this.mPaddingTopNoTitle;
                }

                int var5 = this.getPaddingRight();
                int var6;
                if (var2) {
                    var6 = this.getPaddingBottom();
                } else {
                    var6 = this.mPaddingBottomNoButtons;
                }

                this.setPadding(var3, var4, var5, var6);
            }

        }

        public void setOverScrollMode(int var1) {
            if (var1 != 2) {
                if ((EdgeEffect) ReflectUtils.genericGetField(this, Build.VERSION.SDK_INT >= 29 ? "hidden_mEdgeGlowTop" : "mEdgeGlowTop") == null) {
                    Context var2 = this.getContext();
                    SamsungEdgeEffect var3 = new SamsungEdgeEffect(var2);
                    SamsungEdgeEffect var4 = new SamsungEdgeEffect(var2);
                    var3.setSeslHostView(this);
                    var4.setSeslHostView(this);

                    ReflectUtils.genericSetField(this, Build.VERSION.SDK_INT >= 29 ? "hidden_mEdgeGlowTop" : "mEdgeGlowTop", var3);
                    ReflectUtils.genericSetField(this, Build.VERSION.SDK_INT >= 29 ? "hidden_mEdgeGlowBottom" : "mEdgeGlowBottom", var4);
                }
            } else {
                ReflectUtils.genericSetField(this, Build.VERSION.SDK_INT >= 29 ? "hidden_mEdgeGlowTop" : "mEdgeGlowTop", (EdgeEffect) null);
                ReflectUtils.genericSetField(this, Build.VERSION.SDK_INT >= 29 ? "hidden_mEdgeGlowBottom" : "mEdgeGlowBottom", (EdgeEffect) null);
            }

            super.setOverScrollMode(var1);
        }
    }
    // endregion
    //**********************************************************************************************



}
