package com.elzup.comcolor.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.graphics.ColorUtils;

import com.elzup.comcolor.R;
import com.elzup.comcolor.util.ColorUtil;

public class StateService {
    private Context context;
    private SharedPreferences prefs;

    public StateService(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(statePreferencesKey(), Context.MODE_PRIVATE);
    }

    public int getColor() {
        return prefs.getInt(colorKey(), 0);
    }

    public void addColor(int color) {
        this.setColor(ColorUtils.blendARGB(this.getColor(), color, 0.5f));
    }

    public void setColor(int color) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(colorKey(), color);
        editor.apply();
    }

    public String colorKey() {
        return context.getResources().getString(R.string.prefs_key_color);
    }

    public String statePreferencesKey() {
        return context.getResources().getString(R.string.prefs_state);
    }
}
