package com.buxuan.baseoneuiapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.util.SeslMisc;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.layout.PreferenceFragment;
import com.buxuan.baseoneui.preference.HorizontalRadioPreference;
import com.buxuan.baseoneui.preference.Preference;
import com.buxuan.baseoneui.preference.PreferenceGroup;
import com.buxuan.baseoneui.preference.SwitchPreference;
import com.buxuan.baseoneui.preference.SwitchPreferenceScreen;
import com.buxuan.baseoneui.preference.TipsCardViewPreference;
import com.buxuan.baseoneui.preference.internal.PreferencesRelatedCard;
import com.buxuan.baseoneui.utils.ThemeUtil;
import com.buxuan.baseoneuiapp.MainActivity;


public class MainActivitySecondFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener,
        View.OnClickListener {
    private long mLastClickTime = 0L;
    private MainActivity mActivity;
    private Context mContext;
    private PreferencesRelatedCard mRelatedCard;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity)
            mActivity = ((MainActivity) getActivity());
        mContext = getContext();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        int darkMode = ThemeUtil.getDarkMode(mContext);

        TipsCardViewPreference tipCard = (TipsCardViewPreference) findPreference("tip_card");
        PreferenceGroup parent = getParent(getPreferenceScreen(), tipCard);
        tipCard.setTipsCardListener(new TipsCardViewPreference.TipsCardListener() {
            @Override
            public void onCancelClicked(View view) {
                if (parent != null) {
                    parent.removePreference(tipCard);
                    parent.removePreference(findPreference("spacing"));
                }
            }
        });

        HorizontalRadioPreference darkModePref = (HorizontalRadioPreference) findPreference("dark_mode");
        darkModePref.setOnPreferenceChangeListener(this);
        darkModePref.setDividerEnabled(false);
        darkModePref.setTouchEffectEnabled(false);
        darkModePref.setEnabled(darkMode != ThemeUtil.DARK_MODE_AUTO);
        darkModePref.setValue(SeslMisc.isLightTheme(mContext) ? "0" : "1");

        SwitchPreference autoDarkModePref = (SwitchPreference) findPreference("dark_mode_auto");
        autoDarkModePref.setOnPreferenceChangeListener(this);
        autoDarkModePref.setChecked(darkMode == ThemeUtil.DARK_MODE_AUTO);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setBackgroundColor(getResources().getColor(R.color.item_background_color));
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("com.buxuan.baseoneuiapp_preferences", Context.MODE_PRIVATE);
        SwitchPreferenceScreen switchPreferenceScreen = (SwitchPreferenceScreen) findPreference("switch_preference_screen");
        switchPreferenceScreen.setChecked(sharedPreferences.getBoolean("switch_preference_screen", false));
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String currentDarkMode = String.valueOf(ThemeUtil.getDarkMode(mContext));
        HorizontalRadioPreference darkModePref = (HorizontalRadioPreference) findPreference("dark_mode");

        switch (preference.getKey()) {
            case "dark_mode":
                if (currentDarkMode != newValue) {
                    ThemeUtil.setDarkMode(mActivity, ((String) newValue).equals("0") ? ThemeUtil.DARK_MODE_DISABLED : ThemeUtil.DARK_MODE_ENABLED);
                }
                return true;
            case "dark_mode_auto":
                if ((boolean) newValue) {
                    darkModePref.setEnabled(false);
                    ThemeUtil.setDarkMode(mActivity, ThemeUtil.DARK_MODE_AUTO);
                } else {
                    darkModePref.setEnabled(true);
                }
                return true;
        }

        return false;
    }

    @Override
    public void onResume() {
        setRelatedCardView();
        super.onResume();
    }

    private void setRelatedCardView() {
        if (mRelatedCard == null) {
            mRelatedCard = createRelatedCard(mContext);
            mRelatedCard.addButton("This", this)
                    .addButton("That", this)
                    .addButton("There", this)
                    .show(this);
        }
    }
}

