package com.buxuan.baseoneui.preference.internal;

import android.content.Context;
import android.util.AttributeSet;

import com.buxuan.baseoneui.preference.DialogPreference;

import java.util.Set;


public abstract class AbstractMultiSelectListPreference extends DialogPreference {
    public AbstractMultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract CharSequence[] getEntries();

    public abstract CharSequence[] getEntryValues();

    public abstract Set<String> getValues();

    public abstract void setValues(Set<String> set);
}
