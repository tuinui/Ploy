package com.nos.ploy.flow.ployee.home.content.availability.contract;

import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;

/**
 * Created by Saran on 22/11/2559.
 */

public class NormalItemAvailabilityVM implements AvailabilityViewModel {

    private PloyeeAvailiabilityGson.Data.AvailabilityItem data;


    public NormalItemAvailabilityVM(PloyeeAvailiabilityGson.Data.AvailabilityItem data) {
        this.data = data;
    }

    public PloyeeAvailiabilityGson.Data.AvailabilityItem getData(){
        return data;
    }

    @Override
    public int getViewType() {
        return NORMAL;
    }
}
