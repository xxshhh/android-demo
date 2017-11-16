package com.xxshhh.android.android_demo.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by xxshhh on 2017/11/16.
 */
public final class ToastUtils {

    private static Toast sToast;

    public static void show(Context context, String message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

}
