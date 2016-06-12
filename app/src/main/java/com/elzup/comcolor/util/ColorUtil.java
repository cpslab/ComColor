package com.elzup.comcolor.util;

public class ColorUtil {

    public static final int CLEAR_COLOR_FILTER = 0xff000000;
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
