package com.buxuan.baseoneuiapp;


import static com.buxuan.baseoneui.layout.DrawerLayout.DRAWER_LAYOUT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.util.SeslMisc;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.dialog.AlertDialog;
import com.buxuan.baseoneui.dialog.ClassicColorPickerDialog;
import com.buxuan.baseoneui.dialog.DetailedColorPickerDialog;
import com.buxuan.baseoneui.dialog.ProgressDialog;
import com.buxuan.baseoneui.layout.DrawerLayout;
import com.buxuan.baseoneui.layout.ToolbarLayout;
import com.buxuan.baseoneui.sesl.support.ViewSupport;
import com.buxuan.baseoneui.sesl.utils.ReflectUtils;
import com.buxuan.baseoneui.utils.CustomButtonClickListener;
import com.buxuan.baseoneui.utils.ThemeUtil;
import com.buxuan.baseoneui.view.BottomNavigationView;
import com.buxuan.baseoneui.view.PopupMenu;
import com.buxuan.baseoneuiapp.utils.TabsManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private String[] mTabsTagName;
    private String[] mTabsTitleName;
    private String[] mTabsClassName;

    private boolean mIsLightTheme;
    private String sharedPrefName;

    private Context mContext;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private TabsManager mTabsManager;

    private DrawerLayout drawerLayout;
    private ToolbarLayout toolbarLayout;
    private BottomNavigationView bnvLayout;
    private PopupMenu bnvPopupMenu;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ThemeUtil(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> toolbarLayout.onSearchModeVoiceInputResult(result));

        init();
    }

    @Override
    public void attachBaseContext(Context context) {
        // pre-OneUI
        if (Build.VERSION.SDK_INT <= 28) {
            super.attachBaseContext(ThemeUtil.createDarkModeContextWrapper(context));
        } else
            super.attachBaseContext(context);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // pre-OneUI
        if (Build.VERSION.SDK_INT <= 28) {
            Resources res = getResources();
            res.getConfiguration().setTo(ThemeUtil.createDarkModeConfig(mContext, newConfig));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (bnvLayout != null) {
            bnvLayout.setResumeStatus(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bnvLayout != null) {
            bnvLayout.setResumeStatus(true);
        }
    }

    private void init() {
        ViewSupport.semSetRoundedCorners(getWindow().getDecorView(), 0);

        mIsLightTheme = SeslMisc.isLightTheme(mContext);

        // World8848. Log.d("88888888", "1111111111111" + mIsLightTheme);

        drawerLayout = findViewById(R.id.drawer_view);
        toolbarLayout = drawerLayout.getToolbarLayout();
        bnvLayout = findViewById(R.id.main_samsung_tabs);

        sharedPrefName = "mainactivity_tabs";
        mTabsTagName = getResources().getStringArray(R.array.mainactivity_tab_tag);
        mTabsTitleName = getResources().getStringArray(R.array.mainactivity_tab_title);
        mTabsClassName = getResources().getStringArray(R.array.mainactivity_tab_class);

        mTabsManager = new TabsManager(mContext, sharedPrefName);
        mTabsManager.initTabPosition();

        mFragmentManager = getSupportFragmentManager();

        //DrawerLayout
        drawerLayout.setDrawerButtonOnClickListener(v -> startActivity(new Intent().setClass(mContext, com.buxuan.baseoneuiapp.AboutActivity.class)));
        drawerLayout.setDrawerButtonTooltip(getText(R.string.app_info));
        drawerLayout.setButtonBadges(ToolbarLayout.N_BADGE, DrawerLayout.N_BADGE);

        toolbarLayout.getAppBarLayout().addOnOffsetChangedListener((layout, verticalOffset) -> {
            int totalScrollRange = layout.getTotalScrollRange();
            int inputMethodWindowVisibleHeight = (int) ReflectUtils.genericInvokeMethod(InputMethodManager.class, mContext.getSystemService(INPUT_METHOD_SERVICE), "getInputMethodWindowVisibleHeight");
            LinearLayout nothingLayout = findViewById(R.id.nothing_layout);
            if (nothingLayout != null) {
                if (totalScrollRange != 0) {
                    nothingLayout.setTranslationY(((float) (Math.abs(verticalOffset) - totalScrollRange)) / 2.0f);
                } else {
                    nothingLayout.setTranslationY(((float) (Math.abs(verticalOffset) - inputMethodWindowVisibleHeight)) / 2.0f);
                }
            }
        });

        toolbarLayout.inflateToolbarMenu(R.menu.main);
        toolbarLayout.setOnToolbarMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    toolbarLayout.showSearchMode();
                    toolbarLayout.setSearchModeListener(new ToolbarLayout.SearchModeListener() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }

                        @Override
                        public void onKeyboardSearchClick(CharSequence s) {
                            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVoiceInputClick(Intent intent) {
                            activityResultLauncher.launch(intent);
                        }
                    });
                    break;
                case R.id.info:
                    startActivity(new Intent().setClass(mContext, com.buxuan.baseoneuiapp.AboutActivity.class));
                    break;
                case R.id.item1:
                case R.id.item2:
                case R.id.item3:
                    toolbarLayout.setOverflowMenuBadge(item, toolbarLayout.getOverflowMenuBadge(item) + 1);
                    break;
            }
        });

        //BottomNavigationLayout
        Drawable icon = getDrawable(R.drawable.ic_samsung_drawer);
        icon.setColorFilter(getResources().getColor(R.color.sesl_tablayout_text_color), PorterDuff.Mode.SRC_IN);
        for (String s : mTabsTitleName) {
            bnvLayout.addTab(bnvLayout.newTab().setText(s));
        }
        bnvLayout.addTabCustomButton(icon, new CustomButtonClickListener(bnvLayout) {
            @Override
            public void onClick(View v) {
                popupView(v);
            }
        });

        bnvLayout.addOnTabSelectedListener(new BottomNavigationView.OnTabSelectedListener() {
            public void onTabSelected(BottomNavigationView.Tab tab) {
                int tabPosition = tab.getPosition();
                mTabsManager.setTabPosition(tabPosition);
                setCurrentItem();
            }

            public void onTabUnselected(BottomNavigationView.Tab tab) {
            }

            public void onTabReselected(BottomNavigationView.Tab tab) {
            }
        });
        bnvLayout.updateWidget(this);
        setCurrentItem();
    }

    private void setCurrentItem() {
        if (bnvLayout.isEnabled()) {
            int tabPosition = mTabsManager.getCurrentTab();
            BottomNavigationView.Tab tab = bnvLayout.getTabAt(tabPosition);

            if (tab != null) {
                tab.select();
                setFragment(tabPosition);

                if (tabPosition == 0) {
                    // MainActivityFirstFragment
                    toolbarLayout.setSubtitle("Design");
                    toolbarLayout.setNavigationButtonVisible(true);
                    toolbarLayout.setToolbarMenuItemVisibility(toolbarLayout.getToolbarMenu().findItem(R.id.search), true);
                    ((androidx.drawerlayout.widget.DrawerLayout) drawerLayout.getView(DRAWER_LAYOUT)).setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    // MainActivitySecondFragment
                    toolbarLayout.setSubtitle("Preferences");
                    toolbarLayout.setNavigationButtonVisible(false);
                    toolbarLayout.setToolbarMenuItemVisibility(toolbarLayout.getToolbarMenu().findItem(R.id.search), false);
                    ((androidx.drawerlayout.widget.DrawerLayout) drawerLayout.getView(DRAWER_LAYOUT)).setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }

            }
        }
    }

    private void setFragment(int tabPosition) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        String tabName = mTabsTagName[tabPosition];
        Fragment fragment = mFragmentManager.findFragmentByTag(tabName);

        if (mFragment != null) {
            transaction.hide(mFragment);
        }

        if (fragment != null) {
            mFragment = (Fragment) fragment;
            transaction.show(fragment);
        } else {
            try {
                mFragment = (Fragment) Class.forName(mTabsClassName[tabPosition]).newInstance();
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            transaction.add(R.id.fragment_container, mFragment, tabName);
        }
        transaction.commit();
    }

    // onClick
    public void classicColorPickerDialog(View view) {
        ClassicColorPickerDialog mClassicColorPickerDialog;
        SharedPreferences sharedPreferences = getSharedPreferences("ThemeColor", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "0381fe");

        int currentColor = Color.parseColor("#" + stringColor);

        try {
            mClassicColorPickerDialog = new ClassicColorPickerDialog(this,
                    new ClassicColorPickerDialog.ColorPickerChangedListener() {
                        @Override
                        public void onColorChanged(int i) {
                            if (currentColor != i)
                                ThemeUtil.setColor(MainActivity.this, Color.red(i), Color.green(i), Color.blue(i));
                        }
                    },
                    currentColor);
            mClassicColorPickerDialog.show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void detailedColorPickerDialog(View view) {
        DetailedColorPickerDialog mDetailedColorPickerDialog;
        SharedPreferences sharedPreferences = getSharedPreferences("ThemeColor", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "0381fe");

        float[] currentColor = new float[3];
        Color.colorToHSV(Color.parseColor("#" + stringColor), currentColor);

        mDetailedColorPickerDialog = new DetailedColorPickerDialog(this, 2, currentColor);
        mDetailedColorPickerDialog.setColorPickerChangeListener(new DetailedColorPickerDialog.ColorPickerChangedListener() {
            @Override
            public void onColorChanged(int i, float[] fArr) {
                if (!(fArr[0] == currentColor[0] && fArr[1] == currentColor[1] && fArr[2] == currentColor[2]))
                    ThemeUtil.setColor(MainActivity.this, fArr);
            }

            @Override
            public void onViewModeChanged(int i) {

            }
        });
        mDetailedColorPickerDialog.show();
    }

    public void standardDialog(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Message")
                .setNeutralButton("Maybe", null)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", null)
                .show();
    }

    public void singleChoiceDialog(View view) {
        CharSequence[] charSequences = {"Choice1", "Choice2", "Choice3"};
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setNeutralButton("Maybe", null)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", null)
                .setSingleChoiceItems(charSequences, 0, null)
                .show();
    }

    public void multiChoiceDialog(View view) {
        CharSequence[] charSequences = {"Choice1", "Choice2", "Choice3"};
        boolean[] booleans = {true, false, true};
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setNeutralButton("Maybe", null)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", null)
                .setMultiChoiceItems(charSequences, booleans, null)
                .show();
    }

    public void progressDialog(View view) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Title");
        dialog.setMessage("ProgressDialog");
        dialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", (dialog1, which) -> progressDialogCircleOnly(view));
        dialog.setOnCancelListener(dialog12 -> progressDialogCircleOnly(view));
        dialog.show();
    }

    private void progressDialogCircleOnly(View view) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_CIRCLE_ONLY);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(dialog1 -> Snackbar.make(view, "Text label", Snackbar.LENGTH_SHORT).setAction("Action", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show());
        dialog.show();
    }

    private void popupView(View view) {
        if (bnvPopupMenu == null) {
            bnvPopupMenu = new PopupMenu(view);
            bnvPopupMenu.inflate(R.menu.bnv_menu);
            bnvPopupMenu.setOnMenuItemClickListener(item -> bnvPopupMenu.setMenuItemBadge(item, bnvPopupMenu.getMenuItemBadge(item) + 1));
        }
        bnvPopupMenu.show();
    }
}