package com.nos.ploy.utils;

import java.util.List;

/**
 * Created by Saran on 22/11/2559.
 */

public class RecyclerAdapterUtils {

    public static boolean isAvailableData(List<?> datas, int holderAdapterPosition) {
        return null != datas && !datas.isEmpty() && !(holderAdapterPosition < 0) && datas.size() > holderAdapterPosition;
    }
}
