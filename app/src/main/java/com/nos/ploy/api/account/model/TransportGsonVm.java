package com.nos.ploy.api.account.model;

import android.support.annotation.DrawableRes;

/**
 * Created by Saran on 25/12/2559.
 */

public class TransportGsonVm {

    private String title;
    private long id;
    private @DrawableRes int drawable;
    private boolean isCheck;

    public TransportGsonVm(long id, String title, @DrawableRes int drawable) {
        this.id = id;
        this.drawable = drawable;
        this.title = title;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
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
