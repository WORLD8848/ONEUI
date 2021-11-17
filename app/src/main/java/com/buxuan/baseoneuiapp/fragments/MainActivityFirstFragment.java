package com.buxuan.baseoneuiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.buxuan.baseoneui.R;
import com.buxuan.baseoneui.view.TabLayout;
import com.buxuan.baseoneui.view.ViewPager;
import com.buxuan.baseoneuiapp.tabs.ViewPagerAdapter;


public class MainActivityFirstFragment extends Fragment {

    private AppCompatActivity mActivity;
    private Context mContext;
    private View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) getActivity();
        mContext = mActivity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_first, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().setBackgroundColor(getResources().getColor(R.color.background_color));

        // TabLayout and ViewPager
        TabLayout tabLayout = mRootView.findViewById(R.id.tabLayout);
        ViewPager viewPager = mRootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.updateWidget();
    }

}
