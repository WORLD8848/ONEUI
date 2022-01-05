package com.buxuan.baseoneuiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.buxuan.baseoneui.view.TabLayout;
import com.buxuan.baseoneui.view.ViewPager;
import com.buxuan.baseoneuiapp.R;
import com.buxuan.baseoneuiapp.tabs.ViewPagerAdapter;


public class MainActivityFirstFragment extends Fragment {


    // region AREA: Variables                                       ////////////////////////////////
    /**
     *  MEMO: World8848. change. local variables
     *  private AppCompatActivity   mActivity;
     *  private Context             mContext;
     **/
    private View                mRootView;
    // endregion                                                    ////////////////////////////////



    // region AREA: LifeCycle                                       ////////////////////////////////

    // region AREA: onAttach
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppCompatActivity mActivity = (AppCompatActivity) getActivity();
        Context mContext            = mActivity != null ? mActivity.getApplicationContext() : null;
    }
    // endregion


    // region AREA: onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_first, container, false);
        return mRootView;
    }
    // endregion


    // region AREA: onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         *  MEMO: World8848. change
         *  Origin: getView().setBackgroundColor(getResources().getColor(R.color.background_color));
         */
        view.setBackgroundColor(getResources().getColor(R.color.background_color));

        // TabLayout and ViewPager
        TabLayout tabLayout = mRootView.findViewById(R.id.tabLayout);
        ViewPager viewPager = mRootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.updateWidget();
    }
    // endregion

    // endregion                                                    ////////////////////////////////






}
