package com.nos.ploy.flow.ployee.home.content.availability.contract;

/**
 * Created by Saran on 22/11/2559.
 */

public class HeaderDayAvailabilityVM implements AvailabilityViewModel {
    private String day = "Sat";

    public HeaderDayAvailabilityVM(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    @Override
    public int getAvailibilityViewType() {
        return HEADER_DAY;
    }
}
