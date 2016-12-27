package com.nos.ploy.flow.ployee.home.content.availability.contract;

/**
 * Created by Saran on 21/12/2559.
 */

public class WeekAvailabilityVM implements AvailabilityViewModel {

    private WeekAvailabilityVM() {
    }

    public static WeekAvailabilityVM create() {
        return new WeekAvailabilityVM();
    }

    @Override
    public int getViewType() {
        return WEEK;
    }
}
