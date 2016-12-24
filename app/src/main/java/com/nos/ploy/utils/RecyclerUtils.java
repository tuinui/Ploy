package com.nos.ploy.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * Created by Saran on 22/11/2559.
 */

public class RecyclerUtils {

    public static boolean isAvailableData(List<?> datas, int holderAdapterPosition) {
        return null != datas && !datas.isEmpty() && !(holderAdapterPosition < 0) && datas.size() > holderAdapterPosition;
    }

    public static int getSize(List<?> datas) {
        int size = 0;
        if (null != datas && !datas.isEmpty()) {
            size = datas.size();
        }
        return size;
    }

    public static int calculateNoOfColumns(Context context, int itemWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / itemWidth);
    }
}
