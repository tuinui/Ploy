package com.nos.ploy.flow.ployer.person.maps.viewmodel;

import android.view.View;

import com.google.android.gms.maps.model.MarkerOptions;
import com.nos.ploy.api.ployer.model.PloyerUserListGson;

/**
 * Created by Saran on 8/1/2560.
 */

public class PloyerPersonMapViewModel {

    private PloyerUserListGson.Data.UserService data;
    private MarkerOptions view;

    public PloyerPersonMapViewModel(PloyerUserListGson.Data.UserService data, MarkerOptions view) {
        this.data = data;
        this.view = view;
    }

    public PloyerUserListGson.Data.UserService getData() {
        return data;
    }

    public MarkerOptions getMarkerOptions() {
        return view;
    }
}
