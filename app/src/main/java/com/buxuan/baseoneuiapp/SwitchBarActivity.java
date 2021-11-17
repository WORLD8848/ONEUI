package com.buxuan.baseoneuiapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.layout.SwitchBarLayout;
import com.buxuan.baseoneui.utils.ThemeUtil;
import com.buxuan.baseoneui.view.Switch;
import com.buxuan.baseoneui.view.SwitchBar;

public class SwitchBarActivity extends AppCompatActivity implements SwitchBar.OnSwitchChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ThemeUtil(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switchbar);

        SwitchBarLayout switchBarLayout = findViewById(R.id.switchbarlayout_switchbaractivity);
        switchBarLayout.getSwitchBar().setChecked(getSwitchBarDefaultStatus());
        switchBarLayout.getSwitchBar().addOnSwitchChangeListener(this);
    }

    private boolean getSwitchBarDefaultStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.buxuan.baseoneuiapp_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("switch_preference_screen", false);
    }

    @Override
    public void onSwitchChanged(Switch switchCompat, boolean z) {
        SharedPreferences.Editor editor = getSharedPreferences("com.buxuan.baseoneuiapp_preferences", Context.MODE_PRIVATE).edit();
        editor.putBoolean("switch_preference_screen", z).apply();
    }
}
