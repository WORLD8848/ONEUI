package com.buxuan.baseoneui.preference;

import android.os.Bundle;
import android.view.View;

import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.buxuan.baseoneui.sesl.recyclerview.SeslRecyclerViewAccessibilityDelegate;
import com.buxuan.baseoneui.view.RecyclerView;


public class PreferenceRecyclerViewAccessibilityDelegate extends SeslRecyclerViewAccessibilityDelegate {
    final AccessibilityDelegateCompat mDefaultItemDelegate = super.getItemDelegate();
    final RecyclerView mRecyclerView;
    final AccessibilityDelegateCompat mItemDelegate = new AccessibilityDelegateCompat() {
        @Override
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            mDefaultItemDelegate.onInitializeAccessibilityNodeInfo(host, info);
        }

        @Override
        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            return mDefaultItemDelegate.performAccessibilityAction(host, action, args);
        }
    };

    public PreferenceRecyclerViewAccessibilityDelegate(RecyclerView recyclerView) {
        super(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public AccessibilityDelegateCompat getItemDelegate() {
        return mItemDelegate;
    }
}
