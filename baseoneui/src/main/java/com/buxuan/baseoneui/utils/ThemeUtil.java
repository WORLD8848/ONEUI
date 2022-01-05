package com.buxuan.baseoneui.utils;

import android.app.UiModeManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {

    // region AREA: Constants                                       ////////////////////////////////
    public static final int DARK_MODE_AUTO      = 2;
    public static final int DARK_MODE_DISABLED  = 0;
    public static final int DARK_MODE_ENABLED   = 1;

    private static final String NAME            = "ThemeColor";
    private static final String KEY_COLOR       = "color";
    private static final String KEY_DARK_MODE   = "dark_mode";
    // endregion                                                    ////////////////////////////////



    // region AREA: Constructor                                     ////////////////////////////////
    public ThemeUtil(AppCompatActivity activity) {
        this(activity, "0381fe");
    }

    public ThemeUtil(AppCompatActivity activity, String appColor) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        int darkMode                        = sharedPreferences.getInt(KEY_DARK_MODE, DARK_MODE_AUTO);
        String stringColor                  = sharedPreferences.getString(KEY_COLOR, appColor);

        setDarkMode(activity, darkMode);

        activity.setTheme(activity.getResources().getIdentifier("Color_" + stringColor, "style", activity.getPackageName()));
    }
    // endregion                                                    ////////////////////////////////



    // region AREA: Functions                                       ////////////////////////////////
    // region AREA: createDarkModeConfig
    public static Configuration createDarkModeConfig(Context context, Configuration config) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        int darkMode = sharedPreferences.getInt(KEY_DARK_MODE, DARK_MODE_AUTO);

        int uiModeNight;
        if (darkMode == DARK_MODE_DISABLED) {
            uiModeNight = Configuration.UI_MODE_NIGHT_NO;
        } else if (darkMode == DARK_MODE_ENABLED) {
            uiModeNight = Configuration.UI_MODE_NIGHT_YES;
        } else {
            uiModeNight = ((UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE)).getCurrentModeType() & Configuration.UI_MODE_NIGHT_MASK;
        }

        Configuration newConfig = new Configuration(config);
        int newUiMode = newConfig.uiMode & -Configuration.UI_MODE_NIGHT_MASK | Configuration.UI_MODE_TYPE_NORMAL;
        newConfig.uiMode = newUiMode | uiModeNight;
        return newConfig;
    }
    // endregion


    // region AREA: createDarkModeContextWrapper
    public static ContextWrapper createDarkModeContextWrapper(Context context) {
        Configuration newConfig = createDarkModeConfig(context, context.getResources().getConfiguration());

        if (newConfig != null)
            return new ContextWrapper(context.createConfigurationContext(newConfig));
        else
            return new ContextWrapper(context);
    }
    // endregion


    // region AREA: getDarkMode
    public static int getDarkMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_DARK_MODE, DARK_MODE_AUTO);
    }
    // endregion


    // region AREA: getThemeColor
    public static int getThemeColor(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return Color.parseColor(sharedPreferences.getString(KEY_COLOR, "0381fe"));
    }
    // endregion


    // region AREA: setColor
    /**
     * RGB(RED, GREEN, BLUE) 인자를 가지고 RGB 16진수 코드값을 생성한다.
     * 생성한 후 SharedPreferencesd의 "KEY_COLOR" Key의 value로 할당하고 activity를 재시작한다.
     * @param activity AppCompatActivity
     * @param red int
     * @param green int
     * @param blue int
     */
    public static void setColor(AppCompatActivity activity, int red, int green, int blue) {

        red     = Math.round(red    / 15.0f) * 15;
        green   = Math.round(green  / 15.0f) * 15;
        blue    = Math.round(blue   / 15.0f) * 15;

        SharedPreferences.Editor editor = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_COLOR, Integer.toHexString(Color.rgb(red, green, blue)).substring(2)).apply();

        activity.recreate();
    }

    public static void setColor(AppCompatActivity activity, float red, float green, float blue) {
        setColor(activity, (int) (red * 255), (int) (green * 255), (int) (blue * 255));
    }

    public static void setColor(AppCompatActivity activity, float[] hsv) {
        int c = Color.HSVToColor(hsv);
        setColor(activity, Color.red(c), Color.green(c), Color.blue(c));
    }
    // endregion


    // region AREA: setDarkMode
    /**
     * mode 값으로 darkMode를 할당한다.
     * @param activity AppCompatActivity
     * @param mode mode는 DARK_MODE_ENABLE, DARK_MODE_DISABLE, DARK_MODE_AUTO 중 1가 올 수 있다.
     */
    public static void setDarkMode(AppCompatActivity activity, int mode) {

        // region AREA: 현재 darkMode와 mode 값이 다른 경우 mode 값으로 darkMode를 변경한다.
        if (getDarkMode(activity) != mode) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_DARK_MODE, mode).apply();
        }
        // endregion


        // region AREA: AppCompatDelegate
        if (mode != DARK_MODE_AUTO)
            AppCompatDelegate.setDefaultNightMode(mode == DARK_MODE_ENABLED ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        activity.getDelegate().applyDayNight();
        // endregion
    }
    // endregion
    // endregion                                                    ////////////////////////////////



}