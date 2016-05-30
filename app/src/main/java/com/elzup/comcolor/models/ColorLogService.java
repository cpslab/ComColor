package com.elzup.comcolor.models;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ColorLogService {

    public ColorLogService(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name("comcolor.realm")
                .schemaVersion(1)
                .modules(new ColorLogModule())
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public List<ColorLogObject> getColorListPretty() {
        // NOTE: UnsupportedOperation replace, RealmResult
        List<ColorLogObject> colorList = new ArrayList<>();
        colorList.addAll(this.getColorList());
        // recent 順に反転と連続を取り除く
        Collections.reverse(colorList);
        List<ColorLogObject> colorListPretty = new ArrayList<>();
        colorListPretty.add(colorList.remove(0));
        for (ColorLogObject cl: colorList) {
            if (colorListPretty.get(colorListPretty.size() - 1).getColor() != cl.getColor()) {
                colorListPretty.add(cl);
            }
        }
        return colorListPretty;
    }

    public List<ColorLogObject> getColorList() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ColorLogObject> res = realm.where(ColorLogObject.class).findAll();
        realm.commitTransaction();
        return res;
    }

    public int getColor() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(ColorLogObject.class).count() == 0) {
            int initColor = 0xffffffff;
            setColor(initColor);
            return initColor;
        }
        realm.beginTransaction();
        ColorLogObject res = realm.where(ColorLogObject.class).findAll().last();
        realm.commitTransaction();
        return res.getColor();
    }

    public void addColor(int color) {
        this.setColor(ColorUtils.blendARGB(this.getColor(), color, 0.5f));
    }

    public void setColor(int color) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ColorLogObject colorLog = realm.createObject(ColorLogObject.class);
        colorLog.setColor(color);
        // while (realm.where(ColorLogObject.class).count() > 3) {
        //     realm.where(ColorLogObject.class).findAll().first().deleteFromRealm();
        // }
        realm.commitTransaction();
    }

}
