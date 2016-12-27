package com.nos.ploy.api.account.model;

import android.support.annotation.DrawableRes;

/**
 * Created by Saran on 25/12/2559.
 */

public class TransportGsonVm {

    private String title;
    private long id;
    private
    @DrawableRes
    int drawable;

    public TransportGsonVm(long id, String title, @DrawableRes int drawable) {
        this.id = id;
        this.drawable = drawable;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public int getDrawable() {
        return drawable;
    }

    public String getTitle() {
        return title;
    }

//    public TransportGsonVm() {
//    }
}
