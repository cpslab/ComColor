package com.elzup.comcolor.util;

import android.graphics.Color;

public class ColorUtil {
    public static int blend(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * color値 を読みやすい文字列のフォーマットで返す
     * format: "#{hexRGBVal}({hexAlphaValue})"
     * exsample: red -> #ff0000(00)
     * @param color
     * @return
     */
    public static String toHexRGBText(int color) {
        String hexStr = Integer.toHexString(color);
        return String.format("#%s(%s)", hexStr.substring(2), hexStr.substring(0, 2));
    }
}
