package com.elzup.comcolor.models;

import android.support.v4.graphics.ColorUtils;

import com.elzup.comcolor.util.ColorUtil;

import io.realm.RealmObject;

public class ColorLogObject extends RealmObject {
    private int color;

    public int getColor() {
        return color;
    }

    public String getColorCodeText() {
        return ColorUtil.toHexRGBText(this.color);
    }

    public void setColor(int color) {
        this.color = color;
    }
}