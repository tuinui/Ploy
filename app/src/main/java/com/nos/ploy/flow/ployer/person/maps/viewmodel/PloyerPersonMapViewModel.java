package com.nos.ploy.flow.ployer.person.maps.viewmodel;

import com.google.android.gms.maps.model.MarkerOptions;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;

/**
 * Created by Saran on 8/1/2560.
 */

public class PloyerPersonMapViewModel {

    private ProviderUserListGson.Data.UserService data;
    private MarkerOptions view;

    public PloyerPersonMapViewModel(ProviderUserListGson.Data.UserService data, MarkerOptions view) {
        this.data = data;
        this.view = view;
    }

    public ProviderUserListGson.Data.UserService getData() {
        return data;
    }

    public MarkerOptions getMarkerOptions() {
        return view;
    }
}
