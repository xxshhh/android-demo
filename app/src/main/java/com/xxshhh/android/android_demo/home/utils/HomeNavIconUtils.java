package com.xxshhh.android.android_demo.home.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.pixeden_7_stroke_typeface_library.Pixeden7Stroke;

/**
 * 首页导航图标工具类
 * Created by xwh on 2017/9/15
 */
public class HomeNavIconUtils {

    private static final int NAV_ICON_SIZE = 24;

    public static Drawable getSmileIcon(Context context) {
        return getIcon(context, Pixeden7Stroke.Icon.pe7_7s_smile);
    }

    public static Drawable getPhoneIcon(Context context) {
        return getIcon(context, Pixeden7Stroke.Icon.pe7_7s_phone);
    }

    public static Drawable getLightIcon(Context context) {
        return getIcon(context, Pixeden7Stroke.Icon.pe7_7s_light);
    }

    public static Drawable getMagicIcon(Context context) {
        return getIcon(context, Pixeden7Stroke.Icon.pe7_7s_magic_wand);
    }

    public static Drawable getSettingIcon(Context context) {
        return getIcon(context, Pixeden7Stroke.Icon.pe7_7s_settings);
    }

    private static IconicsDrawable getIcon(Context context, IIcon icon) {
        return new IconicsDrawable(context)
                .icon(icon)
                .sizeDp(NAV_ICON_SIZE);
    }

}
