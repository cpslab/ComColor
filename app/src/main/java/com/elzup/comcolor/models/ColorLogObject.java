package com.elzup.comcolor.models;

import io.realm.RealmObject;

public class ColorLogObject extends RealmObject {
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
