package com.nos.ploy.api.account.model;

import android.support.annotation.DrawableRes;

import com.nos.ploy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 25/12/2559.
 */

public class TransportGsonVm {

//    public static final List<TransportGsonVm> TRANSPORT_DATA = new ArrayList<>();
//
//    static {
//        TRANSPORT_DATA.add(new TransportGsonVm(1, "Walk", R.drawable.selector_drawable_walk_blue_gray));
//        TRANSPORT_DATA.add(new TransportGsonVm(2, "Bicycle", R.drawable.selector_drawable_bike_blue_gray));
//        TRANSPORT_DATA.add(new TransportGsonVm(3, "Car", R.drawable.selector_drawable_car_blue_gray));
//        TRANSPORT_DATA.add(new TransportGsonVm(4, "Motobike", R.drawable.selector_drawable_motobike_blue_gray));
//        TRANSPORT_DATA.add(new TransportGsonVm(5, "Truck", R.drawable.selector_drawable_truck_blue_gray));
//        TRANSPORT_DATA.add(new TransportGsonVm(6, "Public Transport", R.drawable.selector_drawable_bus_blue_gray));
//
//    }

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
