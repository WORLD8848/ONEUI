package com.buxuan.baseoneui.sesl.colorpicker.util;

import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.buxuan.baseoneui.sesl.colorpicker.util.color.SpenColorUtil;

public class SpenSettingUtil {

    // region AREA: Functions                                       ////////////////////////////////

    // region AREA: initDialogWindow
    /**
     * initDialogWindow
     * SpenSettingUtil.initDialogWindow(this, 5376, -1); 호출되는 경우
     * systemUiVisibility = 0, attributes.height = -1 초기화된다.
     * @param dialog Dialog
     * @param i1 int
     * @param i2 int
     */
    public static void initDialogWindow(Dialog dialog, int i1, int i2) {

        Window window            = dialog.getWindow();
        View decorView          = window.getDecorView();
        int systemUiVisibility  = decorView.getSystemUiVisibility();

        int i3 = i1 | systemUiVisibility;

        if (i3 == systemUiVisibility) {
            i3 = systemUiVisibility;
        }

        decorView.setSystemUiVisibility(i3);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = i2;
        window.setAttributes(attributes);
    }
    // endregion

    // endregion                                                    ////////////////////////////////


    public static boolean isNightMode(Context context) {
        return ((UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE)).getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }




    public static int getStatusBarHeight(Context context) {
        int identifier;
        if (context != null && (identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android")) > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static int getColor(Context context, int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(i);
        }
        return context.getResources().getColor(i);
    }

    public static boolean setShadowAlpha(View view, float f) {
        if (view == null || Build.VERSION.SDK_INT < 28) {
            return false;
        }
        view.setOutlineSpotShadowColor((((int) (f * 255.0f)) << 24) | (view.getOutlineSpotShadowColor() & 16777215));
        return true;
    }

    public static int HSVToColor(float[] fArr) {
        return HSVToColor(255, fArr);
    }

    public static int HSVToColor(int i, float[] fArr) {
        return SpenColorUtil.HSVToColor(i, fArr);
    }

}
