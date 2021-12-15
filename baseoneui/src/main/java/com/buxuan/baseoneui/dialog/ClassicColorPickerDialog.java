package com.buxuan.baseoneui.dialog;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.TYPE_BASE_APPLICATION;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.sesl.colorpicker.SeslColorPicker;


public class ClassicColorPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

    //**********************************************************************************************
    // region AREA: Variables
    //**********************************************************************************************
    private static final String TAG                 = "SeslColorPickerDialog";
    private final SeslColorPicker mColorPicker;
    private final ColorPickerChangedListener mColorPickerChangedListener;
    private Integer mCurrentColor;
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Constructor
    //**********************************************************************************************
    public ClassicColorPickerDialog(Context context, ColorPickerChangedListener listener) {
        super(context);
        this.mCurrentColor      = null;
        Context var3            = this.getContext();
        View var4               = LayoutInflater.from(var3).inflate(R.layout.sesl_color_picker_dialog, (ViewGroup) null);
        this.setView(var4);
        this.setButton(-1, var3.getString(R.string.sesl_done), this);
        // MEMO: World8848. change
        // MeMO: Origin. this.setButton(-2, var3.getString(android.R.string.cancel), this);
        this.setButton(-2, var3.getString(R.string.sesl_cancel), this);
        // MEMO: World8848. change. from '1' to 'TYPE_BASE_APPLICATION'
        this.requestWindowFeature(TYPE_BASE_APPLICATION);
        // MEMO: World8848. change. from '16' to 'SOFT_INPUT_ADJUST_RESIZE'
        this.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE);
        this.mColorPickerChangedListener = listener;
        this.mColorPicker = (SeslColorPicker) var4.findViewById(R.id.sesl_color_picker_content_view);
    }

    public ClassicColorPickerDialog(Context context, ColorPickerChangedListener listener, int currentColor) {
        this(context, listener);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(currentColor);
        this.mCurrentColor = currentColor;
        this.mColorPicker.updateRecentColorLayout();
    }

    public ClassicColorPickerDialog(Context context, ColorPickerChangedListener listener, int currentColor, int[] recentColors) {
        this(context, listener);
        this.mColorPicker.getRecentColorInfo().initRecentColorInfo(recentColors);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(currentColor);
        this.mCurrentColor = currentColor;
        this.mColorPicker.updateRecentColorLayout();
    }

    public ClassicColorPickerDialog(Context context, ColorPickerChangedListener listener, int[] recentColors) {
        this(context, listener);
        this.mColorPicker.getRecentColorInfo().initRecentColorInfo(recentColors);
        this.mColorPicker.updateRecentColorLayout();
    }
    // endregion
    //**********************************************************************************************



    //**********************************************************************************************
    // region AREA: Functions
    //**********************************************************************************************
    public SeslColorPicker getColorPicker() {
        return this.mColorPicker;
    }
    // endregion
    //**********************************************************************************************






    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onClick(DialogInterface var1, int var2) {
        if (var2 != -2 && var2 == -1) {
            this.mColorPicker.saveSelectedColor();
            if (this.mColorPickerChangedListener != null) {
                if (!this.mColorPicker.isUserInputValid()) {
                    Integer var3 = this.mCurrentColor;
                    if (var3 != null) {
                        this.mColorPickerChangedListener.onColorChanged(var3);
                        return;
                    }
                }

                this.mColorPickerChangedListener.onColorChanged(this.mColorPicker.getRecentColorInfo().getSelectedColor());
            }
        }

    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setNewColor(Integer var1) {
        this.mColorPicker.getRecentColorInfo().setNewColor(var1);
        this.mColorPicker.updateRecentColorLayout();
    }

    public void setTransparencyControlEnabled(boolean var1) {
        this.mColorPicker.setOpacityBarEnabled(var1);
    }

    public interface ColorPickerChangedListener {
        void onColorChanged(int var1);
    }
}
