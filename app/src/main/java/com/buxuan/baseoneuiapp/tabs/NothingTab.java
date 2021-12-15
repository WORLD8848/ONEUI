package com.buxuan.baseoneuiapp.tabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.buxuan.baseoneuiapp.R;
import com.buxuan.baseoneui.layout.SwipeRefreshLayout;

public class NothingTab extends Fragment {

    private View mRootView;
    private Context mContext;

    public NothingTab() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_nothing_tab, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwipeRefreshLayout swipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> Toast.makeText(mContext, "refreshing", Toast.LENGTH_SHORT).show());

    }
}