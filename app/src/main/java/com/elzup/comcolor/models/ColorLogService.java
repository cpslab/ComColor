package com.elzup.comcolor.models;

import android.content.Context;
import android.graphics.Color;
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
        init();
    }

    private void init(){
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(ColorLogObject.class).count() < 2) {
            setColor(Color.WHITE,Color.WHITE);
        }
    }

    public List<ColorLogObject> getColorListPretty() {
        // NOTE: UnsupportedOperation replace, RealmResult
        List<ColorLogObject> colorList = new ArrayList<>();
        colorList.addAll(this.getColorList());
        // recent 順に反転と連続を取り除く
        Collections.reverse(colorList);
        List<ColorLogObject> colorListPretty = new ArrayList<>();
        colorListPretty.add(colorList.remove(0));
        for (ColorLogObject cl : colorList) {
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

    public ColorLogObject getColor() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ColorLogObject.class).findAll().last();
    }

    public ColorLogObject getPreColor() {
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(ColorLogObject.class).count();
        return realm.where(ColorLogObject.class).findAll().get((int)count - 2);
    }

    public void addColor(int color) {
        this.setColor(ColorUtils.blendARGB(this.getColor().getColor(), color, 0.5f), color);
    }

    public ColorLogObject setColor(int color, int mergedColor) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ColorLogObject colorLog = realm.createObject(ColorLogObject.class);
        colorLog.setColor(color);
        colorLog.setMergedColor(mergedColor);
        // while (realm.where(ColorLogObject.class).count() > 3) {
        //     realm.where(ColorLogObject.class).findAll().first().deleteFromRealm();
        // }
        realm.commitTransaction();
        return colorLog;
    }

}
