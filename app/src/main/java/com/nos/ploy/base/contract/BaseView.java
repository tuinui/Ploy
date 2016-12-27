package com.nos.ploy.base.contract;

import android.content.Context;

/**
 * Created by Saran on 1/12/2559.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);

    void setRefreshing(boolean active);

    void toast(String s);

    void showLoadingDialog(boolean active);

    Context getContext();
}
