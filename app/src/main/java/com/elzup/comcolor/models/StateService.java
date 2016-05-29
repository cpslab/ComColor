package com.elzup.comcolor.models;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class StateService {

    public StateService(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name("comcolor.realm")
                .schemaVersion(1)
                .modules(new ColorLogModule())
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public int getColor() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ColorLogObject> res = realm.where(ColorLogObject.class).findAll();
        if (res.size() == 0) {
            int initColor = 0xffffffff;
            setColor(initColor);
            return initColor;
        }
        realm.commitTransaction();
        return res.last().getColor();
    }

    public void addColor(int color) {
        this.setColor(ColorUtils.blendARGB(this.getColor(), color, 0.5f));
    }

    public void setColor(int color) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ColorLogObject colorLog = realm.createObject(ColorLogObject.class);
        colorLog.setColor(color);
        realm.commitTransaction();
    }

}
