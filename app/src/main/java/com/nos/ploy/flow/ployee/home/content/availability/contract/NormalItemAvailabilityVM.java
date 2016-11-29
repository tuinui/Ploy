package com.nos.ploy.flow.ployee.home.content.availability.contract;

/**
 * Created by Saran on 22/11/2559.
 */

public class NormalItemAvailabilityVM implements AvailabilityViewModel {

    private boolean isAvailable;


    public NormalItemAvailabilityVM(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isChecked() {
        return isAvailable;
    }

    public void setChecked(boolean isAvailable){
        this.isAvailable = isAvailable;
    }


    @Override
    public int getAvailibilityViewType() {
        return ITEM;
    }
}
